package lapr.project.controller;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class getCargoManifestInformationControllerTest {

    @Test
    public  void aTest() throws SQLException {
        getCargoManifestInformationController a = new getCargoManifestInformationController();
        System.out.println(a.findCargoManifests(1, 2010));
    }

}