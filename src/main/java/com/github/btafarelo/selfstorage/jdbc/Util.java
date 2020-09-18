package com.github.btafarelo.selfstorage.jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Util {

    static List<Map<String, Object>> resultSetToMap(final ResultSet resultSet)
            throws SQLException {

        final List<Map<String, Object>> result = new ArrayList<>();

        final ResultSetMetaData metaData = resultSet.getMetaData();
        final int count = metaData.getColumnCount();

        Map<String, Object> row = null;

        while (resultSet.next()) {
            row = new HashMap<>();

            for (int i = 1; i<=count; i++) {
                row.put(metaData.getColumnName(i), resultSet.getString(i));
            }

            result.add(row);
        }

        return result;
    }
}
