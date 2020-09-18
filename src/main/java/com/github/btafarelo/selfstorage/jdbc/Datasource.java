package com.github.btafarelo.selfstorage.jdbc;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Datasource {

    private static DataSource ds;

    private Datasource() {
    }

    private static Connection getConnection() throws NamingException, SQLException {

        if (ds == null) {
            Context ctx = new InitialContext();
            ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/MyDB");
        }

        return ds.getConnection();
    }

    public static void execute(final String sql, final Map<String, String> input) {
        Connection cnn = null;

        try {
            try {
                cnn = Datasource.getConnection();

                PreparedStatement stmt = cnn.prepareStatement(sql);

                int x = 1;

                for (Iterator i = input.keySet().iterator(); i.hasNext(); ) {
                    final String value = input.get(i.next());
                    stmt.setString(x++, value);
                }

                stmt.execute();
            } finally {
                if (cnn != null)
                    cnn.close();
            }
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
    }

    public static List<Map<String, Object>> query(final String sql,
            final Map<String, String> input) {

        List<Map<String, Object>> result = null;

        Connection cnn = null;

        try {
            try {
                cnn = Datasource.getConnection();

                PreparedStatement stmt = cnn.prepareStatement(sql);

                if (input != null) {
                    int x = 1;

                    for (Iterator it = input.keySet().iterator(); it.hasNext();) {
                        final String value = input.get(it.next());
                        stmt.setString(x++, value);
                    }
                }

                result = Util.resultSetToMap(stmt.executeQuery());
            } finally {
                if (cnn != null)
                    cnn.close();
            }
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static void execute(final String sql) {
        Connection cnn = null;

        try {
            try {
                cnn = Datasource.getConnection();

                cnn.prepareStatement(sql).execute();
            } finally {
                if (cnn != null)
                    cnn.close();
            }
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
    }
}
