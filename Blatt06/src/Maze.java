import java.util.*;

/**
 * Class that represents a maze with N*N junctions.
 *
 * @author Vera Röhr
 */
public class Maze {
    private final int N;
    private Graph M;    //Maze
    public int startnode;

    public Maze(int N, int startnode) {

        if (N < 0) throw new IllegalArgumentException("Number of vertices in a row must be nonnegative");
        this.N = N;
        this.M = new Graph(N * N);
        this.startnode = startnode;
        buildMaze();
    }

    public Maze(In in) {
        this.M = new Graph(in);
        this.N = (int) Math.sqrt(M.V());
        this.startnode = 0;
    }


    /**
     * Adds the undirected edge v-w to the graph M.
     *
     * @param v one vertex in the edge
     * @param w the other vertex in the edge
     * @throws IllegalArgumentException unless both {@code 0 <= v < V} and {@code 0 <= w < V}
     */
    public void addEdge(int v, int w) {
        if ((v < 0) && (v >= M.V()) && (w < 0) && (w >= M.V())) {
            throw new IllegalArgumentException();
        } else {
            M().addEdge(v, w);
        }
    }

    /**
     * Returns true if there is an edge between 'v' and 'w'
     *
     * @param v one vertex
     * @param w another vertex
     * @return true or false
     */
    public boolean hasEdge(int v, int w) {
        return M().adj(v).contains(w) || v == w;
    }

    /**
     * Builds a grid as a graph.
     *
     * @return Graph G -- Basic grid on which the Maze is built
     */
    public Graph mazegrid() {
        int vertices = N * N;
        Graph G = new Graph(vertices);
        for (int i = 0; i < vertices - 1; i += N) {
            for (int x = i, a = 0; a < N - 1; x++, a++) {
                G.addEdge(x, x + 1);
            }
        }
        for (int i = 0; i < N; i++) {
            for (int x = i; x < N * (N - 1); x += N) {
                G.addEdge(x, x + N);
            }
        }
        return G;
    }

    /**
     * Builds a random maze as a graph.
     * The maze is build with a randomized DFS as the Graph M.
     */
    private void buildMaze() {
        Graph mazeGraph = mazegrid();
        RandomDepthFirstPaths mazePaths = new RandomDepthFirstPaths(mazeGraph, startnode);
        mazePaths.randomNonrecursiveDFS(mazeGraph);
        int[] randomPath = mazePaths.edge();
        for (int i = 0; i < randomPath.length; ++i) {
            if (i != randomPath[i] && !hasEdge(i,randomPath[i]) && !hasEdge(randomPath[i],i)) {
                addEdge(i, randomPath[i]);
            }
        }
    }

    /**
     * Find a path from node v to w
     *
     * @param v start node
     * @param w end node
     * @return List<Integer> -- a list of nodes on the path from v to w (both included) in the right order.
     */
    public List<Integer> findWay(int v, int w) {
        List<Integer> pathList = new LinkedList<>();
        DepthFirstPaths path = new DepthFirstPaths(M,w); // Reverse order of the path, so the list is in correct order
        path.nonrecursiveDFS(M);
        pathList = path.pathTo(v);
        return pathList;
    }

    /**
     * @return Graph M
     */
    public Graph M() {
        return M;
    }


    public static void main(String[] args) {
        Maze testMaze = new Maze(5, 0);
        Graph testGraph = testMaze.M();
        System.out.println(testGraph.toString());
        GridGraph test = new GridGraph(testGraph,testMaze.findWay(0,5));
    }


}

