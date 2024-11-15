// TeamRepositoryTest.java
package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TeamRepositoryTest {

    private TeamRepository repository;
    private Team team1;
    private Team team2;
    private Team incompleteTeam;

    private Player player1;
    private Player player2;
    private Player player3;
    // ... other players as needed

    @BeforeEach
    public void setUp() {
        repository = new TeamRepository();

        // Initialize players with updated constructor
        player1 = new Player(
                "Player One", "Country A", "League X", "Club Alpha",
                "ST", "ST", 85, 80, 75, 85, 82, 60, 70,
                4, 5, 500000, false
        );
        player2 = new Player(
                "Player Two", "Country B", "League Y", "Club Beta",
                "CM", "CM", 82, 70, 80, 65, 78, 65, 68,
                3, 4, 450000, false
        );
        player3 = new Player(
                "Player Three", "Country C", "League Z", "Club Gamma",
                "GK", "GK", 80, 85, 70, 80, 50, 82, 80,
                1, 3, 400000, false
        );
        // Initialize other players as needed

        // Create complete teams with formation types
        team1 = new Team("Team 1", "433");
        addPlayersToTeam(team1, 1, 11);
        team1.setListed(true);
        repository.addTeamToCommunity(team1);

        team2 = new Team("Team 2", "451");
        addPlayersToTeam(team2, 2, 12);
        team2.setListed(true);
        repository.addTeamToCommunity(team2);

        // Create an incomplete team
        incompleteTeam = new Team("Incomplete Team", "433");
        incompleteTeam.addPlayer(player1);
        incompleteTeam.addPlayer(player2);
        incompleteTeam.setListed(true);
        // Not adding to repository since it's incomplete
    }

    // Helper method to add players to a team and set them in the starting 11
    private void addPlayersToTeam(Team team, int startNumber, int endNumber) {
        for (int i = startNumber; i <= endNumber; i++) {
            Player player = createPlayer(i);
            team.addPlayer(player);
            // Attempt to set the player in the starting 11
            boolean success = team.setPlayerInStarting11(player, true);
            if (!success) {
                // Handle the case where the player cannot be added to the starting 11
                System.out.println("Warning: Could not set player " + player.getName() + " in starting 11 for team " + team.getName());
            }
        }
    }

    private Player createPlayer(int number) {
        // Create players with positions matching the formation
        String position = getPositionForNumber(number);
        return new Player(
                "Player " + number, "Country X", "League Y", "Club Z",
                position, position, 80 + (number % 5), 80, 80, 80, 80, 80, 80,
                3, 3, 400000 + (number * 10000), false
        );
    }

    // Helper method to map a player number to a position
    private String getPositionForNumber(int number) {
        // Example positions for the "433" formation
        String[] positions = {"GK", "LB", "LCB", "RCB", "RB", "LCM", "CM", "RCM", "LW", "RW", "ST"};
        return positions[(number - 1) % positions.length];
    }

    @Test
    public void testAddTeamToCommunity() {
        Team team3 = new Team("Team 3", "433");
        addPlayersToTeam(team3, 1, 11);
        team3.setListed(true);
        repository.addTeamToCommunity(team3);

        List<Team> communityTeams = repository.getAllCommunityTeams();
        assertEquals(3, communityTeams.size());
        assertTrue(communityTeams.contains(team3));
    }

    @Test
    public void testGetAllCommunityTeams() {
        List<Team> allTeams = repository.getAllCommunityTeams();
        assertEquals(2, allTeams.size());
        assertTrue(allTeams.contains(team1));
        assertTrue(allTeams.contains(team2));
    }

    @Test
    public void testGetTeamsByPopularity() {
        team1.likeTeam(); // team1 has 1 like
        team2.likeTeam();
        team2.likeTeam(); // team2 has 2 likes

        List<Team> popularTeams = repository.getTeamsByPopularity();
        assertEquals(2, popularTeams.size());
        assertEquals(team2, popularTeams.get(0)); // Most likes
        assertEquals(team1, popularTeams.get(1));
    }

    @Test
    public void testSearchTeams() {
        // Define search criteria
        int budget = 50000000;
        double minAverageRating = 80.0;
        String desiredPlayerName = "Player 1";

        List<Team> searchResults = repository.searchTeams(budget, minAverageRating, desiredPlayerName);
        assertEquals(1, searchResults.size());
        assertTrue(searchResults.contains(team1));
    }

    @Test
    public void testSearchTeamsNoResults() {
        // Criteria that no team meets
        int budget = 1000;
        double minAverageRating = 99.9;
        String desiredPlayerName = "Nonexistent Player";

        List<Team> searchResults = repository.searchTeams(budget, minAverageRating, desiredPlayerName);
        assertTrue(searchResults.isEmpty());
    }

    @Test
    public void testSearchTeamsByBudget() {
        int budget = team1.getTotalPrice() + 10000;

        List<Team> affordableTeams = repository.searchTeamsByBudget(budget);
        assertTrue(affordableTeams.contains(team1));
        assertFalse(affordableTeams.contains(team2));

        budget = Math.max(team1.getTotalPrice(), team2.getTotalPrice()) + 1000;
        affordableTeams = repository.searchTeamsByBudget(budget);
        assertTrue(affordableTeams.contains(team1));
        assertTrue(affordableTeams.contains(team2));
    }

    @Test
    public void testAddTeamToCommunityNotListed() {
        Team team3 = new Team("Team 3", "433");
        addPlayersToTeam(team3, 1, 11);
        team3.setListed(false);
        repository.addTeamToCommunity(team3);

        List<Team> communityTeams = repository.getAllCommunityTeams();
        assertEquals(2, communityTeams.size()); // team3 should not be added
        assertFalse(communityTeams.contains(team3));
    }

    @Test
    public void testAddIncompleteTeamToCommunity() {
        incompleteTeam.setListed(true);
        repository.addTeamToCommunity(incompleteTeam);

        List<Team> communityTeams = repository.getAllCommunityTeams();
        assertEquals(2, communityTeams.size(), "Incomplete team should not be added to community");
        assertFalse(communityTeams.contains(incompleteTeam), "Community teams should not contain incomplete team");
    }

    @Test
    public void testAddTeamToCommunityNullTeam() {
        assertDoesNotThrow(() -> repository.addTeamToCommunity(null), "Method should handle null input gracefully");
    }
}
