package com.dremio.service.users;

import com.dremio.common.exceptions.UserException;
import com.dremio.datastore.KVUtil;
import com.dremio.datastore.SearchQueryUtils;
import com.dremio.datastore.VersionExtractor;
import com.dremio.datastore.SearchTypes.SearchFieldSorting;
import com.dremio.datastore.SearchTypes.SearchQuery;
import com.dremio.datastore.SearchTypes.SortOrder;
import com.dremio.datastore.api.DocumentConverter;
import com.dremio.datastore.api.DocumentWriter;
import com.dremio.datastore.api.LegacyIndexedStore;
import com.dremio.datastore.api.LegacyIndexedStoreCreationFunction;
import com.dremio.datastore.api.LegacyKVStoreProvider;
import com.dremio.datastore.api.LegacyStoreBuildingFactory;
import com.dremio.datastore.api.LegacyIndexedStore.LegacyFindByCondition;
import com.dremio.datastore.format.Format;
import com.dremio.datastore.indexed.IndexKey;
import com.dremio.service.users.proto.UID;
import com.dremio.service.users.proto.UserAuth;
import com.dremio.service.users.proto.UserConfig;
import com.dremio.service.users.proto.UserInfo;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import io.protostuff.ByteString;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.inject.Inject;
import javax.inject.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleUserService implements UserService {
   private static final Logger logger = LoggerFactory.getLogger(SimpleUserService.class);
   private final Function<UserInfo, User> infoConfigTransformer = new Function<UserInfo, User>() {
      public User apply(UserInfo input) {
         return SimpleUserService.this.fromUserConfig(input.getConfig());
      }
   };
   @VisibleForTesting
   public static final String USER_STORE = "userGroup";
   private static final SecretKeyFactory secretKey = UserServiceUtils.buildSecretKey();
   private LegacyIndexedStore<UID, UserInfo> userStore;
   private final AtomicBoolean anyUserFound = new AtomicBoolean();

   @Inject
   public SimpleUserService(Provider<LegacyKVStoreProvider> kvStoreProvider) {
      this.userStore = (LegacyIndexedStore)((LegacyKVStoreProvider)kvStoreProvider.get()).getStore(SimpleUserService.UserGroupStoreBuilder.class);
   }

   private UserInfo findUserByUserName(String userName) {
      LegacyFindByCondition condition = (new LegacyFindByCondition()).setCondition(SearchQueryUtils.newTermQuery(UserIndexKeys.NAME, userName)).setLimit(1);
      List<UserInfo> userInfos = Lists.newArrayList(KVUtil.values(this.userStore.find(condition)));
      return userInfos.size() == 0 ? null : (UserInfo)userInfos.get(0);
   }

   public User getUser(String userName) throws UserNotFoundException {
      if (SystemUser.SYSTEM_USER.getUserName().equals(userName)) {
         return SystemUser.SYSTEM_USER;
      } else {
         UserInfo userInfo = this.findUserByUserName(userName);
         if (userInfo == null) {
            throw new UserNotFoundException(userName);
         } else {
            return this.fromUserConfig(userInfo.getConfig());
         }
      }
   }

   public User getUser(UID uid) throws UserNotFoundException {
      UserInfo userInfo = (UserInfo)this.userStore.get(uid);
      if (userInfo == null) {
         throw new UserNotFoundException(uid);
      } else {
         return this.fromUserConfig(userInfo.getConfig());
      }
   }

   public User createUser(User userConfig, String authKey) throws IOException, IllegalArgumentException {
      String userName = userConfig.getUserName();
      if (this.findUserByUserName(userName) != null) {
         throw UserException.validationError().message("User '%s' already exists.", new Object[]{userName}).build(logger);
      } else {
         validatePassword(authKey);
         UserConfig newUser = this.toUserConfig(userConfig).setUid(new UID(UUID.randomUUID().toString())).setCreatedAt(System.currentTimeMillis()).setModifiedAt(userConfig.getCreatedAt()).setTag((String)null);
         UserInfo userInfo = new UserInfo();
         userInfo.setConfig(newUser);
         userInfo.setAuth(this.buildUserAuth(newUser.getUid(), authKey));
         this.userStore.put(newUser.getUid(), userInfo);
         return this.fromUserConfig(newUser);
      }
   }

   private void merge(UserConfig newConfig, UserConfig oldConfig) {
      newConfig.setUid(oldConfig.getUid());
      if (newConfig.getCreatedAt() == null) {
         newConfig.setCreatedAt(oldConfig.getCreatedAt());
      }

      if (newConfig.getEmail() == null) {
         newConfig.setEmail(oldConfig.getEmail());
      }

      if (newConfig.getFirstName() == null) {
         newConfig.setFirstName(oldConfig.getFirstName());
      }

      if (newConfig.getLastName() == null) {
         newConfig.setLastName(oldConfig.getLastName());
      }

      if (newConfig.getUserName() == null) {
         newConfig.setUserName(oldConfig.getUserName());
      }

      if (newConfig.getGroupMembershipsList() == null) {
         newConfig.setGroupMembershipsList(oldConfig.getGroupMembershipsList());
      }

      if (newConfig.getTag() == null) {
         newConfig.setTag(oldConfig.getTag());
      }

   }

   public User updateUser(User userGroup, String authKey) throws IOException, IllegalArgumentException, UserNotFoundException {
      UserConfig userConfig = this.toUserConfig(userGroup);
      String userName = userConfig.getUserName();
      UserInfo oldUserInfo = this.findUserByUserName(userName);
      if (oldUserInfo == null) {
         throw new UserNotFoundException(userName);
      } else {
         this.merge(userConfig, oldUserInfo.getConfig());
         userConfig.setModifiedAt(System.currentTimeMillis());
         UserInfo newUserInfo = new UserInfo();
         newUserInfo.setConfig(userConfig);
         if (authKey != null) {
            validatePassword(authKey);
            newUserInfo.setAuth(this.buildUserAuth(oldUserInfo.getConfig().getUid(), authKey));
         } else {
            newUserInfo.setAuth(oldUserInfo.getAuth());
         }

         this.userStore.put(userConfig.getUid(), newUserInfo);
         return this.fromUserConfig(userConfig);
      }
   }

   public User updateUserName(String oldUserName, String newUserName, User userGroup, String authKey) throws IOException, IllegalArgumentException, UserNotFoundException {
      UserInfo oldUserInfo = this.findUserByUserName(oldUserName);
      if (oldUserInfo == null) {
         throw new UserNotFoundException(oldUserName);
      } else if (this.findUserByUserName(newUserName) != null) {
         throw UserException.validationError().message("User '%s' already exists.", new Object[]{newUserName}).build(logger);
      } else {
         UserConfig userConfig = this.toUserConfig(userGroup);
         if (!userConfig.getUserName().equals(newUserName)) {
            throw new IllegalArgumentException("Usernames do not match " + newUserName + " , " + userConfig.getUserName());
         } else {
            this.merge(userConfig, oldUserInfo.getConfig());
            userConfig.setModifiedAt(System.currentTimeMillis());
            UserInfo info = new UserInfo();
            info.setConfig(userConfig);
            if (authKey != null) {
               validatePassword(authKey);
               info.setAuth(this.buildUserAuth(userConfig.getUid(), authKey));
            } else {
               info.setAuth(oldUserInfo.getAuth());
            }

            this.userStore.put(info.getConfig().getUid(), info);
            return this.fromUserConfig(userConfig);
         }
      }
   }

   public void authenticate(String userName, String password) throws UserLoginException {
      UserInfo userInfo = this.findUserByUserName(userName);
      if (userInfo == null) {
         throw new UserLoginException(userName, "Invalid user credentials");
      } else {
         try {
            UserAuth userAuth = userInfo.getAuth();
            byte[] authKey = this.buildUserAuthKey(password, userAuth.getPrefix().toByteArray());
            if (!UserServiceUtils.slowEquals(authKey, userAuth.getAuthKey().toByteArray())) {
               throw new UserLoginException(userName, "Invalid user credentials");
            }
         } catch (InvalidKeySpecException var6) {
            throw new UserLoginException(userName, "Invalid user credentials");
         }
      }
   }

   public Iterable<? extends User> getAllUsers(Integer limit) throws IOException {
      Iterable<? extends User> configs = Iterables.transform(KVUtil.values(this.userStore.find()), this.infoConfigTransformer);
      return limit == null ? configs : Iterables.limit(configs, limit);
   }

   public boolean hasAnyUser() throws IOException {
      if (this.anyUserFound.get()) {
         return true;
      } else {
         boolean userFound = this.getAllUsers(1).iterator().hasNext();
         this.anyUserFound.set(userFound);
         return userFound;
      }
   }

   public Iterable<? extends User> searchUsers(String searchTerm, String sortColumn, SortOrder order, Integer limit) throws IOException {
      limit = limit == null ? 10000 : limit;
      if (searchTerm != null && !searchTerm.isEmpty()) {
         SearchQuery query = SearchQueryUtils.or(new SearchQuery[]{SearchQueryUtils.newContainsTerm(UserIndexKeys.NAME, searchTerm), SearchQueryUtils.newContainsTerm(UserIndexKeys.FIRST_NAME, searchTerm), SearchQueryUtils.newContainsTerm(UserIndexKeys.LAST_NAME, searchTerm), SearchQueryUtils.newContainsTerm(UserIndexKeys.EMAIL, searchTerm)});
         LegacyFindByCondition conditon = (new LegacyFindByCondition()).setCondition(query).setLimit(limit).addSorting(buildSorter(sortColumn, order));
         return Lists.transform(Lists.newArrayList(KVUtil.values(this.userStore.find(conditon))), this.infoConfigTransformer);
      } else {
         return this.getAllUsers(limit);
      }
   }

   private static SearchFieldSorting buildSorter(String sortColumn, SortOrder order) {
      if (sortColumn == null) {
         return UserServiceUtils.DEFAULT_SORTER;
      } else {
         IndexKey key = UserIndexKeys.MAPPING.getKey(sortColumn);
         if (key == null) {
            throw UserException.functionError().message("Unable to sort by field {}", new Object[]{sortColumn}).build(logger);
         } else {
            return key.toSortField(order);
         }
      }
   }

   public void deleteUser(String userName, String version) throws UserNotFoundException, IOException {
      UserInfo info = this.findUserByUserName(userName);
      if (info != null) {
         this.userStore.delete(info.getConfig().getUid(), version);
         if (!this.getAllUsers(1).iterator().hasNext()) {
            this.anyUserFound.set(false);
         }

      } else {
         throw new UserNotFoundException(userName);
      }
   }

   private UserAuth buildUserAuth(UID uid, String authKey) throws IllegalArgumentException {
      UserAuth userAuth = new UserAuth();
      SecureRandom random = new SecureRandom();
      byte[] prefix = new byte[16];
      random.nextBytes(prefix);
      userAuth.setUid(uid);
      userAuth.setPrefix(ByteString.copyFrom(prefix));
      PBEKeySpec spec = new PBEKeySpec(authKey.toCharArray(), prefix, 65536, 128);

      try {
         userAuth.setAuthKey(ByteString.copyFrom(secretKey.generateSecret(spec).getEncoded()));
         return userAuth;
      } catch (InvalidKeySpecException var8) {
         throw new IllegalArgumentException(var8.toString());
      }
   }

   private byte[] buildUserAuthKey(String authKey, byte[] prefix) throws InvalidKeySpecException {
      PBEKeySpec spec = new PBEKeySpec(authKey.toCharArray(), prefix, 65536, 128);
      return secretKey.generateSecret(spec).getEncoded();
   }

   public void setPassword(String userName, String password) throws IllegalArgumentException {
      validatePassword(password);
      UserInfo info = this.findUserByUserName(userName);
      if (info == null) {
         throw new IllegalArgumentException(String.format("user %s does not exist", userName));
      } else {
         info.setAuth(this.buildUserAuth(info.getConfig().getUid(), password));
         this.userStore.put(info.getConfig().getUid(), info);
      }
   }

   @VisibleForTesting
   public static void validatePassword(String input) throws IllegalArgumentException {
      if (!UserServiceUtils.validatePassword(input)) {
         throw UserException.validationError().message("Invalid password: must be at least 8 letters long, must contain at least one number and one letter", new Object[0]).build(logger);
      }
   }

   protected UserConfig toUserConfig(User user) {
      return (new UserConfig()).setUid(user.getUID()).setUserName(user.getUserName()).setFirstName(user.getFirstName()).setLastName(user.getLastName()).setEmail(user.getEmail()).setCreatedAt(user.getCreatedAt()).setModifiedAt(user.getModifiedAt()).setTag(user.getVersion());
   }

   protected User fromUserConfig(UserConfig userConfig) {
      return SimpleUser.newBuilder().setUID(userConfig.getUid()).setUserName(userConfig.getUserName()).setFirstName(userConfig.getFirstName()).setLastName(userConfig.getLastName()).setEmail(userConfig.getEmail()).setCreatedAt(userConfig.getCreatedAt()).setModifiedAt(userConfig.getModifiedAt()).setVersion(userConfig.getTag()).build();
   }

   public static class UserGroupStoreBuilder implements LegacyIndexedStoreCreationFunction<UID, UserInfo> {
      public LegacyIndexedStore<UID, UserInfo> build(LegacyStoreBuildingFactory factory) {
         return factory.newStore().name("userGroup").keyFormat(Format.wrapped(UID.class, UID::getId, UID::new, Format.ofString())).valueFormat(Format.ofProtostuff(UserInfo.class)).versionExtractor(SimpleUserService.UserVersionExtractor.class).buildIndexed(new SimpleUserService.UserConverter());
      }
   }

   private static final class UserVersionExtractor implements VersionExtractor<UserInfo> {
      public String getTag(UserInfo value) {
         return value.getConfig().getTag();
      }

      public void setTag(UserInfo value, String tag) {
         value.getConfig().setTag(tag);
      }

      public Long getVersion(UserInfo value) {
         return value.getConfig().getVersion();
      }

      public void setVersion(UserInfo value, Long version) {
         value.getConfig().setVersion(version);
      }
   }

   private static final class UserConverter implements DocumentConverter<UID, UserInfo> {
      private UserConverter() {
      }

      public void convert(DocumentWriter writer, UID key, UserInfo userInfo) {
         UserConfig userConfig = userInfo.getConfig();
         writer.write(UserIndexKeys.UID, new String[]{userConfig.getUid().getId()});
         writer.write(UserIndexKeys.NAME, new String[]{userConfig.getUserName()});
         writer.write(UserIndexKeys.FIRST_NAME, new String[]{userConfig.getFirstName()});
         writer.write(UserIndexKeys.LAST_NAME, new String[]{userConfig.getLastName()});
         writer.write(UserIndexKeys.EMAIL, new String[]{userConfig.getEmail()});
      }

      // $FF: synthetic method
      UserConverter(Object x0) {
         this();
      }
   }
}
