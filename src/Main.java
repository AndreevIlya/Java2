public class Main {
    public static void main(String[] args) {
        new Thread(ChatServer::runServer).start();
        new ChatClient();
    }
}
