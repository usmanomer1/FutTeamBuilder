package model;



import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Represents a user in the application.
 */
public class User {
    private String username;
    private String passwordHash; // Hashed password for security
    private List<Team> teams;

    public User(String username, String passwordHash, List<Team> teams) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.teams = teams != null ? teams : new ArrayList<>();
    }


    
    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void addTeam(Team team) {
        teams.add(team);
    }

    /**
     * EFFECTS: Returns true if the provided password matches the user's password.
     *
     * @param password the password to check
     * @return true if passwords match, false otherwise
     */
    public boolean checkPassword(String password) {
        return PasswordUtils.verifyPassword(password, passwordHash);
    }


     /**
     * Converts the User to a JSONObject.
     *
     * @return JSONObject representation of the User
     */
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("username", username);
        json.put("passwordHash", passwordHash);

        JSONArray teamsArray = new JSONArray();
        for (Team team : teams) {
            teamsArray.put(team.toJson());
        }
        json.put("teams", teamsArray);

        return json;
    }
}
