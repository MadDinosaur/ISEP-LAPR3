package lapr.project.store.list;

import lapr.project.model.AVL;
import lapr.project.model.Coordinate;
import lapr.project.model.PositioningData;

import java.util.Date;
import java.util.List;

public class PositioningDataTree extends AVL<PositioningData> {
    /**
     * populates and the avl tree
     * @param positioningData a list of positioning data
     */
    public void insertPositioningDataTree(PositioningData positioningData) {
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
        return (getLastDate().getTime() - getFirstDate().getTime())/60000;
    }

    public float totalMovementNumber(){
        return size();
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

    /**
     * returns the data's max speed over ground
     * @param node the node in which the maximum speed is being searched in
     * @return the Data's max Speed Over Ground
     */
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

    /**
     * returns the mean sog value
     * @param node the current node being added to the total count of speed
     * @return returns the mean sog value
     */
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

    /**
     * returns the max cog value
     * @param node the current node being counted for the max course over ground
     * @return returns the max cog value
     */
    private float maxCog(Node<PositioningData> node){
        float max = node.getElement().getCog();
        if (node.getRight() != null){
            float tempMax = maxCog(node.getRight());
            if (tempMax > max)
                max = tempMax;
        }
        if (node.getLeft() != null){
            float tempMax = maxCog(node.getLeft());
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

    /**
     * returns the mean cog value
     * @param node the current node being added to the total count of course over ground
     * @return returns the mean cog value
     */
    private float meanCog(Node<PositioningData> node) {
        if (node == null)
            return 0;

        return meanCog(node.getLeft()) + meanCog(node.getRight()) + node.getElement().getCog();
    }

    /**
     * returns the total traveled distance value
     * @return returns the total traveled distance value
     */
    public double traveledDistance(){
        return traveledDistance(root());
    }

    /**
     * Calculates the traveled distance from every point in the
     * @param node the root node
     * @return the traveled distance
     */
    private double traveledDistance(Node<PositioningData> node){
        float traveledDistance = 0;

        if(node == null)
            return 0;

        if (node.getLeft() != null){
            traveledDistance +=  node.getElement().getCoordinate().getDistanceBetweenCoordinates(biggestElement(node.getLeft()).getCoordinate());
            traveledDistance += traveledDistance(node.getLeft());
        }
        if (node.getRight() != null){
            traveledDistance +=  node.getElement().getCoordinate().getDistanceBetweenCoordinates(smallestElement(node.getRight()).getCoordinate());
            traveledDistance += traveledDistance(node.getRight());
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
     * returns a list of all position data that takes place in the specified time slot
     * @param date1 the initial date
     * @param date2 the final date
     * @return returns a list of all position data that takes place in the specified time slot
     */
    public PositioningDataTree getPositionsByDate(Date date1, Date date2){

        PositioningDataTree tree = new PositioningDataTree();

        getPositionsByDate(date1,date2,root,tree);

        return tree;
    }

    /**
     * returns a Data List with all the position between 2 time periods
     * @param date1 the initial date
     * @param date2 the final date
     * @param node the current node being searched
     * @param result the tree being modified
     */
    private void getPositionsByDate(Date date1, Date date2, Node<PositioningData> node, PositioningDataTree result){

        if ( node == null)
            return;

        if(node.getElement().getBdt().compareTo(date1) > 0 && node.getElement().getBdt().compareTo(date2) < 0){

            result.insert(node.getElement());
            getPositionsByDate(date1,date2,node.getLeft(),result);
            getPositionsByDate(date1,date2,node.getRight(),result);

        } else if (node.getElement().getBdt().compareTo(date1) < 0){

            getPositionsByDate(date1,date2,node.getRight(),result);
        } else
            getPositionsByDate(date1,date2, node.getLeft(),result);

    }

    /**
     * return the biggest element on the tree
     * @return returns the tree's biggest element
     */
    private PositioningData biggestElement(){
        return  biggestElement(root);
    }

    /**
     * returns the biggest element on the tree
     * @param node the current node being searched
     * @return returns the biggest element on the tree
     */
    private PositioningData biggestElement(Node<PositioningData> node){
        if (node.getRight() == null)
            return node.getElement();
        return biggestElement(node.getRight());
    }
}
