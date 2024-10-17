

import java.util.*;
public class Beam_Search {
    public static List<String> beamSearch(Map<String, Map<String, Integer>> graph, String start, String goal, int beamWidth) {
        // PriorityQueue to store paths (with their costs at index 0)
        PriorityQueue<List<String>> queue = new PriorityQueue<>(Comparator.comparingInt(path -> Integer.parseInt(path.get(0))));
        List<String> startPath = new ArrayList<>(Arrays.asList("0", start)); // Path starts from start node
        queue.add(startPath); // Add starting node to queue
        List<String> goalPath = null; // This will store the path to the goal

        while (!queue.isEmpty()) {
            // Temporary list to hold expanded paths at current level
            List<List<String>> currentLevelPaths = new ArrayList<>();
            while (!queue.isEmpty()) {
                currentLevelPaths.add(queue.poll()); // Collect all current paths
            }

            // List to hold the next level of paths (after expansion)
            List<List<String>> expandedPaths = new ArrayList<>();
            for (List<String> path : currentLevelPaths) {
                String currentNode = path.get(path.size() - 1); // Last node in the current path
                if (currentNode.equals(goal)) {
                    goalPath = path; // Found the goal
                    break;
                }
                // Expand current path
                for (String neighbor : graph.get(currentNode).keySet()) {
                    if (!path.contains(neighbor)) {
                        List<String> newPath = new ArrayList<>(path);
                        int newCost = Integer.parseInt(path.get(0)) + graph.get(currentNode).get(neighbor);
                        newPath.set(0, Integer.toString(newCost)); // Update the cost at index 0
                        newPath.add(neighbor); // Add neighbor to path
                        expandedPaths.add(newPath); // Add the new path to the list of expanded paths
                    }
                }
            }

            // Sort expanded paths by their cost (ascending)
            expandedPaths.sort(Comparator.comparingInt(p -> Integer.parseInt(p.get(0))));

            // Add only the best `beamWidth` number of paths back to the queue
            for (int i = 0; i < Math.min(beamWidth, expandedPaths.size()); i++) {
                queue.add(expandedPaths.get(i));
            }

            // If goal is found, break out of the loop
            if (goalPath != null) {
                break;
            }
        }

        return goalPath; // Return the goal path
    }

    public static void main(String[] args) {
        // Define the graph as an adjacency list with edge costs
        Map<String, Map<String, Integer>> graph = new HashMap<>();
        graph.put("S", Map.of("A", 3, "B", 5));
        graph.put("A", Map.of("S", 3, "B", 4, "D", 3));
        graph.put("B", Map.of("S", 5, "A", 4, "C", 4));
        graph.put("C", Map.of("B", 4, "E", 6));
        graph.put("E", Map.of("C", 6));
        graph.put("D", Map.of("A", 3, "G", 5));
        graph.put("G", Map.of("D", 5));

        String start = "S";
        String goal = "G";
        int beamWidth = 2;

        List<String> resultPath = beamSearch(graph, start, goal, beamWidth);

        if (resultPath != null) {
            System.out.println("Path found: " + resultPath);
        } else {
            System.out.println("Goal not reachable.");
        }
    }
}
