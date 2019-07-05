package Server;

import java.util.*;

class ClientStorage {
    private static Map<String, Client> clientMap = new HashMap<>();

    synchronized void addClient(String login,Client client) {
        System.out.println("client added: " + login);
        clientMap.put(login,client);
    }

    synchronized void removeClient(String login) {
        System.out.println("client removed: " + login);
        clientMap.remove(login);
    }

    synchronized Client findClient(String login){
        return clientMap.get(login);
    }

    synchronized boolean containsClient(String login){
        return clientMap.containsKey(login);
    }

    synchronized Map<String, Client> getClients() {
        return clientMap;
    }


}
