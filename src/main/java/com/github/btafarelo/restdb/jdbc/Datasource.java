package com.github.btafarelo.restdb.jdbc;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Datasource {

    private static final Logger LOG = Logger.getLogger(Datasource.class.getName());

    private static final Pattern PARAM_NAME = Pattern.compile(":([^ |,|\\)]+)");

    private static DataSource ds;

    private Datasource() {
    }

    static Connection getConnection() throws NamingException, SQLException {

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
                final Matcher matcher = PARAM_NAME.matcher(sql);

                List<String> paramList = new ArrayList<>();

                while (matcher.find())
                    paramList.add(matcher.group(1));

                cnn = Datasource.getConnection();

                final PreparedStatement stmt = cnn.prepareStatement(
                        matcher.replaceAll( "?"));

                for (int i=0; i < paramList.size(); i++)
                    stmt.setString(i+1, input.get(paramList.get(i)));

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
