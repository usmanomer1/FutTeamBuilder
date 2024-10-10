// TeamRepository.java
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
    public void addTeam(Team team) throws IncompleteTeamException {
        if (team == null) {
            throw new NullPointerException("Team cannot be null.");
        }
        if (team.getPlayers().size() >= 11) {
            teams.add(team);
        } else {
            throw new IncompleteTeamException("Team must have at least 11 players.");
        }
    }

    /**
     * EFFECTS: Returns a list of all teams.
     */
    public List<Team> getAllTeams() {
        return new ArrayList<>(teams);
    }

    /**
     * EFFECTS: Searches for teams based on budget and returns matching teams.
     */
    public List<Team> searchTeamsByBudget(int budget) {
        List<Team> result = new ArrayList<>();
        for (Team t : teams) {
            if (t.getTotalPrice() <= budget) {
                result.add(t);
            }
        }
        return result;
    }

    /**
     * EFFECTS: Returns teams sorted by likes in descending order.
     */
    public List<Team> getTeamsByPopularity() {
        teams.sort((t1, t2) -> t2.getLikes() - t1.getLikes());
        return new ArrayList<>(teams);
    }
    /* REQUIRES: desiredPlayerName != null
     * EFFECTS:
     * Returns a list of teams that satisfy the following conditions:
     * - The team's total price is less than or equal to the given budget.
     * - The team's average rating is greater than or equal to minAverageRating.
     * - The team's chemistry is greater than or equal to the given chemistry value.
     * - The team contains a player whose name matches desiredPlayerName (case-insensitive).
   */
    public List<Team> searchTeams(int budget, double minAverageRating, int chemistry, String desiredPlayerName) {
        List<Team> result = new ArrayList<>();
        for (Team team : teams) {
            if (team.getTotalPrice() <= budget &
                    team.getAverageRating() >= minAverageRating &
                    team.calculateChemistry() >= chemistry &
                    team.hasPlayer(desiredPlayerName)) {
                result.add(team);
            }
        }
        return result;
    }
}
