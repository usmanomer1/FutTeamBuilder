package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTests {

    private Player player;

    @BeforeEach
    public void setUp() {
       player = new Player("Player One", "Country A", "League X", "Club Alpha",
        "ST", "ST", 85, 80, 75, 85, 4, 500000);
    }

    @Test
    public void testPlayerConstructor() {
        assertEquals("Player One", player.getName());
        assertEquals("Country A", player.getNationality());
        assertEquals(85, player.getRating());
        assertEquals("Midfielder", player.getCurrentPosition());
        assertEquals(75, player.getPace());
        assertEquals(80, player.getPassing());
        assertEquals(70, player.getShooting());
        assertEquals(3, player.getWeakFoot());
        assertEquals("Club A", player.getClubAffiliation());
        assertEquals(500000, player.getPrice());
    }

    @Test
public void testIsInPreferredPosition() {
    player.setCurrentPosition("ST");
    assertTrue(player.isInPreferredPosition());

    player.setCurrentPosition("CF");
    assertFalse(player.isInPreferredPosition());
}

@Test
public void testIsPositionCompatible() {
    player.setPreferredPosition("ST");
    player.setCurrentPosition("CF");
    assertTrue(player.isPositionCompatible());

    player.setCurrentPosition("GK");
    assertFalse(player.isPositionCompatible());
}
}