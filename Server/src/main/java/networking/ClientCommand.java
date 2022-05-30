package networking;

import db.dao.UserDAO;
import exceptions.UserNotFoundException;
import objects.User;
import utilitaries.ConnectionTimeout;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

/**
 * Class for managing commands given by a <code>Client</code> instance to the <code>Server</code>
 */
public class ClientCommand implements Runnable {
    DataInputStream read;
    DataOutputStream write;
    Socket socket;

    /**
     * Opens sockets for communicating with the <code>Client</code>
     *
     * @param socket socket to be opened
     * @throws IOException thrown in case of socket creation failure
     */
    public ClientCommand(Socket socket) throws IOException {
        this.read = new DataInputStream(socket.getInputStream());
        this.write = new DataOutputStream(socket.getOutputStream());
        this.socket = socket;
    }

    /**
     * Override for <code>run()</code> that reads and manages client commands
     */
    @Override
    public void run() {
        String line;
        User connectedUser = null;
        boolean exit = false;
        ConnectionTimeout connectionTimeout = new ConnectionTimeout(300);
        Thread thread = new Thread(connectionTimeout);
        thread.start();
        while (!exit) {
            try {
                line = read.readUTF();
                System.out.print("Received command: ");
                System.out.println(line);
                String[] components = line.split(" ");

                System.out.println(Server.getUserCounter());
                System.out.println(Server.isUseCounter());

                if (connectionTimeout.isConnectionTimedOut()) { // && connectedUser != null) {
                    write.writeUTF("Connection timed out.\n");
                    exit = true;
                    System.exit(0);
                }

                switch (components[0]) {
                    case "exit" -> {
                        int userCounter = Server.getUserCounter();
                        Server.setUserCounter(userCounter - 1);
                        write.writeUTF("Client exited.\n");
                        exit = true;
                        connectionTimeout.resetTimeout();

                        if (Server.isUseCounter() && Server.getUserCounter() == 0) {
                            System.exit(0);
                        }
                    }
                    case "stop" -> {
                        if (connectedUser == null)
                            write.writeUTF("Must be logged in to use this command.\nEnter command: ");
                        else if (!Server.isUseCounter()) {
                            Server.setUseCounter(true);
                            write.writeUTF("Sent 'stop' command to server. It will shut down when no other users are connected.\nEnter command: ");
                        } else
                            write.writeUTF("Sent 'stop' command to server. It will shut down when no other users are connected.\nEnter command: ");
                    }
                    case "register" -> {
                        if (components.length != 2)
                            write.writeUTF("Name should be a word!\nEnter command: ");
                        else if (connectedUser != null)
                            write.writeUTF("Can't register while logged in.\nEnter command: ");
                        else {
                            Integer id = UserDAO.findByName(components[1]);

                            if (id != null)
                                write.writeUTF("User already exists.\nEnter command: ");
                            else {
                                UserDAO.create(components[1]);
                                write.writeUTF("User " + components[1] + " was registered.\nEnter command: ");
                            }
                        }
                        connectionTimeout.resetTimeout();
                    }
                    case "login" -> {
                        if (components.length != 2)
                            write.writeUTF("Name should be a word!\nEnter command: ");
                        else if (connectedUser != null)
                            write.writeUTF("Can't log in while logged in.\nEnter command: ");
                        else {
                            try {
                                Integer id = UserDAO.findByName(components[1]);

                                if (id == null) {
                                    write.writeUTF("User doesn't exist.\nEnter command: ");
                                    throw new UserNotFoundException("User doesn't exist.");
                                } else {
                                    connectedUser = new User(components[1]);
                                    write.writeUTF("User " + components[1] + " connected.\nEnter command: ");
                                }

                            } catch (UserNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                        connectionTimeout.resetTimeout();
                    }
                    case "logout" -> {
                        if (connectedUser != null) {
                            write.writeUTF("Logged out successfully.\nEnter command: ");
                            connectedUser = null;
                        } else {
                            write.writeUTF("Cannot log out while not being logged in.\nEnter command: ");
                        }
                        connectionTimeout.resetTimeout();
                    }
                    case "open_map" -> write.writeUTF("Open map.\n");
                    case "help" -> {
                        String message;
                        if (connectedUser == null) {
                            message = """
                                    List of available commands:
                                    \t1. login
                                    \t2. register
                                    \t3. exit
                                    """;
                        } else {
                            message = """
                                    List of available commands:
                                    \t1. open_map
                                    \t2. stop
                                    \t3. exit
                                    """;
                        }
                        write.writeUTF(message + "\nEnter command:");
                    }
                    default -> {
                        write.writeUTF("Command unknown.\nEnter command: ");
                        connectionTimeout.resetTimeout();
                    }
                }
            } catch (IOException | SQLException ex) {//| SQLException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("Closing connection...");

        try {
            socket.close();
            read.close();
            write.close();
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }
}

