package Server;

import java.io.IOException;
import java.util.Map;

class MessageService {
    private final ClientStorage clientStorage;

    MessageService(ClientStorage clientStorage) {
        this.clientStorage = clientStorage;
    }

    synchronized void sendMessages(String message) {
        for(Map.Entry<String,Client> clientEntry : clientStorage.getClients().entrySet()){
            try {
                System.out.println(String.format("sending message '%s' to '%s'", message,
                        clientEntry.getKey()));
                clientEntry.getValue().getOutputStream().writeUTF(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    synchronized void sendPrivateMessage(String login,String message) {
        try {
            Client receiver = clientStorage.findClient(login);
            System.out.println(receiver);
            if(receiver != null){
                receiver.getOutputStream().writeUTF(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
