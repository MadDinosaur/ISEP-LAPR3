package lapr.project.controller;

import lapr.project.model.Ship;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class VesselSinkControllerTest {

    String shipName = "Example";
    String mmsi = "210950000";
    int imo = 9450648;
    String callSign = "C4SQ2";
    int vesselType = 0;
    float length = 0;
    float width = 0;
    float draft = 0;


    @Test
    public void vesselSink(){
        Ship ship = new Ship(mmsi,shipName,imo,callSign,vesselType,length,width,draft);
        ship.setLength(320.04f);
        ship.setWidth(33.53f);
        VesselSinkController  controller = new VesselSinkController();
        HashMap<String,Double> result = controller.vesselSink(ship,300);

        assertNotNull(result);
        assertEquals(result.get("Height"),0.013978270746282107);
        assertEquals(result.get("Container Weight"),150000);
        assertEquals(result.get("Pressure"),8.20839504739878E7);
    }

}