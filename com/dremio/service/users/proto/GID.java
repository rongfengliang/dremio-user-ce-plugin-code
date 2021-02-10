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
import java.util.HashMap;
import java.util.Objects;

public final class GID implements Externalizable, Message<GID>, Schema<GID> {
   static final GID DEFAULT_INSTANCE = new GID();
   private String id;
   private static final HashMap<String, Integer> __fieldMap = new HashMap();

   public static Schema<GID> getSchema() {
      return DEFAULT_INSTANCE;
   }

   public static GID getDefaultInstance() {
      return DEFAULT_INSTANCE;
   }

   public GID() {
   }

   public GID(String id) {
      this.id = id;
   }

   public String getId() {
      return this.id;
   }

   public GID setId(String id) {
      this.id = id;
      return this;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj != null && this.getClass() == obj.getClass()) {
         GID that = (GID)obj;
         return Objects.equals(this.id, that.id);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.id});
   }

   public String toString() {
      return "GID{id=" + this.id + '}';
   }

   public void readExternal(ObjectInput in) throws IOException {
      GraphIOUtil.mergeDelimitedFrom(in, this, this);
   }

   public void writeExternal(ObjectOutput out) throws IOException {
      GraphIOUtil.writeDelimitedTo(out, this, this);
   }

   public Schema<GID> cachedSchema() {
      return DEFAULT_INSTANCE;
   }

   public GID newMessage() {
      return new GID();
   }

   public Class<GID> typeClass() {
      return GID.class;
   }

   public String messageName() {
      return GID.class.getSimpleName();
   }

   public String messageFullName() {
      return GID.class.getName();
   }

   public boolean isInitialized(GID message) {
      return message.id != null;
   }

   public void mergeFrom(Input input, GID message) throws IOException {
      int number = input.readFieldNumber(this);

      while(true) {
         switch(number) {
         case 0:
            return;
         case 1:
            message.id = input.readString();
            break;
         default:
            input.handleUnknownField(number, this);
         }

         number = input.readFieldNumber(this);
      }
   }

   public void writeTo(Output output, GID message) throws IOException {
      if (message.id == null) {
         throw new UninitializedMessageException(message);
      } else {
         output.writeString(1, message.id, false);
      }
   }

   public String getFieldName(int number) {
      switch(number) {
      case 1:
         return "id";
      default:
         return null;
      }
   }

   public int getFieldNumber(String name) {
      Integer number = (Integer)__fieldMap.get(name);
      return number == null ? 0 : number;
   }

   static {
      __fieldMap.put("id", 1);
   }
}
