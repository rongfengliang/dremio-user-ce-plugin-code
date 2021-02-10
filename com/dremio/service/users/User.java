package com.dremio.service.users;

import com.dremio.service.users.proto.UID;

public interface User {
   UID getUID();

   String getUserName();

   String getFirstName();

   String getLastName();

   String getEmail();

   long getCreatedAt();

   long getModifiedAt();

   String getVersion();

   String getExtra();
}
