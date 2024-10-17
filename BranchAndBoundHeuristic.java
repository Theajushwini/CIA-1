import java.util.*;

public class BranchAndBoundHeuristic {
    public static void main(String[] args) {
        // Define the main graph with actual distances
        Map<String, Map<String, Integer>> d = new HashMap<>();
        d.put("S", Map.of("A", 3, "B", 5));
        d.put("A", Map.of("S", 3, "B", 4, "D", 3));
        d.put("B", Map.of("S", 5, "A", 4, "C", 4));
        d.put("C", Map.of("B", 4, "E", 6));
        d.put("E", Map.of("C", 6));
        d.put("D", Map.of("A", 3, "G", 5));
        d.put("G", Map.of("D", 5));

        // Define the heuristic values (estimate of remaining distance to the goal)
        Map<String, Map<String, Double>> d_h = new HashMap<>();
        d_h.put("S", Map.of("A", 7.38, "B", 6.0));
        d_h.put("A", Map.of("S", Double.POSITIVE_INFINITY, "B", 6.0, "D", 5.0));
        d_h.put("B", Map.of("S", Double.POSITIVE_INFINITY, "A", 7.38, "C", 7.58));
        d_h.put("C", Map.of("B", 6.0, "E", Double.POSITIVE_INFINITY));
        d_h.put("E", Map.of("C", 7.58));
        d_h.put("D", Map.of("A", 7.38, "G", 0.0));
        d_h.put("G", Map.of("D", 5.0));

        // Call the branch and bound with heuristic function
        List<Object> result = branchAndBoundWithHeuristics(d, d_h, "S", "G");
        System.out.println("Final Path: " + result);
    }

    // Branch and Bound with Heuristic function
    public static List<Object> branchAndBoundWithHeuristics(Map<String, Map<String, Integer>> d,
                                                            Map<String, Map<String, Double>> d_h,
                                                            String start, String end) {
        PriorityQueue<List<Object>> queue = new PriorityQueue<>(Comparator.comparingDouble(a -> (double) a.get(1)));  // Prioritize by (distance + heuristic)
        Map<String, Double> distanceToNode = new HashMap<>();  // Track the actual distance to each node
        Map<String, List<Object>> bestPathToNode = new HashMap<>();  // Track the best path to each node

        // Add the starting point to the queue with cost 0 and heuristic
        queue.add(Arrays.asList(0.0, d_h.get(start).values().stream().min(Double::compare).orElse(0.0), start));  // (actual_cost, cost + heuristic, path...)

        distanceToNode.put(start, 0.0);
        bestPathToNode.put(start, Arrays.asList(0.0, start));

        System.out.println("Starting traversal from node: " + start);

        while (!queue.isEmpty()) {
            List<Object> current = queue.poll();  // Get the lowest cost path
            double distanceSoFar = (double) current.get(0);
            String currentNode = (String) current.get(2);

            // Print the current path being explored
            System.out.println("Exploring path: " + current);

            // If the goal is reached, check if we can find a better path
            if (currentNode.equals(end)) {
                return bestPathToNode.get(end);  // Return the best path to the goal
            }

            // Explore neighbors
            for (String neighbor : d.get(currentNode).keySet()) {
                double distanceToNeighbor = distanceSoFar + d.get(currentNode).get(neighbor);
                double heuristicForNeighbor = d_h.get(currentNode).getOrDefault(neighbor, 0.0);

                // If this path is better, update and continue exploring
                if (!distanceToNode.containsKey(neighbor) || distanceToNeighbor < distanceToNode.get(neighbor)) {
                    distanceToNode.put(neighbor, distanceToNeighbor);

                    // Create a new path with actual distance and priority (distance + heuristic)
                    List<Object> newPath = new ArrayList<>(bestPathToNode.get(currentNode));
                    newPath.set(0, distanceToNeighbor);  // Update actual distance
                    newPath.add(neighbor);  // Add neighbor to the path

                    // Add to queue: (actual distance, distance + heuristic, node)
                    queue.add(Arrays.asList(distanceToNeighbor, distanceToNeighbor + heuristicForNeighbor, neighbor));
                    bestPathToNode.put(neighbor, newPath);
                }
            }
        }

        // If no path was found, return an empty list
        return new ArrayList<>();
    }
}
