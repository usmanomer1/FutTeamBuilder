package model;

import org.json.JSONObject;

/**
 * Represents a FIFA player with specific attributes.
 */
public class Player {

    private String league;
    private String name;
    private String nationality;
    private int rating;
    private String currentPosition;
    private int pace;
    private int passing;
    private int shooting;
    private int weakFoot;
    private String clubAffiliation;
    private int price;
    private String preferredPosition;
    private boolean isInStarting11;
    private int dribbling;
    private int defending;
    private int physicality;
    private int skillMoves;

    public Player(String name, String nationality, String league, String clubAffiliation, String preferredPosition,
                  String currentPosition, int rating, int pace, int passing, int shooting, int dribbling, int defending,
                  int physicality, int skillMoves, int weakFoot, int price, boolean isInStarting11) {
        this.name = name;
        this.nationality = nationality;
        this.league = league;
        this.clubAffiliation = clubAffiliation;
        this.preferredPosition = preferredPosition != null ? preferredPosition.toUpperCase() : null;
    this.currentPosition = currentPosition != null ? currentPosition.toUpperCase() : null;
        this.rating = rating;
        this.pace = pace;
        this.passing = passing;
        this.shooting = shooting;
        this.dribbling = dribbling;
        this.defending = defending;
        this.physicality = physicality;
        this.skillMoves = skillMoves;
        this.weakFoot = weakFoot;
        this.price = price;
        this.isInStarting11 = isInStarting11;
    }

    // Getters and setters for new attributes

    public int getDribbling() {
        return dribbling;
    }

    public void setDribbling(int dribbling) {
        this.dribbling = dribbling;
    }

    public int getDefending() {
        return defending;
    }

    public void setDefending(int defending) {
        this.defending = defending;
    }

    public int getPhysicality() {
        return physicality;
    }

    public void setPhysicality(int physicality) {
        this.physicality = physicality;
    }

    public int getSkillMoves() {
        return skillMoves;
    }

    public void setSkillMoves(int skillMoves) {
        this.skillMoves = skillMoves;
    }

    // Existing getters and setters

    public boolean isInStarting11() {
        return isInStarting11;
    }

    public void setInStarting11(boolean isInStarting11) {
        this.isInStarting11 = isInStarting11;
    }

    public String getName() {
        return name;
    }

  

    public String getNationality() {
        return nationality;
    }
   

    public int getRating() {
        return rating;
    }



    public String getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(String position) {
        this.currentPosition = position;
    }

    public boolean isInPreferredPosition() {
        return preferredPosition.equalsIgnoreCase(currentPosition);
    }

    /**
     * EFFECTS: Returns true if the player's current position is compatible with their preferred position.
     */
    public boolean isPositionCompatible() {
        return PositionCompatibility.isCompatible(preferredPosition, currentPosition);
    }

    public String getPreferredPosition() {
        return preferredPosition;
    }

    public void setPreferredPosition(String preferredPosition) {
        this.preferredPosition = preferredPosition;
    }

    public int getPace() {
        return pace;
    }

    public String getLeague() {
        return league;
    }

    public int getPassing() {
        return passing;
    }

    public int getShooting() {
        return shooting;
    }

    public int getWeakFoot() {
        return weakFoot;
    }

    public String getClubAffiliation() {
        return clubAffiliation;
    }

    public int getPrice() {
        return price;
    }

    /**
     * Converts the player to a JSON object.
     *
     * @return JSON representation of the player
     */
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("nationality", nationality);
        json.put("league", league);
        json.put("clubAffiliation", clubAffiliation);
        json.put("preferredPosition", preferredPosition);
        json.put("currentPosition", currentPosition);
        json.put("rating", rating);
        json.put("pace", pace);
        json.put("shooting", shooting);
        json.put("passing", passing);
        json.put("dribbling", dribbling);
        json.put("defending", defending);
        json.put("physicality", physicality);
        json.put("skillMoves", skillMoves);
        json.put("weakFoot", weakFoot);
        json.put("price", price);
        json.put("isInStarting11", isInStarting11);
        return json;
    }
}
