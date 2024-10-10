
package model;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TeamRepoTests {

    private TeamRepository repository;
    private Team team1;
    private Team team2;
    private Team incompleteTeam;
    private Team team3;
    private Team team4;

    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;
    private Player player5;
    private Player player6;
    private Player player7;
    private Player player8;
    private Player player9;
    private Player player10;
    private Player player11;
    private Player player12;

    @BeforeEach
    @SuppressWarnings("methodlength")
    public void setUp() {
        repository = new TeamRepository();

        // Create players
        player1 = new Player("Player One", "Country A", "League X", "Club Alpha",
                "ST", "ST", 85, 80, 75, 85, 4, 500000);
        player2 = new Player("Player Two", "Country B", "League Y", "Club Beta",
                "CM", "CM", 82, 70, 80, 65, 3, 450000);
        player3 = new Player("Player Three", "Country C", "League Z", "Club Gamma",
                "GK", "GK", 80, 85, 70, 80, 5, 400000);
        player4 = new Player("Player Four", "Country D", "League W", "Club Delta",
                "LW", "LW", 88, 90, 85, 87, 5, 600000);
        player5 = new Player("Player Five", "Country E", "League V", "Club Epsilon",
                "RB", "RB", 78, 75, 65, 70, 2, 350000);
        player6 = new Player("Player Six", "Country F", "League U", "Club Zeta",
                "LB", "LB", 81, 82, 79, 80, 3, 380000);
        player7 = new Player("Player Seven", "Country G", "League T", "Club Eta",
                "CB", "CB", 84, 70, 65, 60, 2, 420000);
        player8 = new Player("Player Eight", "Country H", "League S", "Club Theta",
                "CB", "CB", 83, 68, 66, 62, 2, 410000);
        player9 = new Player("Player Nine", "Country I", "League R", "Club Iota",
                "CDM", "CDM", 79, 72, 74, 68, 3, 370000);
        player10 = new Player("Player Ten", "Country J", "League Q", "Club Kappa",
                "RM", "RM", 80, 85, 80, 78, 4, 390000);
        player11 = new Player("Player Eleven", "Country K", "League P", "Club Lambda",
                "LM", "LM", 82, 84, 81, 79, 4, 400000);
        player12 = new Player("Player Twelve", "Country L", "League O", "Club Mu",
                "CAM", "CAM", 86, 88, 85, 83, 5, 650000);

        // Create teams with at least 11 players
        team1 = new Team();
        for (int i = 1; i <= 11; i++) {
            team1.addPlayer(getPlayerByNumber(i));
        }
        team1.likeTeam();
        team1.likeTeam(); // 2 likes

        team2 = new Team();
        for (int i = 2; i <= 12; i++) {
            team2.addPlayer(getPlayerByNumber(i));
        }
        team2.likeTeam(); // 1 like

        // Create an incomplete team with fewer than 11 players
        incompleteTeam = new Team();
        incompleteTeam.addPlayer(player1);
        incompleteTeam.addPlayer(player2);
        // Only 2 players in incompleteTeam

        // Add teams to repository
        try {
            repository.addTeam(team1);
            repository.addTeam(team2);
        } catch (IncompleteTeamException e) {
            fail("Exception should not have been thrown for complete teams.");
        }

        // Note: team3 and team4 are added in specific test methods when needed
    }

    @SuppressWarnings("methodlength")
    // Helper method to get players by number
    private Player getPlayerByNumber(int number) {
        switch (number) {
            case 1:
                return player1;
            case 2:
                return player2;
            case 3:
                return player3;
            case 4:
                return player4;
            case 5:
                return player5;
            case 6:
                return player6;
            case 7:
                return player7;
            case 8:
                return player8;
            case 9:
                return player9;
            case 10:
                return player10;
            case 11:
                return player11;
            case 12:
                return player12;
            default:
                return null;
        }
    }

    @Test
    public void testAddTeamIncompleteTeamThrowsException() {
        assertThrows(IncompleteTeamException.class, () -> {
            repository.addTeam(incompleteTeam);
        });
    }

    @Test
    public void testAddTeamCompleteTeamAdded() {
        team3 = new Team();
        for (int i = 1; i <= 12; i++) { // Adding 12 players
            team3.addPlayer(getPlayerByNumber(i));
        }
        try {
            repository.addTeam(team3);
        } catch (IncompleteTeamException e) {
            fail("Exception should not have been thrown for a complete team.");
        }
        List<Team> allTeams = repository.getAllTeams();
        assertEquals(3, allTeams.size()); // Now should have 3 teams
        assertTrue(allTeams.contains(team3));
    }

    @Test
    public void testAddTeamTeamWithExactly11Players() {
        team4 = new Team();
        for (int i = 1; i <= 11; i++) {
            team4.addPlayer(getPlayerByNumber(i));
        }
        try {
            repository.addTeam(team4);
        } catch (IncompleteTeamException e) {
            fail("Exception should not have been thrown for a complete team.");
        }
        List<Team> allTeams = repository.getAllTeams();
        assertEquals(3, allTeams.size()); // Now should have 3 teams
        assertTrue(allTeams.contains(team4));
    }

    @Test
    public void testGetAllTeams() {
        List<Team> allTeams = repository.getAllTeams();
        assertEquals(2, allTeams.size());
        assertTrue(allTeams.contains(team1));
        assertTrue(allTeams.contains(team2));
    }

    @Test
    public void testGetTeamsByPopularity() {
        // Adding team3 and team4 for this test
        team3 = new Team();
        for (int i = 1; i <= 11; i++) {
            team3.addPlayer(getPlayerByNumber(i));
        }
        team3.likeTeam();
        team3.likeTeam();
        team3.likeTeam(); // 3 likes

        team4 = new Team();
        for (int i = 2; i <= 12; i++) {
            team4.addPlayer(getPlayerByNumber(i));
        }
        // No likes

        try {
            repository.addTeam(team3);
            repository.addTeam(team4);
        } catch (IncompleteTeamException e) {
            fail("Exception should not have been thrown for complete teams.");
        }

        List<Team> popularTeams = repository.getTeamsByPopularity();
        // Teams should be sorted by likes in descending order
        assertEquals(4, popularTeams.size());
        assertEquals(team3, popularTeams.get(0)); // 3 likes
        assertEquals(team1, popularTeams.get(1)); // 2 likes
        assertEquals(team2, popularTeams.get(2)); // 1 like
        assertEquals(team4, popularTeams.get(3)); // 0 likes
    }

    @SuppressWarnings("methodlength")
    @Test
    public void testGetTeamsByPopularityAfterLikesChange() {
        // Adding team3 and team4 for this test
        team3 = new Team();
        for (int i = 1; i <= 11; i++) {
            team3.addPlayer(getPlayerByNumber(i));
        }
        team3.likeTeam();
        team3.likeTeam(); // 2 likes
        team3.likeTeam(); // 3 likes

        team4 = new Team();
        for (int i = 2; i <= 12; i++) {
            team4.addPlayer(getPlayerByNumber(i));
        }
        // No likes initially

        try {
            repository.addTeam(team3);
            repository.addTeam(team4);
        } catch (IncompleteTeamException e) {
            fail("Exception should not have been thrown for complete teams.");
        }

        // Like team4 three times
        team4.likeTeam();
        team4.likeTeam();
        team4.likeTeam(); // 3 likes
        team4.likeTeam(); // 4 likes

        List<Team> popularTeams = repository.getTeamsByPopularity();
        // Teams should be re-sorted based on updated likes
        assertEquals(4, popularTeams.size());
        assertEquals(team4, popularTeams.get(0)); // team4 now has 4 likes
        assertEquals(team3, popularTeams.get(1)); // team3 has 3 likes
        assertEquals(team1, popularTeams.get(2)); // 2 likes
        assertEquals(team2, popularTeams.get(3)); // 1 like
    }

    @Test
    public void testSearchTeams() {
        // Assuming the chemistry and average ratings are calculated correctly

        // Define search criteria
        int budget = team1.getTotalPrice() + 1000;
        double minAverageRating = team1.getAverageRating() - 0.1; // Slightly less than team1's average rating
        int chemistry = team1.calculateChemistry() - 1; // Slightly less than team1's chemistry
        String desiredPlayerName = "Player One";

        // Search for teams that meet the criteria
        List<Team> searchResults = repository.searchTeams(budget, minAverageRating, chemistry, desiredPlayerName);
        assertEquals(1, searchResults.size());
        assertTrue(searchResults.contains(team1));

        // Now adjust criteria to include team2
        budget = Math.max(team1.getTotalPrice(), team2.getTotalPrice()) + 1000;
        minAverageRating = Math.min(team1.getAverageRating(), team2.getAverageRating()) - 0.1;
        chemistry = Math.min(team1.calculateChemistry(), team2.calculateChemistry()) - 1;
        desiredPlayerName = "Player Two";

        searchResults = repository.searchTeams(budget, minAverageRating, chemistry, desiredPlayerName);
        assertEquals(2, searchResults.size());
        assertTrue(searchResults.contains(team2));
    }

    @Test
    public void testSearchTeamsNoResults() {
        // Criteria that no team meets
        int budget = 1000; // Too low
        double minAverageRating = 99.9; // Unrealistically high
        int chemistry = 1000; // Unrealistically high
        String desiredPlayerName = "Nonexistent Player";

        List<Team> searchResults = repository.searchTeams(budget, minAverageRating, chemistry, desiredPlayerName);
        assertTrue(searchResults.isEmpty());
    }

    @Test
    public void testSearchTeamsPlayerNotInTeam() {
        // Search for a player not in any team
        int budget = 10000000; // High budget to include all teams
        double minAverageRating = 0.0; // Low to include all teams
        int chemistry = 0; // Low to include all teams
        String desiredPlayerName = "Nonexistent Player";

        List<Team> searchResults = repository.searchTeams(budget, minAverageRating, chemistry, desiredPlayerName);
        assertTrue(searchResults.isEmpty());
    }

    @Test
public void testSearchTeamsByBudget() {
    // Get total prices of team1 and team2
        int team1Price = team1.getTotalPrice();
        int team2Price = team2.getTotalPrice();

    // Scenario 1: Budget lower than any team's price
        List<Team> affordableTeams = repository.searchTeamsByBudget(1000);
        assertTrue(affordableTeams.isEmpty(), "No teams should be affordable with a budget of 1000");

    // Scenario 2: Budget allows for team1 only
        int budget = team1Price + 1000; // Slightly above team1's price
        affordableTeams = repository.searchTeamsByBudget(budget);
        assertEquals(1, affordableTeams.size(), "Only team1 should be affordable");
        assertTrue(affordableTeams.contains(team1), "Affordable teams should contain team1");

    // Scenario 3: Budget allows for both teams
        budget = Math.max(team1Price, team2Price) + 1000;
        affordableTeams = repository.searchTeamsByBudget(budget);
        assertEquals(2, affordableTeams.size(), "Both teams should be affordable");
        assertTrue(affordableTeams.contains(team1), "Affordable teams should contain team1");
        assertTrue(affordableTeams.contains(team2), "Affordable teams should contain team2");
    }

    @Test
    public void testAddTeamNullTeamThrowsException() {
        assertThrows(NullPointerException.class, () -> {
            repository.addTeam(null);
        }, "Expected addTeam(null) to throw NullPointerException");
    }


}
