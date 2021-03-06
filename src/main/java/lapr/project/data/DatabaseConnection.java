/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.data;

import oracle.jdbc.pool.OracleDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author nunocastro
 */
public class DatabaseConnection {

    /**
     * The data source
     */
    private OracleDataSource oracleDataSource;

    /**
     * the connection
     */
    private Connection connection;

    /**
     * the error shown when a bad command is issued
     */
    private SQLException error;

    /**
     * starts up the database connection
     * @param url the connections url
     * @param username the connections user
     * @param password the connection's password
     */
    public DatabaseConnection(String url, String username, String password) {
        try {
            oracleDataSource = new OracleDataSource();

            oracleDataSource.setURL(url);

            connection = oracleDataSource.getConnection(username, password);
        } catch (SQLException e) {
            Logger.getLogger(DatabaseConnection.class.getName())
                    .log(Level.SEVERE, null, e);
            System.err.format("SQL State: %s\n%s", e.getSQLState(),
                    e.getMessage());
        }
    }

    /**
     * returns the connection to the server
     * @return returns the connection to the server
     */
    public Connection getConnection() {
        if (connection == null) {
            throw new RuntimeException("Connection does not exit");
        }
        return connection;
    }

    /**
     * registers the last known error
     * @param error the last issued error
     */
    public void registerError(SQLException error) {
        this.error = error;
    }

    /**
     * returns the last error issued
     * @return returns the last error issued
     */
    public SQLException getLastError() {
        SQLException lastError = this.error;
        registerError(null);
        return lastError;
    }
}
