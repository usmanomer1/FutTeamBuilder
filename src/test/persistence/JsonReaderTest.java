
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
            assertEquals(3, players.size(), "Team should have 11 players");

            Player player = players.get(0);
            assertEquals("Lionel Messi", player.getName());
            assertEquals("Argentina", player.getNationality());
            // ... Additional assertions for player attributes

        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }
    }

    public static class TestUtils {

        public static void writeStringToFile(String filePath, String content) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
                writer.print(content);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public static String getSampleJsonContent() {
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
                    "          \"formationType\": \"433\",\n" +
                    "          \"players\": [\n" +
                    "            {\n" +
                    "              \"league\": \"Ligue 1\",\n" +
                    "              \"name\": \"Lionel Messi\",\n" +
                    "              \"nationality\": \"Argentina\",\n" +
                    "              \"rating\": 93,\n" +
                    "              \"currentPosition\": \"RW\",\n" +
                    "              \"pace\": 85,\n" +
                    "              \"passing\": 91,\n" +
                    "              \"shooting\": 92,\n" +
                    "              \"weakFoot\": 4,\n" +
                    "              \"clubAffiliation\": \"PSG\",\n" +
                    "              \"price\": 1000000,\n" +
                    "              \"preferredPosition\": \"RW\",\n" +
                    "              \"isInStarting11\": true,\n" +
                    "              \"dribbling\": 96,\n" +
                    "              \"defending\": 40,\n" +
                    "              \"physicality\": 65,\n" +
                    "              \"skillMoves\": 5\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"league\": \"Serie A\",\n" +
                    "              \"name\": \"Cristiano Ronaldo\",\n" +
                    "              \"nationality\": \"Portugal\",\n" +
                    "              \"rating\": 92,\n" +
                    "              \"currentPosition\": \"ST\",\n" +
                    "              \"pace\": 89,\n" +
                    "              \"passing\": 81,\n" +
                    "              \"shooting\": 93,\n" +
                    "              \"weakFoot\": 5,\n" +
                    "              \"clubAffiliation\": \"Manchester United\",\n" +
                    "              \"price\": 1500000,\n" +
                    "              \"preferredPosition\": \"ST\",\n" +
                    "              \"isInStarting11\": true,\n" +
                    "              \"dribbling\": 87,\n" +
                    "              \"defending\": 35,\n" +
                    "              \"physicality\": 78,\n" +
                    "              \"skillMoves\": 5\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"league\": \"Ligue 1\",\n" +
                    "              \"name\": \"Neymar Jr\",\n" +
                    "              \"nationality\": \"Brazil\",\n" +
                    "              \"rating\": 91,\n" +
                    "              \"currentPosition\": \"LW\",\n" +
                    "              \"pace\": 92,\n" +
                    "              \"passing\": 86,\n" +
                    "              \"shooting\": 85,\n" +
                    "              \"weakFoot\": 5,\n" +
                    "              \"clubAffiliation\": \"Paris Saint-Germain\",\n" +
                    "              \"price\": 1200000,\n" +
                    "              \"preferredPosition\": \"LW\",\n" +
                    "              \"isInStarting11\": true,\n" +
                    "              \"dribbling\": 95,\n" +
                    "              \"defending\": 35,\n" +
                    "              \"physicality\": 60,\n" +
                    "              \"skillMoves\": 5\n" +
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
