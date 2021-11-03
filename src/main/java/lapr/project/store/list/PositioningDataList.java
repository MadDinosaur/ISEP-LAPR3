package lapr.project.store.list;

import lapr.project.model.PositioningData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PositioningDataList {

    /**
     * list of a given ships positioning data
     */
    List<PositioningData> positioningDataList = new ArrayList<>();

    /**
     * populates and rearranges the list
     * @param positioningDataList a list of positioning data
     */
    public void addPositioningDataList(List<PositioningData> positioningDataList) {
        this.positioningDataList.addAll(positioningDataList);
        Collections.sort(this.positioningDataList);
    }

    /**
     * returns the list of position data
     * @return returns the list of position data
     */
    public List<PositioningData> getPositioningDataList() {
        return positioningDataList;
    }
}
