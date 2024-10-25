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

        // Initialize players
        player1 = new Player("Player One", "Country A", "League X", "Club Alpha",
                "ST", "LM", 85, 80, 75, 85, 4, 500000);
        player2 = new Player("Player Two", "Country B", "League Y", "Club Beta",
                "CM", "GK", 82, 70, 80, 65, 3, 450000);
        player3 = new Player("Player Three", "Country C", "League Z", "Club Gamma",
                "GK", "GK", 80, 85, 70, 80, 5, 400000);
        // Initialize other players as needed

        // Create complete teams
        team1 = new Team("Team 1");
        for (int i = 1; i <= 11; i++) {
            team1.addPlayer(createPlayer(i));
        }
        team1.setListed(true);
        repository.addTeamToCommunity(team1);

        team2 = new Team("Team 2");
        for (int i = 2; i <= 12; i++) {
            team2.addPlayer(createPlayer(i));
        }
        team2.setListed(true);
        repository.addTeamToCommunity(team2);

        // Create an incomplete team
        incompleteTeam = new Team("Incomplete Team");
        incompleteTeam.addPlayer(player1);
        incompleteTeam.addPlayer(player2);
        incompleteTeam.setListed(true);
        // Not adding to repository since it's incomplete
    }

    private Player createPlayer(int number) {
        switch (number) {
            case 1:
                return player1;
            case 2:
                return player2;
            case 3:
                return player3;
            // Add cases for other players
            default:
                return new Player("Player " + number, "Country X", "League Y", "Club Z",
                        "Position", "Position", 80, 80, 80, 80, 4, 400000 + (number * 10000));
        }
    }
    

    @Test
    public void testAddTeamToCommunity() {
        Team team3 = new Team("Team 3");
        for (int i = 1; i <= 11; i++) {
            team3.addPlayer(createPlayer(i));
        }
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
        //int chemistry = 50;
         
        String desiredPlayerName = "Player One";

        List<Team> searchResults = repository.searchTeams(budget, minAverageRating,  desiredPlayerName);
        assertEquals(1, searchResults.size());
        assertTrue(searchResults.contains(team1));
    }

    @Test
    public void testSearchTeamsNoResults() {
        // Criteria that no team meets
        int budget = 1000;
        double minAverageRating = 99.9;
        //int chemistry = 100;
        String desiredPlayerName = "Nonexistent Player";

        List<Team> searchResults = repository.searchTeams(budget, minAverageRating,  desiredPlayerName);
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
        Team team3 = new Team("Team 3");
        for (int i = 1; i <= 11; i++) {
            team3.addPlayer(createPlayer(i));
        }
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
