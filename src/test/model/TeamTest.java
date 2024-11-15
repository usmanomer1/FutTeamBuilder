// TeamTest.java
package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TeamTest {

    private Team team;
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;

    @BeforeEach
    public void setUp() {
        team = new Team("Test Team", "433"); // Team now requires a formation type
        player1 = new Player(
                "Player One", "Country A", "League X", "Club Alpha",
                "ST", "ST", 85, 80, 75, 85, 82, 60, 70,
                4, 5, 500000, false
        );
        player2 = new Player(
                "Player Two", "Country A", "League X", "Club Beta",
                "CM", "CM", 82, 70, 80, 65, 78, 65, 68,
                3, 4, 450000, false
        );
        player3 = new Player(
                "Player Three", "Country B", "League Y", "Club Beta",
                "GK", "GK", 80, 85, 70, 80, 50, 82, 80,
                1, 3, 400000, false
        );
        player4 = new Player(
                "Player Four", "Country C", "League Y", "Club Gamma",
                "RB", "RB", 78, 75, 65, 70, 76, 70, 72,
                2, 3, 350000, false
        );
    }

    @Test
    public void testAddPlayer() {
        assertTrue(team.addPlayer(player1));
        assertEquals(1, team.getPlayers().size());
        assertTrue(team.addPlayer(player2));
        assertEquals(2, team.getPlayers().size());

        // Adding more players to test capacity
        for (int i = 1; i <= 20; i++) {
            Player extraPlayer = new Player(
                    "Player " + i, "Country A", "League X", "Club Alpha",
                    "ST", "ST", 85, 80, 75, 85, 82, 60, 70,
                    4, 5, 500000, false
            );
            assertTrue(team.addPlayer(extraPlayer));
        }
        assertEquals(22, team.getPlayers().size()); // 2 initial players + 20 added
    }

    @Test
    public void testAddPlayer_TeamFull() {
        // Fill the team with 23 players
        for (int i = 1; i <= 23; i++) {
            Player player = new Player(
                    "Player " + i, "Country", "League", "Club",
                    "Position", "Position", 80, 80, 80, 80, 80, 80, 80,
                    3, 3, 100000, false
            );
            assertTrue(team.addPlayer(player));
        }
        // Attempt to add another player
        Player extraPlayer = new Player(
                "Extra Player", "Country", "League", "Club",
                "Position", "Position", 80, 80, 80, 80, 80, 80, 80,
                3, 3, 100000, false
        );
        assertFalse(team.addPlayer(extraPlayer)); // Should not be added
        assertEquals(23, team.getPlayers().size());
    }

    @Test
    public void testSetPlayerInStarting11() {
        team.addPlayer(player1);
        team.addPlayer(player2);
        // Set players in starting 11
        assertTrue(team.setPlayerInStarting11(player1, true));
        assertTrue(team.setPlayerInStarting11(player2, true));
        // Starting lineup should have 2 players
        assertEquals(2, team.getStartingPlayers().size());
    }

    @Test
    public void testSetPlayerInStarting11_InvalidPosition() {
        team.addPlayer(player1);
        // Change player's position to one not in the formation
        player1.setCurrentPosition("InvalidPosition");
        assertFalse(team.setPlayerInStarting11(player1, true));
        assertEquals(0, team.getStartingPlayers().size());
    }

    @Test
    public void testIsStartingLineupComplete() {
        // Add players to team and set in starting 11
        addStartingPlayersToTeam();
        assertTrue(team.isStartingLineupComplete());
    }

    @Test
    public void testCalculateChemistry() {
        // Add starting players
        addStartingPlayersToTeam();
        // Calculate chemistry
        int chemistry = team.calculateChemistry();
        // Since exact chemistry depends on player attributes and links, check for expected range
        assertTrue(chemistry > 0);
        System.out.println("Team Chemistry: " + chemistry);
    }

    @Test
    public void testCalculateChemistry_IncompleteLineup() {
        // Add some players but not enough to complete the lineup
        team.addPlayer(player1);
        team.setPlayerInStarting11(player1, true);
        assertFalse(team.isStartingLineupComplete());
        int chemistry = team.calculateChemistry();
        assertEquals(0, chemistry); // Chemistry should be 0 for incomplete lineup
    }

    @Test
    public void testRemovePlayer() {
        team.addPlayer(player1);
        team.addPlayer(player2);
        assertTrue(team.removePlayer(player1));
        assertEquals(1, team.getPlayers().size());
        assertFalse(team.getPlayers().contains(player1));
        assertTrue(team.getPlayers().contains(player2));
    }

    @Test
    public void testGetTotalPrice() {
        team.addPlayer(player1);
        team.addPlayer(player2);
        assertEquals(950000, team.getTotalPrice());
    }

    @Test
    public void testGetAverageRating() {
        assertEquals(0.0, team.getAverageRating(), 0.01);
        team.addPlayer(player1);
        team.addPlayer(player2);
        assertEquals(83.5, team.getAverageRating(), 0.01);
    }

    @Test
    public void testLikeTeam() {
        assertEquals(0, team.getLikes());
        team.likeTeam();
        assertEquals(1, team.getLikes());
        team.likeTeam();
        assertEquals(2, team.getLikes());
    }

    @Test
    public void testIsListed() {
        assertFalse(team.isListed());
        team.setListed(true);
        assertTrue(team.isListed());
    }

    @Test
    public void testHasPlayer() {
        team.addPlayer(player1);
        assertTrue(team.hasPlayer("Player One"));
        assertFalse(team.hasPlayer("Player Two"));
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

    // Helper method to add starting players matching the formation
    private void addStartingPlayersToTeam() {
        // Positions required by the "433" formation
        String[] positions = {"GK", "LB", "LCB", "RCB", "RB", "LCM", "CM", "RCM", "LW", "RW", "ST"};
        for (String position : positions) {
            Player player = new Player(
                    "Player " + position, "Country", "League", "Club",
                    position, position, 80, 80, 80, 80, 80, 80, 80,
                    3, 3, 100000, false
            );
            team.addPlayer(player);
            team.setPlayerInStarting11(player, true);
        }
    }
}
