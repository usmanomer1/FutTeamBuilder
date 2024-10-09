package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTests {

    private Player player;

    @BeforeEach
    public void setUp() {
        player = new Player("Player One", "Country A", 85, "Midfielder", 75, 80, 70, 3, "Club A", 500000);
    }

    @Test
    public void testPlayerConstructor() {
        assertEquals("Player One", player.getName());
        assertEquals("Country A", player.getNationality());
        assertEquals(85, player.getRating());
        assertEquals("Midfielder", player.getPosition());
        assertEquals(75, player.getPace());
        assertEquals(80, player.getPassing());
        assertEquals(70, player.getShooting());
        assertEquals(3, player.getWeakFoot());
        assertEquals("Club A", player.getClubAffiliation());
        assertEquals(500000, player.getPrice());
    }
}