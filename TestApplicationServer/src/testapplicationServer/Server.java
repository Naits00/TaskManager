package testapplicationServer;

import java.io.*;
import java.net.*;

public class Server {

    private static final int PORT = 6666; // случайный порт (может быть любое число от 1025 до 65535)
    private static Socket clientSocket = null;
    private static ServerSocket serverSocket = null;

    public static void main(String[] ar) {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Ждем подключения клиента!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                clientSocket = serverSocket.accept();
                System.out.println("Клиент подключен!");
            } catch (IOException e) {
                System.out.println("I/O error: " + e);
            }
           new Thread (new SingleThread(clientSocket)).start();

        }
    }

}
