package com.dremio.service.users;

public class UserLoginException extends Exception {
   private static final long serialVersionUID = 1L;
   private final String userName;

   public UserLoginException(String userName, String error) {
      super(String.format("%s, user %s", error, userName));
      this.userName = userName;
   }

   public String getString() {
      return this.userName;
   }
}
