package lapr.project.data;

import lapr.project.store.ShipStore;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MainStorage {

    /**
     * The apps Ship information store
     */
    private ShipStore shipStore;

    /**
     * initiates the Storage
     */
    private MainStorage(){
        loadProperties();
        shipStore = new ShipStore();
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
     * singleton used to maintain the same information trough all the app
     */
    private static MainStorage singleton = null;
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
