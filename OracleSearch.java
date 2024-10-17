import java.util.*;

public class OracleSearch {
    public static void main(String[] args) {
        Map<String, Map<String, Integer>> d = new HashMap<>();
        d.put("S", Map.of("A", 3, "B", 5));
        d.put("A", Map.of("S", 3, "B", 4, "D", 3));
        d.put("B", Map.of("S", 5, "A", 4, "C", 4));
        d.put("C", Map.of("B", 4, "E", 6));
        d.put("E", Map.of("C", 6));
        d.put("D", Map.of("A", 3, "G", 5));
        d.put("G", Map.of("D", 5));

        List<List<Object>> result = oracleSearch(d, "S", "G", 10);
        System.out.println("All Paths: " + result);
    }

    public static List<List<Object>> oracleSearch(Map<String, Map<String, Integer>> d, String start, String goal, int maxNoGoalCutoff) {
        List<List<Object>> res = new ArrayList<>();
        List<List<Object>> queue = new ArrayList<>();
        queue.add(new ArrayList<>(Arrays.asList(0, start))); // Add initial state [0 cost, start node]

        int f = 0; // Counter for found goals

        while (!queue.isEmpty()) {
            List<Object> curr = queue.remove(0); // Pop the first path
            String currNode = (String) curr.get(curr.size() - 1); // Last node in the current path

            // Get the neighbors of the current node
            Map<String, Integer> neighbors = d.getOrDefault(currNode, new HashMap<>());
            List<List<Object>> tmp = new ArrayList<>();

            // Explore each neighbor
            for (String neighbor : neighbors.keySet()) {
                if (curr.contains(neighbor)) {
                    continue; // Skip if the neighbor is already in the current path (to avoid cycles)
                }

                // Create a new path by appending the neighbor
                List<Object> newPath = new ArrayList<>(curr);
                newPath.add(neighbor);
                newPath.set(0, (int) newPath.get(0) + neighbors.get(neighbor)); // Update cumulative cost
                tmp.add(newPath);

                // If we reached the goal, count it
                if (goal.equals(neighbor)) {
                    f++;
                }
            }

            // Add the new paths to the queue
            for (List<Object> t : tmp) {
                queue.add(t);
            }

            // Sort queue by the cumulative cost (index 0 of each path)
            queue.sort(Comparator.comparingInt(p -> (int) p.get(0)));

            // Add all generated paths to the result
            res.addAll(tmp);

            // If the goal cutoff is reached, stop
            if (f == maxNoGoalCutoff) {
                break;
            }
        }

        // Filter paths that reach the goal
        List<List<Object>> goalPaths = new ArrayList<>();
        for (List<Object> path : res) {
            if (path.contains(goal)) {
                goalPaths.add(path);
            }
        }

        // Print the paths to the goal
        System.out.println("Oracle Paths: " + goalPaths);

        return res; // Return all paths explored
    }
}
