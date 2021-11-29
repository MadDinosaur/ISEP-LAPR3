package lapr.project.mappers;

import java.util.logging.Level;
import java.util.logging.Logger;
import lapr.project.mappers.dto.StorageDTO;
import lapr.project.model.Coordinate;
import lapr.project.model.Storage;


public class StorageMapper {
    /**
     * Private constructor because the mapper is an Utility class
     */
    private StorageMapper(){
        throw new IllegalStateException("Utility class");
    }

    /**
     * Converts a Storage object into a Storage DTO
     * @param storage The Storage object that will be Converted
     * @return The DTO that resulted from the conversion
     */
    public static StorageDTO toDTO(Storage storage) {
        return new StorageDTO(Integer.toString(storage.getIdentification()), storage.getName(), storage.getContinent(), storage.getCountry(), Float.toString(storage.getCoordinate().getLongitude()), Float.toString(storage.getCoordinate().getLatitude()));
    }

    /**
     * Converts a Storage DTO into a Storage
     * @param dto A Storage DTO
     * @return The Storage created from the Storage DTO
     */
    public static Storage toModel(StorageDTO dto) {
        try {
            Coordinate coord = new Coordinate(Float.parseFloat(dto.getLongitude()), Float.parseFloat(dto.getLatitude()));

            return new Storage(Integer.parseInt(dto.getIdentification()), dto.getName(), dto.getContinent(), dto.getCountry(), coord);
        } catch (IllegalArgumentException e) {
            Logger.getLogger(StorageMapper.class.getName()).log(Level.WARNING, e.getMessage());
            return null;
        }
    }
}
