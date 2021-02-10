package com.dremio.service.users;

import com.dremio.service.users.proto.UID;

public class SystemUser {
   public static final String SYSTEM_USERNAME = "$dremio$";
   public static final User SYSTEM_USER = SimpleUser.newBuilder().setUID(new UID("1")).setUserName("$dremio$").setCreatedAt(System.currentTimeMillis()).setEmail("dremio@dremio.net").build();

   public static boolean isSystemUserName(String username) {
      return "$dremio$".equals(username);
   }
}
