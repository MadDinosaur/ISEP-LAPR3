package lapr.project.controller;

import lapr.project.model.Coordinate;
import lapr.project.model.Country;
import lapr.project.model.Location;
import lapr.project.model.Storage;
import lapr.project.store.PortsGraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class ShortestPathControllerTest {
    private final Storage storageLeixoes = new Storage(13012,"Leixoes","Europe","Portugal",new Coordinate((float)-8.70,(float)41.18));
    private final Storage storageSetubal = new Storage(13013,"Setubal","Europe","Portugal",new Coordinate((float)-8.92,(float)38.50));
    private final Storage storagePontaDelg = new Storage(13014,"Ponta Delgada","Europe","Portugal",new Coordinate((float)-25.67,(float)37.73));
    private final Storage storageRostock = new Storage(20072,"Rostock","Europe","Germany",new Coordinate((float)12.10,(float)54.15));
    private final Storage storageHamburg = new Storage(224858,"Hamburg","Europe","Germany",new Coordinate((float)9.98,(float)53.55));

    private final Country unitedKingdom = new Country("Europe", "United Kingdom", new Coordinate((float)53.46,(float)-3.03), "UK", "UNK", 0, "London");
    private final Country portugal = new Country("Europe", "Portugal", new Coordinate((float)-10.03,(float)33.46), "PT", "UNK", 0, "Lisbon");
    private final Country spain = new Country("Europe", "Spain", new Coordinate((float)-3.683,(float)40.400), "PT", "UNK", 0, "Madrid");
    private final Country france = new Country("Europe", "France", new Coordinate((float)2.333,(float)48.867), "PT", "UNK", 0, "Paris");
    private final Country germany = new Country("Europe", "Germany", new Coordinate((float)13.400,(float)52.517), "PT", "UNK", 0, "Berlin");
    private final Country poland = new Country("Europe", "Poland", new Coordinate((float)21.000,(float)52.250), "PT", "UNK", 0, "Warsaw");
    private final Country russia = new Country("Europe", "Russia", new Coordinate((float)37.60,(float)55.75), "PT", "UNK", 0, "Moscow");
    private final Country netherlands = new Country("Europe", "Netherlands", new Coordinate((float)4.917,(float)52.35), "PT", "UNK", 0, "Amsterdam");
    private final Country austria = new Country("Europe", "Austria", new Coordinate((float)16.367,(float)48.200), "PT", "UNK", 0, "Vienna");
    private final Country slovakia = new Country("Europe", "Slovakia", new Coordinate((float)17.117,(float)48.150), "PT", "UNK", 0, "Bratislava");

    private PortsGraph portsGraph = new PortsGraph();

    @BeforeEach
    void setUp() {
        portsGraph.insertLocation(storageLeixoes);
        portsGraph.insertLocation(storageSetubal);
        portsGraph.insertLocation(storagePontaDelg);
        portsGraph.insertLocation(storageRostock);
        portsGraph.insertLocation(storageHamburg);

        portsGraph.insertLocation(unitedKingdom);
        portsGraph.insertLocation(portugal);
        portsGraph.insertLocation(spain);
        portsGraph.insertLocation(france);
        portsGraph.insertLocation(germany);
        portsGraph.insertLocation(poland);
        portsGraph.insertLocation(russia);
        portsGraph.insertLocation(netherlands);
        portsGraph.insertLocation(austria);
        portsGraph.insertLocation(slovakia);

        portsGraph.insertPath(storageSetubal, portugal, storageSetubal.distanceBetween(portugal));
        portsGraph.insertPath(storageRostock, germany, storageRostock.distanceBetween(germany));

        portsGraph.insertPortPath(13012, 13013, 20);
        portsGraph.insertPortPath(13013, 13014, 50);
        portsGraph.insertPortPath(13012, 13014, 60);
        portsGraph.insertPortPath(13014, 20072, 2000);
        portsGraph.insertPortPath(20072, 224858, 60);

        portsGraph.insertCountryPath("Portugal", "Spain");
        portsGraph.insertCountryPath("Spain", "France");
        portsGraph.insertCountryPath("France", "Germany");
        portsGraph.insertCountryPath("Germany", "Poland");
        portsGraph.insertCountryPath("Germany", "Netherlands");
        portsGraph.insertCountryPath("Germany", "Austria");
        portsGraph.insertCountryPath("Austria", "Slovakia");
        portsGraph.insertCountryPath("Slovakia", "Poland");
        portsGraph.insertCountryPath("Poland", "Russia");

        assertEquals(15, portsGraph.getMg().numVertices());
        assertEquals(32, portsGraph.getMg().numEdges());
    }

    @Test
    void getShortestPathN() {
        ShortestPathController ctrl = new ShortestPathController(portsGraph);

        LinkedList<Location> places = new LinkedList<>();

        places.add(spain);
        places.add(austria);

        LinkedList<Location> result = ctrl.getShortestPathN(places ,storageLeixoes, russia);

        LinkedList<Location> expected = new LinkedList<>();

        expected.add(storageLeixoes);
        expected.add(storageSetubal);
        expected.add(portugal);
        expected.add(spain);
        expected.add(france);
        expected.add(germany);
        expected.add(austria);
        expected.add(slovakia);
        expected.add(poland);
        expected.add(russia);

        assertNotNull(result);
        assertEquals(expected.size(), result.size());

        for (int i = 0; i < expected.size(); i++)
            assertEquals(expected.get(i), result.get(i));
    }

    @Test
    void landOrSeaPath() {
        ShortestPathController ctrl = new ShortestPathController(portsGraph);

        LinkedList<Location> result = ctrl.landOrSeaPath(storageLeixoes, russia);

        LinkedList<Location> expected = new LinkedList<>();

        expected.add(storageLeixoes);
        expected.add(storagePontaDelg);
        expected.add(storageRostock);
        expected.add(germany);
        expected.add(poland);
        expected.add(russia);

        assertNotNull(result);
        assertEquals(expected.size(), result.size());

        for (int i = 0; i < expected.size(); i++)
            assertEquals(expected.get(i), result.get(i));
    }

    @Test
    void landOrSeaPathNoPath() {
        ShortestPathController ctrl = new ShortestPathController(portsGraph);

        LinkedList<Location> result = ctrl.landOrSeaPath(storageLeixoes, unitedKingdom);

        assertTrue(result.isEmpty());
    }

    @Test
    void shortestMaritimePath() {
        ShortestPathController ctrl = new ShortestPathController(portsGraph);

        LinkedList<Location> result = ctrl.shortestMaritimePath(storageLeixoes, storageHamburg);

        LinkedList<Location> expected = new LinkedList<>();

        expected.add(storageLeixoes);
        expected.add(storagePontaDelg);
        expected.add(storageRostock);
        expected.add(storageHamburg);

        assertNotNull(result);
        assertEquals(expected.size(), result.size());

        for (int i = 0; i < expected.size(); i++)
            assertEquals(expected.get(i), result.get(i));
    }

    @Test
    void shortestLandPathSuccess() {
        ShortestPathController ctrl = new ShortestPathController(portsGraph);

        LinkedList<Location> result = ctrl.shortestLandPath(spain, russia);

        LinkedList<Location> expected = new LinkedList<>();

        expected.add(spain);
        expected.add(france);
        expected.add(germany);
        expected.add(poland);
        expected.add(russia);

        assertNotNull(result);
        assertEquals(expected.size(), result.size());

        for (int i = 0; i < expected.size(); i++)
            assertEquals(expected.get(i), result.get(i));
    }

    @Test
    void shortestLandPathStartEndOnPort() {
        ShortestPathController ctrl = new ShortestPathController(portsGraph);

        LinkedList<Location> result = ctrl.shortestLandPath(storageSetubal, storageRostock);

        LinkedList<Location> expected = new LinkedList<>();

        expected.add(storageSetubal);
        expected.add(portugal);
        expected.add(spain);
        expected.add(france);
        expected.add(germany);
        expected.add(storageRostock);

        assertNotNull(result);
        assertEquals(expected.size(), result.size());

        for (int i = 0; i < expected.size(); i++)
            assertEquals(expected.get(i), result.get(i));

        result = ctrl.shortestLandPath(storageLeixoes, storageRostock);
        assertTrue(result.isEmpty());

        result = ctrl.shortestLandPath(storageSetubal, storageHamburg);
        assertTrue(result.isEmpty());

        result = ctrl.shortestLandPath(storageLeixoes, storageHamburg);
        assertTrue(result.isEmpty());
    }

    @Test
    void getPathDistance() {
        LinkedList<Location> path = new LinkedList<>();
        ShortestPathController ctrl = new ShortestPathController(portsGraph);

        double result = ctrl.getPathDistance(path);
        double expected = 0.00;

        assertEquals(expected, result);

        path.add(storageLeixoes);
        path.add(storagePontaDelg);     //60
        path.add(storageRostock);       //2000
        path.add(storageHamburg);       //60

        result = ctrl.getPathDistance(path);
        expected = 2120.00;

        assertEquals(expected, result);

        path.removeLast();

        result = ctrl.getPathDistance(path);
        expected = 2060.00;

        assertEquals(expected, result);
    }
}