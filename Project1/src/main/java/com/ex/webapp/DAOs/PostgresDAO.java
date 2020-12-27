package com.ex.webapp.DAOs;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public abstract class PostgresDAO {
    protected String username;
    protected String password;
    protected String url;

    /**
     * This constructor creates a Data Access Object.
     *
     * @author Timothy Cahill
     * */
    public PostgresDAO() {
        try {
            Properties dbProperties = new Properties();
            dbProperties.load(this.getClass().getResourceAsStream("../../../../db.properties"));

            this.url = dbProperties.getProperty("db.url");
            this.username = dbProperties.getProperty("db.username");
            this.password = dbProperties.getProperty("db.password");
        }
        catch (IOException |NullPointerException e) {
            System.out.println("Unable to load db.properties.");
            System.out.println(e.getMessage());
        }
    }

    /**
     * This method returns an active connection to the database.
     *
     * @author Timothy Cahill
     * */
    protected Connection getConnection() {
        try {
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return DriverManager.getConnection(this.url, this.username, this.password);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
