// Team.java
package model;

import java.util.ArrayList;
import java.util.List;


/**
 * Represents a FIFA Ultimate Team composed of players.
 */
public class Team {
    private int totalPrice;
    private  int averageRating;;

    private List<Player> players;
    private int likes;

    /**
     * MODIFIES: this
     * EFFECTS: Initializes an empty team with zero likes.
     */
    public Team() {
        this.players = new ArrayList<>();
        this.likes = 0;
    }

    /**
     * REQUIRES: players.size() < 23
     * MODIFIES: this
     * EFFECTS: Adds a player to the team.
     */
    public void addPlayer(Player player) {
       
    }

    /**
     * MODIFIES: this
     * EFFECTS: Removes a player from the team.
     */
    public void removePlayer(Player player) {
        
    }

    /**
     * EFFECTS: Returns the total price of the team.
     */
    public int getTotalPrice() {
        
       return totalPrice;

    }

    /**
     * EFFECTS: Returns the average rating of the team.
     */
    public double getAverageRating() {
   
        return averageRating;
    }

    /**
     * MODIFIES: this
     * EFFECTS: Increments the likes count by one.
     */
    public void likeTeam() {
        
    }

    /**
     * EFFECTS: Returns the number of likes.
     */
    public int getLikes() {
        return likes;
    }

    /**
     * EFFECTS: Returns the list of players in the team.
     */
    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }
}
