package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

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
    private static ClientsDB clientsDB = new ClientsDB();
    private static ClientStorage clientStorage = new ClientStorage();
    private static MessageService messageService = new MessageService(clientStorage);

    public static void main(String[] args) throws IOException {

        try (ServerSocket serverSocket = new ServerSocket(4444)) {
            System.out.println("server started");
            while (true) {
                Socket socket = serverSocket.accept();

                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                String[] data = inputStream.readUTF().split("&");
                System.out.println("New" + data[0]);
                if (data[0].equals("login")) {
                    if (clientsDB.isClientNotInDB(data[1])) {
                        clientsDB.addToDB(data[1], data[2]);
                        Client client = createClient(data,inputStream,outputStream,socket);
                        System.out.println("New client connected:" + client + "::" + socket);
                    } else {
                        if (clientsDB.checkAuth(data[1], data[2])) {
                            Client client = createClient(data,inputStream,outputStream,socket);
                            System.out.println("Old client connected:" + client + "::" + socket);
                        } else {
                            System.out.println("Attempt to login failed for " + data[1] + " " + data[2]);
                            outputStream.writeUTF("fail");
                            startLoginAfterFailureThread(socket, inputStream, outputStream);
                        }
                    }
                }
            }
        }
    }

    private static boolean loginAfterFailure(Socket socket,DataInputStream inputStream,DataOutputStream outputStream) throws IOException{
        String[] data = inputStream.readUTF().split("&");
        System.out.println("After failure " + data[0]);
        if (data[0].equals("login")) {
            if (clientsDB.isClientNotInDB(data[1])) {
                clientsDB.addToDB(data[1], data[2]);
                Client client = createClient(data,inputStream,outputStream,socket);
                System.out.println("New client connected:" + client + "::" + socket);
                return true;
            } else {
                if (clientsDB.checkAuth(data[1], data[2])) {
                    Client client = createClient(data,inputStream,outputStream,socket);
                    System.out.println("Old client connected:" + client + "::" + socket);
                    return true;
                } else {
                    if(clientStorage.containsClient(data[1])){
                        System.out.println(data[1] + " is already logged in.");
                        outputStream.writeUTF("occupied");
                    }else {
                        System.out.println("Attempt to login failed for " + data[1]);
                        outputStream.writeUTF("fail");
                    }
                }
            }
        }
        return false;
    }

    private static void listenToInputStream (Client client, Socket socket) throws IOException {
        if (!socket.isClosed()) {
            String[] data = client.getInputStream().readUTF().split("&");
            System.out.println("listen " + data[0]);
            switch (data[0]) {
                case "login":
                    if (clientsDB.checkAuth(data[1], data[2])) {
                        System.out.println("Client connected again: " + client + "::" + socket);
                        clientStorage.addClient(client);
                        client.getOutputStream().writeUTF("logged");
                        client.addLogins();
                    } else {
                        System.out.println("Attempt to login failed for " + data[1] + " " + data[2]);
                        client.getOutputStream().writeUTF("fail");
                    }
                    break;
                case "logout":
                    System.out.println("Client disconnected:" + client + "::" + socket);
                    client.getOutputStream().writeUTF("logout");
                    clientStorage.removeClient(client);
                    break;
                case "logoutFull":
                    System.out.println("Client is off.");
                    try {
                        System.out.println("Socket for " + client + " is off");
                        socket.close();
                    } catch (IOException e) {
                        System.out.println("Unable to close socket");
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    private static Client createClient(
            String[] data,
            DataInputStream inputStream,
            DataOutputStream outputStream,
            Socket socket) throws IOException{
        Client client = new Client(data[1], data[2], inputStream, outputStream);
        clientStorage.addClient(client);
        outputStream.writeUTF("logged");
        client.addLogins();
        startListenThread(client,socket);
        new Thread(() -> new ClientService(client, messageService, clientStorage)
                .processMessage()).start();
        return client;
    }

    private static void startListenThread(Client client, Socket socket){
        Thread logoutThread = new Thread(() -> {
            while (true) {
                try {
                    listenToInputStream(client, socket);
                }catch (SocketException e) {
                    e.printStackTrace();
                    System.out.println("Listener failed");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        logoutThread.setDaemon(true);
        logoutThread.start();
    }

    private static void startLoginAfterFailureThread(Socket socket,DataInputStream inputStream,DataOutputStream outputStream){
        Thread loginAfter = new Thread(() -> {
            while (true) {
                try {
                    if(loginAfterFailure(socket,inputStream,outputStream)) return;
                } catch (IOException e) {
                    System.out.println("Failed after failure");
                    e.printStackTrace();
                }
            }
        });
        loginAfter.setDaemon(true);
        loginAfter.start();
    }
}