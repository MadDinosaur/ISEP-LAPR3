package lapr.project.mappers;

import lapr.project.mappers.dto.CountryDTO;
import lapr.project.model.Coordinate;
import lapr.project.model.Country;

import java.util.HashMap;
import java.util.Map;

public class CountryMapper {

    /**
     * transform an Country object into a DTO with all of it's information
     * @param country the country to be transformed to a dto
     * @return a DTO with all of the country's object information
     */
    public static CountryDTO toDTO(Country country){
        return new CountryDTO(country.getContinent(), country.getCountry(), Float.toString(country.getCoordinate().getLongitude()),
                Float.toString(country.getCoordinate().getLatitude()), country.getCapital(), country.getAlpha2(), country.getAlpha3(),
                Float.toString(country.getPopulation()));
    }

    /**
     * returns a map with all the country info and their colours
     * @param map the map to be transformed into a dto
     * @return a map with all the initial map information
     */
    public static Map<CountryDTO, Integer> toDTO(Map<Country, Integer> map){
        Map<CountryDTO, Integer> mapDTO = new HashMap<>();
        for (Country country: map.keySet()){
            mapDTO.put(CountryMapper.toDTO(country), map.get(country));
        }
        return mapDTO;
    }


    /**
     * transforms a DTO into it's model
     * @param country the DTO information to be transferred into a model object
     * @return returns a country with the information stored in the DTO
     */
    public static Country toModel(CountryDTO country){
        return new Country(country.getContinent(), country.getCountry(),
                new Coordinate(Float.parseFloat(country.getLongitude()), Float.parseFloat(country.getLatitude())), country.getAlpha2(),
                country.getAlpha3(), Float.parseFloat(country.getPopulation()), country.getCapital());
    }

    /**
     * returns a map with all the country info and their colours
     * @param map the dto to be transformed into a map
     * @return a map with all the initial map information
     */
    public static Map<Country, Integer> toModel(Map<CountryDTO, Integer> map){
        Map<Country, Integer> mapDTO = new HashMap<>();
        for (CountryDTO country: map.keySet()){
            mapDTO.put(CountryMapper.toModel(country), map.get(country));
        }
        return mapDTO;
    }
}
