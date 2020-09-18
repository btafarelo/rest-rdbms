package com.github.btafarelo.selfstorage.listeners;

import com.github.btafarelo.selfstorage.jdbc.Datasource;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

@WebListener
public class ContextListener implements ServletContextListener {

    private final Logger logger = Logger.getLogger(ContextListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        logger.info("Initializing the database.");

        try {
            final String file = getClass().getResource("/script.sql") + "";

            logger.info("Script file: " + file);

            InputStream stream = getClass().getResourceAsStream("/script.sql");

            final BufferedReader reader = new BufferedReader(
                    new InputStreamReader(stream));

            final StringBuilder script = new StringBuilder();

            while (reader.ready())
                script.append(reader.readLine());

            for(String s : script.toString().split(";"))
                Datasource.execute(s);
        } catch(Exception ex) {
            throw new RuntimeException("Wasn't possible to initialize the database.", ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
