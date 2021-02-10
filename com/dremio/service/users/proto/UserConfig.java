package com.dremio.service.users.proto;

import io.protostuff.GraphIOUtil;
import io.protostuff.Input;
import io.protostuff.Message;
import io.protostuff.Output;
import io.protostuff.Schema;
import io.protostuff.UninitializedMessageException;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public final class UserConfig implements Externalizable, Message<UserConfig>, Schema<UserConfig> {
   static final UserConfig DEFAULT_INSTANCE = new UserConfig();
   private UID uid;
   private String userName;
   private String firstName;
   private String lastName;
   private String email;
   private Long createdAt;
   private Long modifiedAt;
   private List<GID> groupMemberships;
   private Long version;
   private String tag;
   private static final HashMap<String, Integer> __fieldMap = new HashMap();

   public static Schema<UserConfig> getSchema() {
      return DEFAULT_INSTANCE;
   }

   public static UserConfig getDefaultInstance() {
      return DEFAULT_INSTANCE;
   }

   public UserConfig() {
   }

   public UserConfig(UID uid) {
      this.uid = uid;
   }

   public UID getUid() {
      return this.uid;
   }

   public UserConfig setUid(UID uid) {
      this.uid = uid;
      return this;
   }

   public String getUserName() {
      return this.userName;
   }

   public UserConfig setUserName(String userName) {
      this.userName = userName;
      return this;
   }

   public String getFirstName() {
      return this.firstName;
   }

   public UserConfig setFirstName(String firstName) {
      this.firstName = firstName;
      return this;
   }

   public String getLastName() {
      return this.lastName;
   }

   public UserConfig setLastName(String lastName) {
      this.lastName = lastName;
      return this;
   }

   public String getEmail() {
      return this.email;
   }

   public UserConfig setEmail(String email) {
      this.email = email;
      return this;
   }

   public Long getCreatedAt() {
      return this.createdAt;
   }

   public UserConfig setCreatedAt(Long createdAt) {
      this.createdAt = createdAt;
      return this;
   }

   public Long getModifiedAt() {
      return this.modifiedAt;
   }

   public UserConfig setModifiedAt(Long modifiedAt) {
      this.modifiedAt = modifiedAt;
      return this;
   }

   public List<GID> getGroupMembershipsList() {
      return this.groupMemberships;
   }

   public UserConfig setGroupMembershipsList(List<GID> groupMemberships) {
      this.groupMemberships = groupMemberships;
      return this;
   }

   /** @deprecated */
   @Deprecated
   public Long getVersion() {
      return this.version;
   }

   /** @deprecated */
   @Deprecated
   public UserConfig setVersion(Long version) {
      this.version = version;
      return this;
   }

   public String getTag() {
      return this.tag;
   }

   public UserConfig setTag(String tag) {
      this.tag = tag;
      return this;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj != null && this.getClass() == obj.getClass()) {
         UserConfig that = (UserConfig)obj;
         return Objects.equals(this.uid, that.uid) && Objects.equals(this.userName, that.userName) && Objects.equals(this.firstName, that.firstName) && Objects.equals(this.lastName, that.lastName) && Objects.equals(this.email, that.email) && Objects.equals(this.createdAt, that.createdAt) && Objects.equals(this.modifiedAt, that.modifiedAt) && Objects.equals(this.groupMemberships, that.groupMemberships) && Objects.equals(this.version, that.version) && Objects.equals(this.tag, that.tag);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.uid, this.userName, this.firstName, this.lastName, this.email, this.createdAt, this.modifiedAt, this.groupMemberships, this.version, this.tag});
   }

   public String toString() {
      return "UserConfig{uid=" + this.uid + ", userName=" + this.userName + ", firstName=" + this.firstName + ", lastName=" + this.lastName + ", email=" + this.email + ", createdAt=" + this.createdAt + ", modifiedAt=" + this.modifiedAt + ", groupMemberships=" + this.groupMemberships + ", version=" + this.version + ", tag=" + this.tag + '}';
   }

   public void readExternal(ObjectInput in) throws IOException {
      GraphIOUtil.mergeDelimitedFrom(in, this, this);
   }

   public void writeExternal(ObjectOutput out) throws IOException {
      GraphIOUtil.writeDelimitedTo(out, this, this);
   }

   public Schema<UserConfig> cachedSchema() {
      return DEFAULT_INSTANCE;
   }

   public UserConfig newMessage() {
      return new UserConfig();
   }

   public Class<UserConfig> typeClass() {
      return UserConfig.class;
   }

   public String messageName() {
      return UserConfig.class.getSimpleName();
   }

   public String messageFullName() {
      return UserConfig.class.getName();
   }

   public boolean isInitialized(UserConfig message) {
      return message.uid != null;
   }

   public void mergeFrom(Input input, UserConfig message) throws IOException {
      int number = input.readFieldNumber(this);

      while(true) {
         switch(number) {
         case 0:
            return;
         case 1:
            message.uid = (UID)input.mergeObject(message.uid, UID.getSchema());
            break;
         case 2:
            message.userName = input.readString();
            break;
         case 3:
            message.firstName = input.readString();
            break;
         case 4:
            message.lastName = input.readString();
            break;
         case 5:
            message.email = input.readString();
            break;
         case 6:
            message.createdAt = input.readUInt64();
            break;
         case 7:
            message.modifiedAt = input.readUInt64();
            break;
         case 8:
            if (message.groupMemberships == null) {
               message.groupMemberships = new ArrayList();
            }

            message.groupMemberships.add((GID)input.mergeObject((Object)null, GID.getSchema()));
            break;
         case 9:
            message.version = input.readInt64();
            break;
         case 10:
            message.tag = input.readString();
            break;
         default:
            input.handleUnknownField(number, this);
         }

         number = input.readFieldNumber(this);
      }
   }

   public void writeTo(Output output, UserConfig message) throws IOException {
      if (message.uid == null) {
         throw new UninitializedMessageException(message);
      } else {
         output.writeObject(1, message.uid, UID.getSchema(), false);
         if (message.userName != null) {
            output.writeString(2, message.userName, false);
         }

         if (message.firstName != null) {
            output.writeString(3, message.firstName, false);
         }

         if (message.lastName != null) {
            output.writeString(4, message.lastName, false);
         }

         if (message.email != null) {
            output.writeString(5, message.email, false);
         }

         if (message.createdAt != null) {
            output.writeUInt64(6, message.createdAt, false);
         }

         if (message.modifiedAt != null) {
            output.writeUInt64(7, message.modifiedAt, false);
         }

         if (message.groupMemberships != null) {
            Iterator var3 = message.groupMemberships.iterator();

            while(var3.hasNext()) {
               GID groupMemberships = (GID)var3.next();
               if (groupMemberships != null) {
                  output.writeObject(8, groupMemberships, GID.getSchema(), true);
               }
            }
         }

         if (message.version != null) {
            output.writeInt64(9, message.version, false);
         }

         if (message.tag != null) {
            output.writeString(10, message.tag, false);
         }

      }
   }

   public String getFieldName(int number) {
      switch(number) {
      case 1:
         return "uid";
      case 2:
         return "userName";
      case 3:
         return "firstName";
      case 4:
         return "lastName";
      case 5:
         return "email";
      case 6:
         return "createdAt";
      case 7:
         return "modifiedAt";
      case 8:
         return "groupMemberships";
      case 9:
         return "version";
      case 10:
         return "tag";
      default:
         return null;
      }
   }

   public int getFieldNumber(String name) {
      Integer number = (Integer)__fieldMap.get(name);
      return number == null ? 0 : number;
   }

   static {
      __fieldMap.put("uid", 1);
      __fieldMap.put("userName", 2);
      __fieldMap.put("firstName", 3);
      __fieldMap.put("lastName", 4);
      __fieldMap.put("email", 5);
      __fieldMap.put("createdAt", 6);
      __fieldMap.put("modifiedAt", 7);
      __fieldMap.put("groupMemberships", 8);
      __fieldMap.put("version", 9);
      __fieldMap.put("tag", 10);
   }
}
