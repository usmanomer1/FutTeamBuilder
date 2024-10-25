
package persistence;

import model.Player;
import model.Team;
import model.User;
import model.UserManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest {

    private static final String TEST_FILE = "./data/testUsers.json";
    private JsonReader reader;

    @BeforeEach
    public void setUp() {
        reader = new JsonReader(TEST_FILE);
    }

    @Test
    public void testReadNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        assertThrows(IOException.class, () -> {
            reader.read();
        });
    }

    @Test
    public void testReadEmptyUserManager() {
        // Prepare an empty JSON file for testing
        String emptyJson = "{ \"users\": [] }";
        TestUtils.writeStringToFile(TEST_FILE, emptyJson);

        try {
            UserManager userManager = reader.read();
            Collection<User> users = userManager.getAllUsers();
            assertTrue(users.isEmpty(), "UserManager should be empty");
        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }
    }

    @Test
    public void testReadUserManagerWithUsers() {
        // Prepare a JSON file with users, teams, and players
        String jsonContent = TestUtils.getSampleJsonContent();
        TestUtils.writeStringToFile(TEST_FILE, jsonContent);

        try {
            UserManager userManager = reader.read();
            Collection<User> users = userManager.getAllUsers();
            assertEquals(1, users.size(), "There should be 1 user");

            User user = users.iterator().next();
            assertEquals("john_doe", user.getUsername());

            List<Team> teams = user.getTeams();
            assertEquals(1, teams.size(), "User should have 1 team");

            Team team = teams.get(0);
            assertEquals("John's Dream Team", team.getName());
            assertEquals(5, team.getLikes());
            assertTrue(team.isListed());

            List<Player> players = team.getPlayers();
            assertEquals(11, players.size(), "Team should have 11 players");

            Player player = players.get(0);
            assertEquals("Lionel Messi", player.getName());
            assertEquals("Argentina", player.getNationality());
            // ... Additional assertions for player attributes

        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }
    }
    public class TestUtils {

    public static void writeStringToFile(String filePath, String content) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.print(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getSampleJsonContent() {
        // Return a sample JSON string representing users, teams, and players
        return "{\n" +
            "  \"users\": [\n" +
            "    {\n" +
            "      \"username\": \"john_doe\",\n" +
            "      \"passwordHash\": \"65536:AbCdEf123...:GhIjKl456...\",\n" +
            "      \"teams\": [\n" +
            "        {\n" +
            "          \"name\": \"John's Dream Team\",\n" +
            "          \"likes\": 5,\n" +
            "          \"isListed\": true,\n" +
            "          \"players\": [\n" +
            "            {\n" +
            "              \"name\": \"Lionel Messi\",\n" +
            "              \"nationality\": \"Argentina\",\n" +
            "              \"league\": \"Ligue 1\",\n" +
            "              \"clubAffiliation\": \"PSG\",\n" +
            "              \"preferredPosition\": \"RW\",\n" +
            "              \"currentPosition\": \"RW\",\n" +
            "              \"rating\": 93,\n" +
            "              \"pace\": 85,\n" +
            "              \"shooting\": 92,\n" +
            "              \"passing\": 91,\n" +
            "              \"skillMoves\": 5,\n" +
            "              \"price\": 1000000\n" +
            "            },\n" +
            "            {\n" +
            "              \"name\": \"Cristiano Ronaldo\",\n" +
            "              \"nationality\": \"Portugal\",\n" +
            "              \"league\": \"Serie A\",\n" +
            "              \"clubAffiliation\": \"Manchester United\",\n" +
            "              \"preferredPosition\": \"ST\",\n" +
            "              \"currentPosition\": \"ST\",\n" +
            "              \"rating\": 92,\n" +
            "              \"pace\": 89,\n" +
            "              \"shooting\": 93,\n" +
            "              \"passing\": 81,\n" +
            "              \"skillMoves\": 5,\n" +
            "              \"price\": 1500000\n" +
            "            },\n" +
            "            {\n" +
            "              \"name\": \"Neymar Jr\",\n" +
            "              \"nationality\": \"Brazil\",\n" +
            "              \"league\": \"Ligue 1\",\n" +
            "              \"clubAffiliation\": \"Paris Saint-Germain\",\n" +
            "              \"preferredPosition\": \"LW\",\n" +
            "              \"currentPosition\": \"LW\",\n" +
            "              \"rating\": 91,\n" +
            "              \"pace\": 92,\n" +
            "              \"shooting\": 85,\n" +
            "              \"passing\": 86,\n" +
            "              \"skillMoves\": 5,\n" +
            "              \"price\": 1200000\n" +
            "            },\n" +
            "            {\n" +
            "              \"name\": \"Kevin De Bruyne\",\n" +
            "              \"nationality\": \"Belgium\",\n" +
            "              \"league\": \"Premier League\",\n" +
            "              \"clubAffiliation\": \"Manchester City\",\n" +
            "              \"preferredPosition\": \"CM\",\n" +
            "              \"currentPosition\": \"CM\",\n" +
            "              \"rating\": 91,\n" +
            "              \"pace\": 76,\n" +
            "              \"shooting\": 86,\n" +
            "              \"passing\": 94,\n" +
            "              \"skillMoves\": 4,\n" +
            "              \"price\": 1100000\n" +
            "            },\n" +
            "            {\n" +
            "              \"name\": \"Robert Lewandowski\",\n" +
            "              \"nationality\": \"Poland\",\n" +
            "              \"league\": \"Bundesliga\",\n" +
            "              \"clubAffiliation\": \"Bayern Munich\",\n" +
            "              \"preferredPosition\": \"ST\",\n" +
            "              \"currentPosition\": \"ST\",\n" +
            "              \"rating\": 91,\n" +
            "              \"pace\": 78,\n" +
            "              \"shooting\": 92,\n" +
            "              \"passing\": 78,\n" +
            "              \"skillMoves\": 4,\n" +
            "              \"price\": 1000000\n" +
            "            },\n" +
            "            {\n" +
            "              \"name\": \"Kylian Mbappe\",\n" +
            "              \"nationality\": \"France\",\n" +
            "              \"league\": \"Ligue 1\",\n" +
            "              \"clubAffiliation\": \"Paris Saint-Germain\",\n" +
            "              \"preferredPosition\": \"ST\",\n" +
            "              \"currentPosition\": \"ST\",\n" +
            "              \"rating\": 90,\n" +
            "              \"pace\": 96,\n" +
            "              \"shooting\": 88,\n" +
            "              \"passing\": 82,\n" +
            "              \"skillMoves\": 5,\n" +
            "              \"price\": 1300000\n" +
            "            },\n" +
            "            {\n" +
            "              \"name\": \"Mohamed Salah\",\n" +
            "              \"nationality\": \"Egypt\",\n" +
            "              \"league\": \"Premier League\",\n" +
            "              \"clubAffiliation\": \"Liverpool\",\n" +
            "              \"preferredPosition\": \"RW\",\n" +
            "              \"currentPosition\": \"RW\",\n" +
            "              \"rating\": 90,\n" +
            "              \"pace\": 93,\n" +
            "              \"shooting\": 88,\n" +
            "              \"passing\": 81,\n" +
            "              \"skillMoves\": 4,\n" +
            "              \"price\": 900000\n" +
            "            },\n" +
            "            {\n" +
            "              \"name\": \"Sergio Ramos\",\n" +
            "              \"nationality\": \"Spain\",\n" +
            "              \"league\": \"Ligue 1\",\n" +
            "              \"clubAffiliation\": \"Paris Saint-Germain\",\n" +
            "              \"preferredPosition\": \"CB\",\n" +
            "              \"currentPosition\": \"CB\",\n" +
            "              \"rating\": 89,\n" +
            "              \"pace\": 75,\n" +
            "              \"shooting\": 66,\n" +
            "              \"passing\": 78,\n" +
            "              \"skillMoves\": 3,\n" +
            "              \"price\": 800000\n" +
            "            },\n" +
            "            {\n" +
            "              \"name\": \"Virgil van Dijk\",\n" +
            "              \"nationality\": \"Netherlands\",\n" +
            "              \"league\": \"Premier League\",\n" +
            "              \"clubAffiliation\": \"Liverpool\",\n" +
            "              \"preferredPosition\": \"CB\",\n" +
            "              \"currentPosition\": \"CB\",\n" +
            "              \"rating\": 89,\n" +
            "              \"pace\": 77,\n" +
            "              \"shooting\": 60,\n" +
            "              \"passing\": 72,\n" +
            "              \"skillMoves\": 2,\n" +
            "              \"price\": 750000\n" +
            "            },\n" +
            "            {\n" +
            "              \"name\": \"Manuel Neuer\",\n" +
            "              \"nationality\": \"Germany\",\n" +
            "              \"league\": \"Bundesliga\",\n" +
            "              \"clubAffiliation\": \"Bayern Munich\",\n" +
            "              \"preferredPosition\": \"GK\",\n" +
            "              \"currentPosition\": \"GK\",\n" +
            "              \"rating\": 89,\n" +
            "              \"pace\": 60,\n" +
            "              \"shooting\": 60,\n" +
            "              \"passing\": 70,\n" +
            "              \"skillMoves\": 1,\n" +
            "              \"price\": 700000\n" + 
            "            },\n" +
            "            {\n" +
            "              \"name\": \"New Player\",\n" +
            "              \"nationality\": \"Country\",\n" +
            "              \"league\": \"League\",\n" +
            "              \"clubAffiliation\": \"Club\",\n" +
            "              \"preferredPosition\": \"Position\",\n" +
            "              \"currentPosition\": \"Position\",\n" +
            "              \"rating\": 85,\n" +
            "              \"pace\": 80,\n" +
            "              \"shooting\": 85,\n" +
            "              \"passing\": 80,\n" +
            "              \"skillMoves\": 4,\n" +
            "              \"price\": 2000000\n" +
            "            }\n" +
            "          ]\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ]\n" +
            "}";
    }
}

}
