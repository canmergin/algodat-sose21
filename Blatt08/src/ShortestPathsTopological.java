import java.util.Arrays;
import java.util.Stack;

public class ShortestPathsTopological {
    private int[] parent;
    private int s;
    private double[] dist;

    public ShortestPathsTopological(WeightedDigraph G, int s) {
        TopologicalWD topologicalWD = new TopologicalWD(G);
        topologicalWD.dfs(s);
        this.parent = new int[G.V()];
        this.dist = new double[G.V()];
        Arrays.fill(dist, Double.MAX_VALUE);
        dist[s] = 0;
        this.s = s;
        Stack<Integer> postOrder = topologicalWD.order();
        while (!postOrder.isEmpty()) {
            int v = postOrder.lastElement();
            for (DirectedEdge e : G.incident(v)) {
                relax(e);
            }
            postOrder.pop();
        }


    }

    public void relax(DirectedEdge e) {
        if (dist[e.to()] > dist[e.from()] + e.weight()) {
            dist[e.to()] = dist[e.from()] + e.weight();
            parent[e.to()] = e.from();
        }
    }

    public boolean hasPathTo(int v) {
        return parent[v] >= 0;
    }

    public Stack<Integer> pathTo(int v) {
        if (!hasPathTo(v)) {
            return null;
        }
        Stack<Integer> path = new Stack<>();
        for (int w = v; w != s; w = parent[w]) {
            path.push(w);
        }
        path.push(s);
        return path;
    }
}

