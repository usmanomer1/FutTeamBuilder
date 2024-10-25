// UserManagerTest.java
package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserManagerTest {

    private UserManager userManager;

    @BeforeEach
    public void setUp() {
        userManager = new UserManager();
    }

    @Test
    public void testSignUpNewUser() {
        User user = userManager.signUp("john_doe", "password123");
        assertNotNull(user);
        assertEquals("john_doe", user.getUsername());
        assertNotNull(user.getPasswordHash());
    }

    @Test
    public void testSignUpExistingUser() {
        userManager.signUp("john_doe", "password123");
        assertThrows(IllegalArgumentException.class, () -> {
            userManager.signUp("john_doe", "newpassword");
        });
    }

    @Test
    public void testLoginSuccess() {
        userManager.signUp("john_doe", "password123");
        User user = userManager.login("john_doe", "password123");
        assertNotNull(user);
        assertEquals("john_doe", user.getUsername());
    }

    @Test
    public void testLoginWrongPassword() {
        userManager.signUp("john_doe", "password123");
        assertThrows(IllegalArgumentException.class, () -> {
            userManager.login("john_doe", "wrongpassword");
        });
    }

    @Test
    public void testLoginNonExistentUser() {
        assertThrows(IllegalArgumentException.class, () -> {
            userManager.login("nonexistent", "password");
        });
    }

    @Test
    public void testGetAllUsers() {
        userManager.signUp("john_doe", "password123");
        userManager.signUp("jane_smith", "password456");
        assertEquals(2, userManager.getAllUsers().size());
    }

    @Test
    public void testSaveAndLoadUsers() {
        // Prepare test data
        userManager.signUp("john_doe", "password123");
        userManager.signUp("jane_smith", "password456");

        // Save users
        userManager.saveUsers();

        // Create a new UserManager and load users
        UserManager newUserManager = new UserManager();
        newUserManager.loadUsers();

        assertEquals(2, newUserManager.getAllUsers().size());
        assertNotNull(newUserManager.login("john_doe", "password123"));
        assertNotNull(newUserManager.login("jane_smith", "password456"));
    }
}
