package com.github.btafarelo.restdb.listeners;

import com.github.btafarelo.restdb.jdbc.Database;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.logging.Logger;

@WebListener
public class ContextListener implements ServletContextListener {

    private final Logger logger = Logger.getLogger(ContextListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        Database.initializer();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
