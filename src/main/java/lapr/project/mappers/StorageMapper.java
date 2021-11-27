package lapr.project.mappers;

import lapr.project.mappers.dto.StorageDTO;
import lapr.project.model.Coordinate;
import lapr.project.model.Storage;

import java.util.ArrayList;
import java.util.List;

public class StorageMapper {
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
        try{
            Coordinate coord = new Coordinate(Float.parseFloat(dto.getLongitude()), Float.parseFloat(dto.getLatitude()));

            return new Storage(Integer.parseInt(dto.getIdentification()), dto.getName(), dto.getContinent(), dto.getCountry(), coord);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Transforms a list of Storage DTOs into a list of Storage
     * @param storageData a mapper with all the storage information
     * @return a storage list
     */
    public static List<Storage> toModel(List<StorageDTO> storageData){
        List<Storage> storageList = new ArrayList<>();
        for(StorageDTO dto : storageData){
            Storage storage = toModel(dto);
            if (storage != null){
                storageList.add(storage);
            }

        }
        return storageList;
    }
}
