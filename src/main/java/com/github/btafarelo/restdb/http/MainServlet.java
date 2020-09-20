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

@WebServlet("/*")
public class MainServlet extends HttpServlet {

    private final Logger logger = Logger.getLogger(MainServlet.class.getName());

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
}
