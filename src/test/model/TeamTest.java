
package model;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class TeamTest {

    private Team team;
    private Player player1;
    private Player player2;

    @BeforeEach
    public void setUp() {
        team = new Team();
        player1 = new Player("Player One", "Country A", 85, "Midfielder", 75, 80, 70, 3, "Club A", 500000);
        player2 = new Player("Player Two", "Country B", 88, "Forward", 85, 75, 90, 4, "Club B", 750000);
    }

    @Test
    public void testAddPlayer() {
        team.addPlayer(player1);
        assertEquals(1, team.getPlayers().size());
        team.addPlayer(player2);
        assertEquals(2, team.getPlayers().size());
    }

    @Test
    public void testGetTotalPrice() {
        team.addPlayer(player1);
        team.addPlayer(player2);
        assertEquals(1250000, team.getTotalPrice());
    }

    @Test
    public void testLikeTeam() {
        team.likeTeam();
        assertEquals(1, team.getLikes());
    }

    
    @Test
    public void testRemovePlayer() {
        team.addPlayer(player1);
        team.addPlayer(player2);
        team.removePlayer(player1);
        assertEquals(1, team.getPlayers().size());
        assertFalse(team.getPlayers().contains(player1));
    }

    @Test
    public void testGetAverageRating() {
        team.addPlayer(player1);
        team.addPlayer(player2);
        assertEquals(86.5, team.getAverageRating(), 0.01);
    }

    @Test
    public void testGetLikes() {
        assertEquals(0, team.getLikes());
        team.likeTeam();
        assertEquals(1, team.getLikes());
    }

    @Test
    public void testGetPlayers() {
        team.addPlayer(player1);
        team.addPlayer(player2);
        List<Player> players = team.getPlayers();
        assertEquals(2, players.size());
        assertTrue(players.contains(player1));
        assertTrue(players.contains(player2));
    }
}
