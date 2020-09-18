package com.github.btafarelo.selfstorage.processors;

import com.github.btafarelo.selfstorage.jdbc.Datasource;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Get {

    private static final String SQL = "select * from %s";

    private static final String SQL_BY_ID = SQL + " where id = ?";

    private String schema;

    private String table;

    public Get(String schema, String table) {
        this.schema = schema;
        this.table = table;
    }

    public List<Map<String, Object>> doGet(final long id)
            throws IOException {

        Map<String, String> input = new HashMap<>();
        input.put("id", "" + id);

        return Datasource.query(String.format(SQL_BY_ID, table), input);
    }

    public List<Map<String, Object>> doGet() {
        return Datasource.query(String.format(SQL, table), null);
    }
}
