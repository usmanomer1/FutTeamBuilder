package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Represents a FIFA Ultimate Team composed of players.
 */
public class Team {

    private List<Player> players;
    private Map<String, Player> starting11 = new HashMap<>();

    private int likes;
    private boolean isListed;
    private String name;
    private Formation formation;

    /**
     * MODIFIES: this
     * EFFECTS: Initializes an empty team with zero likes and sets the formation.
     */
    public Team(String name, String formationType) {
        this.players = new ArrayList<>();
        this.likes = 0;
        this.isListed = false;
        this.name = name;
        this.formation = new Formation(formationType);
        EventLog.getInstance().logEvent(new Event("Team " + name + " created with formation " + formationType));
    }

    /**
     * EFFECTS: Returns true if the team is listed in the community.
     *
     * @return true if listed, false if saved as draft
     */
    public boolean isListed() {
        return isListed;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    /**
     * MODIFIES: this
     * EFFECTS: Sets the team's listing status.
     *
     * @param listed true to list the team, false to save as draft
     */
    public void setListed(boolean listed) {
        isListed = listed;
    }

    /**
     * EFFECTS: Returns the name of the team.
     *
     * @return team name
     */
    public String getName() {
        return name;
    }

    /**
     * REQUIRES: players.size() < 23
     * MODIFIES: this
     * EFFECTS: Adds a player to the team if they are not already in the team.
     */
    public boolean addPlayer(Player player) {
        if (players.size() < 23 && !hasPlayer(player.getName())) {
            players.add(player);
            EventLog.getInstance().logEvent(new Event("player added to team: " + player.getName()));
            if (player.isInStarting11()) {
                boolean success = setPlayerInStarting11(player, true);
                if (!success) {
                    // Cannot set player in starting 11, handle accordingly
                    System.out.println("Warning: Could not set player " + player.getName() + " in starting 11.");
                    // Optionally set player's isInStarting11 to false
                    player.setInStarting11(false);
                }
            }
            return true;
        } else {
            // Cannot add player: team is full or player is already in the team
            return false;
        }
    }
    

    // Removes a player from the team
    // EFFECTS: Returns true if the player was removed, false if the player was not found
    public boolean removePlayer(Player player) {
        return players.remove(player);
    }

    @SuppressWarnings("methodlength")
    // Sets the player's status in the starting 11
    // EFFECTS: Returns true if operation was successful, false otherwise
    public boolean setPlayerInStarting11(Player player, boolean inStarting11) {
        if (players.contains(player)) {
            if (inStarting11) {
                Set<String> requiredPositions = formation.getRequiredPositions().stream()
                        .map(String::toUpperCase)
                        .collect(Collectors.toSet());
                String playerPosition = player.getCurrentPosition().toUpperCase();
    
                if (!requiredPositions.contains(playerPosition)) {
                    // Player's position does not match the formation
                    return false;
                }
                if (starting11.size() >= requiredPositions.size() && !player.isInStarting11()) {
                    // Starting lineup is full
                    return false;
                } else {
                    player.setInStarting11(true);
                    starting11.put(playerPosition, player);
                    return true;
                }
            } else {
                player.setInStarting11(false);
                starting11.remove(player.getCurrentPosition().toUpperCase());
                return true;
            }
        } else {
            // Player is not in the team
            return false;
        }
    }
    
    // EFFECTS: Returns a list of players in the starting 11
    public List<Player> getStartingPlayers() {
        return players.stream()
                .filter(Player::isInStarting11)
                .collect(Collectors.toList());
    }

    // EFFECTS: Returns a list of substitutes and reserves (not in starting 11)
    public List<Player> getSubstitutes() {
        return players.stream()
                .filter(p -> !p.isInStarting11())
                .collect(Collectors.toList());
    }

    public boolean isStartingLineupComplete() {
        Set<String> requiredPositions = formation.getRequiredPositions().stream()
                .map(String::toUpperCase)
                .collect(Collectors.toSet());
        Set<String> startingPositions = starting11.keySet().stream()
                .map(String::toUpperCase)
                .collect(Collectors.toSet());
    
   
    
        boolean isComplete = startingPositions.containsAll(requiredPositions);
       
    
        return isComplete;
    }
    
    

    // EFFECTS: Returns the total price of all players in the team
    public int getTotalPrice() {
        return players.stream()
                .mapToInt(Player::getPrice)
                .sum();
    }

    // EFFECTS: Returns the average rating of all players in the team
    public double getAverageRating() {
        if (players.isEmpty()) {
            return 0.0;
        }
        return players.stream()
                .mapToInt(Player::getRating)
                .average()
                .orElse(0.0);
    }

    /**
     * MODIFIES: this
     * EFFECTS: Increments the likes count by one.
     */
    public void likeTeam() {
        likes++;
    }

    /**
     * Checks if the team has a player with the given name.
     *
     * REQUIRES: playerName != null
     * EFFECTS:
     * Returns true if there is a player in the team whose name matches playerName (case-insensitive);
     * otherwise, returns false.
     */
    public boolean hasPlayer(String playerName) {
        for (Player player : players) {
            if (player.getName().equalsIgnoreCase(playerName)) {
                return true;
            }
        }
        return false;
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

    // EFFECTS: Returns the total chemistry of the team
    public int calculateChemistry() {
        List<Player> startingPlayers = getStartingPlayers();
        if (!isStartingLineupComplete()) {
            // Starting lineup is incomplete
            return 0; // Or handle this case as per your application's requirements
        }

        int chemistry = 0;

        // Position chemistry
        for (Player player : startingPlayers) {
            if (player.isInPreferredPosition()) {
                chemistry += 5; // Full chemistry for correct position
            } else if (player.isPositionCompatible()) {
                chemistry += 2; // Partial chemistry for compatible position
            } else {
                chemistry -= 3; // Penalty for incorrect position
            }
        }

        // Link chemistry between players
        chemistry += calculateLinkChemistry(startingPlayers);

        return chemistry;
    }

    // Helper method to calculate link chemistry between starting players
    private int calculateLinkChemistry(List<Player> startingPlayers) {
        int linkChemistry = 0;
        Set<String> processedPairs = new HashSet<>();

        for (Player player : startingPlayers) {
            List<String> linkedPositions = formation.getLinkedPositions(player.getCurrentPosition());
            for (String linkedPosition : linkedPositions) {
                Player linkedPlayer = getPlayerByPosition(linkedPosition, startingPlayers);
                if (linkedPlayer != null) {
                    String pairKey = generatePairKey(player, linkedPlayer);
                    if (!processedPairs.contains(pairKey)) {
                        linkChemistry += calculateLinkChemistryBetweenPlayers(player, linkedPlayer);
                        processedPairs.add(pairKey);
                    }
                }
            }
        }
        return linkChemistry;
    }

    // Helper method to get a player by position from the starting players
    private Player getPlayerByPosition(String position, List<Player> startingPlayers) {
        for (Player player : startingPlayers) {
            if (player.getCurrentPosition().equalsIgnoreCase(position)) {
                return player;
            }
        }
        return null;
    }

    // Helper method to generate a unique key for a pair of players
    private String generatePairKey(Player p1, Player p2) {
        String name1 = p1.getName();
        String name2 = p2.getName();
        return name1.compareTo(name2) < 0 ? name1 + "-" + name2 : name2 + "-" + name1;
    }

    // EFFECTS: Calculates chemistry between two players based on shared attributes
    private int calculateLinkChemistryBetweenPlayers(Player p1, Player p2) {
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

    public boolean isComplete() {
        return players.size() >= 11;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("likes", likes);
        json.put("isListed", isListed);
        json.put("formationType", formation.getFormationType()); // Include formation type
    
        JSONArray playersArray = new JSONArray();
        for (Player player : players) {
            playersArray.put(player.toJson());
        }
        json.put("players", playersArray);
    
        return json;
    }
    

    // Additional getters and setters for formation if needed
    public Formation getFormation() {
        return formation;
    }

    public void setFormation(String formationType) {
        this.formation = new Formation(formationType);
    }
}
