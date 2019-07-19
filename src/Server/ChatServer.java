package Server;

import History.History;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

class ChatServer {
    private static final ClientsDB clientsDB = new ClientsDB();
    private static final ClientStorage clientStorage = new ClientStorage();
    private static final MessageService messageService = new MessageService(clientStorage);
    private static final ExecutorService executorService = Executors.newCachedThreadPool(new ThreadBuilder());
    private static final int PORT = 4444;

    private static final Map<String,Responder> responderMap = initResponderMap();
    private static final History serverHistory = new History(null, "ServerHistory", "sh.txt");
    private static final Logger LOGGER = LogManager.getLogger(ChatServer.class);

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) throws IOException {

        listenExit();
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            LOGGER.info("Server started");
            while (true) {
                Socket socket = serverSocket.accept();

                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                String[] data = inputStream.readUTF().split("&");
                LOGGER.info("Incoming message type: " + data[0]);
                switch (data[0]) {
                    case "login":
                        if (clientsDB.addToDB(data[1], data[2])) {
                            Client client = createClient(data, inputStream, outputStream, socket);
                            LOGGER.info("New client connected:" + client + "::" + socket);
                            serverHistory.writeHistory("New client connected:" + client + "::" + socket);
                        } else {
                            if (clientsDB.checkAuth(data[1], data[2])) {
                                Client client = createClient(data, inputStream, outputStream, socket);
                                LOGGER.info("Old client connected:" + client + "::" + socket);
                                serverHistory.writeHistory("Old client connected:" + client + "::" + socket);
                            } else {
                                LOGGER.info("Attempt to login failed for " + data[1] + " " + data[2]);
                                serverHistory.writeHistory("Attempt to login failed for " + data[1] + " " + data[2]);
                                outputStream.writeUTF("fail");
                                startLoginAfterFailureThread(socket, inputStream, outputStream);
                            }
                        }
                        break;
                    case "exit":
                        try {
                            LOGGER.info("Socket " + socket + " closed.");
                            serverHistory.writeHistory("Socket " + socket + " closed.");
                            socket.close();
                        } catch (IOException e) {
                            LOGGER.error("Unable to close socket" + e);
                        }
                        break;
                    case "reconnect":
                        LOGGER.info("Reconnection started at" + socket);
                        serverHistory.writeHistory("Reconnection started at" + socket);
                        startLoginAfterFailureThread(socket, inputStream, outputStream);
                        break;
                }
            }
        }
    }

    private interface Responder {
        void respond(String[] s,
                     Client client,
                     Socket socket,
                     ClientService clientService) throws IOException;
    }

    private static Map<String, Responder> initResponderMap() {
        Map<String, Responder> map = new HashMap<>();
        map.put("login", (data,client,socket,clientService) -> {
            if (clientsDB.checkAuth(data[1], data[2])) {
                if (data[1].equals(client.getLogin())) {
                    LOGGER.info("Client connected again: " + client + "::" + socket);
                    serverHistory.writeHistory("Client connected again: " + client + "::" + socket);
                } else {
                    LOGGER.info("Client " + data[1] + " connected instead of: " + client + "::" + socket);
                    serverHistory.writeHistory("Client " + data[1] + " connected instead of: " + client + "::" + socket);
                    client.setLogin(data[1]);
                    client.setPassword(data[2]);
                }
                clientStorage.addClient(data[1], client);
                client.getOutputStream().writeUTF("logged");
                clientService.processMessage(data[1] + " enters chat.");
                serverHistory.writeHistory(data[1] + " enters chat.");
            } else {
                LOGGER.info("Attempt to login failed for " + data[1] + " " + data[2]);
                serverHistory.writeHistory("Attempt to login failed for " + data[1] + " " + data[2]);
                client.getOutputStream().writeUTF("fail");
            }
        });
        map.put("logout", (data,client,socket,clientService) -> {
            LOGGER.info("Client disconnected:" + client + "::" + socket);
            serverHistory.writeHistory("Client disconnected:" + client + "::" + socket);
            client.getOutputStream().writeUTF("logout");
            clientStorage.removeClient(client.getLogin());
            clientService.processMessage(data[1] + " leaves chat.");
            serverHistory.writeHistory(data[1] + " leaves chat.");
        });
        map.put("logoutFull", (data,client,socket,clientService) -> {
            LOGGER.info("Client " + client + " is off.");
            clientStorage.removeClient(client.getLogin());
            try {
                LOGGER.info("Socket for " + client + " is off");
                serverHistory.writeHistory("Socket for " + client + " is off");
                clientService.processMessage(data[1] + " leaves chat.");
                serverHistory.writeHistory(data[1] + " leaves chat.");
                socket.close();
            } catch (IOException e) {
                LOGGER.error("Unable to close socket " + socket + " " + e);
                serverHistory.writeHistory("Unable to close socket " + socket);
            }
        });
        map.put("exit", (data,client,socket,clientService) -> {
            System.out.println("Exit.");
            try {
                LOGGER.info("Socket closed " + socket);
                serverHistory.writeHistory("Socket closed " + socket);
                socket.close();
            } catch (IOException e) {
                LOGGER.error("Unable to close socket " + socket + " " + e);
                serverHistory.writeHistory("Unable to close socket" + socket);
            }
        });
        map.put("message", (data,client,socket,clientService) -> {
            LOGGER.info("Received message");
            data[1] = moderateLine(data[1]);
            clientService.processMessage(data[1]);
            serverHistory.writeHistory("Received message: " + data[1]);
        });
        map.put("pm", (data,client,socket,clientService) -> {
            LOGGER.info("Received private message");
            data[2] = moderateLine(data[2]);
            clientService.processPrivateMessage(data[1],data[2]);
            serverHistory.writeHistory("Received private message^ " + data[1] + " " + data[2]);
        });
        return map;
    }

    private static Client createClient(
            @NotNull String[] data,
            DataInputStream inputStream,
            DataOutputStream outputStream,
            Socket socket) throws IOException{
        Client client = new Client(data[1], data[2], inputStream, outputStream);
        clientStorage.addClient(data[1],client);
        outputStream.writeUTF("logged");
        ClientService clientService = new ClientService(client, messageService, clientStorage);
        clientService.processMessage(data[1] + " enters chat.");
        serverHistory.writeHistory(data[1] + " enters chat.");
        startListenThread(client,socket, clientService);
        return client;
    }

    private static void startListenThread(Client client, Socket socket,ClientService clientService){
        executorService.execute(() -> {
            while (true) {
                try {
                    listenToInputStream(client, socket,clientService);
                } catch (IOException e) {
                    LOGGER.error("Listener failed " + e);
                    break;
                }
            }
        });
    }

    private static void startLoginAfterFailureThread(Socket socket,DataInputStream inputStream,DataOutputStream outputStream){
        executorService.execute(() -> {
            while (true) {
                try {
                    if(loginAfterFailure(socket,inputStream,outputStream)) break;
                } catch (IOException e) {
                    LOGGER.error("Failed after failure" + e);
                    break;
                }
            }
        });
    }

    private static boolean loginAfterFailure(Socket socket, @NotNull DataInputStream inputStream, DataOutputStream outputStream) throws IOException {
        String[] data = inputStream.readUTF().split("&");
        LOGGER.info("Trying to log in after failure " + data[0]);
        if (data[0].equals("login")) {
            if (clientsDB.addToDB(data[1], data[2])) {
                Client client = createClient(data,inputStream,outputStream,socket);
                LOGGER.info("New client connected:" + client + "::" + socket);
                serverHistory.writeHistory("New client connected:" + client + "::" + socket);
                return true;
            } else {
                if (clientsDB.checkAuth(data[1], data[2])) {
                    Client client = createClient(data,inputStream,outputStream,socket);
                    LOGGER.info("Old client connected:" + client + "::" + socket);
                    serverHistory.writeHistory("Old client connected:" + client + "::" + socket);
                    return true;
                } else {
                    if(clientStorage.containsClient(data[1])){
                        LOGGER.info(data[1] + " is already logged in.");
                        serverHistory.writeHistory(data[1] + " is already logged in.");
                        outputStream.writeUTF("occupied");
                    }else {
                        LOGGER.info("Attempt to login failed for " + data[1]);
                        serverHistory.writeHistory("Attempt to login failed for " + data[1]);
                        outputStream.writeUTF("fail");
                    }
                }
            }
        }
        return false;
    }

    private static void listenToInputStream(Client client, @NotNull Socket socket, ClientService clientService) throws IOException {
        if (!socket.isClosed()) {
            String[] data = client.getInputStream().readUTF().split("&");
            LOGGER.info("Listen input. Type: " + data[0]);
            responderMap.get(data[0]).respond(data,client,socket,clientService);
        }
    }

    private static void listenExit(){
        Scanner scanner = new Scanner(System.in);
        executorService.execute(() -> {
            while (true) {
                if (scanner.next().equals("exit")) {
                    LOGGER.info("Shutting down server.");
                    clientsDB.close();
                    executorService.shutdown();
                    System.exit(0);
                }
            }
        });
    }

    private static String moderateLine(String str) {
        for (ForbiddenWords fw : ForbiddenWords.values()) {
            if (str.contains(fw.toString())) {
                System.out.println("Message contains forbidden word " + fw.toString() + ".");
                return "Message is deleted by moderator.";
            }
        }
        return str;
    }

    private static class ThreadBuilder implements ThreadFactory {

        @Override
        public Thread newThread(@NotNull Runnable r) {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        }
    }
}