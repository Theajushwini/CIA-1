import java.util.*;

public class AStarAlgorithm {
    public static void main(String[] args) {
        // Define the graph with actual distances
        Map<String, Map<String, Integer>> graph = new HashMap<>();
        graph.put("S", Map.of("A", 3, "B", 5));
        graph.put("A", Map.of("S", 3, "B", 4, "D", 3));
        graph.put("B", Map.of("S", 5, "A", 4, "C", 4));
        graph.put("C", Map.of("B", 4, "E", 6));
        graph.put("E", Map.of("C", 6));
        graph.put("D", Map.of("A", 3, "G", 5));
        graph.put("G", Map.of("D", 5));

        // Define the heuristic values
        Map<String, Map<String, Double>> heuristics = new HashMap<>();
        heuristics.put("S", Map.of("A", 7.38, "B", 6.0));
        heuristics.put("A", Map.of("S", Double.POSITIVE_INFINITY, "B", 6.0, "D", 5.0));
        heuristics.put("B", Map.of("S", Double.POSITIVE_INFINITY, "A", 7.38, "C", 7.58));
        heuristics.put("C", Map.of("B", 6.0, "E", Double.POSITIVE_INFINITY));
        heuristics.put("E", Map.of("C", 7.58));
        heuristics.put("D", Map.of("A", 7.38, "G", 0.0));
        heuristics.put("G", Map.of("D", 5.0));

        // Call the A* algorithm
        List<Object> result = aStar(graph, heuristics, "S", "G");
        System.out.println("Final Path: " + result);
    }

    // A* algorithm function
    public static List<Object> aStar(Map<String, Map<String, Integer>> graph,
                                     Map<String, Map<String, Double>> heuristics,
                                     String start, String goal) {
        // Priority queue for managing the frontier (nodes to be explored)
        PriorityQueue<List<Object>> openSet = new PriorityQueue<>(Comparator.comparingDouble(a -> (double) a.get(0)));
        Map<String, Double> costSoFar = new HashMap<>();  // Actual cost to reach each node
        Map<String, List<Object>> bestPathToNode = new HashMap<>();  // Best path to each node
        Set<String> closedSet = new HashSet<>();  // Set for visited nodes

        // Initialize the starting node
        openSet.add(Arrays.asList(0.0, start));  // (f_score, start)
        costSoFar.put(start, 0.0);
        bestPathToNode.put(start, Arrays.asList(0.0, start));

        System.out.println("Starting traversal from node: " + start);

        while (!openSet.isEmpty()) {
            List<Object> current = openSet.poll();  // Get the node with the lowest f_score
            double currentCost = (double) current.get(0);
            String currentNode = (String) current.get(1);

            // Print the current node being explored
            System.out.println("Exploring node: " + currentNode + " with cost: " + currentCost);

            // If the goal is reached, return the best path
            if (currentNode.equals(goal)) {
                return bestPathToNode.get(goal);  // Return the best path to the goal
            }

            // Explore neighbors
            for (String neighbor : graph.get(currentNode).keySet()) {
                // Skip if neighbor is already visited
                if (closedSet.contains(neighbor)) {
                    continue;
                }

                // Calculate new cost to reach neighbor
                double newCost = currentCost + graph.get(currentNode).get(neighbor);
                double heuristicForNeighbor = heuristics.get(neighbor).getOrDefault(goal, 0.0);
                double fScore = newCost + heuristicForNeighbor;  // f(n) = g(n) + h(n)

                // If this path to the neighbor is better
                if (!costSoFar.containsKey(neighbor) || newCost < costSoFar.get(neighbor)) {
                    costSoFar.put(neighbor, newCost);
                    List<Object> newPath = new ArrayList<>(bestPathToNode.get(currentNode));
                    newPath.set(0, newCost);  // Update the actual cost
                    newPath.add(neighbor);  // Add neighbor to the path

                    openSet.add(Arrays.asList(fScore, neighbor));  // Add to open set: (f_score, node)
                    bestPathToNode.put(neighbor, newPath);  // Record the best path to this neighbor
                }
            }
            // Add the current node to the closed set after exploring neighbors
            closedSet.add(currentNode);
        }

        // If no path was found, return an empty list
        return new ArrayList<>();
    }
}
