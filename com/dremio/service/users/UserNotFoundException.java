package com.dremio.service.users;

import com.dremio.service.users.proto.UID;

public class UserNotFoundException extends Exception {
   private static final long serialVersionUID = 1L;

   public UserNotFoundException(String userName) {
      super(String.format("User '%s' not found", userName));
   }

   public UserNotFoundException(UID uid) {
      super(String.format("User with given id '%s' not found", uid.getId()));
   }
}
