
package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

/**
 * Provides utilities for determining position compatibility.
 */
public class PositionCompatibility {

    private static final Map<String, List<String>> compatibilityMap = new HashMap<>();

    static {
 
        compatibilityMap.put("GK", Arrays.asList("GK"));
        compatibilityMap.put("RB", Arrays.asList("RB", "RWB", "CB"));
        compatibilityMap.put("RWB", Arrays.asList("RWB", "RB", "RM"));
        compatibilityMap.put("CB", Arrays.asList("CB", "RB", "LB"));
        compatibilityMap.put("LB", Arrays.asList("LB", "LWB", "CB"));
        compatibilityMap.put("LWB", Arrays.asList("LWB", "LB", "LM"));
        compatibilityMap.put("CDM", Arrays.asList("CDM", "CM", "CB"));
        compatibilityMap.put("CM", Arrays.asList("CM", "CAM", "CDM"));
        compatibilityMap.put("CAM", Arrays.asList("CAM", "CM", "CF"));
        compatibilityMap.put("RM", Arrays.asList("RM", "RW", "RWB"));
        compatibilityMap.put("LM", Arrays.asList("LM", "LW", "LWB"));
        compatibilityMap.put("RW", Arrays.asList("RW", "RM", "RF"));
        compatibilityMap.put("LW", Arrays.asList("LW", "LM", "LF"));
        compatibilityMap.put("CF", Arrays.asList("CF", "ST", "CAM"));
        compatibilityMap.put("ST", Arrays.asList("ST", "CF"));
        compatibilityMap.put("RF", Arrays.asList("RF", "RW", "ST"));
        compatibilityMap.put("LF", Arrays.asList("LF", "LW", "ST"));
    }

   /**
 * REQUIRES: primaryPosition and otherPosition are not null
 * EFFECTS: Returns true if the positions are compatible; false otherwise.
 */
    public static boolean isCompatible(String preferredPosition, String currentPosition) {
        List<String> compatiblePositions = compatibilityMap.get(preferredPosition.toUpperCase());
        if (compatiblePositions != null) {
            return compatiblePositions.contains(currentPosition.toUpperCase());
        }
        return false;
    }
}
