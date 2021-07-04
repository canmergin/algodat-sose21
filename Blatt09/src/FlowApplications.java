import java.util.*;


public class FlowApplications {

    /**
     * cloneFlowNetwork() makes a deep copy of a FlowNetwork
     * (FlowNetwork has unfortunately no copy constructor)
     *
     * @param flowNetwork the flow network that should be cloned
     * @return cloned flow network (deep copy) with same order of edges
     */
    private static FlowNetwork cloneFlowNetwork(FlowNetwork flowNetwork) {
        int V = flowNetwork.V();
        FlowNetwork clone = new FlowNetwork(V);

//        Simple version (but reverses order of edges)
//        for (FlowEdge e : flowNetwork.edges()) {
//            FlowEdge eclone = new FlowEdge(e.from(), e.to(), e.capacity());
//            clone.addEdge(eclone);
//        }

        for (int v = 0; v < flowNetwork.V(); v++) {
            // reverse so that adjacency list is in same order as original
            Stack<FlowEdge> reverse = new Stack<>();
            for (FlowEdge e : flowNetwork.adj(v)) {
                if (e.to() != v) {
                    FlowEdge eclone = new FlowEdge(e.from(), e.to(), e.capacity());
                    reverse.push(eclone);
                }
            }
            while (!reverse.isEmpty()) {
                clone.addEdge(reverse.pop());
            }
        }
        return clone;
    }


    /**
     * numberOfEdgeDisjointPaths() returns the (maximum) number of edge-disjoint paths that exist in
     * an undirected graph between two nodes s and t using Edmonds-Karp.
     *
     * @param graph the graph that is to be investigated
     * @param s     node on one end of the path
     * @param t     node on the other end of the path
     * @return number of edge-disjoint paths in graph between s and t
     */

    public static int numberOfEdgeDisjointPaths(Graph graph, int s, int t) {
        FlowNetwork flowNetworkOfGraph = new FlowNetwork(graph.V());
        for (int i = 0; i < graph.V(); i++) {
            Iterator<Integer> adj = graph.adj(i).iterator();
            while (adj.hasNext()) {
                FlowEdge flowEdgeOfGraph = new FlowEdge(i, adj.next(), 1.00); // Capacity of the edges are 1.00, so when the FordFulkerson runs their capacity is reduced to 0.00, so they can not be used again.
                flowNetworkOfGraph.addEdge(flowEdgeOfGraph);
            }
        }
        FordFulkerson fordFulkersonAlgorithm = new FordFulkerson(flowNetworkOfGraph, s, t);
        int edgeDisjointPathCount = (int) fordFulkersonAlgorithm.value();
        return edgeDisjointPathCount;
    }

    /**
     * edgeDisjointPaths() returns a maximal set of edge-disjoint paths that exist in
     * an undirected graph between two nodes s and t using Edmonds-Karp.
     *
     * @param graph the graph that is to be investigated
     * @param s     node on one end of the path
     * @param t     node on the other end of the path
     * @return a {@code Bag} of edge-disjoint paths in graph between s and t
     * Each path is stored in a {@code LinkedList<Integer>}.
     */

    public static Bag<LinkedList<Integer>> edgeDisjointPaths(Graph graph, int s, int t) {
        FlowNetwork flowNetworkOfGraph = new FlowNetwork(graph.V());
        for (int i = 0; i < graph.V(); i++) {
            Iterator<Integer> adj = graph.adj(i).iterator();
            while (adj.hasNext()) {
                FlowEdge flowEdgeOfGraph = new FlowEdge(i, adj.next(), 1.00); // Capacity of the edges are 1.00, so when the FordFulkerson runs their capacity is reduced to 0.00, so they can not be used again.
                flowNetworkOfGraph.addEdge(flowEdgeOfGraph);
            }
        }
        FordFulkerson fordFulkersonAlgorithm = new FordFulkerson(flowNetworkOfGraph, s, t);
        Bag<LinkedList<Integer>> paths = new Bag<>();
        int j = 0;
        int condition = (int) fordFulkersonAlgorithm.value();
        while (j < condition) {
            LinkedList<Integer> path = new LinkedList<>();
            int v = s;
            path.add(v);
            while (v != t) {
                Iterator<FlowEdge> edges = flowNetworkOfGraph.adj(v).iterator();
                FlowEdge currentEdge = edges.next();
                while ((currentEdge.flow() != 1.0 || currentEdge.from() != v) && edges.hasNext()) {
                    currentEdge = edges.next();
                }
                path.add(currentEdge.other(v));
                currentEdge.addResidualFlowTo(currentEdge.from(), 1.00);
                v = currentEdge.other(v);
            }
            paths.add(path);
            j++;
        }
        return paths;
    }


