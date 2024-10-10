
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
        for (int i=1; i<22; i++){
            team.addPlayer(new Player("Player "+i, "Country A", "League X", "Club Alpha",
            "ST", "ST", 85, 80, 75, 85, 4, 500000));
        }
        assertEquals(23, team.getPlayers().size());
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

    @Test
    public void testCalclateChemistry() {
       

    // Create players
    Player player1 = new Player("Player One", "Country A", "League X", "Club Alpha",
            "ST", "ST", 85, 80, 75, 85, 4, 500000);

    Player player2 = new Player("Player Two", "Country A", "League X", "Club Beta",
            "CM", "CDM", 82, 70, 80, 65, 3, 450000);

    Player player3 = new Player("Player Three", "Country B", "League Y", "Club Beta",
            "LW", "LM", 80, 85, 70, 80, 5, 400000);

    Player player4 = new Player("Player Four", "Country C", "League Z", "Club Gamma",
            "RB", "GK", 78, 75, 65, 70, 2, 350000);

    // Add players to team
    Team team = new Team();
    team.addPlayer(player1);
    team.addPlayer(player2);
    team.addPlayer(player3);
    team.addPlayer(player4);

    // Calculate chemistry
    int chemistry = team.calculateChemistry();

    // Manually calculated expected chemistry is 11
    assertEquals(11, chemistry);



    }

    @Test
    public void testLinkChemistry() {
     
    Player player1 = new Player("Player One", "Country A", "League X", "Club Alpha",
            "ST", "ST", 85, 80, 75, 85, 4, 500000);
    Player player2 = new Player("Player Two", "Country B", "League Y", "Club Alpha",
            "CM", "CM", 82, 70, 80, 65, 3, 450000);
    Player player3 = new Player("Player Three", "Country C", "League Z", "Club Alpha",
            "GK", "GK", 80, 85, 70, 80, 5, 400000);

    Team team = new Team();
    team.addPlayer(player1);
    team.addPlayer(player2);
    team.addPlayer(player3);

    // Neutralize position chemistry
    // Assuming position chemistry is already calculated separately
    

    int totalChemistry = team.calculateChemistry();

    // Position Chemistry: 3 * 5 = 15 (since all are in preferred positions)
    // Link Chemistry: 3 pairs * 3 = 9
    // Total Chemistry: 15 (position) + 9 (link) = 24

    // Since we are focusing on link chemistry, we can subtract position chemistry
    int expectedLinkChemistry = 9;
    int actualLinkChemistry = totalChemistry - (3 * 5); // Subtract position chemistry

    assertEquals(expectedLinkChemistry, actualLinkChemistry);


    }
}
