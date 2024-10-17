import java.util.*;

public class BranchAndBoundWithExtendedList {
    public static void main(String[] args) {
        // Define the graph as a Map of Maps
        Map<String, Map<String, Integer>> d = new HashMap<>();
        d.put("S", Map.of("A", 3, "B", 5));
        d.put("A", Map.of("S", 3, "B", 4, "D", 3));
        d.put("B", Map.of("S", 5, "A", 4, "C", 4));
        d.put("C", Map.of("B", 4, "E", 6));
        d.put("E", Map.of("C", 6));
        d.put("D", Map.of("A", 3, "G", 5));
        d.put("G", Map.of("D", 5));

        // Call the branchAndBoundWithExtendedList function
        List<List<Object>> result = branchAndBoundWithExtendedList(d, "S", "G");
        System.out.println("Final Path: " + result);
    }

    public static List<List<Object>> branchAndBoundWithExtendedList(Map<String, Map<String, Integer>> d, String start, String end) {
        List<List<Object>> exploredPaths = new ArrayList<>();  // Store paths explored
        Set<String> extendedList = new HashSet<>();  // To store fully explored nodes
        PriorityQueue<List<Object>> queue = new PriorityQueue<>(Comparator.comparingInt(a -> (int) a.get(0)));  // Min-heap priority queue
        int oracle = Integer.MAX_VALUE;  // Start with an infinite oracle

        // Add the starting point to the queue with cost 0
        queue.add(Arrays.asList(0, start));

        while (!queue.isEmpty()) {
            List<Object> currentPath = queue.poll();  // Current path with its cost
            String currentNode = (String) currentPath.get(currentPath.size() - 1);  // Current node

            // If the node is already fully explored, skip it
            if (extendedList.contains(currentNode)) continue;

            // If we reached the goal, update the oracle
            if (currentNode.equals(end)) {
                if ((int) currentPath.get(0) < oracle) {
                    oracle = (int) currentPath.get(0);
                }
                exploredPaths.add(currentPath);  // Store the path to the goal
                continue;
            }

            // Explore neighbors
            for (String neighbor : d.get(currentNode).keySet()) {
                if (currentPath.contains(neighbor)) continue;  // Avoid loops

                // Create a new path
                List<Object> newPath = new ArrayList<>(currentPath);
                int newCost = (int) currentPath.get(0) + d.get(currentNode).get(neighbor);  // Cumulative cost

                // Only add paths that are less than or equal to the current oracle
                if (newCost <= oracle) {
                    newPath.set(0, newCost);  // Update cost
                    newPath.add(neighbor);    // Add neighbor to the path
                    queue.add(newPath);       // Add to the queue
                    if (!neighbor.equals(end)) {
                        exploredPaths.add(newPath);  // Add to the explored path list for intermediate steps
                    }
                }
            }

            // Mark the current node as fully explored
            extendedList.add(currentNode);
        }

        return exploredPaths;
    }
}
