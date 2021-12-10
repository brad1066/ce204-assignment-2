import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MST {
    // A class to hold Edge data
    private static class Edge implements Comparable<Edge> {
        int x, y; /* Endpoints */
        double w; /* Weight    */

        // Basic constructor, setting x, y and w members to provided values;
        private Edge(int x, int y, double w) {
            this.x = x;
            this.y = y;
            this.w = w;
        }

        @Override
        public int compareTo(Edge that) {
            if (this.w == that.w) return 0;
            return this.w < that.w ? 1 : -1; // Descending Order
        }
    }

    // Creates a randomly weighted undirected graph with n vertices
    static Graph getRandomGraph(int n) {
        Graph g = new MatrixGraph(n, false);
        Random r = new Random();
        // Iterate through all permutations of vertices
        for (int i = 0; i < g.numVertices(); i++) {
            for (int j = i+1; j < g.numVertices(); j++) {
                // Add an edge between the two vertices with a random weight
                g.addEdge(i, j, r.nextDouble()*10);
            }
        }
        return g;
    }

    // Returns the the sum of the edge weights
    static double getTotalEdgeWeight(Graph g) {
        double total_weight = 0;

        ArrayList<Edge> edges = getEdges(g);

        for (Edge edge : edges) {
            total_weight += edge.w;
        }

        return total_weight;
    }

    // Returns whether the Graph is connected or not;
    static boolean isConnected(Graph g) {
        // Create a list of flags to denote if a vertex has been visited or not, filling it with false flags;
        boolean[] visited = new boolean[g.numVertices];
        Arrays.fill(visited, false);

        // Create a LinkedQueue to store the vertices to poll, starting with the 1st (0th index) vertex
        ConcurrentLinkedQueue<Integer> searchQueue = new ConcurrentLinkedQueue<>(); // Use poll to get next item off queue
        searchQueue.add(0);

        // While the searchQueue has vertices to poll
        while (!searchQueue.isEmpty()) {
            // Get the next vertex to poll, and search add any unvisited neighbours to the searchQueue
            int i = searchQueue.poll();
            for (int n : g.neighbours(i)) {
                if (!visited[n]) searchQueue.add(n);
            }
            // Mark this vertex as visited
            visited[i] = true;
        }
        // If the graph is not connected, then it will have at least 1 unvisited vertex. If any are flagged as unvisited
        //    then return false (a disconnected flag).
        for (boolean b : visited)
            if (!b) return false;
        // All vertices have been visited, so return true (a connected flag)
        return true;
    }

    // Make the graph provided as an argument into an MST graph
    static void makeMST(Graph g) {
        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i<g.numVertices; i++) {
            for (int j = i+1; j<g.numVertices(); j++) {
                if (g.isEdge(i, j)) edges.add(new Edge(i, j, g.weight(i, j)));
            }
        }
        Collections.sort(edges);
        for (Edge edge : edges) {
            g.deleteEdge(edge.x, edge.y);
            if (isConnected(g)) g.addEdge(edge.x, edge.y, edge.w);
        }
    }

    public static ArrayList<Edge> getEdges (Graph g) {
        boolean[] explored = new boolean[g.numVertices()];
        Arrays.fill(explored, false);
        ArrayList<Edge> edges = new ArrayList<>();

        // Create a LinkedQueue to store the vertices to poll, starting with the 1st (0th index) vertex
        ConcurrentLinkedQueue<Integer> searchQueue = new ConcurrentLinkedQueue<>(); // Use poll to get next item off queue
        searchQueue.add(0);

        // While the searchQueue has vertices to poll
        while (searchQueue.stream().count() != 0) {
            // Get the next vertex to poll, and search add any unvisited neighbours to the searchQueue
            int i = searchQueue.poll();
            if (explored[i]) continue;
            for (int n : g.neighbours(i)) {
                if (explored[n]) continue;
                searchQueue.add(n);
                edges.add(new Edge(i, n, g.weight(i, n)));
            }
            explored[i] = true;
        }
        return edges;
    }

    public static void main(String[] args) {
        // Test A: Create a GOE graph, make it an MST, then calculate and display the weight.
        Graph goe = GraphOfEssex.getGraph();
        makeMST(goe);
        double goe_MST_weight = getTotalEdgeWeight(goe);
        System.out.printf("Test A: The Graph of Essex has an MST total weight of %.2f\n", goe_MST_weight);

        // Test B: Create 20 random graphs (with 100 vertices each), finding the average MST weight
        System.out.println("Starting Test B:\n\tNote that my implementation does finish, but takes an exceptionally" +
                                           "\n\tlong time to do so. The average that I have observed (it took a few" +
                                           "\n\thours) was within the range provided in the brief");
        double sumOfWeights = 1;
        for (int i = 0; i < 20; i++) {
            System.out.println("Starting Graph " + (i + 1));
            Graph g = getRandomGraph(100);
            System.out.println("Generated Graph " + (i + 1));
            makeMST(g);
            System.out.println("MST of Graph " + (i + 1));
            sumOfWeights += getTotalEdgeWeight(g);
        }
        double aveMSTEdgeWeight = sumOfWeights/20;

        System.out.printf("Test B: The average edge weight of 20 random MSTs (100 vertices) is approx %.2f\n", aveMSTEdgeWeight);

    }
}
