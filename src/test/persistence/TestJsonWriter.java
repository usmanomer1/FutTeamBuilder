// JsonWriterTest.java
package persistence;

import model.Player;
import model.Team;
import model.User;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class TestJsonWriter {

    private static final String TEST_FILE = "./data/testWriteUsers.json";
    private JsonWriter writer;

    @BeforeEach
    public void setUp() {
        writer = new JsonWriter(TEST_FILE);
    }

    @Test
    public void testWriteInvalidFile() {
        JsonWriter invalidWriter = new JsonWriter("./data/\0invalid.json");
        try {
            invalidWriter.open();
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Expected exception
        }
    }

    @Test
    public void testWriteEmptyUserManager() {
        try {
            writer.open();
            writer.writeAllUsers(new java.util.ArrayList<>());
            writer.close();

            String content = new String(Files.readAllBytes(Paths.get(TEST_FILE)));
            assertEquals("{\"users\": []}", content.trim());
        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }
    }

    @Test
public void testWriteUserManagerWithUsers() {
    try {
        // Create a user and a team
        User user = new User("john_doe", "hashed_password", null);
        Team team = new Team("John's Dream Team");
        team.setLikes(5);
        team.setListed(true);

        // Add players to the team
        for (int i = 1; i <= 11; i++) {
            Player player = new Player("Player " + i, "Country", "League", "Club",
                    "Position", "Position", 80, 80, 80, 80, 4, 500000);
            team.addPlayer(player);
        }

        user.addTeam(team);

        // Write to file
        writer.open();
        writer.writeAllUsers(java.util.Arrays.asList(user));
        writer.close();

        // Read the content from the file
        String content = new String(Files.readAllBytes(Paths.get(TEST_FILE)));
        System.out.println("JSON Content Written:\n" + content);

        // Parse the JSON content
        JSONObject jsonObject = new JSONObject(content);
        JSONArray usersArray = jsonObject.getJSONArray("users");
        JSONObject userJson = usersArray.getJSONObject(0);

        // Assertions for user
        String username = userJson.getString("username");
        assertEquals("john_doe", username);

        String passwordHash = userJson.getString("passwordHash");
        assertEquals("hashed_password", passwordHash);

        // Assertions for team
        JSONArray teamsArray = userJson.getJSONArray("teams");
        JSONObject teamJson = teamsArray.getJSONObject(0);
        String teamName = teamJson.getString("name");
        assertEquals("John's Dream Team", teamName);

        int likes = teamJson.getInt("likes");
        assertEquals(5, likes);

        boolean isListed = teamJson.getBoolean("isListed");
        assertTrue(isListed);

        // Assertions for players
        JSONArray playersArray = teamJson.getJSONArray("players");
        assertEquals(11, playersArray.length()); // Check the number of players

        for (int i = 0; i < playersArray.length(); i++) {
            JSONObject playerJson = playersArray.getJSONObject(i);
            String playerName = playerJson.getString("name");
            assertTrue(playerName.contains("Player " + (i + 1)), "Player name should match");
            assertEquals(80, playerJson.getInt("rating"));
            assertEquals(80, playerJson.getInt("pace"));
            assertEquals(80, playerJson.getInt("shooting"));
            assertEquals(80, playerJson.getInt("passing"));
        }

    } catch (IOException e) {
        e.printStackTrace();
        fail("IOException should not have been thrown");
    }
}

}
