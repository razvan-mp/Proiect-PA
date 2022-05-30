package networking;

import java.io.IOException;
import java.net.Socket;

/**
 * Class for initializing sockets
 */
public class ServerSocket {
    int port;
    Socket socket = null;
    java.net.ServerSocket server = null;

    /**
     * Assigns port and calls <code>initalizeServer()</code>
     * @param port port to be assigned
     */
    public ServerSocket(int port) {
        this.port = port;
        initializeServer();
    }

    private void initializeServer() {
        try {
            server = new java.net.ServerSocket(port);
            System.out.println("Server started");

            System.out.println("Waiting for a client ...");
        } catch (IOException exception) {
            exception.getStackTrace();
        }
    }

}
