package model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class PositionCompatibilityTests {

    @Test
    public void testIsCompatible_SamePosition() {
        assertTrue(PositionCompatibility.isCompatible("ST", "ST"));
    }

    @Test
    public void testIsCompatible_CompatiblePosition() {
        assertTrue(PositionCompatibility.isCompatible("ST", "CF"));
    }

    @Test
    public void testIsCompatible_IncompatiblePosition() {
        assertFalse(PositionCompatibility.isCompatible("ST", "GK"));
    }

    @Test
    public void testIsCompatible_InvalidPosition() {
        assertFalse(PositionCompatibility.isCompatible("ST", "XYZ"));
    }

    @Test
    public void testIsCompatible_CaseInsensitive() {
        assertTrue(PositionCompatibility.isCompatible("st", "Cf"));
    }

}
