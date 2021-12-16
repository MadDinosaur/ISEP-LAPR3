package lapr.project.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColourGraphControllerTest {

    @Test
    public void ColourGraphControllerTest1(){
        ColourGraphController colourGraphController = new ColourGraphController();
        assertEquals(colourGraphController.getCountryColours().size(), 0);
        assertNotNull(colourGraphController.getCountryColours());
        assertNotNull(colourGraphController.getCountryAndBorderColours());
    }

}