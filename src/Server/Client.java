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

    String getLogin() {
        return login;
    }

    int getPasswordHash() {
        return passwordHash;
    }

    DataInputStream getInputStream() {
        return is;
    }

    DataOutputStream getOutputStream() {
        return os;
    }

    @Override
    public String toString() {
        return "Client{" +
                "login='" + login + '\'' +
                '}';
    }



}
