package lapr.project.store;

import lapr.project.mappers.dto.StorageDTO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StorageFileReader {

    /**
     * Hides the implicit public controller
     */
    private StorageFileReader(){}

    /**
     * Returns a list with the storage information
     * @param path the path of the cvs file
     * @return returns a list with the storage information
     */
    public static List<StorageDTO> readStorageFile(String path){
        List<StorageDTO> storageList;

        try(BufferedReader reader = new BufferedReader(new FileReader(path))){
            reader.readLine();
            String line;
            List<String[]> dataSet = new ArrayList<>();

            while ((line = reader.readLine()) != null)
                dataSet.add(line.split(","));

            storageList = populateList(dataSet);

        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
        return storageList;
    }


    /**
     * Returns a list with the storage information
     * @param dataSet the list of lines available on the read file
     * @return returns a map with the storage information
     */
    private static List<StorageDTO> populateList(List<String[]> dataSet){
        List<StorageDTO> storageList = new ArrayList<>();

        for(String[] line : dataSet){
            if(!checkIfAlreadyRegistered(storageList,line)){
                StorageDTO storage = new StorageDTO(line[2],line[3],line[0],line[1],line[5],line[4]);
                storageList.add(storage);
            }
        }
        return storageList;
    }


    /**
     * Checks if the storage is already in the list
     * @param storageDTOList a list with the already registered ports
     * @param line the current line being analyzed
     * @return true if the storage is already registered
     */
    private static boolean checkIfAlreadyRegistered(List<StorageDTO> storageDTOList, String[] line){


        for (StorageDTO dto : storageDTOList){

            String cont = dto.getContinent();
            String country = dto.getCountry();
            String code = dto.getIdentification();
            String name = dto.getName();
            String lat = dto.getLatitude();
            String lon = dto.getLongitude();


            if(cont.equals(line[0]) && country.equals(line[1]) && code.equals(line[2]) && name.equals(line[3]) && lat.equals(line[4]) && lon.equals(line[5]))
                return true;

        }
        return false;
    }


}
