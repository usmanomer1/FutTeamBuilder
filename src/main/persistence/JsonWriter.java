// JsonWriter.java
package persistence;

import model.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Collection;

/**
 * Writes JSON representation of user data to file.
 */
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    /**
     * Constructs a JsonWriter to write to the specified destination file.
     *
     * @param destination the destination file path
     */
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    /**
     * Opens the writer. Throws FileNotFoundException if the destination file cannot be opened for writing.
     *
     * @throws FileNotFoundException if the destination file cannot be opened
     */
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }



    /**
     * Closes the writer.
     */
    public void close() {
        writer.close();
    }

    /**
     * Saves the given JSON string to file.
     *
     * @param json the JSON string to save
     */
    private void saveToFile(String json) {
        writer.print(json);
    }

     /**
     * Writes JSON representation of all users to file.
     *
     * @param users the collection of users to write
     */
    public void writeAllUsers(Collection<User> users) {
        JSONObject json = new JSONObject();
        JSONArray usersArray = new JSONArray();
        for (User user : users) {
            usersArray.put(user.toJson());
        }
        json.put("users", usersArray);
        saveToFile(json.toString(TAB));
    }
}
