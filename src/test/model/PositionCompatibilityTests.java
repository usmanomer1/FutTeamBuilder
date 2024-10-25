package model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class PositionCompatibilityTests {

    @Test
    public void setup() {
        new PositionCompatibility();
    }

    @Test
    public void testIsCompatibleSamePosition() {
        assertTrue(PositionCompatibility.isCompatible("ST", "ST"));
    }

    @Test
    public void testIsCompatibleCompatiblePosition() {
        assertTrue(PositionCompatibility.isCompatible("ST", "CF"));
    }

    @Test
    public void testIsCompatibleIncompatiblePosition() {
        assertFalse(PositionCompatibility.isCompatible("ST", "GK"));
    }

    @Test
    public void testIsCompatibleInvalidPosition() {
        assertFalse(PositionCompatibility.isCompatible("ST", "XYZ"));
    }

    @Test
    public void testIsCompatibleCaseInsensitive() {
        assertTrue(PositionCompatibility.isCompatible("st", "Cf"));
    }

}
