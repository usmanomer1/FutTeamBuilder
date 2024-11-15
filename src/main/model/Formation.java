package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Formation {
    private String formationType;
    private Map<String, List<String>> positionAdjacencyMap;

    // Static map to hold all formations data
    private static final Map<String, Map<String, List<String>>> formationsData = new HashMap<>();
    private static final String FORMATIONS_FILE = "./data/formations.json";

    static {
        loadFormationsData();
    }

    public String getFormationType() {
        return formationType;
    }

    public Formation(String formationType) {
        this.formationType = formationType;
        if (!formationsData.containsKey(formationType)) {
            throw new IllegalArgumentException("Unsupported formation type: " + formationType);
        }
        this.positionAdjacencyMap = formationsData.get(formationType);
    }

    // EFFECTS: Returns a list of positions linked to the given position
    public List<String> getLinkedPositions(String position) {
        return positionAdjacencyMap.getOrDefault(position.toUpperCase(), new ArrayList<>());
    }

    // EFFECTS: Returns the set of positions required in this formation
    public Set<String> getRequiredPositions() {
        return positionAdjacencyMap.keySet();
    }

    // Loads the formations data from the JSON file using the file system
    private static void loadFormationsData() {
        try {
            String content = new String(Files.readAllBytes(Paths.get(FORMATIONS_FILE)));
            JSONObject rootObject = new JSONObject(content);

            // Iterate over each formation
            for (String formationName : rootObject.keySet()) {
                JSONObject formationObject = rootObject.getJSONObject(formationName);

                Map<String, List<String>> adjacencyMap = new HashMap<>();

                // Get edges and build adjacency map
                JSONArray edgesArray = formationObject.getJSONArray("edges");
                for (int i = 0; i < edgesArray.length(); i++) {
                    JSONArray edge = edgesArray.getJSONArray(i);
                    String position1 = edge.getString(0);
                    String position2 = edge.getString(1);

                    // Add position1 -> position2 link
                    adjacencyMap.computeIfAbsent(position1, k -> new ArrayList<>()).add(position2);
                    // Add position2 -> position1 link
                    adjacencyMap.computeIfAbsent(position2, k -> new ArrayList<>()).add(position1);
                }

                formationsData.put(formationName, adjacencyMap);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load formations data.");
        }
    }
}
