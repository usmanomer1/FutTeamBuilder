package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Formation {
    private String formationType;
    private Map<String, List<String>> positionAdjacencyMap;
    private Map<String, double[]> positionCoordinates;

    private static final Map<String, Map<String, List<String>>> formationsData = new HashMap<>();
    private static final Map<String, Map<String, double[]>> positionsData = new HashMap<>();
    private static final String FORMATIONS_FILE = "./data/formations.json";

    static {
        loadFormationsData();
    }

    public Formation(String formationType) {
        this.formationType = formationType;
        if (!formationsData.containsKey(formationType)) {
            throw new IllegalArgumentException("Unsupported formation type: " + formationType);
        }
        this.positionAdjacencyMap = formationsData.get(formationType);
        this.positionCoordinates = positionsData.get(formationType);
    }

    public String getFormationType() {
        return formationType;
    }

    public List<String> getLinkedPositions(String position) {
        return positionAdjacencyMap.getOrDefault(position.toUpperCase(), new ArrayList<>());
    }

    public Set<String> getRequiredPositions() {
        return positionAdjacencyMap.keySet().stream()
                .map(pos -> pos.toUpperCase().trim())
                .collect(Collectors.toSet());
    }

    public Map<String, double[]> getPositionCoordinates() {
        return positionCoordinates;
    }

    public static Set<String> getAllFormationTypes() {
        return formationsData.keySet();
    }


    @SuppressWarnings("methodlength")
    public static void loadFormationsData() {
        try {
            String content = new String(Files.readAllBytes(Paths.get(FORMATIONS_FILE)));
            JSONObject rootObject = new JSONObject(content);

            for (String formationName : rootObject.keySet()) {
                JSONObject formationObject = rootObject.getJSONObject(formationName);

                Map<String, List<String>> adjacencyMap = new HashMap<>();
                Map<String, double[]> coordinatesMap = new HashMap<>();

                // Build adjacency map
                JSONArray edgesArray = formationObject.getJSONArray("edges");
                for (int i = 0; i < edgesArray.length(); i++) {
                    JSONArray edge = edgesArray.getJSONArray(i);
                    String position1 = edge.getString(0).toUpperCase().trim();
                    String position2 = edge.getString(1).toUpperCase().trim();

                    adjacencyMap.computeIfAbsent(position1, k -> new ArrayList<>()).add(position2);
                    adjacencyMap.computeIfAbsent(position2, k -> new ArrayList<>()).add(position1);
                }

                // Build positions map
                JSONObject positionsObject = formationObject.getJSONObject("pos");
                for (String position : positionsObject.keySet()) {
                    JSONArray coordinates = positionsObject.getJSONArray(position);
                    double x = coordinates.getDouble(0);
                    double y = coordinates.getDouble(1);
                    coordinatesMap.put(position.toUpperCase().trim(), new double[]{x, y});
                }

                formationsData.put(formationName, adjacencyMap);
                positionsData.put(formationName, coordinatesMap);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load formations data.");
        }
    }

    public static Set<String> getPositionsForFormation(String formationType) {
        if (!positionsData.containsKey(formationType)) {
            throw new IllegalArgumentException("Unsupported formation type: " + formationType);
        }
        return positionsData.get(formationType).keySet();
    }
}
