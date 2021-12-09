package lapr.project.model;

import lapr.project.exception.IllegalStorageException;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;

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
    public void createValidStorage21CharacterTest(){
        String continent2 = "123456789012345678901";
        String country2 = "123456789012345678901";
        String name2 = "123456789012345678901";
        int identification2 = 1234567890;
        Storage storage = new Storage(identification2,name2,continent2,country2,coordinate);
        assertNotNull(storage);
        assertEquals(storage.getCoordinate(),coordinate);
        assertEquals(storage.getContinent(),continent2);
        assertEquals(storage.getCountry(),country2);
        assertEquals(storage.getIdentification(),identification2);
        assertEquals(storage.getName(),name2);
        storage.setIdentification(12345);
        assertEquals(storage.getIdentification(),12345);
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

        Exception exception4 = assertThrows(IllegalArgumentException.class, () ->{
            Storage storage = new Storage(-2147483647,name,continent,country,coordinate);
        });
        String expectedMessage4 = "Identification \"-2147483647\" is not supported. (Cannot be bigger than 10 characters)";
        String actualMessage4 = exception4.getMessage();
        assertTrue(actualMessage4.contains(expectedMessage4));
    }


    @Test
    public void storageToStringTest(){
        Storage storage = new Storage(identification,name,continent,country,coordinate);
        String result = storage.toString();
        assertNotNull(result);
    }
}
