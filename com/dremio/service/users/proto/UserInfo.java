package com.dremio.service.users.proto;

import io.protostuff.GraphIOUtil;
import io.protostuff.Input;
import io.protostuff.Message;
import io.protostuff.Output;
import io.protostuff.Schema;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;
import java.util.Objects;

public final class UserInfo implements Externalizable, Message<UserInfo>, Schema<UserInfo> {
   static final UserInfo DEFAULT_INSTANCE = new UserInfo();
   private UserConfig config;
   private UserAuth auth;
   private static final HashMap<String, Integer> __fieldMap = new HashMap();

   public static Schema<UserInfo> getSchema() {
      return DEFAULT_INSTANCE;
   }

   public static UserInfo getDefaultInstance() {
      return DEFAULT_INSTANCE;
   }

   public UserConfig getConfig() {
      return this.config;
   }

   public UserInfo setConfig(UserConfig config) {
      this.config = config;
      return this;
   }

   public UserAuth getAuth() {
      return this.auth;
   }

   public UserInfo setAuth(UserAuth auth) {
      this.auth = auth;
      return this;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj != null && this.getClass() == obj.getClass()) {
         UserInfo that = (UserInfo)obj;
         return Objects.equals(this.config, that.config) && Objects.equals(this.auth, that.auth);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.config, this.auth});
   }

   public String toString() {
      return "UserInfo{config=" + this.config + ", auth=" + this.auth + '}';
   }

   public void readExternal(ObjectInput in) throws IOException {
      GraphIOUtil.mergeDelimitedFrom(in, this, this);
   }

   public void writeExternal(ObjectOutput out) throws IOException {
      GraphIOUtil.writeDelimitedTo(out, this, this);
   }

   public Schema<UserInfo> cachedSchema() {
      return DEFAULT_INSTANCE;
   }

   public UserInfo newMessage() {
      return new UserInfo();
   }

   public Class<UserInfo> typeClass() {
      return UserInfo.class;
   }

   public String messageName() {
      return UserInfo.class.getSimpleName();
   }

   public String messageFullName() {
      return UserInfo.class.getName();
   }

   public boolean isInitialized(UserInfo message) {
      return true;
   }

   public void mergeFrom(Input input, UserInfo message) throws IOException {
      int number = input.readFieldNumber(this);

      while(true) {
         switch(number) {
         case 0:
            return;
         case 1:
            message.config = (UserConfig)input.mergeObject(message.config, UserConfig.getSchema());
            break;
         case 2:
            message.auth = (UserAuth)input.mergeObject(message.auth, UserAuth.getSchema());
            break;
         default:
            input.handleUnknownField(number, this);
         }

         number = input.readFieldNumber(this);
      }
   }

   public void writeTo(Output output, UserInfo message) throws IOException {
      if (message.config != null) {
         output.writeObject(1, message.config, UserConfig.getSchema(), false);
      }

      if (message.auth != null) {
         output.writeObject(2, message.auth, UserAuth.getSchema(), false);
      }

   }

   public String getFieldName(int number) {
      switch(number) {
      case 1:
         return "config";
      case 2:
         return "auth";
      default:
         return null;
      }
   }

   public int getFieldNumber(String name) {
      Integer number = (Integer)__fieldMap.get(name);
      return number == null ? 0 : number;
   }

   static {
      __fieldMap.put("config", 1);
      __fieldMap.put("auth", 2);
   }
}
