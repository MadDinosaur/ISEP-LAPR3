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

    private OracleDataSource oracleDataSource;

    private Connection connection;

    private SQLException error;

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

    public void getDatabaseReport(){
        try {
            String sqlCommand = "SELECT table_name FROM user_tables";
            PreparedStatement getTableNamesStatement = connection.prepareStatement(sqlCommand);
            FileWriter myWriter = new FileWriter("output\\filename.txt");
            try (ResultSet tableNames = getTableNamesStatement .executeQuery()) {
                while (tableNames.next()){
                    myWriter.write("Table : " + tableNames.getString("table_name") + "\n");
                    sqlCommand = "SELECT count(*) FROM " + tableNames.getString("table_name");
                    getTableNamesStatement = connection.prepareStatement(sqlCommand);
                    try (ResultSet rowCount = getTableNamesStatement.executeQuery()) {
                        rowCount.next();
                        myWriter.write("Rows : " + rowCount.getInt(1) + "\n\n");
                    }
                }
            }
            myWriter.close();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        if (connection == null) {
            throw new RuntimeException("Connection does not exit");
        }
        return connection;
    }

    public void registerError(SQLException error) {
        this.error = error;
    }

    public SQLException getLastError() {
        SQLException lastError = this.error;
        registerError(null);
        return lastError;
    }
}
