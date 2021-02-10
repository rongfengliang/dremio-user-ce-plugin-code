package com.dremio.service.users;

import com.dremio.datastore.SearchTypes.SearchFieldSorting;
import com.dremio.datastore.SearchTypes.SortOrder;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;
import javax.crypto.SecretKeyFactory;

public class UserServiceUtils {
   public static final Pattern PASSWORD_MATCHER = Pattern.compile("(?=.*[0-9])(?=.*[a-zA-Z]).{8,}");
   public static final SearchFieldSorting DEFAULT_SORTER;

   public static SecretKeyFactory buildSecretKey() {
      try {
         return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
      } catch (NoSuchAlgorithmException var1) {
         throw new RuntimeException("Failed to initialize usergroup service", var1);
      }
   }

   public static boolean validatePassword(String input) throws IllegalArgumentException {
      return input != null && !input.isEmpty() && PASSWORD_MATCHER.matcher(input).matches();
   }

   public static boolean slowEquals(byte[] a, byte[] b) {
      int diff = a.length ^ b.length;

      for(int i = 0; i < a.length && i < b.length; ++i) {
         diff |= a[i] ^ b[i];
      }

      return diff == 0;
   }

   static {
      DEFAULT_SORTER = UserIndexKeys.NAME.toSortField(SortOrder.ASCENDING);
   }
}
