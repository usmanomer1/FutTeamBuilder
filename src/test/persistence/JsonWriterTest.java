// JsonWriterTest.java
package persistence;

import model.Player;
import model.Team;
import model.User;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class JsonWriterTest {

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
            fail("Expected FileNotFoundException to be thrown");
        } catch (FileNotFoundException e) {
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
            assertEquals("{\"users\":[]}", content.trim().replaceAll("\\s+", ""));
        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }
    }

    @Test
    public void testWriteUserManagerWithUsers() {
        try {
            // Create a user and a team
            User user = new User("john_doe", "hashed_password", null);
            Team team = new Team("John's Dream Team", "433"); // Updated constructor with formationType
            team.setLikes(5);
            team.setListed(true);

            // Add players to the team and set them in the starting 11
            for (int i = 1; i <= 11; i++) {
                String position = getPositionForNumber(i);
                Player player = new Player(
                        "Player " + i,
                        "Country",
                        "League",
                        "Club",
                        position, // preferredPosition
                        position, // currentPosition
                        80,       // rating
                        80,       // pace
                        80,       // passing
                        80,       // shooting
                        80,       // dribbling
                        80,       // defending
                        80,       // physicality
                        4,        // skillMoves
                        5,        // weakFoot
                        500000,   // price
                        true      // isInStarting11
                );
                team.addPlayer(player);
                team.setPlayerInStarting11(player, true);
            }

            user.addTeam(team);

            // Write to file
            writer.open();
            writer.writeAllUsers(java.util.Collections.singletonList(user));
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

            String formationType = teamJson.getString("formationType");
            assertEquals("433", formationType);

            // Assertions for players
            JSONArray playersArray = teamJson.getJSONArray("players");
            assertEquals(11, playersArray.length()); // Check the number of players

            for (int i = 0; i < playersArray.length(); i++) {
                JSONObject playerJson = playersArray.getJSONObject(i);
                String playerName = playerJson.getString("name");
                assertEquals("Player " + (i + 1), playerName);

                assertEquals(80, playerJson.getInt("rating"));
                assertEquals(80, playerJson.getInt("pace"));
                assertEquals(80, playerJson.getInt("shooting"));
                assertEquals(80, playerJson.getInt("passing"));
                assertEquals(80, playerJson.getInt("dribbling"));
                assertEquals(80, playerJson.getInt("defending"));
                assertEquals(80, playerJson.getInt("physicality"));
                assertEquals(4, playerJson.getInt("skillMoves"));
                assertEquals(5, playerJson.getInt("weakFoot"));
                assertEquals(500000, playerJson.getInt("price"));

                String preferredPosition = playerJson.getString("preferredPosition");
                String currentPosition = playerJson.getString("currentPosition");
                String position = getPositionForNumber(i + 1);
                assertEquals(position, preferredPosition);
                assertEquals(position, currentPosition);

                boolean isInStarting11 = playerJson.getBoolean("isInStarting11");
                assertTrue(isInStarting11);
            }

        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException should not have been thrown");
        }
    }

    // Helper method to map player numbers to positions for the "433" formation
    private String getPositionForNumber(int number) {
        String[] positions = {"GK", "LB", "LCB", "RCB", "RB", "LCM", "CM", "RCM", "LW", "RW", "ST"};
        return positions[(number - 1) % positions.length];
    }
}
