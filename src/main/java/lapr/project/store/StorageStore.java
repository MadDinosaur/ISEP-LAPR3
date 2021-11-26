package lapr.project.store;

import lapr.project.model.BST2DTree;
import lapr.project.model.Coordinate;
import lapr.project.model.Storage;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that will serve as the Storage Store
 */
public class StorageStore extends BST2DTree<Storage> {

    /**
     * Adds a list of storage to the store
     * @param toAddList the storage list that will be added
     * @return True if added with success, False if there was a problem adding the storage
     */
    public boolean addStorage(List<Storage> toAddList) {

        List<Storage> newFullList = inOrder();
        List<Node<Storage>> nodeList = new ArrayList<>();

        for(Storage storage : toAddList){
            if(validateStorage(storage))
                newFullList.add(storage);
        }

        for(Storage storage : newFullList){
            Coordinate coordinate = storage.getCoordinate();
            nodeList.add(new Node<Storage>(storage,coordinate.getLongitude(),coordinate.getLatitude()));
        }

        insert(nodeList);

        return true;
    }

    /**
     * Validates the given storage
     * @param storage a storage
     * @return True if the storage is valid, False if it is not valid
     */
    private boolean validateStorage(Storage storage) {
        return storage != null;
    }

    /**
     * Returns the closest Storage to the given coordinates
     * @param coordinate the Coordinate we want to check
     * @return the Storage that is closest to the given coordinates
     */
    public Storage searchClosestStorage(Coordinate coordinate) {
        return findNearestNeighbour(coordinate.getLongitude(), coordinate.getLatitude());
    }
}