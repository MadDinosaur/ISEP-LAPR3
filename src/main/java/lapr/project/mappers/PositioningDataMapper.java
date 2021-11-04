package lapr.project.mappers;

import lapr.project.mappers.dto.PositioningDataDTO;
import lapr.project.model.Coordinate;
import lapr.project.model.PositioningData;
import lapr.project.store.list.PositioningDataList;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PositioningDataMapper {

    /**
     * transforms a dto int an positioningData object
     * @param dto a package with all the positioning data
     * @return positioningData object
     */
    public static PositioningData toModel(PositioningDataDTO dto){
        try {
            return new PositioningData(dto.getBdt(), new Coordinate(Float.parseFloat(dto.getLongitude()), Float.parseFloat(dto.getLatitude())), Float.parseFloat(dto.getSog()),
                    Float.parseFloat(dto.getCog()), Float.parseFloat(dto.getHeading()), dto.getPosition(), dto.getTransceiverClass());
        } catch (IllegalArgumentException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * transforms a list of dtos into a list of positioningData
     * @param dtoList a package with all the positioning data
     * @return positioningData List
     */
    public static PositioningDataList toModel(List<PositioningDataDTO> dtoList){
        List<PositioningData> positioningDataList = new ArrayList<>();
        for (PositioningDataDTO positioningDataDTO : dtoList) {
            PositioningData positioningData = toModel(positioningDataDTO);
            if (positioningData != null){
                positioningDataList.add(positioningData);
            }
        }
        PositioningDataList positioningDataList1 = new PositioningDataList();
        positioningDataList1.addPositioningDataList(positioningDataList);
        return positioningDataList1;
    }

    public static PositioningDataDTO toDTO(PositioningData positionData){
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        Date bdt = positionData.getBdt();
        String bdtString = df.format(bdt);
        return new PositioningDataDTO(bdtString,Float.toString(positionData.getCoordinate().getLatitude()), Float.toString(positionData.getCoordinate().getLongitude()),
                Float.toString(positionData.getSog()), Float.toString(positionData.getCog()), Float.toString(positionData.getHeading()), positionData.getPosition(), positionData.getTransceiverClass());
    }

    public static List<PositioningDataDTO> toDTO(List<PositioningData> positioningDataList){

        List<PositioningDataDTO> tListDTO = new ArrayList<>();

        for (PositioningData positioningData : positioningDataList) {
            tListDTO.add(toDTO(positioningData));
        }

        return tListDTO;
    }
}
