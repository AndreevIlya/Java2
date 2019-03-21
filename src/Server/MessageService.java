package Server;

import java.io.IOException;

class MessageService {
    private final ClientStorage clientStorage;

    MessageService(ClientStorage clientStorage) {
        this.clientStorage = clientStorage;
    }

    synchronized void sendMessages(String message) {
        clientStorage.getClients().forEach(client -> {
            try {
                System.out.println(String.format("sending message '%s' to '%s'", message, client));
                client.getOutputStream().writeUTF(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
}
