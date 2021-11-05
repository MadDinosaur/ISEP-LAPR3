package lapr.project.model;

import lapr.project.mappers.dto.PositioningDataDTO;
import lapr.project.mappers.dto.ShipDTO;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShipFileReader {

    /**
     * returns a map with the ship information and it's positioning data
     * @param path the path to the cvs file
     * @return returns a map with the ship information and it's positioning data
     * @throws FileNotFoundException return a null map if the file reading goes wrong
     */
    public static Map<ShipDTO, List<PositioningDataDTO>> readShipFile(String path)  {
        Map<ShipDTO, List<PositioningDataDTO>> shipMap;
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            reader.readLine();
            String line;
            List<String[]> dataSet = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                dataSet.add(line.split(","));
            }
            shipMap = populateMap(dataSet);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return shipMap;
    }

    /**
     * returns a map with the ship information and it's positioning data
     * @param dataSet the list of lines available on the read file
     * @return returns a map with the ship information and it's positioning data
     */
    private static Map<ShipDTO, List<PositioningDataDTO>> populateMap(List<String[]> dataSet) {
        Map<ShipDTO, List<PositioningDataDTO>> shipMap = new HashMap<>();
        for (String[] line : dataSet ){
            if (!checkIfAlreadyRegistered(shipMap, line)){
                List<PositioningDataDTO> list = new ArrayList<>();
                ShipDTO ship = new ShipDTO(line[0],line[7],line[8].substring(3),line[9],line[10],line[11],line[12],line[13]);
                PositioningDataDTO positioningData = new PositioningDataDTO(line[1],line[2],line[3],line[4],line[5],line[6],line[14],line[15]);
                list.add(positioningData);
                shipMap.put(ship, list);
            }
        }
        return shipMap;
    }

    /**
     * checks if a ship is already within the map and if so adds it to the list of positioning data
     * @param shipDTOListMap a list with the already registered ships and positioning data
     * @param line teh current line being analyzed
     * @return true if the ship was already registered
     */
    private static boolean checkIfAlreadyRegistered(Map<ShipDTO, List<PositioningDataDTO>> shipDTOListMap, String[] line){
        try{
            float temp = Float.parseFloat(line[13]);
            if (temp < 0){
                return true;
            }
        } catch (NumberFormatException e){
            e.printStackTrace();
        }
        for (ShipDTO dto : shipDTOListMap.keySet()){
            if (dto.getMmsi().equals(line[0]) && dto.getCallSign().equals(line[9]) && dto.getImo().equals(line[8].substring(3))) {
                shipDTOListMap.get(dto).add(new PositioningDataDTO(line[1],line[2],line[3],line[4],line[5],line[6],line[14],line[15]));
                return true;
            }
        }
        return false;
    }
}
