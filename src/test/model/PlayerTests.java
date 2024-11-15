package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTests {

    private Player player;

    @BeforeEach
    public void setUp() {
        player = new Player(
                "Player One",
                "Country A",
                "League X",
                "Club Alpha",
                "ST",          // preferredPosition
                "ST",          // currentPosition
                85,            // rating
                80,            // pace
                75,            // passing
                85,            // shooting
                82,            // dribbling
                60,            // defending
                70,            // physicality
                4,             // skillMoves
                5,             // weakFoot
                500000,        // price
                false          // isInStarting11
        );
    }

    @Test
    public void testPlayerConstructor() {
        assertEquals("Player One", player.getName());
        assertEquals("Country A", player.getNationality());
        assertEquals("League X", player.getLeague());
        assertEquals("Club Alpha", player.getClubAffiliation());
        assertEquals("ST", player.getPreferredPosition());
        assertEquals("ST", player.getCurrentPosition());
        assertEquals(85, player.getRating());
        assertEquals(80, player.getPace());
        assertEquals(75, player.getPassing());
        assertEquals(85, player.getShooting());
        assertEquals(82, player.getDribbling());
        assertEquals(60, player.getDefending());
        assertEquals(70, player.getPhysicality());
        assertEquals(4, player.getSkillMoves());
        assertEquals(5, player.getWeakFoot());
        assertEquals(500000, player.getPrice());
        assertFalse(player.isInStarting11());
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

        player.setPreferredPosition("BOGUS");
        player.setCurrentPosition("ST");
        assertFalse(player.isPositionCompatible());
    }

   
}
