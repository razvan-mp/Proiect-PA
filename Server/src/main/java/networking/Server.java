package networking;

import java.io.IOException;

/**
 * Main <code>Server</code> class that manages the incoming connections and creates a separate thread for
 * each connection
 */
public class Server {
    public static boolean serverRunning = true;
    private static int userCounter = 0;
    private static boolean useCounter = false;

    public Server(int port) throws IOException {
        ServerSocket serverSocketClass = new ServerSocket(port);

        while (serverRunning) {
            serverSocketClass.socket = serverSocketClass.server.accept();
            System.out.println("Client accepted");
            userCounter++;
            ClientCommand clientCommand = new ClientCommand(serverSocketClass.socket);
            Thread client = new Thread(clientCommand);
            client.start();

            if (userCounter == 0 && useCounter)
                serverRunning = false;
        }
    }

    public static int getUserCounter() {
        return userCounter;
    }

    public static void setUserCounter(int userCounter) {
        Server.userCounter = userCounter;
    }

    public static boolean isUseCounter() {
        return useCounter;
    }

    public static void setUseCounter(boolean useCounter) {
        Server.useCounter = useCounter;
    }
}