import java.util.*;
public class BFS {
    public static void main(String[] args) {
        Map<String, Map<String, Integer>> graph = new HashMap<>();

        graph.put("S", Map.of("A", 3, "B", 5));
        graph.put("A", Map.of("S", 3, "B", 4, "D", 3));
        graph.put("B", Map.of("S", 5, "A", 4, "C", 4));
        graph.put("C", Map.of("B", 4, "E", 6));
        graph.put("E", Map.of("C", 6));
        graph.put("D", Map.of("A", 3, "G", 5));
        graph.put("G", Map.of("D", 5));

        bfs(graph, "S", "G");
    }

    public static void bfs(Map<String, Map<String, Integer>> graph, String start, String goal) {
        Queue<List<String>> queue = new LinkedList<>();
        List<String> initialPath = new ArrayList<>();
        initialPath.add(start);
        queue.add(initialPath);

        while (!queue.isEmpty()) {
            List<String> path = queue.poll();
            String currentNode = path.get(path.size() - 1);

            if (currentNode.equals(goal)) {
                System.out.println("Path to goal: " + path);
                return;
            }

            Map<String, Integer> neighbors = graph.getOrDefault(currentNode, new HashMap<>());
            for (String neighbor : neighbors.keySet()) {
                if (!path.contains(neighbor)) {
                    List<String> newPath = new ArrayList<>(path);
                    newPath.add(neighbor);
                    queue.add(newPath);
                }
            }
        }
        System.out.println("Goal not reachable");
    }
}
