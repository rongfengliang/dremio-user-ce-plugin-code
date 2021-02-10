package com.dremio.service.users;

import com.dremio.service.users.proto.UID;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public final class SimpleUser implements User {
   private final UID uid;
   private final String userName;
   private final String firstName;
   private final String lastName;
   private final String email;
   private final long createdAt;
   private final long modifiedAt;
   private final String version;
   private final String extra;

   @JsonCreator
   private SimpleUser(@JsonProperty("uid") UID uid, @JsonProperty("userName") String userName, @JsonProperty("firstName") String firstName, @JsonProperty("lastName") String lastName, @JsonProperty("email") String email, @JsonProperty("createdAt") long createdAt, @JsonProperty("modifiedAt") long modifiedAt, @JsonProperty("version") String version, @JsonProperty("extra") String extra) {
      this.uid = uid;
      this.userName = userName;
      this.firstName = firstName;
      this.lastName = lastName;
      this.email = email;
      this.createdAt = createdAt;
      this.modifiedAt = modifiedAt;
      this.version = version;
      this.extra = extra;
   }

   public UID getUID() {
      return this.uid;
   }

   public String getUserName() {
      return this.userName;
   }

   public String getFirstName() {
      return this.firstName;
   }

   public String getLastName() {
      return this.lastName;
   }

   public String getEmail() {
      return this.email;
   }

   public long getCreatedAt() {
      return this.createdAt;
   }

   public long getModifiedAt() {
      return this.modifiedAt;
   }

   public String getVersion() {
      return this.version;
   }

   public String getExtra() {
      return this.extra;
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.uid, this.userName, this.firstName, this.lastName, this.email, this.createdAt, this.modifiedAt, this.version, this.extra});
   }

   public boolean equals(Object obj) {
      if (obj == null) {
         return false;
      } else if (!(obj instanceof SimpleUser)) {
         return false;
      } else {
         SimpleUser other = (SimpleUser)obj;
         return Objects.equals(this.uid, other.uid) && Objects.equals(this.userName, other.userName) && Objects.equals(this.firstName, other.firstName) && Objects.equals(this.lastName, other.lastName) && Objects.equals(this.email, other.email) && Objects.equals(this.createdAt, other.createdAt) && Objects.equals(this.modifiedAt, other.modifiedAt) && Objects.equals(this.version, other.version) && Objects.equals(this.extra, other.extra);
      }
   }

   public String toString() {
      return "SimpleUser [uid=" + this.uid + ", userName=" + this.userName + ", firstName=" + this.firstName + ", lastName=" + this.lastName + ", email=" + this.email + ", createdAt=" + this.createdAt + ", modifiedAt=" + this.modifiedAt + ", version=" + this.version + ", extra=" + this.extra + "]";
   }

   public static SimpleUser.Builder newBuilder() {
      return new SimpleUser.Builder();
   }

   public static SimpleUser.Builder newBuilder(User user) {
      return new SimpleUser.Builder(user.getUID(), user.getUserName(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getCreatedAt(), user.getModifiedAt(), user.getVersion(), user.getExtra());
   }

   // $FF: synthetic method
   SimpleUser(UID x0, String x1, String x2, String x3, String x4, long x5, long x6, String x7, String x8, Object x9) {
      this(x0, x1, x2, x3, x4, x5, x6, x7, x8);
   }

   public static final class Builder {
      private UID uid;
      private String userName;
      private String firstName;
      private String lastName;
      private String email;
      private long createdAt;
      private long modifiedAt;
      private String version;
      private String extra;

      private Builder() {
      }

      private Builder(UID uid, String userName, String firstName, String lastName, String email, long createdAt, long modifiedAt, String version, String extra) {
         this.uid = uid;
         this.userName = userName;
         this.firstName = firstName;
         this.lastName = lastName;
         this.email = email;
         this.createdAt = createdAt;
         this.modifiedAt = modifiedAt;
         this.version = version;
         this.extra = extra;
      }

      public UID getUID() {
         return this.uid;
      }

      public SimpleUser.Builder setUID(UID uid) {
         this.uid = uid;
         return this;
      }

      public String getUserName() {
         return this.userName;
      }

      public SimpleUser.Builder setUserName(String userName) {
         this.userName = userName;
         return this;
      }

      public String getFirstName() {
         return this.firstName;
      }

      public SimpleUser.Builder setFirstName(String firstName) {
         this.firstName = firstName;
         return this;
      }

      public String getLastName() {
         return this.lastName;
      }

      public SimpleUser.Builder setLastName(String lastName) {
         this.lastName = lastName;
         return this;
      }

      public String getEmail() {
         return this.email;
      }

      public SimpleUser.Builder setEmail(String email) {
         this.email = email;
         return this;
      }

      public long getCreatedAt() {
         return this.createdAt;
      }

      public SimpleUser.Builder setCreatedAt(long createdAt) {
         this.createdAt = createdAt;
         return this;
      }

      public long getModifiedAt() {
         return this.modifiedAt;
      }

      public SimpleUser.Builder setModifiedAt(long modifiedAt) {
         this.modifiedAt = modifiedAt;
         return this;
      }

      public String getVersion() {
         return this.version;
      }

      public SimpleUser.Builder setVersion(String version) {
         this.version = version;
         return this;
      }

      public String getExtra() {
         return this.extra;
      }

      public SimpleUser.Builder setExtra(String extra) {
         this.extra = extra;
         return this;
      }

      public SimpleUser build() {
         return new SimpleUser(this.uid, this.userName, this.firstName, this.lastName, this.email, this.createdAt, this.modifiedAt, this.version, this.extra);
      }

      // $FF: synthetic method
      Builder(Object x0) {
         this();
      }

      // $FF: synthetic method
      Builder(UID x0, String x1, String x2, String x3, String x4, long x5, long x6, String x7, String x8, Object x9) {
         this(x0, x1, x2, x3, x4, x5, x6, x7, x8);
      }
   }
}
