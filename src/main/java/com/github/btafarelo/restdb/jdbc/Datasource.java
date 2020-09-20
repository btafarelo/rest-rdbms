package com.github.btafarelo.restdb.jdbc;

import com.github.btafarelo.restdb.http.Input;

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

    public static int execute(final Input input) {
        Connection cnn = null;

        try {
            try {
                final String sql = Database.SQL.get(input.getTableName()).
                        get(input.getMethod());

                final Matcher matcher = PARAM_NAME.matcher(sql);

                final List<String> paramList = new ArrayList<>();

                while (matcher.find())
                    paramList.add(matcher.group(1));

                cnn = Datasource.getConnection();

                final PreparedStatement stmt = cnn.prepareStatement(
                        matcher.replaceAll( "?"));

                final int count = stmt.getParameterMetaData().getParameterCount();

                for (int i=0; i < count; i++)
                    stmt.setString(i+1, input.getParams().get(paramList.get(i)));

                return stmt.executeUpdate();
            } finally {
                if (cnn != null)
                    cnn.close();
            }
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public static List<Map<String, Object>> query(final Input input) {

        List<Map<String, Object>> result = null;

        Connection cnn = null;

        try {
            try {
                cnn = Datasource.getConnection();

                final StringBuilder sql = new StringBuilder();
                sql.append(Database.SQL.get(input.getTableName()).
                        get(input.getMethod()));

                Iterator<String> it = input.getParams().keySet().iterator();

                if (it.hasNext()) {
                    sql.append(" where ");

                    String name = null;

                    while (it.hasNext()) {
                        name = it.next();

                        sql.append(name);
                        sql.append("=:");
                        sql.append(name);

                        if (it.hasNext()) {
                            sql.append(" and ");
                        }
                    }
                }

                final Matcher matcher = PARAM_NAME.matcher(sql);

                final List<String> paramList = new ArrayList<>();

                while (matcher.find())
                    paramList.add(matcher.group(1));

                cnn = Datasource.getConnection();

                final PreparedStatement stmt = cnn.prepareStatement(
                        matcher.replaceAll( "?"));

                final int count = stmt.getParameterMetaData().getParameterCount();

                for (int i=1 ; i<=count;) {
                    String value = input.getParams().get(paramList.get(i-1));
                    stmt.setString(i++, value);
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
}
