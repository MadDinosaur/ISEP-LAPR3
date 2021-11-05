package lapr.project.store;

public class MainStorage {

    /**
     * The apps Ship information store
     */
    private ShipStore shipStore;

    /**
     * initiates the Storage
     */
    private MainStorage(){
        shipStore = new ShipStore();
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
