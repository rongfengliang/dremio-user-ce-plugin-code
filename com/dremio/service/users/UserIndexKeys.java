package com.dremio.service.users;

import com.dremio.datastore.SearchTypes.SearchFieldSorting.FieldType;
import com.dremio.datastore.indexed.FilterIndexMapping;
import com.dremio.datastore.indexed.IndexKey;

public interface UserIndexKeys {
   IndexKey UID = IndexKey.newBuilder("id", "id", String.class).setSortedValueType(FieldType.STRING).build();
   IndexKey NAME = IndexKey.newBuilder("name", "USERNAME", String.class).setSortedValueType(FieldType.STRING).build();
   IndexKey FIRST_NAME = IndexKey.newBuilder("first", "FIRST_NAME", String.class).setSortedValueType(FieldType.STRING).build();
   IndexKey LAST_NAME = IndexKey.newBuilder("last", "LAST_NAME", String.class).setSortedValueType(FieldType.STRING).build();
   IndexKey EMAIL = IndexKey.newBuilder("email", "EMAIL", String.class).setSortedValueType(FieldType.STRING).build();
   IndexKey ROLE = IndexKey.newBuilder("role", "ROLE", String.class).build();
   FilterIndexMapping MAPPING = new FilterIndexMapping(new IndexKey[]{UID, NAME, FIRST_NAME, LAST_NAME, EMAIL});
}
