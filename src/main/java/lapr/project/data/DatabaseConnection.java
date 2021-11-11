/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.data;

import lapr.project.model.PositioningData;
import oracle.jdbc.pool.OracleDataSource;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
     * Writes a detailed report about the table filled in the database
     */
    public void getDatabaseReport(){
        try {
            String sqlCommand = "SELECT table_name FROM user_tables";
            try(PreparedStatement getTableNamesStatement = connection.prepareStatement(sqlCommand)) {
                try (FileWriter myWriter = new FileWriter("output\\filename.txt")) {
                    try (ResultSet tableNames = getTableNamesStatement.executeQuery()) {
                        while (tableNames.next()) {
                            myWriter.write("Table : " + tableNames.getString("table_name") + "\n");
                            sqlCommand = "SELECT count(*) FROM " + tableNames.getString("table_name");
                            try (PreparedStatement getTableStatement = connection.prepareStatement(sqlCommand)) {
                                try (ResultSet rowCount = getTableStatement.executeQuery()) {
                                    rowCount.next();
                                    myWriter.write("Rows : " + rowCount.getInt(1) + "\n\n");
                                }
                            }
                        }
                    }
                }
            }

        } catch (IOException | SQLException e) {
            e.printStackTrace();
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
