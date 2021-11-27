package lapr.project.mappers.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StorageDTOTest {
    private final String identification = "10000";
    private final String name = "Larnaca";
    private final String continent = "Europe";
    private final String country = "Cyprus";
    private final String longitude = "33.65";
    private final String latitude = "34.91f";
    private final StorageDTO storageDTO = new StorageDTO(identification, name, continent, country, longitude, latitude);

    @Test
    void getIdentification() {
        assertNotNull(storageDTO.getIdentification());
        assertEquals(storageDTO.getIdentification(), identification);
    }

    @Test
    void getName() {
        assertNotNull(storageDTO.getName());
        assertEquals(storageDTO.getName(), name);
    }

    @Test
    void getContinent() {
        assertNotNull(storageDTO.getContinent());
        assertEquals(storageDTO.getContinent(), continent);
    }

    @Test
    void getCountry() {
        assertNotNull(storageDTO.getCountry());
        assertEquals(storageDTO.getCountry(), country);
    }

    @Test
    void getLongitude() {
        assertNotNull(storageDTO.getLongitude());
        assertEquals(storageDTO.getLongitude(), longitude);
    }

    @Test
    void getLatitude() {
        assertNotNull(storageDTO.getLatitude());
        assertEquals(storageDTO.getLatitude(), latitude);
    }
}