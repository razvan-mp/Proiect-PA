package app;

import networking.Client;

import java.io.IOException;

/**
 * Main class that initializes a connection and starts the application
 */
public class Main {
    public static void main(String[] args) {
        try {
            new Client("127.0.0.1", 5000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}