package lapr.project.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CartesianCoordinateTest {


    @Test
    public void coordinateTest(){
        CartesianCoordinate<Integer> cartesianCoordinate = new CartesianCoordinate<>(1,1,1);
        cartesianCoordinate.setX(2);
        cartesianCoordinate.setY(2);
        cartesianCoordinate.setZ(2);
        assertEquals(2, cartesianCoordinate.getX());
        assertEquals(2, cartesianCoordinate.getY());
        assertEquals(2, cartesianCoordinate.getZ());

        assertEquals(cartesianCoordinate, cartesianCoordinate);
        assertNotEquals(cartesianCoordinate, null);
        assertNotEquals(cartesianCoordinate, "3");
        assertNotEquals(cartesianCoordinate, new CartesianCoordinate<>(1,2,2));
        assertNotEquals(cartesianCoordinate, new CartesianCoordinate<>(2,1,2));
        assertNotEquals(cartesianCoordinate, new CartesianCoordinate<>(2,2,1));
        assertEquals(cartesianCoordinate, new CartesianCoordinate<>(2,2,2));

        assertEquals(cartesianCoordinate.toString(), "(2,2,2)" );
    }
}