package lapr.project.model;

import lapr.project.controller.GetPositionByDateController;
import lapr.project.controller.ReadShipFileController;
import lapr.project.controller.SearchForShipController;
import lapr.project.mappers.dto.PositioningDataDTO;
import lapr.project.mappers.dto.ShipDTO;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * CalculatorExample Class Unit Testing.
 *
 * @author Nuno Bettencourt nmb@isep.ipp.pt on 24/10/21.
 */
public class CalculatorExampleTest {
    /**
     * Ensure second operand can assume a negative value.
     */
    @Test
    public void ensureSecondNegativeOperandWorks() {
        int expected = 5;
        int firstOperand = 10;
        int secondOperand = -5;
        CalculatorExample calculator = new CalculatorExample();
        int result = calculator.sum(firstOperand, secondOperand);
        assertEquals(expected, result);
    }
}
