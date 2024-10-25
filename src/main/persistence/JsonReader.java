// JsonReader.java

package persistence;

import model.User;
import model.UserManager;
import model.Team;
import model.Player;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Reads user data from JSON file.
 */
public class JsonReader {
    private String source;

    /**
     * Constructs a JsonReader to read from the specified source file.
     *
     * @param source the source file path
     */
    public JsonReader(String source) {
        this.source = source;
    }

    /**
     * Reads users from file and returns a UserManager containing them.
     *
     * @return UserManager with loaded users
     * @throws IOException if an error occurs reading data from file
     */
    public UserManager read() throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(source)));
        JSONObject json = new JSONObject(content);
        return parseUserManager(json);
    }

    /**
     * Parses UserManager from JSON object.
     *
     * @param json the JSON object
     * @return UserManager
     */
    private UserManager parseUserManager(JSONObject json) {
        UserManager userManager = new UserManager();
        JSONArray usersArray = json.getJSONArray("users");
        for (Object userObj : usersArray) {
            JSONObject userJson = (JSONObject) userObj;
            User user = parseUser(userJson);
            userManager.addUser(user);
        }
        return userManager;
    }

    /**
     * Parses a User from JSON object.
     *
     * @param json the JSON object
     * @return User
     */
    private User parseUser(JSONObject json) {
        String username = json.getString("username");
        String passwordHash = json.getString("passwordHash");
        User user = new User(username, passwordHash, null);

        JSONArray teamsArray = json.getJSONArray("teams");
        for (Object teamObj : teamsArray) {
            JSONObject teamJson = (JSONObject) teamObj;
            Team team = parseTeam(teamJson);
            user.addTeam(team);
        }
        return user;
    }

    /**
     * Parses a Team from JSON object.
     *
     * @param json the JSON object
     * @return Team
     */
    private Team parseTeam(JSONObject json) {
        String name = json.getString("name");
        int likes = json.getInt("likes");
        boolean isListed = json.getBoolean("isListed");
        Team team = new Team(name);
        team.setLikes(likes);
        team.setListed(isListed);

        JSONArray playersArray = json.getJSONArray("players");
        for (Object playerObj : playersArray) {
            JSONObject playerJson = (JSONObject) playerObj;
            Player player = parsePlayer(playerJson);
            team.addPlayer(player);
        }
        return team;
    }

    /**
     * Parses a Player from JSON object.
     *
     * @param json the JSON object
     * @return Player
     */
    private Player parsePlayer(JSONObject json) {
        String name = json.getString("name");
        String nationality = json.getString("nationality");
        String league = json.getString("league");
        String clubAffiliation = json.getString("clubAffiliation");
        String preferredPosition = json.getString("preferredPosition");
        String currentPosition = json.getString("currentPosition");
        int rating = json.getInt("rating");
        int pace = json.getInt("pace");
        int shooting = json.getInt("shooting");
        int passing = json.getInt("passing");
        int skillMoves = json.getInt("skillMoves");
        int price = json.getInt("price");

        return new Player(name, nationality, league, clubAffiliation, preferredPosition,
                currentPosition, rating, pace, shooting, passing, skillMoves, price);
    }
}
