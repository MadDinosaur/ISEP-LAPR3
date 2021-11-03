package lapr.project.mappers;

import lapr.project.mappers.dto.PositioningDataDTO;
import lapr.project.mappers.dto.ShipDTO;
import lapr.project.model.Ship;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShipMapper {

    public static ShipDTO toDTO(Ship ship){
        return new ShipDTO(ship.getMmsi(), ship.getShipName(),Integer.toString(ship.getImo()) , ship.getCallSign(),Integer.toString(ship.getVesselType()),
                Long.toString(ship.getLength()), Long.toString(ship.getWidth()), Long.toString(ship.getDraft()) );
    }

    /**
     * transforms a dto int an Ship object
     * @param dto a package with all the Ship data
     * @return Ship object
     */
    public static Ship toModel(ShipDTO dto){
        try{
            return new Ship(dto.getMmsi(), dto.getShipName(), Integer.parseInt(dto.getImo()), dto.getCallSign(), Integer.parseInt(dto.getVesselType()),
                    Double.valueOf(dto.getLength()).longValue(), Double.valueOf(dto.getWidth()).longValue(), Double.valueOf(dto.getDraft()).longValue());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * transforms a list of dtos into a list of Ships
     * @param shipData a mapper with all the ship information and their positioning data
     * @return a ship list
     */
    public static List<Ship> toModel(Map<ShipDTO, List<PositioningDataDTO>> shipData){
        List<Ship> shipList = new ArrayList<>();
        for (ShipDTO dto : shipData.keySet()){
            Ship ship = toModel(dto);
            if (ship != null) {
                ship.setPositioningDataList(PositioningDataMapper.toModel(shipData.get(dto)));
                shipList.add(ship);
            }
        }
        return shipList;
    }
}
