package com.github.btafarelo.selfstorage.processors;


import com.github.btafarelo.selfstorage.jdbc.Datasource;

import java.util.HashMap;
import java.util.Map;

public class Delete {

    private static final String SQL = "delete from %s where id = ?";

    private String schema;

    private String table;

    public Delete(String schema, String table) {
        this.schema = schema;
        this.table = table;
    }

    public void doDelete(final long id) {
        Map<String, String> input = new HashMap<>();
        input.put("id", "" + id);

        Datasource.execute(String.format(SQL, table), input);
    }
}
