package lapr.project.store;

public class Storage {

    /**
     * The apps Ship information store
     */
    private ShipStore shipStore;

    /**
     * initiates the Storage
     */
    private Storage(){
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
    private static Storage singleton = null;
    public static Storage getInstance()
    {
        if(singleton == null)
        {
            synchronized(Storage.class)
            {
                singleton = new Storage();
            }
        }
        return singleton;
    }
}
