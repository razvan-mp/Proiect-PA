package app;

import networking.Server;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Main class that starts the server and begins accepting connections
 */
public class Main {
    public static void main(String[] args) {
        try {
            new Server(5000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
