package Server;

import java.io.IOException;

public class ClientService {
    private final Client client;
    private final MessageService messageService;
    private final ClientStorage clientStorage;

    public ClientService(Client client, MessageService messageService, ClientStorage clientStorage) {
        this.client = client;
        this.messageService = messageService;
        this.clientStorage = clientStorage;
    }

    public void processMessage() {
        try {
            while (true) {
                String message = client.getInputStream().readUTF();
                System.out.println(String.format("received message '%s' to '%s'", message, client));

                messageService.sendMessages(client.getLogin() + "::" + message);
            }
        } catch (IOException io) {
            clientStorage.removeClient(client);
            io.printStackTrace();
        }
    }
}
