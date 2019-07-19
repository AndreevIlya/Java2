package Server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Map;

class MessageService {
    private static final Logger LOGGER = LogManager.getLogger(MessageService.class);
    private final ClientStorage clientStorage;

    MessageService(ClientStorage clientStorage) {
        this.clientStorage = clientStorage;
    }

    synchronized void sendMessages(String message) {
        for(Map.Entry<String,Client> clientEntry : clientStorage.getClients().entrySet()){
            try {
                LOGGER.info(String.format("sending message '%s' to '%s'", message,
                        clientEntry.getKey()));
                clientEntry.getValue().getOutputStream().writeUTF(message);
            } catch (IOException e) {
                LOGGER.error("Sending message failed " + e);
            }
        }
    }

    synchronized void sendPrivateMessage(String login,String message) {
        try {
            Client receiver = clientStorage.findClient(login);
            LOGGER.info(receiver);
            if(receiver != null){
                receiver.getOutputStream().writeUTF(message);
            }
        } catch (IOException e) {
            LOGGER.error("Sending private message failed " + e);
        }
    }
}
