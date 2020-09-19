package com.github.btafarelo.selfstorage.processors;

import com.github.btafarelo.selfstorage.jdbc.Datasource;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Put {

    private static final String SQL = "update %s set %s where id = ?";

    private String schema;

    private String table;

    public Put(String schema, String table) {
        this.schema = schema;
        this.table = table;
    }

    public void doPut(final Map<String, String> input, final long id) {

        final StringBuilder params = new StringBuilder();

        for(Iterator i = input.keySet().iterator(); i.hasNext(); ) {
            params.append(i.next());
            params.append("=?");

            if (i.hasNext())
                params.append(", ");
        }

        input.put("id", "" + id);

        Datasource.execute(String.format(SQL, table, params), input);
    }
}
