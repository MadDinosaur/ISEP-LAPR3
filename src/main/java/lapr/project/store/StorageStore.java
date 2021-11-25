package lapr.project.store;

import lapr.project.model.BST2DTree;
import lapr.project.model.Coordinate;
import lapr.project.model.Storage;

/**
 * A class that will serve as the Storage Store
 */
public class StorageStore extends BST2DTree<Storage> {

    /**
     * Adds a valid Storage to the Store
     * @param storage the storage that will be added
     * @return True if added with success, False if there was a problem adding the storage
     */
    public boolean addStorage(Storage storage) {
        if (!validateStorage(storage))
            return false;

        Coordinate coordinate = storage.getCoordinate();

        insert(storage, coordinate.getLongitude(), coordinate.getLatitude());
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