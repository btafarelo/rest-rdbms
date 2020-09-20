package com.github.btafarelo.restdb.jdbc;

import javax.naming.NamingException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static java.lang.String.format;

public class Database {

    private final static Logger LOG = Logger.getLogger(Database.class.getName());

    private static final String SELECT = "select * from %s";

    private static final String INSERT = "insert into %s (%s) values (%s)";

    private static final String DELETE = "delete from %s where id = :id";

    private static final String UPDATE = "update %s set %s where id = :id";

    public static final Map<String, Map<String, String>> SQL = new HashMap<>();

    public static void initializer() {
        LOG.info("Initializing the database.");

        try {
            final String file = Database.class.getResource("/script.sql") + "";

            LOG.info("Script file: " + file);

            final BufferedReader reader = new BufferedReader(
                    new InputStreamReader(Database.class.
                            getResourceAsStream("/script.sql")));

            final StringBuilder script = new StringBuilder();

            while (reader.ready())
                script.append(reader.readLine());

            for(String s : script.toString().split(";"))
                Datasource.execute(s);

            prepareSQL();
        } catch(Exception ex) {
            throw new RuntimeException("Wasn't possible to initialize the database.", ex);
        }
    }

    private static void prepareSQL() throws SQLException, NamingException {
        final Connection cnn = Datasource.getConnection();
        final DatabaseMetaData metaData = cnn.getMetaData();
        final ResultSet tables = metaData.getTables(null, null, null,
                new String[]{"TABLE"});

        String table, columns, insertValues, updateSet;

        Map<String, String> operations = null;

        while (tables.next()) {
            table = tables.getString(3);

            columns = getColumns(metaData, table);

            insertValues = columns.replaceAll("([^,]+)", ":$1");

            updateSet = columns.replaceAll("([^,]+)", "$1 = :$1");

            operations = new HashMap<>();
            operations.put("SELECT", format(SELECT, table));
            operations.put("DELETE", format(DELETE, table));
            operations.put("INSERT", format(INSERT, table, columns, insertValues));
            operations.put("UPDATE", format(UPDATE, table, updateSet));

            SQL.put(table.toLowerCase(), operations);

            for (String s : operations.values()) {
                LOG.info(s);
            }
        }

        tables.close();
        cnn.close();
    }

    private static String getColumns(DatabaseMetaData metaData, String table)
            throws SQLException {
        final StringBuilder result = new StringBuilder();

        final ResultSet rs = metaData.getColumns(null, null, table, null);

        while (rs.next()) {
            if ("YES".equals(rs.getString("IS_AUTOINCREMENT")))
                continue;

            result.append(rs.getString("COLUMN_NAME").toLowerCase());

            if (!rs.isLast())
                result.append(",");
        }

        rs.close();

        return result.toString();
    }
}
