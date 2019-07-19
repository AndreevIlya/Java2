package Server;

import Message.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Contract;

class ClientService {
    private static final Logger LOGGER = LogManager.getLogger(ClientService.class);
    private final Client client;
    private final MessageService messageService;
    private final ClientStorage clientStorage;

    @Contract(pure = true)
    ClientService(Client client, MessageService messageService, ClientStorage clientStorage) {
        this.client = client;
        this.messageService = messageService;
        this.clientStorage = clientStorage;
    }

    void processMessage(String message) {
        LOGGER.info(String.format("received message '%s' to '%s'", message, client));
        Message processedMessage = new Message(message);
        message = "message&" + client.getLogin() + ": " +
                processedMessage.splitMessage() + "&" +
                processedMessage.formatTime();

        messageService.sendMessages(message);
    }

    void processPrivateMessage(String login,String message) {
        if(clientStorage.containsClient(login)) {
            LOGGER.info(String.format("received message '%s' to '%s'", message, client));
            if(client.getLogin().equals(login)){
                messageService.sendPrivateMessage(login, "You cannot write to yourself.");
            }else{
                Message messageToSend = new Message(message);
                message = "message&" + client.getLogin() + " to " + login + ": " +
                        messageToSend.splitMessage() + "&" +
                        messageToSend.formatTime();
                messageService.sendPrivateMessage(login, message);
                messageService.sendPrivateMessage(client.getLogin(), message);
            }
        }
    }

}
