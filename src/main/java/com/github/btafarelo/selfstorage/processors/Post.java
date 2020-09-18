package com.github.btafarelo.selfstorage.processors;

import com.github.btafarelo.selfstorage.jdbc.Datasource;

import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

public class Post {

    private static final Logger logger = Logger.getLogger(Post.class.getName());

    private static final String SQL = "insert into %s (%s) values (%s)";

    private final String schema;

    private final String table;

    public Post(final String schema, final String table) {
        this.schema = schema;
        this.table = table;
    }

    public void doPost(final Map<String, String> input, final OutputStream resp) {

        final StringBuilder columns = new StringBuilder();
        final StringBuilder values = new StringBuilder();

        for(Iterator i = input.keySet().iterator(); i.hasNext(); ) {
            columns.append(i.next());
            values.append("?");

            if (i.hasNext()) {
                columns.append(", ");
                values.append(", ");
            }
        }

        Datasource.execute(String.format(SQL, table, columns, values), input);
    }
}
