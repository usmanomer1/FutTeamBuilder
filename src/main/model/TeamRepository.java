

package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores and manages all the teams created by users.
 */
public class TeamRepository {

    private List<Team> communityTeams;

    /**
     * MODIFIES: this
     * EFFECTS: Initializes an empty repository of community teams.
     */
    public TeamRepository() {
        this.communityTeams = new ArrayList<>();
    }

    /**
     * EFFECTS: Adds a team to the community repository if it is listed.
     *
     * @param team the team to add
     */
    public void addTeamToCommunity(Team team) {
        if (team != null && team.isListed() && team.isComplete()) {
            communityTeams.add(team);
        }
    }
    
    

    /**
     * EFFECTS: Returns a list of all community teams.
     */
    public List<Team> getAllCommunityTeams() {
        return new ArrayList<>(communityTeams);
    }

    /**
     * EFFECTS: Searches community teams based on budget and returns matching teams.
     *
     * @param budget the maximum budget
     * @return list of teams within the budget
     */
    public List<Team> searchTeamsByBudget(int budget) {
        List<Team> result = new ArrayList<>();
        for (Team t : communityTeams) {
            if (t.getTotalPrice() <= budget) {
                result.add(t);
            }
        }
        return result;
    }

    /**
     * EFFECTS: Returns community teams sorted by likes in descending order.
     *
     * @return list of teams sorted by popularity
     */
    public List<Team> getTeamsByPopularity() {
        communityTeams.sort((t1, t2) -> t2.getLikes() - t1.getLikes());
        return new ArrayList<>(communityTeams);
    }

    /**
     * EFFECTS:
     * Returns a list of community teams that satisfy the following conditions:
     * - The team's total price is less than or equal to the given budget.
     * - The team's average rating is greater than or equal to minAverageRating.
     * - The team's chemistry is greater than or equal to the given chemistry value.
     * - The team contains a player whose name matches desiredPlayerName (case-insensitive).
     *
     * @param budget            the maximum budget
     * @param minAverageRating  the minimum average rating
     * @param chemistry         the minimum chemistry
     * @param desiredPlayerName the desired player name (can be partial or empty)
     * @return list of matching teams
     */
    public List<Team> searchTeams(int budget, double minAverageRating, String desiredPlayerName) {
        List<Team> result = new ArrayList<>();
        for (Team team : communityTeams) {
            boolean matchesBudget = team.getTotalPrice() <= budget;
            boolean matchesRating = team.getAverageRating() >= minAverageRating;
            boolean matchesPlayer = desiredPlayerName == null || desiredPlayerName.isEmpty()
                    || team.hasPlayer(desiredPlayerName);
    
            // Debugging output
            System.out.println("Team: " + team.getName());
            System.out.println("Total Price: " + team.getTotalPrice() + ", Budget: " + budget + ", Matches Budget: " + matchesBudget);
            System.out.println("Average Rating: " + team.getAverageRating() + ", Min Rating: " + minAverageRating + ", Matches Rating: " + matchesRating);
            System.out.println("Desired Player: " + desiredPlayerName + ", Matches Player: " + matchesPlayer);
    
            if (matchesBudget && matchesRating && matchesPlayer) {
                result.add(team);
            }
        }
        return result;
    }
    
}
