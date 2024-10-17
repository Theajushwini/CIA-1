import java.util.*;

public class DFS {
    public static void main(String[] args) {
        Map<String, Map<String, Integer>> graph = new HashMap<>();

        graph.put("S", Map.of("A", 3, "B", 5));
        graph.put("A", Map.of("S", 3, "B", 4, "D", 3));
        graph.put("B", Map.of("S", 5, "A", 4, "C", 4));
        graph.put("C", Map.of("B", 4, "E", 6));
        graph.put("E", Map.of("C", 6));
        graph.put("D", Map.of("A", 3, "G", 5));
        graph.put("G", Map.of("D", 5));

        dfs(graph, "S", "G", new HashSet<>(), new ArrayList<>());
    }

    public static void dfs(Map<String, Map<String, Integer>> graph, String start, String goal, Set<String> visited, List<String> path) {
        visited.add(start);
        path.add(start);

        if (start.equals(goal)) {
            System.out.println("Path to goal: " + path);
            return;
        }

        // Get neighbors and sort them alphabetically to enforce SADG order
        Map<String, Integer> neighbors = graph.getOrDefault(start, new HashMap<>());
        List<String> sortedNeighbors = new ArrayList<>(neighbors.keySet());
        Collections.sort(sortedNeighbors); // Sorting alphabetically to enforce correct order

        for (String neighbor : sortedNeighbors) {
            if (!visited.contains(neighbor)) {
                dfs(graph, neighbor, goal, visited, path);
            }
        }

        path.remove(path.size() - 1);  // Backtrack
    }
}
