package Server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class ClientStorage {
    private static List<Client> clients = Collections.synchronizedList(new ArrayList<>());

    void addClient(Client client) {
        System.out.println("client added: "+client);
        clients.add(client);
    }

    void removeClient(Client client) {
        System.out.println("client removed: " + client);
        clients.remove(client);
    }

    Client findClient(String login){
        for (Client client : clients){
            if (client.getLogin().equals(login)){
                return client;
            }
        }
        return null;
    }

    List<Client> getClients() {
        return clients;
    }


}
