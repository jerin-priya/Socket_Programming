package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandlerSystem implements Runnable {
    private Socket clientSocket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private int clientNumber;

    public ClientHandlerSystem(Socket clientSocket, int clientNumber) {
        this.clientSocket = clientSocket;
        this.clientNumber = clientNumber;
    }


    public void run() {
        try {
            dataInputStream = new DataInputStream(clientSocket.getInputStream());
            dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

            String message;
            while (true) {
                message = dataInputStream.readUTF();
                System.out.println("Client-" + clientNumber + " says: " + message);

                if (message.equalsIgnoreCase("_stop"))
                    break;
            }
            clientSocket.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void sendMessage(String message) throws IOException {
        dataOutputStream.writeUTF(message);
        dataOutputStream.flush();
    }
}

