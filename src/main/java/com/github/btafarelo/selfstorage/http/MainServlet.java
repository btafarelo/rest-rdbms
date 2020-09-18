package com.github.btafarelo.selfstorage.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.btafarelo.selfstorage.processors.Get;
import com.github.btafarelo.selfstorage.processors.Post;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet("/*")
public class MainServlet extends HttpServlet {

    private final Logger logger = Logger.getLogger(MainServlet.class.getName());

    private static final Pattern FIND_BY_ID_REGEX = Pattern.compile("^\\/(.*\\/?)+([0-9]+)$");

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private String schema;

    private String table;

    private long id;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if ("POST".equals(req.getMethod()))
            if (!"application/json".equals(req.getContentType()))
                resp.sendError(400, "Content type not allowed.");

        table = req.getPathInfo().split("/")[1];

        super.service(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        List result = null;
        
        resp.setContentType("application/json");

        final Get get = new Get(schema, table);

        if (isGetById(req)) {
            result = get.doGet(id);
        } else {
            result = get.doGet();
        }

        if (result != null)
            objectMapper.writeValue(resp.getOutputStream(), result);
    }

    private boolean isGetById(HttpServletRequest req) {
        boolean result = false;

        final Matcher matcher = FIND_BY_ID_REGEX.matcher(req.getPathInfo());

        if ((result = matcher.matches())) {
            id = Long.parseLong(matcher.group(2));
        }

        return result;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws  IOException {

        Post post = new Post(schema, table);

        post.doPost(getContent(req), resp.getOutputStream());
    }

    private Map getContent(HttpServletRequest req) throws IOException {
        return objectMapper.readValue(req.getInputStream(), Map.class);
    }
}
