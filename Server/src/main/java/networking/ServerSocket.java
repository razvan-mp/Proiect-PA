package networking;

import java.io.IOException;
import java.net.Socket;

public class ServerSocket {
    int port;
    Socket socket = null;
    java.net.ServerSocket server = null;


    public ServerSocket(int port) {
        this.port = port;
        initializeServer();
    }

    private void initializeServer() {
        // starts server and waits for a connection
        try {
            server = new java.net.ServerSocket(port);
            System.out.println("Server started");

            System.out.println("Waiting for a client ...");
        } catch (IOException exception) {
            exception.getStackTrace();
        }
    }

}
