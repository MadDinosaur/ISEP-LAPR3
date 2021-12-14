package lapr.project.store;

import lapr.project.model.Coordinate;
import lapr.project.model.Country;
import lapr.project.model.Storage;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PortsGraphTest {

    private final Storage storage1 = new Storage(29002,"Liverpool","Europe","United Kingdom",new Coordinate((float)53.46,(float)-3.03));
    private final Storage storage2 = new Storage(29006,"Lisboa","Europe","Portugal",new Coordinate((float)33.46,(float)-10.03));;
    private final Country country1 = new Country("Europe", "United Kingdom", new Coordinate((float)53.46,(float)-3.03), "UK", "UNK", 0, "London");
    private final Country country2 = new Country("Europe", "Portugal", new Coordinate((float)33.46,(float)-10.03), "UK", "UNK", 0, "Lisbon");;

    private PortsGraph portsGraph = new PortsGraph();

    @Test
    public void portTest(){
        portsGraph.insertLocation(storage1);
        portsGraph.insertLocation(storage2);
        portsGraph.insertLocation(country1);
        portsGraph.insertLocation(country2);

        portsGraph.insertPath(storage1, country1, storage1.distanceBetween(country1));
        portsGraph.insertPath(storage2, country2, storage2.distanceBetween(country2));

        portsGraph.insertPortPath(29002, 29007, 100);
        portsGraph.insertPortPath(29007, 29002, 100);
        portsGraph.insertPortPath(29007, 29008, 100);
        portsGraph.insertPortPath(29002, 29006, 100);

        portsGraph.insertCountryPath("paris", "United Kingdom");
        portsGraph.insertCountryPath( "United Kingdom", "paris");
        portsGraph.insertCountryPath( "paris", "paris");
        portsGraph.insertCountryPath("Portugal", "United Kingdom");

        assertEquals(portsGraph.getMg().numVertices(), 4);
        assertEquals(portsGraph.getMg().numEdges(), 8);

        portsGraph.setUpGraph(0);
        portsGraph.setUpGraph(2);

        Map<Country, Integer> map = portsGraph.colourCountries();
        assertEquals(map.get(country1), 1);
        assertEquals(map.get(country2), 0);
        assertNotNull(portsGraph.showColours(map));
    }
}