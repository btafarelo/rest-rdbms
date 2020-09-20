package com.github.btafarelo.restdb.http;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Input {

    private static final Pattern FIND_BY_ID_REGEX = Pattern.compile("^\\/(.*\\/?)+([0-9]+)$");

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final String tableName;

    private final String method;

    private final Map<String, String> params;

    public Input(final HttpServletRequest request) throws IOException {
        method = request.getMethod();

        tableName = request.getPathInfo().split("\\/")[1];

        if (request.getInputStream().available() > 0)
            params = objectMapper.readValue(request.getInputStream(), Map.class);
        else
            params = new HashMap<>();

        final Matcher matcher = FIND_BY_ID_REGEX.matcher(request.getPathInfo());

        if (matcher.matches())
            params.put("id", matcher.group(2));
    }

    public String getTableName() {
        return tableName;
    }

    public String getMethod() {
        return method;
    }

    public Map<String, String> getParams() {
        return params;
    }
}
