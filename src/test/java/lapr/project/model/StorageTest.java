package lapr.project.model;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;


public class StorageTest {
    private String continent = "Europe";
    private String country = "United Kingdom";
    private String name = "Liverpool";
    private int identification = 29002;
    private Coordinate coordinate = new Coordinate((float)53.46,(float)-3.03);

    @Test
    public void createValidStorageTest(){
        Storage storage = new Storage(identification,name,continent,country,coordinate);
        assertNotNull(storage);
        assertEquals(storage.getCoordinate(),coordinate);
        assertEquals(storage.getContinent(),continent);
        assertEquals(storage.getCountry(),country);
        assertEquals(storage.getIdentification(),identification);
        assertEquals(storage.getName(),name);
        storage.setName("Barcelona");
        assertEquals(storage.getName(),"Barcelona");
    }

    @Test
    public void createIllegalDataTest(){
        Exception exception1 = assertThrows(IllegalArgumentException.class, () ->{
            Storage storage = new Storage(identification,name,continent,"ThisNameHasExactly30Characters",coordinate);
        });

        String expectedMessage1 = "Country \"ThisNameHasExactly30Characters\" is not supported.";
        String actualMessage1 = exception1.getMessage();

        assertTrue(actualMessage1.contains(expectedMessage1));

        Exception exception2 = assertThrows(IllegalArgumentException.class, () ->{
            Storage storage = new Storage(identification,name,"ThisNameHasExactly30Characters",country,coordinate);
        });

        String expectedMessage2 = "Continent \"ThisNameHasExactly30Characters\" is not supported.";
        String actualMessage2 = exception2.getMessage();

        assertTrue(actualMessage2.contains(expectedMessage2));

        Exception exception3 = assertThrows(IllegalArgumentException.class, () ->{
            Storage storage = new Storage(identification,"ThisNameHasExactly30Characters",continent,country,coordinate);
        });

        String expectedMessage3 = "Name \"ThisNameHasExactly30Characters\" is not supported.";
        String actualMessage3 = exception3.getMessage();

        assertTrue(actualMessage3.contains(expectedMessage3));
        

    }


}
