package com.github.btafarelo.restdb.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.btafarelo.restdb.jdbc.Datasource;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet("/*")
public class MainServlet extends HttpServlet {

    private final Logger logger = Logger.getLogger(MainServlet.class.getName());

    private static final Pattern FIND_BY_ID_REGEX = Pattern.compile("^\\/(.*\\/?)+([0-9]+)$");

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private Input input;
    
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
            
        this.input = new Input(req);

        if ("GET".equals(req.getMethod())) {
            resp.setContentType("application/json");
            objectMapper.writeValue(resp.getOutputStream(), Datasource.query(input));
        } else
            Datasource.execute(input);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
/*
        List result = null;

        final Get get = new Get(input);

        if (isGetById(req))
            result = get.doGetById();
        else
            result = get.doGet();

        resp.setContentType("application/json");

        if (result != null && !result.isEmpty())
            objectMapper.writeValue(resp.getOutputStream(), result);
 */
    }

    private boolean isGetById(HttpServletRequest req) {
        final Matcher matcher = FIND_BY_ID_REGEX.matcher(req.getPathInfo());

        return matcher.matches();
    }
}
