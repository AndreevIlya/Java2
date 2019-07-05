package Server;


import Message.Message;

class ClientService {
    private final Client client;
    private final MessageService messageService;
    private final ClientStorage clientStorage;

    ClientService(Client client, MessageService messageService, ClientStorage clientStorage) {
        this.client = client;
        this.messageService = messageService;
        this.clientStorage = clientStorage;
    }

    void processMessage(String message) {
        System.out.println(String.format("received message '%s' to '%s'", message, client));
        Message processedMessage = new Message(message);
        message = "message&" + client.getLogin() + ": " +
                processedMessage.splitMessage() + "&" +
                processedMessage.getTime();

        messageService.sendMessages(message);
    }

    void processPrivateMessage(String login,String message) {
        if(clientStorage.containsClient(login)) {
            System.out.println(String.format("received message '%s' to '%s'", message, client));
            if(client.getLogin().equals(login)){
                messageService.sendPrivateMessage(login, "You cannot write to yourself.");
            }else{
                Message messageToSend = new Message(message);
                message = "message&" + client.getLogin() + " to " + login + ": " +
                        messageToSend.splitMessage() + "&" +
                        messageToSend.getTime();
                messageService.sendPrivateMessage(login, message);
                messageService.sendPrivateMessage(client.getLogin(), message);
            }
        }
    }

}
