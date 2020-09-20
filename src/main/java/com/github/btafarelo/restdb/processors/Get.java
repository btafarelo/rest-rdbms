package com.github.btafarelo.restdb.processors;

import com.github.btafarelo.restdb.http.Input;
import com.github.btafarelo.restdb.jdbc.Database;
import com.github.btafarelo.restdb.jdbc.Datasource;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Get {

    private static final String SQL = "select * from %s";

    private static final String SQL_BY_ID = SQL + " where id = ?";

    private Input input;

    public Get(final Input input) {
        this.input = input;
    }

    public List<Map<String, Object>> doGetById()
            throws IOException {

        return Datasource.query(String.format(SQL_BY_ID, input.getTableName()),
                input.getParams());
    }

    public List<Map<String, Object>> doGet() {
        return Datasource.query(Database.SQL.get(
                input.getTableName()).get("SELECT"), null);
    }
}
