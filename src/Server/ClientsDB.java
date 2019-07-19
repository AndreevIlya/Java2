package Server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Closeable;
import java.sql.*;

class ClientsDB implements Closeable{
    private static final String JDBC_DRIVER = "org.postgresql.Driver";
    private static final String DATABASE = "jdbc:postgresql://127.0.0.1:5432/javachat?user=postgres&password=c8cbdcdf";
    private static final Logger LOGGER = LogManager.getLogger(ClientsDB.class);

    private Connection connection;

    ClientsDB(){
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DATABASE);
        } catch (ClassNotFoundException | SQLException e) {
            LOGGER.error("Unable to init connection to DB " + e);
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
            LOGGER.error("SQLException while SELECT check query " + e);
        } finally {
            try {
                checkPresenceStatement.close();
            } catch (SQLException e2) {
                LOGGER.error("Unable to close SELECT check statement " + e2);
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
                LOGGER.info(login + " is now added to DB.");
                return true;
            } catch (SQLException e) {
                LOGGER.error("SQLException while INSERT query " + e);
                return false;
            } finally {
                try {
                    addStatement.close();
                } catch (SQLException e2) {
                    LOGGER.error("Unable to close INSERT statement " + e2);
                }
            }
        } else {
            LOGGER.info(login + " have already been added to DB.");
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
            LOGGER.error("SQLException while INSERT check auth query " + e);
        } finally {
            try {
                checkPresenceStatement.close();
            } catch (SQLException e2) {
                LOGGER.error("Unable to close INSERT check auth statement " + e2);
            }
        }
        return result;
    }

    @Override
    public void close(){
        try {
            connection.close();
        } catch (SQLException e) {
            LOGGER.error("Unable to close connection to DB " + e);
        }
    }
}
