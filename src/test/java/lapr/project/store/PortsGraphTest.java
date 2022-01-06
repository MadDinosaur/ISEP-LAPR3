package lapr.project.store;

import lapr.project.model.Coordinate;
import lapr.project.model.Country;
import lapr.project.model.Storage;
import lapr.project.model.Tree2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PortsGraphTest {

    private final Storage storage1 = new Storage(29002,"Liverpool","Europe","United Kingdom",new Coordinate((float)53.46,(float)-3.03));
    private final Storage storage2 = new Storage(29006,"Lisboa","Europe","Portugal",new Coordinate((float)33.46,(float)-10.03));;
    private final Country country1 = new Country("Europe", "United Kingdom", new Coordinate((float)53.46,(float)-3.03), "UK", "UNK", 0, "London");
    private final Country country2 = new Country("Europe", "Portugal", new Coordinate((float)33.46,(float)-10.03), "UK", "UNK", 0, "Lisbon");;

    private PortsGraph portsGraph = new PortsGraph();

    @BeforeEach
    void setUp() {
        portsGraph.insertLocation(storage1);
        portsGraph.insertLocation(storage2);
        portsGraph.insertLocation(country1);
        portsGraph.insertLocation(country2);

        portsGraph.insertPath(storage1, country1, storage1.distanceBetween(country1));
        portsGraph.insertPath(storage2, country2, storage2.distanceBetween(country2));

        portsGraph.insertPortPath(29002, 29007, 20);
        portsGraph.insertPortPath(29007, 29002, 50);
        portsGraph.insertPortPath(29007, 29008, 100);
        portsGraph.insertPortPath(29002, 29006, 60);

        portsGraph.insertCountryPath("paris", "United Kingdom");
        portsGraph.insertCountryPath( "United Kingdom", "paris");
        portsGraph.insertCountryPath( "paris", "paris");
        portsGraph.insertCountryPath("Portugal", "United Kingdom");

        assertEquals(portsGraph.getMg().numVertices(), 4);
        assertEquals(portsGraph.getMg().numEdges(), 8);
    }

    @Test
    public void portTest(){
        Map<Country, Integer> map = portsGraph.colourCountries();
        assertEquals(map.get(country1), 0);
        assertEquals(map.get(country2), 1);
        assertNotNull(portsGraph.showColours(map));
    }

    @Test
    void minDistanceByContinent() {
        HashMap<String, PortsGraph> graphMap = new HashMap<>();
        graphMap.put("Europe", portsGraph);

        HashMap<String, List<String>> result = portsGraph.minDistanceByContinent(graphMap, 2);

        String expected = "Storage 29002: Name - Liverpool; Continent - Europe; Country - United Kingdom; Longitude - 53,46; Latitude - -3,03";

        assertEquals(expected.replaceAll(",", "."), result.get("Europe").get(0).replaceAll(",", "."));
        assertEquals(1, result.size());
        assertEquals(2, result.get("Europe").size());
    }
}