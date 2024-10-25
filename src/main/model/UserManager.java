// UserManager.java


package model;

import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages user signup and login.
 */
public class UserManager {
    private Map<String, User> users;
    private static final String USERS_FILE = "./data/users.json";

    /**
     * EFFECTS: Initializes an empty user manager.
     */
    public UserManager() {
        users = new HashMap<>();
    }

    /**
     * EFFECTS: Signs up a new user if the username is not already taken.
     *
     * @param username the desired username
     * @param password the desired password
     * @return the created User object
     * @throws IllegalArgumentException if the username is already taken
     */
    public User signUp(String username, String password) {
        if (users.containsKey(username)) {
            throw new IllegalArgumentException("Username already taken.");
        }
        User user = new User(username, PasswordUtils.hashPassword(password), null);
        users.put(username, user);
        return user;
    }

    /**
     * EFFECTS: Logs in a user if the username and password are correct.
     *
     * @param username the username
     * @param password the password
     * @return the User object
     * @throws IllegalArgumentException if login fails
     */
    public User login(String username, String password) {
        if (!users.containsKey(username)) {
            throw new IllegalArgumentException("User does not exist.");
        }
        User user = users.get(username);
        if (user.checkPassword(password)) {
            return user;
        } else {
            throw new IllegalArgumentException("Incorrect password.");
        }
    }

    /**
     * EFFECTS: Adds a user to the manager.
     *
     * @param user the user to add
     */
    public void addUser(User user) {
        users.put(user.getUsername(), user);
    }

    /**
     * EFFECTS: Returns a collection of all users.
     *
     * @return collection of users
     */
    public Collection<User> getAllUsers() {
        return users.values();
    }

    /**
     * EFFECTS: Saves all users to a JSON file.
     */
    public void saveUsers() {
        JsonWriter writer = new JsonWriter(USERS_FILE);
        try {
            writer.open();
            writer.writeAllUsers(users.values());
            writer.close();
            System.out.println("Users saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving users to file.");
           
        }
    }

    /**
     * EFFECTS: Loads users from a JSON file.
     */
    public void loadUsers() {
        JsonReader reader = new JsonReader(USERS_FILE);
        try {
            UserManager loadedManager = reader.read();
            this.users = loadedManager.users;
            System.out.println("Users loaded successfully.");
        } catch (IOException e) {
            System.out.println("Error loading users from file.");
           
        }
    }
}
