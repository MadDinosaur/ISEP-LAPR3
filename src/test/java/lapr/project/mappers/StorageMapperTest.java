package lapr.project.mappers;

import lapr.project.mappers.dto.StorageDTO;
import lapr.project.model.Coordinate;
import lapr.project.model.Storage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StorageMapperTest {
    private static Storage  storage;
    private static StorageDTO storageDTO;

    @BeforeAll
    static void setUp() {
        int identification = 10000;
        String name = "Larnaca";
        String continent = "Europe";
        String country = "Cyprus";
        Coordinate coordinate = new Coordinate(33.65f, 34.91f);

        storage = new Storage(identification, name, continent, country, coordinate);
        storageDTO = new StorageDTO(Integer.toString(identification), name, continent, country, Float.toString(coordinate.getLongitude()), Float.toString(coordinate.getLatitude()));
    }

    @Test
    void toDTOTestSuccess() {
        StorageDTO dtoResult = StorageMapper.toDTO(storage);

        assertNotNull(dtoResult);

        assertEquals(dtoResult.getIdentification(), storageDTO.getIdentification());
        assertEquals(dtoResult.getName(), storageDTO.getName());
        assertEquals(dtoResult.getContinent(), storageDTO.getContinent());
        assertEquals(dtoResult.getCountry(), storageDTO.getCountry());
        assertEquals(dtoResult.getLongitude(), storageDTO.getLongitude());
        assertEquals(dtoResult.getLatitude(), storageDTO.getLatitude());
    }

    @Test
    void toModelTestSuccess() {
        Storage storageResult = StorageMapper.toModel(storageDTO);

        assertNotNull(storageResult);

        assertEquals(storageResult.getIdentification(), storage.getIdentification());
        assertEquals(storageResult.getName(), storage.getName());
        assertEquals(storageResult.getContinent(), storage.getContinent());
        assertEquals(storageResult.getCountry(), storage.getCountry());
        assertEquals(storageResult.getCoordinate().getLongitude(), storage.getCoordinate().getLongitude());
        assertEquals(storageResult.getCoordinate().getLatitude(), storage.getCoordinate().getLatitude());
    }
}