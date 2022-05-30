package db.dao;

import db.singleton.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * UserDAO for working with the <code>users</code> database
 */
public class UserDAO {
    /**
     * Adds a new user to the database
     * @param name name of the user to be added
     * @throws SQLException thrown in case of an event on <code>insert</code>
     */
    public static void create(String name) throws SQLException {
        Connection con = Database.getConnection();
        try (PreparedStatement pstmt = con.prepareStatement(
                "insert into users (name) values (?)")) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            con.commit();
        }
    }

    /**
     * Finds a user based on the ID provided
     * @param userId ID to be searched
     * @return name of the user
     * @throws SQLException thrown in case of an event on <code>select</code>
     */
    public static String findById(Integer userId) throws SQLException {
        Connection connection = Database.getConnection();

        Statement statement = connection.createStatement();

        String sql = "SELECT name from USERS WHERE id=" + userId;

        ResultSet resultSet = statement.executeQuery(sql);
        return resultSet.next() ? resultSet.getString(1) : null;
    }

    /**
     * Finds a user based on the name provided
     * @param name name of the user to be searched
     * @return ID of the user searched
     * @throws SQLException thrown in case of an event on <code>select</code>
     */
    public static Integer findByName(String name) throws SQLException {
        Connection connection = Database.getConnection();

        Statement statement = connection.createStatement();

        String sql = "SELECT id from users where name='" + name + "'";

        ResultSet result = statement.executeQuery(sql);
        return result.next() ? result.getInt(1) : null;
    }
}
