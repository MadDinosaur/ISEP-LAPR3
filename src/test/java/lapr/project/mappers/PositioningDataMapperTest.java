package lapr.project.mappers;

import lapr.project.mappers.dto.PositioningDataDTO;
import lapr.project.model.PositioningData;
import lapr.project.store.list.PositioningDataList;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PositioningDataMapperTest {

    private String bdt = "01/01/20021 13:00";
    private String latitude = "90.0";
    private String longitude = "180.0";
    private String sog = "12.0";
    private String cog = "359.0";
    private String heading = "359.0";
    private String position = "42.0";
    private String transceiverClass = "A";

    @Test
    public void toModelAndToDtoTest(){
        PositioningDataDTO dto = new PositioningDataDTO(bdt, latitude, longitude, sog, cog, heading, position, transceiverClass);
        PositioningData positioningData = PositioningDataMapper.toModel(dto);
        assertNotNull(positioningData);
        PositioningDataDTO dto1 = PositioningDataMapper.toDTO(positioningData);
        assertEquals(dto.getBdt(), dto1.getBdt());
        assertEquals(dto.getLatitude(), dto1.getLatitude());
        assertEquals(dto.getLongitude(), dto1.getLongitude());
        assertEquals(dto.getSog(), dto1.getSog());
        assertEquals(dto.getCog(), dto1.getCog());
        assertEquals(dto.getHeading(), dto1.getHeading());
        assertEquals(dto.getPosition(), dto1.getPosition());
        assertEquals(dto.getTransceiverClass(), dto1.getTransceiverClass());
    }

    @Test
    public void illegalToModelTest(){
        PositioningDataDTO dto = new PositioningDataDTO(bdt, latitude, longitude, "asd", cog, heading, position, transceiverClass);
        PositioningData positioningData = PositioningDataMapper.toModel(dto);
        assertNull(positioningData);
    }

    @Test
    public void toModelAndToDtoListTest(){
        PositioningDataDTO dto = new PositioningDataDTO(bdt, latitude, longitude, sog, cog, heading, position, transceiverClass);
        PositioningDataDTO dto2 = new PositioningDataDTO(bdt, latitude, longitude, "asd", cog, heading, position, transceiverClass);
        List<PositioningDataDTO> dtoList = new ArrayList<>();
        dtoList.add(dto);
        dtoList.add(dto2);
        PositioningDataList positioningData = PositioningDataMapper.toModel(dtoList);
        assertEquals(positioningData.getPositioningDataList().get(0).getTransceiverClass(), transceiverClass);
        List<PositioningDataDTO> dto1 = PositioningDataMapper.toDTO(positioningData.getPositioningDataList());
        assertEquals(dto.getBdt(), dto1.get(0).getBdt());
        assertEquals(dto.getLatitude(), dto1.get(0).getLatitude());
        assertEquals(dto.getLongitude(), dto1.get(0).getLongitude());
        assertEquals(dto.getSog(), dto1.get(0).getSog());
        assertEquals(dto.getCog(), dto1.get(0).getCog());
        assertEquals(dto.getHeading(), dto1.get(0).getHeading());
        assertEquals(dto.getPosition(), dto1.get(0).getPosition());
        assertEquals(dto.getTransceiverClass(), dto1.get(0).getTransceiverClass());
    }
}