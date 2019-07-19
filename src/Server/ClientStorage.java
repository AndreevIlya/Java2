package Server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

class ClientStorage {
    private static final Map<String, Client> clientMap = new HashMap<>();
    private static final Logger LOGGER = LogManager.getLogger(ClientStorage.class);

    synchronized void addClient(String login,Client client) {
        LOGGER.info("client added: " + login);
        clientMap.put(login,client);
    }

    synchronized void removeClient(String login) {
        LOGGER.info("client removed: " + login);
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
