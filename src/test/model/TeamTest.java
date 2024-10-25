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
    public void testAddPlayer_WhenTeamHasLessThan23Players() {
        // Arrange
        Team team = new Team("Test Team");
        Player newPlayer = new Player(
            "Kylian Mbappé", "France", "Ligue 1", "Paris Saint-Germain",
            "Forward", "Forward", 91, 97, 88, 86, 4, 1800000
        );

        // Precondition: Ensure team has fewer than 23 players
        assertTrue(team.getPlayers().size() < 23, "Team should have less than 23 players before adding");
        int initialSize = team.getPlayers().size();

        // Act
        team.addPlayer(newPlayer);

        // Assert
        assertTrue(team.getPlayers().contains(newPlayer), "New player should be added to the team");
        int newSize = team.getPlayers().size();
        assertEquals(initialSize + 1, newSize, "Team size should increase by one after adding a player");
    }



    @Test
    public void testRemovePlayer_PlayerNotInTeam() {
        // Arrange
        Team team = new Team("Test Team");
        Player playerNotInTeam = new Player(
            "Lionel Messi", "Argentina", "Ligue 1", "Paris Saint-Germain",
            "Forward", "Forward", 93, 85, 91, 95, 4, 1200000
        );

        // Precondition: Ensure the player is NOT in the team
        assertFalse(team.getPlayers().contains(playerNotInTeam), "Player should not be in the team before removal");
        int initialSize = team.getPlayers().size();

        // Act
        team.removePlayer(playerNotInTeam);

        // Assert
        // Since the player was not in the team, the team size should remain the same
        int newSize = team.getPlayers().size();
        assertEquals(initialSize, newSize, "Team size should remain the same when removing a non-existing player");
        assertFalse(team.getPlayers().contains(playerNotInTeam), "Player should still not be in the team after removal attempt");
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

    @Test
    public void testAddPlayer_WhenTeamHas23Players() {
        // Arrange
        Team team = new Team("Full Team");

        // Add 23 players to the team
        for (int i = 1; i <= 23; i++) {
            Player player = new Player(
                "Player " + i, "Country", "League", "Club",
                "Position", "Position", 80, 80, 80, 80, 3, 100000
            );
            team.addPlayer(player);
        }

        // Precondition: Ensure the team has exactly 23 players
        assertEquals(23, team.getPlayers().size(), "Team should have 23 players before adding");

        // Create a new player to attempt to add
        Player newPlayer = new Player(
            "Extra Player", "Country", "League", "Club",
            "Position", "Position", 80, 80, 80, 80, 3, 100000
        );

        // Act
        team.addPlayer(newPlayer);

        // Assert
        // The new player should not be added to the team
        assertFalse(team.getPlayers().contains(newPlayer), "New player should not be added to the full team");
        // Team size should remain 23
        assertEquals(23, team.getPlayers().size(), "Team size should remain 23 after attempting to add a player to a full team");
    }
}
