// Team.java
package model;

import java.util.ArrayList;
import java.util.List;


/**
 * Represents a FIFA Ultimate Team composed of players.
 */
public class Team {
   

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
        if (players.size() < 23) {
            players.add(player);
        }
    }

    /**
     * MODIFIES: this
     * EFFECTS: Removes a player from the team.
     */
    public void removePlayer(Player player) {
        players.remove(player);
    }

    /**
     * EFFECTS: Returns the total price of the team.
     */
    public int getTotalPrice() {
        int totalPrice = 0;
        for (Player p : players) {
            totalPrice += p.getPrice();
        }
        return totalPrice;
    }

    /**
     * EFFECTS: Returns the average rating of the team.
     */
    public double getAverageRating() {
        if (players.isEmpty()) {
            return 0;
        }
        int totalRating = 0;
        for (Player p : players) {
            totalRating += p.getRating();
        }
        return (double) totalRating / players.size();
    }

    /**
     * MODIFIES: this
     * EFFECTS: Increments the likes count by one.
     */
    public void likeTeam() {
        likes++;
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
 
 
    // EFFECTS: Returns the total chemistry of the team.
    public int calculateChemistry() {
        int chemistry = 0;

        // Position chemistry
        for (Player player : players) {
            if (player.isInPreferredPosition()) {
                chemistry += 5; // Full chemistry for correct position
            } else if (player.isPositionCompatible()) {
                chemistry += 2; // Partial chemistry for compatible position
            } else {
                chemistry -= 3; // Penalty for incorrect position
            }
        }

        // Link chemistry between players
        for (int i = 0; i < players.size(); i++) {
            Player p1 = players.get(i);
            for (int j = i + 1; j < players.size(); j++) {
                Player p2 = players.get(j);

                int linkChemistry = calculateLinkChemistry(p1, p2);
                chemistry += linkChemistry;
            }
        }

        return chemistry;
    }

    /**
     * EFFECTS: Calculates chemistry between two players based on shared attributes.
     */
    private int calculateLinkChemistry(Player p1, Player p2) {
        int linkChemistry = 0;
        if (p1.getClubAffiliation().equalsIgnoreCase(p2.getClubAffiliation())) {
            linkChemistry += 3;
        } else if (p1.getNationality().equalsIgnoreCase(p2.getNationality())
                && p1.getLeague().equalsIgnoreCase(p2.getLeague())) {
            linkChemistry += 2;
        } else if (p1.getNationality().equalsIgnoreCase(p2.getNationality())
                || p1.getLeague().equalsIgnoreCase(p2.getLeague())) {
            linkChemistry += 1;
        }
        return linkChemistry;
    }

    
    




}
