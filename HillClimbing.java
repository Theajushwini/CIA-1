import java.util.*;

public class HillClimbing {
    public static void main(String[] args) {
        Map<String, Map<String, Integer>> d = new HashMap<>();
        d.put("S", Map.of("A", 3, "B", 5));
        d.put("A", Map.of("S", 3, "B", 4, "D", 3));
        d.put("B", Map.of("S", 5, "A", 4, "C", 4));
        d.put("C", Map.of("B", 4, "E", 6));
        d.put("E", Map.of("C", 6));
        d.put("D", Map.of("A", 3, "G", 5));
        d.put("G", Map.of("D", 5));

        Map<String, Map<String, Double>> d_h = new HashMap<>();
        d_h.put("S", Map.of("A", 7.38, "B", 6.0));
        d_h.put("A", Map.of("S", Double.POSITIVE_INFINITY, "B", 6.0, "D", 5.0));
        d_h.put("B", Map.of("S", Double.POSITIVE_INFINITY, "A", 7.38, "C", 7.58));
        d_h.put("C", Map.of("B", 6.0, "E", Double.POSITIVE_INFINITY));
        d_h.put("E", Map.of("C", 7.58));
        d_h.put("D", Map.of("A", 7.38, "G", 0.0));
        d_h.put("G", Map.of("D", 5.0));

        List<String> path = hillClimbing(d, "S", "G", d_h);
        System.out.println("Final path: " + path);
    }

    public static List<String> hillClimbing(Map<String, Map<String, Integer>> d, String start, String goal, Map<String, Map<String, Double>> d_h) {
        List<String> path = new ArrayList<>();
        String current = start;
        path.add(current);

        while (!current.equals(goal)) {
            Map<String, Integer> neighbors = d.get(current);
            if (neighbors == null || neighbors.isEmpty()) break;  // No neighbors, stop

            String currentFinal = current; // Create a final copy for lambda
            String bestNeighbor = Collections.min(neighbors.keySet(), Comparator.comparing(n -> d_h.get(currentFinal).getOrDefault(n, Double.POSITIVE_INFINITY)));
            path.add(bestNeighbor);
            current = bestNeighbor;  // Update current after lambda has finished using it
            System.out.println("Current node: " + current + ", Path: " + path);
        }

        return path;
    }
}