    /**
     * isUnique determines for a given Flow Network that has a guaranteed minCut,
     * if that one is unique, meaning it's the only minCut in that network
     *
     * @param flowNetworkIn the graph that is to be investigated
     * @param s             source node s
     * @param t             sink node t
     * @return true if the minCut is unique, otherwise false
     */

    public static boolean isUnique(FlowNetwork flowNetworkIn, int s, int t) {
        FlowNetwork flowNetworkCopy = new FlowNetwork(flowNetworkIn.V());
        for (FlowEdge e : flowNetworkIn.edges()) {
            flowNetworkCopy.addEdge(e);
            FlowEdge copyEdge = new FlowEdge(e.to(), e.from(), e.capacity());
            flowNetworkCopy.addEdge(copyEdge);
        }
        boolean[] markedForS = new boolean[flowNetworkIn.V()];
        boolean[] markedForT = new boolean[flowNetworkIn.V()];
        FordFulkerson fordFulkersonReverse = new FordFulkerson(flowNetworkCopy, t, s);
        for (int i = 0; i < flowNetworkCopy.V(); i++) {
            markedForT[i] = fordFulkersonReverse.inCut(i);
        }
        FordFulkerson fordFulkerson = new FordFulkerson(flowNetworkIn, s, t);
        for (int i = 0; i < flowNetworkIn.V(); i++) {
            markedForS[i] = fordFulkerson.inCut(i);
        }
        for (int i = 0; i < markedForS.length; i++) {
            if (markedForT[i] == markedForS[i]) {
                return false;
            }
        }
        return true;
    }


    /**
     * findBottlenecks finds all bottleneck nodes in the given flow network
     * and returns the indices in a Linked List
     *
     * @param flowNetwork the graph that is to be investigated
     * @param s           index of the source node of the flow
     * @param t           index of the target node of the flow
     * @return {@code LinkedList<Integer>} containing all bottleneck vertices
     * @throws IllegalArgumentException is flowNetwork does not have a unique cut
     */

    public static LinkedList<Integer> findBottlenecks(FlowNetwork flowNetwork, int s, int t) {
        FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, s, t);
        LinkedList<Integer> bottlenecks = new LinkedList<>();
        for (FlowEdge e : flowNetwork.edges()) {
            if ((fordFulkerson.inCut(e.from()) != fordFulkerson.inCut(e.to())) && !bottlenecks.contains(e.from())) {
                bottlenecks.add(e.from());
            }
        }
        bottlenecks.sort(Comparator.naturalOrder());
        return bottlenecks;
    }

    public static void main(String[] args) {

        // Test for Task 2.1 and 2.2 (useful for debugging!)
/*        Graph graph = new Graph(new In("Graph1.txt"));
        int s = 8;
        int t = 3;
        int n = numberOfEdgeDisjointPaths(graph, s, t);
        System.out.println("#numberOfEdgeDisjointPaths: " + n);
        Bag<LinkedList<Integer>> paths = edgeDisjointPaths(graph, s, t);
        for (LinkedList<Integer> path : paths) {
            System.out.println(path);
        }
*/


        // Example for Task 3.1 and 3.2 (useful for debugging!)
        FlowNetwork flowNetwork = new FlowNetwork(new In("Flussgraph2.txt"));
        int s = 0;
        int t = 5;
        boolean unique = isUnique(flowNetwork, s, t);
        System.out.println("Is mincut unique? " + unique);
        // Flussgraph1 is non-unique, so findBottlenecks() should be tested with Flussgraph2
        flowNetwork = new FlowNetwork(new In("Flussgraph2.txt"));
        LinkedList<Integer> bottlenecks = findBottlenecks(flowNetwork, s, t);
        System.out.println("Bottlenecks: " + bottlenecks);
    }

}
