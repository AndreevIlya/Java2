package Server;

import java.io.*;
import java.sql.*;
import java.util.HashMap;

class ClientsDB implements Closeable{
    private static final String JDBC_DRIVER = "org.postgresql.Driver";
    private static final String DATABASE = "jdbc:postgresql://127.0.0.1:5432/javachat?user=postgres&password=mypass";
    private Connection connection;

    ClientsDB(){
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DATABASE);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isClientNotInDB(String login){
        Statement checkPresenceStatement = null;
        ResultSet logins;
        try {
            String selectLogins = "SELECT name FROM users";
            checkPresenceStatement = connection.createStatement();
            logins = checkPresenceStatement.executeQuery(selectLogins);
            while (logins.next()){
                if(login.equals(logins.getString("name"))) return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                checkPresenceStatement.close();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
        return true;
    }

    boolean addToDB(String login, String pass){
        if(isClientNotInDB(login)) {
            PreparedStatement addStatement = null;
            try {
                String addSQL = "INSERT INTO users (name,password) VALUES (?,?)";
                addStatement = connection.prepareStatement(addSQL);
                addStatement.setString(1, login);
                addStatement.setString(2, Integer.toString(pass.hashCode()));
                addStatement.executeUpdate();
                System.out.println(login + " is added to DB.");
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("SQLException.");
                return false;
            } finally {
                try {
                    addStatement.close();
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }
            }
        } else {
            System.out.println(login + " is not added to DB.");
            return false;
        }
    }

    boolean checkAuth(String login, String pass){
        Statement checkPresenceStatement = null;
        ResultSet entries;
        boolean result = false;
        try {
            String selectEntries = "SELECT name,password FROM users";
            checkPresenceStatement = connection.createStatement();
            entries = checkPresenceStatement.executeQuery(selectEntries);
            while (entries.next() && !result){
                if(login.equals(entries.getString("name"))) {
                    result = Integer.toString(pass.hashCode()).equals(entries.getString("password"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                checkPresenceStatement.close();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public void close(){
        try {
            connection.close();
        } catch (SQLException e2) {
            e2.printStackTrace();
        }
    }
}
