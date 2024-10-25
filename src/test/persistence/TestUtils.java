// TestUtils.java
package persistence;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.Test;

public class TestUtils {



    
    public static void writeStringToFile(String filePath, String content) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.print(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   
    public static String getSampleJsonContent() {
        // Return a sample JSON string representing users, teams, and players
        return "{\n" +
                "  \"users\": [\n" +
                "    {\n" +
                "      \"username\": \"john_doe\",\n" +
                "      \"passwordHash\": \"65536:AbCdEf123...:GhIjKl456...\",\n" +
                "      \"teams\": [\n" +
                "        {\n" +
                "          \"name\": \"John's Dream Team\",\n" +
                "          \"likes\": 5,\n" +
                "          \"isListed\": true,\n" +
                "          \"players\": [\n" +
                "            {\n" +
                "              \"name\": \"Lionel Messi\",\n" +
                "              \"nationality\": \"Argentina\",\n" +
                "              \"league\": \"Ligue 1\",\n" +
                "              \"clubAffiliation\": \"PSG\",\n" +
                "              \"preferredPosition\": \"RW\",\n" +
                "              \"currentPosition\": \"RW\",\n" +
                "              \"rating\": 93,\n" +
                "              \"pace\": 85,\n" +
                "              \"shooting\": 92,\n" +
                "              \"passing\": 91,\n" +
                "              \"skillMoves\": 5,\n" +
                "              \"price\": 1000000\n" +
                "            }\n" +
                "            // Add 10 more players here to make 11 in total\n" +
                "          ]\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";
    }
}
