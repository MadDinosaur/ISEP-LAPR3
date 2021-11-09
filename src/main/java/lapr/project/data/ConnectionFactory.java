package lapr.project.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class ConnectionFactory {

    /**
     * Logger class.
     */
    private static final Logger LOGGER = Logger.getLogger("MainLog");

    /**
     * This is the size of the connection pool.
     */
    private final Integer connectionPoolCount = 1;

    private Integer connectionPoolRequest = 0;

    private final List<DatabaseConnection> databaseConnectionList = new ArrayList<>();

    public DatabaseConnection getDatabaseConnection() {
        DatabaseConnection databaseConnection;
        if (++connectionPoolRequest > connectionPoolCount) {
            connectionPoolRequest = 1;
        }

        if (connectionPoolRequest > databaseConnectionList.size()) {
            databaseConnection = new DatabaseConnection(url(), user(), password());
            databaseConnectionList.add(databaseConnection);
        } else {
            databaseConnection = databaseConnectionList.get(connectionPoolRequest - 1);
        }

        return databaseConnection;
    }

    /**
     * Get Database URL from properties file.
     * @return url property
     */
    private String url() {
        return System.getProperty("database.url");
    }

    /**
     * Get Database user from properties file.
     * @return user property
     */
    private String user() {
        return System.getProperty("database.user");
    }

    /**
     * Get Database password from properties file.
     * @return password property
     */
    private String password() {
        return System.getProperty("database.password");
    }
}
