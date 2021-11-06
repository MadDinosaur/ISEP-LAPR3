package lapr.project.mappers;

import lapr.project.mappers.dto.PositioningDataDTO;
import lapr.project.mappers.dto.ShipDTO;
import lapr.project.model.PositioningData;
import lapr.project.model.Ship;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ShipMapperTest {
    private String mmsi = "111111111";
    private String shipName = "ship name";
    private String imo = "1111111";
    private String callSign = "SH123";
    private String vesselType = "12345";
    private String length = "12.0";
    private String width = "12.0";
    private String draft = "12.0";

    @Test
    public void toDTOAndToModelTest(){
        ShipDTO dto = new ShipDTO(mmsi, shipName, imo, callSign, vesselType, length, width, draft);
        Ship ship = ShipMapper.toModel(dto);
        assertNotNull(ship);
        ShipDTO dto1 = ShipMapper.toDTO(ship);
        assertNotNull(dto1);
    }

    @Test
    public void illegalToModelTest(){
        ShipDTO dto = new ShipDTO(mmsi, shipName, imo, callSign, "asd", length, width, draft);
        Ship ship = ShipMapper.toModel(dto);
        assertNull(ship);
    }

    @Test
    public void toModelListTest(){
        Map<ShipDTO, List<PositioningDataDTO>> shipData = new HashMap<>();
        ShipDTO dto1 = new ShipDTO(mmsi, shipName, imo, callSign, vesselType, length, width, draft);
        ShipDTO dto2 = new ShipDTO(mmsi, shipName, imo, callSign, "asd", length, width, draft);
        PositioningDataDTO dto = new PositioningDataDTO("01/01/20021 13:00", "90.0", "180.0", "12.0", "359.0","359.0", "42.0","A");
        List<PositioningDataDTO> dtoList = new ArrayList<>();
        dtoList.add(dto);
        shipData.put(dto1, dtoList);
        shipData.put(dto2, dtoList);
        List<Ship> ship = ShipMapper.toModel(shipData);
        assertNotNull(ship.get(0));
    }
}