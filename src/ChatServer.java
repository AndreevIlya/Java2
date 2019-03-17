import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ChatServer {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try(ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("server started");
            Socket socket = serverSocket.accept();
            System.out.println("client connected " + socket);

            Thread threadIn = new Thread(() -> {
                try (DataInputStream inputStream = new DataInputStream(socket.getInputStream())) {
                    while (true) {
                        String message = inputStream.readUTF();
                        System.out.println("Received message: " + message);
                    }
                } catch (IOException e) {
                        e.printStackTrace();
                }
            });
            threadIn.start();
            Thread threadOut = new Thread(() -> {
                try (DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())) {
                    while (true) {
                        String message = scanner.nextLine();
                        outputStream.writeUTF(message);
                    }
                } catch (IOException e) {
                        e.printStackTrace();
                }
            });
            threadOut.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}