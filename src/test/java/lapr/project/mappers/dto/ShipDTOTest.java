package lapr.project.mappers.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShipDTOTest {

    private String mmsi = "111111111";
    private String shipName = "ship name";
    private String imo = "1111111";
    private String callSign = "SH123";
    private String vesselType = "SH123";
    private String length = "12.0";
    private String width = "12.0";
    private String draft = "12.0";

    @Test
    public void getTest(){
        ShipDTO dto = new ShipDTO(mmsi, shipName, imo, callSign, vesselType, length, width, draft);
        assertEquals(dto.getMmsi(), mmsi);
        assertEquals(dto.getShipName(), shipName);
        assertEquals(dto.getImo(), imo);
        assertEquals(dto.getCallSign(), callSign);
        assertEquals(dto.getVesselType(), vesselType);
        assertEquals(dto.getLength(), length);
        assertEquals(dto.getWidth(), width);
        assertEquals(dto.getDraft(), draft);
    }
}
