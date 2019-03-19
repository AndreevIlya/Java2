package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

class ChatServer {
    /*private static Scanner scanner = new Scanner(System.in);
    private final static int PORT = 8080;

    static void runServer() {
        try(ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started");
            Socket socket = serverSocket.accept();
            System.out.println("Client connected " + socket);

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
            threadIn.setDaemon(true);
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
            threadOut.setDaemon(true);
            threadOut.start();
            System.out.println("In and out threads started.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
    private static ClientStorage clientStorage = new ClientStorage();
    //private static MessageService messageService = new MessageService(clientStorage);

    public static void main(String[] args) throws IOException {

        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("server started");
            ClientsDB clientsDB = new ClientsDB();
            while (true) {
                Socket socket = serverSocket.accept();
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

                String[] data = inputStream.readUTF().split("&");
                if(clientsDB.isClientNotInDB(data[0])){
                    clientsDB.addToDB(data[0],data[1]);
                    Client client = new Client(data[0],data[1], inputStream, outputStream);
                    System.out.println("New client connected:" + client + "::" + socket);
                    clientStorage.addClient(client);
                    outputStream.writeUTF(data[0]);
                }else{
                    if(clientsDB.checkAuth(data[0],data[1])){
                        Client client = new Client(data[0],data[1], inputStream, outputStream);
                        System.out.println("Old client connected:" + client + "::" + socket);
                        clientStorage.addClient(client);
                        outputStream.writeUTF(data[0]);
                    }else{
                        System.out.println("Attempt to login failed for " + data[0] + " " + data[1]);
                        outputStream.writeUTF("Failed.");
                    }
                }
                /*new Thread(() -> new ClientServiceImpl(client, messageService, clientStorage)
                        .processMessage()).start();*/
            }
        }
    }
}