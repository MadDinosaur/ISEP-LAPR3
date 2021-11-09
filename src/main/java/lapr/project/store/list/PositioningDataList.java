package lapr.project.store.list;

import lapr.project.mappers.PositioningDataMapper;
import lapr.project.mappers.dto.PositioningDataDTO;
import lapr.project.model.AVL;
import lapr.project.model.BST;
import lapr.project.model.Coordinate;
import lapr.project.model.PositioningData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class PositioningDataList extends AVL<PositioningData> {


    /**
     * populates and the avl tree
     * @param positioningData a list of positioning data
     */
    public void insertPositioningDataList(PositioningData positioningData) {
        insert(positioningData);
    }

    /**
     * returns the first date values
     * @return returns the first date values
     */
    public Date getFirstDate(){
        return smallestElement().getBdt();
    }

    /**
     * returns the last date values
     * @return returns the last date values
     */
    public Date getLastDate(){
        return biggestElement().getBdt();
    }

    /**
     * returns the total movement time values
     * @return returns the total movement time values
     */
    public float totalMovementTime(){
        return getLastDate().getTime() - getFirstDate().getTime();
    }

    /**
     * returns the departure latitude value
     * @return returns the departure latitude value
     */
    public Coordinate departureCoordinates(){
        return smallestElement().getCoordinate();
    }

    /**
     * returns the arrival latitude value
     * @return returns the arrival latitude value
     */
    public Coordinate arrivalCoordinates(){
        return biggestElement().getCoordinate();
    }

    /**
     * returns the max sog value
     * @return returns the max sog value
     */
    public float maxSog(){
        return maxSog(root());
    }

    private float maxSog(Node<PositioningData> node){
        float max = node.getElement().getSog();
        if (node.getRight() != null){
            float tempMax = maxSog(node.getRight());
            if (tempMax > max)
                max = tempMax;
        }
        if (node.getLeft() != null){
            float tempMax = maxSog(node.getLeft());
            if (tempMax > max)
                max = tempMax;
        }
        return max;
    }

    /**
     * returns the mean sog value
     * @return returns the mean sog value
     */
    public float meanSog(){
        return meanSog(root) / size();
    }

    private float meanSog(Node<PositioningData> node) {
        if (node == null)
            return 0;

        return meanSog(node.getLeft()) + meanSog(node.getRight()) + node.getElement().getSog();
    }

    /**
     * returns the max cog value
     * @return returns the max cog value
     */
    public float maxCog(){
        return maxCog(root());
    }

    private float maxCog(Node<PositioningData> node){
        float max = node.getElement().getCog();
        if (node.getRight() != null){
            float tempMax = maxSog(node.getRight());
            if (tempMax > max)
                max = tempMax;
        }
        if (node.getLeft() != null){
            float tempMax = maxSog(node.getLeft());
            if (tempMax > max)
                max = tempMax;
        }
        return max;
    }

    /**
     * returns the mean cog value
     * @return returns the mean cog value
     */
    public float meanCog(){
        return meanCog(root) / size();
    }

    private float meanCog(Node<PositioningData> node) {
        if (node == null)
            return 0;

        return meanSog(node.getLeft()) + meanSog(node.getRight()) + node.getElement().getCog();
    }

    /**
     * returns the total traveled distance value
     * @return returns the total traveled distance value
     */
    public double traveledDistance(){
        return traveledDistance(root());
    }

    private double traveledDistance(Node<PositioningData> node){
        float traveledDistance = 0;
        if (node.getLeft() != null){
            traveledDistance +=  node.getElement().getCoordinate().getDistanceBetweenCoordinates(biggestElement(node.getLeft()).getCoordinate());
        }
        if (node.getRight() != null){
            traveledDistance +=  node.getElement().getCoordinate().getDistanceBetweenCoordinates(biggestElement(node.getRight()).getCoordinate());
        }
        return traveledDistance;
    }

    /**
     * returns the delta distance value
     * @return returns the delta distance value
     */
    public double deltaDistance(){
        return departureCoordinates().getDistanceBetweenCoordinates(arrivalCoordinates());
    }

    /**
     * tranforms degrees into radians
     * @param deg degrees of an angle
     * @return value of degrees to radious
     */
    private float toRadious(float deg) {
        return (float) (deg * Math.PI / 180.0);
    }

    /**
     * returns a list of all position data that takes place in the specified time slot
     * @param date1 the initial date
     * @param date2 the final date
     * @return returns a list of all position data that takes place in the specified time slot
     */
    public PositioningDataList getPositionsByDate(Date date1, Date date2){

        PositioningDataList  tree = new PositioningDataList();

        getPositionsByDate(date1,date2,root,tree);

        return tree ;
    }



    private void getPositionsByDate(Date date1, Date date2, Node<PositioningData> node, PositioningDataList result){

        if ( node == null)
            return;

        if(node.getElement().getBdt().compareTo(date1) > 0 && node.getElement().getBdt().compareTo(date2) < 0){

            result.insert(node.getElement());
            getPositionsByDate(date1,date2,node.getLeft(),result);
            getPositionsByDate(date1,date2,node.getRight(),result);
        }
        else if (node.getElement().getBdt().compareTo(date1) < 0){

            getPositionsByDate(date1,date2,node.getRight(),result);
        }
        else
            getPositionsByDate(date1,date2, node.getLeft(),result);

    }

    private PositioningData biggestElement(){
        return  biggestElement(root);
    }

    private PositioningData biggestElement(Node<PositioningData> node){
        if (node.getRight() == null)
            return node.getElement();
        return smallestElement(node.getLeft());
    }

//    public BST<PositioningDataDTO> treeToDTO (){
//        BST<PositioningDataDTO> snapshot = new BST<>();
//        if (root!=null)
//            treeToDTO(root(), snapshot);   // fill the snapshot recursively
//        return snapshot;
//    }
//
//    private void treeToDTO(Node<PositioningData> node, BST<PositioningDataDTO> snapshot) {
//        if (node == null)
//            return;
//        treeToDTO(node.getLeft(), snapshot);
//        snapshot.insert(PositioningDataMapper.toDTO(node.getElement()));
//        treeToDTO(node.getRight(), snapshot);
//    }
}
