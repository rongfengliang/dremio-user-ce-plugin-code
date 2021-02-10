package com.dremio.service.users.proto;

import io.protostuff.ByteString;
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

public final class UserAuth implements Externalizable, Message<UserAuth>, Schema<UserAuth> {
   static final UserAuth DEFAULT_INSTANCE = new UserAuth();
   private UID uid;
   private ByteString prefix;
   private ByteString authKey;
   private static final HashMap<String, Integer> __fieldMap = new HashMap();

   public static Schema<UserAuth> getSchema() {
      return DEFAULT_INSTANCE;
   }

   public static UserAuth getDefaultInstance() {
      return DEFAULT_INSTANCE;
   }

   public UID getUid() {
      return this.uid;
   }

   public UserAuth setUid(UID uid) {
      this.uid = uid;
      return this;
   }

   public ByteString getPrefix() {
      return this.prefix;
   }

   public UserAuth setPrefix(ByteString prefix) {
      this.prefix = prefix;
      return this;
   }

   public ByteString getAuthKey() {
      return this.authKey;
   }

   public UserAuth setAuthKey(ByteString authKey) {
      this.authKey = authKey;
      return this;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj != null && this.getClass() == obj.getClass()) {
         UserAuth that = (UserAuth)obj;
         return Objects.equals(this.uid, that.uid) && Objects.equals(this.prefix, that.prefix) && Objects.equals(this.authKey, that.authKey);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.uid, this.prefix, this.authKey});
   }

   public String toString() {
      return "UserAuth{uid=" + this.uid + ", prefix=" + this.prefix + ", authKey=" + this.authKey + '}';
   }

   public void readExternal(ObjectInput in) throws IOException {
      GraphIOUtil.mergeDelimitedFrom(in, this, this);
   }

   public void writeExternal(ObjectOutput out) throws IOException {
      GraphIOUtil.writeDelimitedTo(out, this, this);
   }

   public Schema<UserAuth> cachedSchema() {
      return DEFAULT_INSTANCE;
   }

   public UserAuth newMessage() {
      return new UserAuth();
   }

   public Class<UserAuth> typeClass() {
      return UserAuth.class;
   }

   public String messageName() {
      return UserAuth.class.getSimpleName();
   }

   public String messageFullName() {
      return UserAuth.class.getName();
   }

   public boolean isInitialized(UserAuth message) {
      return true;
   }

   public void mergeFrom(Input input, UserAuth message) throws IOException {
      int number = input.readFieldNumber(this);

      while(true) {
         switch(number) {
         case 0:
            return;
         case 1:
            message.uid = (UID)input.mergeObject(message.uid, UID.getSchema());
            break;
         case 2:
            message.prefix = input.readBytes();
            break;
         case 3:
            message.authKey = input.readBytes();
            break;
         default:
            input.handleUnknownField(number, this);
         }

         number = input.readFieldNumber(this);
      }
   }

   public void writeTo(Output output, UserAuth message) throws IOException {
      if (message.uid != null) {
         output.writeObject(1, message.uid, UID.getSchema(), false);
      }

      if (message.prefix != null) {
         output.writeBytes(2, message.prefix, false);
      }

      if (message.authKey != null) {
         output.writeBytes(3, message.authKey, false);
      }

   }

   public String getFieldName(int number) {
      switch(number) {
      case 1:
         return "uid";
      case 2:
         return "prefix";
      case 3:
         return "authKey";
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
      __fieldMap.put("prefix", 2);
      __fieldMap.put("authKey", 3);
   }
}
