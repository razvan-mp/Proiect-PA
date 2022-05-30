package db.dao;

import db.singleton.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public static void create(String name) throws SQLException {
        Connection con = Database.getConnection();
        try (PreparedStatement pstmt = con.prepareStatement(
                "insert into users (name) values (?)")) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            con.commit();
        }
    }

    public static String findById(Integer userId) throws SQLException {
        Connection connection = Database.getConnection();

        Statement statement = connection.createStatement();

        String sql = "SELECT name from USERS WHERE id=" + userId;

        ResultSet resultSet = statement.executeQuery(sql);
        return resultSet.next() ? resultSet.getString(1) : null;
    }

    public static Integer findByName(String name) throws SQLException {
        Connection connection = Database.getConnection();

        Statement statement = connection.createStatement();

        String sql = "SELECT id from users where name='" + name + "'";

        ResultSet result = statement.executeQuery(sql);
        return result.next() ? result.getInt(1) : null;
    }
}
