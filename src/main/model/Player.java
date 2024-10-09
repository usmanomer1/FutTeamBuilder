// Player.java
package model;

/**
 * Represents a FIFA player with specific attributes.
 */
public class Player {

    private String name;
    private String nationality;
    private int rating;
    private String position;
    private int pace;
    private int passing;
    private int shooting;
    private int weakFoot;
    private String clubAffiliation;
    private int price;

    /**
     * REQUIRES: name, nationality, position, clubAffiliation are non-empty strings;
     *           rating, pace, passing, shooting, weakFoot, price >= 0.
     * MODIFIES: this
     * EFFECTS: Initializes a player with the given attributes.
     */
    public Player(String name, String nationality, int rating, String position,
                  int pace, int passing, int shooting, int weakFoot,
                  String clubAffiliation, int price) {
        this.name = name;
        this.nationality = nationality;
        this.rating = rating;
        this.position = position;
        this.pace = pace;
        this.passing = passing;
        this.shooting = shooting;
        this.weakFoot = weakFoot;
        this.clubAffiliation = clubAffiliation;
        this.price = price;
    }

   
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getPace() {
        return pace;
    }

    public void setPace(int pace) {
        this.pace = pace;
    }

    public int getPassing() {
        return passing;
    }

    public void setPassing(int passing) {
        this.passing = passing;
    }

    public int getShooting() {
        return shooting;
    }

    public void setShooting(int shooting) {
        this.shooting = shooting;
    }

    public int getWeakFoot() {
        return weakFoot;
    }

    public void setWeakFoot(int weakFoot) {
        this.weakFoot = weakFoot;
    }

    public String getClubAffiliation() {
        return clubAffiliation;
    }

    public void setClubAffiliation(String clubAffiliation) {
        this.clubAffiliation = clubAffiliation;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    

}

