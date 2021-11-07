package lapr.project.mappers.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositioningDataDTOTest {

    private String bdt = "01/01/20021 00:00";
    private String latitude = "90";
    private String longitude = "180";
    private String sog = "12";
    private String cog = "359";
    private String heading = "359";
    private String position = "42";
    private String transceiverClass = "A";

    @Test
    public void DTOTest(){
        PositioningDataDTO dto = new PositioningDataDTO(bdt, latitude, longitude, sog, cog, heading, position, transceiverClass);
        assertEquals(dto.getBdt(), bdt);
        assertEquals(dto.getLatitude(), latitude);
        assertEquals(dto.getLongitude(), longitude);
        assertEquals(dto.getSog(), sog);
        assertEquals(dto.getCog(), cog);
        assertEquals(dto.getHeading(), heading);
        assertEquals(dto.getPosition(), position);
        assertEquals(dto.getTransceiverClass(), transceiverClass);
    }
}