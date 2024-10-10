
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
 * REQUIRES: team != null
 * MODIFIES: this
 * EFFECTS: If the team has at least 11 players, adds the team to the repository;
 *          otherwise, does not add the team.
 */

    public void addTeam(Team team) throws IncompleteTeamException {
        
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

    
    // EFFECTS: Searches for teams based on budget, minimum average rating and
    // a player you might want to have on the team and then
    // returns matching teams.
    public List<Team> searchTeams(int budget, double minAverageRating, int chemistry,String desiredPlayerName) {
        return new ArrayList<>();
    }
    
}
