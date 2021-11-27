package lapr.project.data;

import lapr.project.store.ShipStore;
import lapr.project.store.StorageStore;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MainStorage {

    /**
     * The apps Ship information store
     */
    private ShipStore shipStore;

    /**
     * The apps Storage information store
     */
    private StorageStore storageStore;

    /**
     * the app's connection to the database
     */
    private DatabaseConnection databaseConnection;

    /**
     * initiates the Storage
     */
    private MainStorage() {
        loadProperties();
        shipStore = new ShipStore();
        storageStore = new StorageStore();
//        ConnectionFactory connectionFactory = new ConnectionFactory();
//        databaseConnection = connectionFactory.getDatabaseConnection();
    }

    /**
     * Load Properties from application.properties file.
     */
    private void loadProperties(){
        try {
            //Load existing properties.
            Properties properties = new Properties(System.getProperties());

            //Read new properties from file.
            InputStream inputStream =
                    getClass().getClassLoader().getResourceAsStream(
                            "application.properties");
            properties.load(inputStream);
            inputStream.close();

            //Set new properties.
            System.setProperties(properties);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * returns the apps Ship information store
     * @return returns the apps Ship information store
     */
    public ShipStore getShipStore() {
        return shipStore;
    }

    /**
     * returns the app's Storage information store
     * @return returns the app's Storage information store
     */
    public StorageStore getStorageStore() {
        return storageStore;
    }

    /**
     * returns the app's database connection
     * @return returns the app's database connection
     */
    public DatabaseConnection getDatabaseConnection() {
        return databaseConnection;
    }

    /**
     * singleton used to maintain the same information trough all the app
     */
    private static MainStorage singleton = null;

    /**
     * returns this object's main instance
     * @return returns this object's main instance
     */
    public static MainStorage getInstance()
    {
        if(singleton == null)
        {
            synchronized(MainStorage.class)
            {
                singleton = new MainStorage();
            }
        }
        return singleton;
    }
}
