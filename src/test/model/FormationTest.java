package model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class FormationTest {
    private static Formation formation442;
    private static Formation formation433;

    @BeforeAll
    static void setup() {
        formation442 = new Formation("442");
        formation433 = new Formation("433");
    }

    @Test
    void testValidFormationInitialization() {
        assertEquals("442", formation442.getFormationType());
        Set<String> requiredPositions = formation442.getRequiredPositions();
        assertTrue(requiredPositions.contains("GK"));
        assertTrue(requiredPositions.contains("LCM"));
        assertTrue(requiredPositions.contains("RCM"));
    }

    @Test
    void testInvalidFormationInitialization() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new Formation("999"));
        assertEquals("Unsupported formation type: 999", exception.getMessage());
    }

    @Test
    void testLinkedPositions() {
        List<String> linkedToST = formation433.getLinkedPositions("ST");
        assertTrue(linkedToST.contains("RW"));
        assertTrue(linkedToST.contains("LW"));

        List<String> linkedToCM = formation442.getLinkedPositions("CM");
        assertTrue(linkedToCM.isEmpty()); // CM is not in 442
    }

    @Test
    void testPositionCoordinates() {
        Map<String, double[]> coordinates = formation442.getPositionCoordinates();

        assertArrayEquals(new double[]{3, 0}, coordinates.get("GK"));
        assertArrayEquals(new double[]{0, 2}, coordinates.get("LB"));
    }

    @Test
    void testLoadFormationsData() {
        assertDoesNotThrow(() -> Formation.loadFormationsData());
        assertTrue(Formation.getAllFormationTypes().contains("442"));
        assertTrue(Formation.getAllFormationTypes().contains("433"));
        assertFalse(Formation.getAllFormationTypes().contains("999"));
    }
}
