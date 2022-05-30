package networking;

import map.RouteSeeker;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Client class that manages commands to the server in regard to working with the application
 */
public class Client {
    public Client(String address, int port) throws IOException{
        Socket socket;
        DataInputStream read;
        DataOutputStream write;
        Thread map;
        RouteSeeker routeSeeker = new RouteSeeker();

        socket = new Socket(address, port);
        read = new DataInputStream(socket.getInputStream());
        write = new DataOutputStream(socket.getOutputStream());
        System.out.println("Connected");

        String line = "";
        String response;
        Scanner scanner = new Scanner(System.in);

        while (!line.equals("exit")) {
            line = scanner.nextLine();
            write.writeUTF(line);
            response = read.readUTF();
            System.out.print(response);

            if("Open map.\n".equals(response))
            {
                map = new Thread(routeSeeker);
                map.start();
            }
            if ("Server stopped.\n".equals(response)) {
                break;
            }
            if ("Connection timed out.\n".equals(response)) {
                break;
            }
        }

        write.close();
        read.close();
        socket.close();

    }
}
