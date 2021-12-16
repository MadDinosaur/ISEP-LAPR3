package lapr.project.mappers;

import lapr.project.mappers.dto.CountryDTO;
import lapr.project.model.Coordinate;
import lapr.project.model.Country;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CountryMapperTest {

    private String continent = "Europe";
    private String countryName = "United Kingdom";
    private String alpha2 = "UK";
    private String alpha3 = "UNK";
    private int population = 0;
    private String capital = "London";
    private Coordinate coordinate = new Coordinate((float)53.46,(float)-3.03);

    @Test
    public void mapperTest(){
        Country country = new Country(continent, countryName, coordinate, alpha2, alpha3, population, capital);
        CountryDTO countryDTO = CountryMapper.toDTO(country);
        assertNotNull(countryDTO);
        Country country1 = CountryMapper.toModel(countryDTO);
        assertNotNull(country1);
        assertEquals(country, country1);
        assertEquals(country1.getContinent(), continent);
        assertEquals(country1.getCoordinate().getLongitude(), coordinate.getLongitude());
        assertEquals(country1.getCoordinate().getLatitude(), coordinate.getLatitude());
        assertEquals(country1.getCountry(), countryName);
        assertEquals(country1.getPopulation(), population);
        assertEquals(country1.getAlpha2(), alpha2);
        assertEquals(country1.getAlpha3(), alpha3);
        assertEquals(country1.getCapital(), capital);
        Map<Country, Integer> map = new HashMap<>();
        map.put(country, 1);
        Map<CountryDTO, Integer> mapDTO = CountryMapper.toDTO(map);
        assertEquals(mapDTO.size(),1);
        Map<Country, Integer> map1 = CountryMapper.toModel(mapDTO);
        assertEquals(map1.size(),1);
    }
}