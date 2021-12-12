package lapr.project.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CountryTest {
    private String continent = "Europe";
    private String countryName = "United Kingdom";
    private String alpha2 = "UK";
    private String alpha3 = "UNK";
    private int population = 0;
    private String capital = "London";
    private Coordinate coordinate = new Coordinate((float)53.46,(float)-3.03);

    @Test
    public void createValidCountryTest(){
        Country country = new Country(continent, countryName, coordinate, alpha2, alpha3, population, capital);
        assertNotNull(country);
        assertEquals(country.getCoordinate(),coordinate);
        assertEquals(country.getContinent(),continent);
        assertEquals(country.getCountry(),countryName);
        assertEquals(country.getAlpha2(), alpha2);
        assertEquals(country.getAlpha3(), alpha3);
        assertEquals(country.getPopulation(), population);
        assertEquals(country.getCapital(), capital);
        country.setCapital("123456789123456789123");
        assertEquals(country.getCapital(), "123456789123456789123" );
    }

    @Test
    public void createIllegalDataTest(){
        Exception exception1 = assertThrows(IllegalArgumentException.class, () ->{
            Country country = new Country(continent, countryName, coordinate, "unk", alpha3, population, capital);
        });

        String expectedMessage1 = "Alpha 2 code \"unk\" must have 2 digits";
        String actualMessage1 = exception1.getMessage();

        assertTrue(actualMessage1.contains(expectedMessage1));

        Exception exception2 = assertThrows(IllegalArgumentException.class, () ->{
            Country country = new Country(continent, countryName, coordinate, alpha2, "uk", population, capital);
        });

        String expectedMessage2 = "Alpha 3 code \"uk\" must have 3 digits";
        String actualMessage2 = exception2.getMessage();

        assertTrue(actualMessage2.contains(expectedMessage2));

        Exception exception3 = assertThrows(IllegalArgumentException.class, () ->{
            Country country = new Country(continent, countryName, coordinate, alpha2, alpha3, -3, capital);
        });

        String expectedMessage3 = "Population \"-3.0\" cannot be negative";
        String actualMessage3 = exception3.getMessage();

        assertTrue(actualMessage3.contains(expectedMessage3.replaceAll(",", ".")));

        Exception exception4 = assertThrows(IllegalArgumentException.class, () ->{
            Country country = new Country(continent, countryName, coordinate, alpha2, alpha3, population,  "thisStringHasMoreThan21Characters");
        });

        String expectedMessage4 = "Capital \"thisStringHasMoreThan21Characters\" is not supported.";
        String actualMessage4 = exception4.getMessage();

        assertTrue(actualMessage4.contains(expectedMessage4));
    }

    @Test
    public void equalsTest(){
        Country country1 = new Country(continent, countryName, coordinate, alpha2, alpha3, population, capital);
        String a = "storage";
        Country country2 = new Country(continent, countryName, coordinate, alpha2, alpha3, population, capital);

        assertEquals(country1, country2);
        assertNotEquals(a, country1);
        assertEquals(country1, country2);
        country2.setCapital("asd");
        assertNotEquals(country1, country2);
    }

    @Test
    public void storageToStringTest(){
        Country country = new Country(continent, countryName, coordinate, alpha2, alpha3, population, capital);
        String result = country.toString();
        assertNotNull(result);
    }
}