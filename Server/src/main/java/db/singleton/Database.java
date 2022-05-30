package db.singleton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String URL = "jdbc:postgresql://localhost:5432/route_seeker";
    private static final String USER = "postgres";
    private static final String PASS = "cucuruz2002";
    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            createConnection();
            System.out.println("Connection was created");
        }
        return connection;
    }

    private static void createConnection() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASS);
            connection.setAutoCommit(false);
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public static void closeConnection() {
        if (connection != null)
            try {
                connection.close();
            } catch (SQLException exception) {
                System.out.println(exception.getMessage());
            }
    }
}