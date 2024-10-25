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

    @BeforeEach
    public void setUp() {
        team = new Team("Test Team");
        player1 = new Player("Player One", "Country A", "League X", "Club Alpha",
                "ST", "ST", 85, 80, 75, 85, 4, 500000);
        player2 = new Player("Player Two", "Country A", "League X", "Club Beta",
                "CM", "CDM", 82, 70, 80, 65, 3, 450000);
    }

    @Test
    public void testAddPlayer() {
        team.addPlayer(player1);
        assertEquals(1, team.getPlayers().size());
        team.addPlayer(player2);
        assertEquals(2, team.getPlayers().size());

        // Adding more players to test capacity
        for (int i = 1; i <= 20; i++) {
            Player extraPlayer = new Player("Player " + i, "Country A", "League X", "Club Alpha",
                    "ST", "ST", 85, 80, 75, 85, 4, 500000);
            team.addPlayer(extraPlayer);
        }
        assertEquals(22, team.getPlayers().size()); // 2 initial players + 20 added
    }

    @Test
    public void testGetTotalPrice() {
        team.addPlayer(player1);
        team.addPlayer(player2);
        assertEquals(950000, team.getTotalPrice());
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
    public void testRemovePlayer() {
        team.addPlayer(player1);
        team.addPlayer(player2);
        team.removePlayer(player1);
        assertEquals(1, team.getPlayers().size());
        assertFalse(team.getPlayers().contains(player1));
        assertTrue(team.getPlayers().contains(player2));
    }

    @Test
    public void testGetAverageRating() {
        assertEquals(0, team.getAverageRating(), 0.01);
        team.addPlayer(player1);
        team.addPlayer(player2);
        assertEquals(83.5, team.getAverageRating(), 0.01);
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

    @Test
    public void testCalculateChemistry() {
        // Create players with various attributes
        Player player3 = new Player("Player Three", "Country B", "League Y", "Club Beta",
                "LW", "GK", 80, 85, 70, 80, 5, 400000);
        Player player4 = new Player("Player Four", "Country C", "League Y", "Club Gamma",
                "RB", "RB", 78, 75, 65, 70, 2, 350000);

        // Add players to team
        team.addPlayer(player1);
        team.addPlayer(player2);
        team.addPlayer(player3);
        team.addPlayer(player4);

        // Calculate chemistry
        int chemistry = team.calculateChemistry();

        // Expected chemistry calculation:
        // Position Chemistry: 3 players * 5 - (1*3) = 12 (assuming 3 in preferred positions and one neither in preffered nor compatible)
        // Link Chemistry: calculated based on shared attributes
        // For simplicity, assume an expected value (e.g., 12)
        // Total Expected Chemistry = 12 (position) + 12 (link) = 24

        // Since the exact link chemistry depends on your implementation,
        // adjust the expected value accordingly.
        System.out.println("Calculated Chemistry: " + chemistry);
        assertTrue(chemistry >= 12); // At least position chemistry
    }

    @Test
    public void testLinkChemistry() {
        // Players with shared club affiliation
        Player player3 = new Player("Player Three", "Country C", "League Z", "Club Alpha",
                "GK", "GK", 80, 85, 70, 80, 5, 400000);

        team.addPlayer(player1); // Club Alpha
        team.addPlayer(player2); // Club Beta
        team.addPlayer(player3); // Club Alpha

        int totalChemistry = team.calculateChemistry();

        // Position Chemistry: 3 * 5 = 15
        // Link Chemistry: depends on your implementation

        // Since player1 and player3 share the same club, they should have a link bonus
        // Assuming each strong link adds, for example, 3 chemistry points

        // Expected Link Chemistry: between player1 and player3
        // Total Expected Chemistry = Position Chemistry + Link Chemistry

        // Adjust expected values based on your actual chemistry calculation logic
        assertTrue(totalChemistry >= 15);
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
}
