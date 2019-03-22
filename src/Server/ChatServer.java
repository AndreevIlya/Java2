package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

class ChatServer {
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
                System.out.println("New " + data[0]);
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
                }else if (data[0].equals("exit")) {
                    System.out.println("Exit.");
                    try {
                        System.out.println("Socket closed.");
                        socket.close();
                    } catch (IOException e) {
                        System.out.println("Unable to close socket");
                        e.printStackTrace();
                    }
                }
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
        ClientService clientService = new ClientService(client, messageService, clientStorage);
        clientService.processMessage(data[1] + " enters chat.");
        startListenThread(client,socket, clientService);
        return client;
    }

    private static void startListenThread(Client client, Socket socket,ClientService clientService){
        Thread logoutThread = new Thread(() -> {
            while (true) {
                try {
                    listenToInputStream(client, socket,clientService);
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

    private static void listenToInputStream (Client client, Socket socket, ClientService clientService) throws IOException {
        if (!socket.isClosed()) {
            String[] data = client.getInputStream().readUTF().split("&");
            System.out.println("listen " + data[0]);
            switch (data[0]) {
                case "login":
                    if (clientsDB.checkAuth(data[1], data[2])) {
                        System.out.println("Client connected again: " + client + "::" + socket);
                        clientStorage.addClient(client);
                        client.getOutputStream().writeUTF("logged");
                        clientService.processMessage(data[1] + " enters chat.");
                    } else {
                        System.out.println("Attempt to login failed for " + data[1] + " " + data[2]);
                        client.getOutputStream().writeUTF("fail");
                    }
                    break;
                case "logout":
                    System.out.println("Client disconnected:" + client + "::" + socket);
                    client.getOutputStream().writeUTF("logout");
                    clientStorage.removeClient(client);
                    clientService.processMessage(data[1] + " leaves chat.");
                    break;
                case "logoutFull":
                    System.out.println("Client is off.");
                    clientStorage.removeClient(client);
                    try {
                        System.out.println("Socket for " + client + " is off");
                        clientService.processMessage(data[1] + " leaves chat.");
                        socket.close();
                    } catch (IOException e) {
                        System.out.println("Unable to close socket");
                        e.printStackTrace();
                    }
                    break;
                case "exit":
                    System.out.println("Exit.");
                    try {
                        System.out.println("Socket closed.");
                        socket.close();
                    } catch (IOException e) {
                        System.out.println("Unable to close socket");
                        e.printStackTrace();
                    }
                    break;
                case "message":
                    System.out.println("Received message");
                    clientService.processMessage(data[1]);
                    break;
                case "pm":
                    System.out.println("Received private message");
                    clientService.processPrivateMessage(data[1],data[2]);
            }
        }
    }
}