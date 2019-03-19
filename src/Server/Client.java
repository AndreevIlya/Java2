package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;

class Client {
    private final String login;
    private final int passwordHash;
    private final DataInputStream is;
    private final DataOutputStream os;


    Client(String login, String password, DataInputStream is, DataOutputStream os) {
        this.login = login;
        this.passwordHash = password.hashCode();
        this.is = is;
        this.os = os;
    }

    public String getLogin() {
        return login;
    }

    public int getPasswordHash() {
        return passwordHash;
    }

    public DataInputStream getIs() {
        return is;
    }

    public DataOutputStream getOs() {
        return os;
    }

    @Override
    public String toString() {
        return "Client{" +
                "login='" + login + '\'' +
                '}';
    }


}
