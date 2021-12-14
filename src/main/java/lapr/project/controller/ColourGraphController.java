package lapr.project.controller;

import lapr.project.data.MainStorage;
import lapr.project.model.Country;
import lapr.project.store.PortsGraph;
import lapr.project.store.ShipStore;

import java.util.Map;

public class ColourGraphController {

    /**
     *  The current ship store
     */
    private final PortsGraph portsGraph;

    /**
     * Calls the creator with the current storage instance
     */
    public ColourGraphController() {
        this(MainStorage.getInstance());
    }

    /**
     * Creates a instance of the controller with the current storage instance
     * @param storage the storage instance used to store all information
     */
    public ColourGraphController(MainStorage storage) {
        this.portsGraph = storage.getPortsGraph();
    }

    /**
     * returns the countries colours
     * @return returns the countries colours
     */
    public Map<Country, Integer> getCountryColours(){
        return portsGraph.colourCountries();
    }

    /**
     * this controller is purely used to support a requirement and is in no way Functional
     * It Creates a String with the countries the mao has and with it's bordering countries
     * @param map the graph's colours
     * @return A String of the country's colour and it' bordering countries
     */
    public String getCountryAndBorderColours(Map<Country, Integer> map){
        return portsGraph.showColours(map);
    }
}
