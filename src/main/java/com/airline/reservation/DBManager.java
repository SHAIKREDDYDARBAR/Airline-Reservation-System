package com.airline.reservation;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DBManager {
    private static final String URL = "jdbc:mysql://localhost:3306/airline_db";
    private static final String USER = "root"; // <-- REPLACE THIS
    private static final String PASSWORD = "----------"; // <-- REPLACE THIS
    public static Connection getConnection() throws SQLException {
        try {
            // Ensure the JDBC driver is loaded
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            // This error occurs if the mysql-connector-j-x.x.x.jar is not in WEB-INF/lib
            throw new SQLException("MySQL JDBC Driver not found. Please add the JAR file to WEB-INF/lib.", e);
        }
    }
}

