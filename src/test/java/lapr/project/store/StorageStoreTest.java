package lapr.project.store;

import lapr.project.mappers.StorageMapper;
import lapr.project.mappers.dto.StorageDTO;
import lapr.project.model.Coordinate;
import lapr.project.model.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StorageStoreTest {
    StorageStore storageStore = new StorageStore();
    Storage storage1, storage2, storage3;
    Coordinate coord1, coord2, coord3;
    List<Storage> storageList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        coord1 = new Coordinate(14.54306f, 35.84194f);
        coord2 = new Coordinate(10.21666667f, 56.15f);
        coord3 = new Coordinate(12.61666667f, 55.7f);

        storage1 = new Storage(10138, "Marsaxlokk", "Europe", "Malta", coord1);
        storage2 = new Storage(10358, "Aarhus", "Europe", "Denmark", coord2);
        storage3 = new Storage(10563, "Copenhagen", "Europe", "Denmark", coord3);

        storageList.add(storage1);
        storageList.add(storage2);
        storageList.add(storage3);
    }

    @Test
    void createStorageListSuccess() {
        StorageDTO storageDTO1 = StorageMapper.toDTO(storage1);
        StorageDTO storageDTO2 = StorageMapper.toDTO(storage2);
        StorageDTO storageDTO3 = StorageMapper.toDTO(storage3);

        List<StorageDTO> storageDTOList = new ArrayList<>();

        storageDTOList.add(storageDTO1);
        storageDTOList.add(storageDTO2);
        storageDTOList.add(storageDTO3);

        List<Storage> result = storageStore.createStorageList(storageDTOList);

        assertNotNull(result);
        assertEquals(result.size(), storageList.size());
    }

    @Test
    void addStorageListSuccess() {
        assertTrue(storageStore.addStorageList(storageList));
        assertEquals(storageStore.inOrder().size(), storageList.size());
    }

    @Test
    void searchClosestStorageSuccess() {
        Coordinate coord = new Coordinate(14f, 35f);

        storageStore.addStorageList(storageList);

        Storage result = storageStore.searchClosestStorage(coord);

        assertNotNull(result);
        assertEquals(result.getIdentification(), storage1.getIdentification());
    }
}