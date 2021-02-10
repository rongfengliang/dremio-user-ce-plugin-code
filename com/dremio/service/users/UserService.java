package com.dremio.service.users;

import com.dremio.datastore.SearchTypes.SortOrder;
import com.dremio.service.users.proto.UID;
import java.io.IOException;

public interface UserService {
   User getUser(String var1) throws UserNotFoundException;

   User getUser(UID var1) throws UserNotFoundException;

   User createUser(User var1, String var2) throws IOException, IllegalArgumentException;

   User updateUser(User var1, String var2) throws IOException, IllegalArgumentException, UserNotFoundException;

   User updateUserName(String var1, String var2, User var3, String var4) throws IOException, IllegalArgumentException, UserNotFoundException;

   void deleteUser(String var1, String var2) throws UserNotFoundException, IOException;

   void authenticate(String var1, String var2) throws UserLoginException;

   Iterable<? extends User> getAllUsers(Integer var1) throws IOException;

   boolean hasAnyUser() throws IOException;

   Iterable<? extends User> searchUsers(String var1, String var2, SortOrder var3, Integer var4) throws IOException;
}
