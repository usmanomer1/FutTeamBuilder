// UserTest.java
package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User user;
    private Team team1;
    private Team team2;

    @BeforeEach
    public void setUp() {
        user = new User("john_doe", "hashed_password", null);
        team1 = new Team("Team 1");
        team2 = new Team("Team 2");
    }

    @Test
    public void testAddTeam() {
        user.addTeam(team1);
        assertEquals(1, user.getTeams().size());
        user.addTeam(team2);
        assertEquals(2, user.getTeams().size());
        assertTrue(user.getTeams().contains(team1));
        assertTrue(user.getTeams().contains(team2));
    }

    @Test
    public void testCheckPassword() {
        // Assuming PasswordUtils.verifyPassword is correctly implemented
        // Mock PasswordUtils for testing purposes or use actual method
        String plainPassword = "password123";
        String hashedPassword = PasswordUtils.hashPassword(plainPassword);
        user = new User("john_doe", hashedPassword, null);

        assertTrue(user.checkPassword(plainPassword));
        assertFalse(user.checkPassword("wrongpassword"));
    }

    @Test
    public void testToJson() {
        user.addTeam(team1);
        user.addTeam(team2);

        org.json.JSONObject json = user.toJson();
        assertEquals("john_doe", json.getString("username"));
        assertEquals(user.getPasswordHash(), json.getString("passwordHash"));
        assertEquals(2, json.getJSONArray("teams").length());
    }
}