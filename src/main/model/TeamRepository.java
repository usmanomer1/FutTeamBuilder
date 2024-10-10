
package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores and manages all the teams created by users.
 */
public class TeamRepository {

    private List<Team> teams;

    /**
     * MODIFIES: this
     * EFFECTS: Initializes an empty repository of teams.
     */
    public TeamRepository() {
        this.teams = new ArrayList<>();
    }

    /**
     * MODIFIES: this
     * EFFECTS: Adds a team to the repository.
     */
    public void addTeam(Team team) {
        
    }

    /**
     * EFFECTS: Returns a list of all teams.
     */
    public List<Team> getAllTeams() {
        return new ArrayList<>();
    }

    /**
     * EFFECTS: Searches for teams based on budget and returns matching teams.
     */
    public List<Team> searchTeamsByBudget(int budget) {
        
       
        return new ArrayList<>();
    }

    /**
     * EFFECTS: Returns teams sorted by likes in descending order.
     */
    public List<Team> getTeamsByPopularity() {
    
        return new ArrayList<>();
    }
}
