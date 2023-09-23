package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static Scanner scanner = new Scanner(System.in);
    private static List<ClientHandlerSystem> clients = new ArrayList<>();
    private static int clientCounter = 0;

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(5);

        try {
            ServerSocket serverSocket = new ServerSocket(5001);
            System.out.println("Listening to port: 5001");

            // Start a thread to read and broadcast server input
            Thread inputThread = new Thread(Server::readAndBroadcastInput);
            inputThread.start();

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("client-" + ++clientCounter + " connected");

                // Create a new thread to handle the client
                ClientHandlerSystem clientHandlerSystem = new ClientHandlerSystem(clientSocket, clientCounter);
                clients.add(clientHandlerSystem);
                executor.submit(clientHandlerSystem);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // Broadcast a message to all connected clients
    public static void broadcastMessage(String message) {
        for (ClientHandlerSystem client : clients) {
            try {
                client.sendMessage(message);
            } catch (IOException e) {
                System.out.println("Error broadcasting message: " + e.getMessage());
            }
        }
    }

    // Read input from the server console and broadcast it to clients
    public static void readAndBroadcastInput() {
        while (true) {
            String input = scanner.nextLine();
            broadcastMessage("Server: " + input);
        }
    }
}
