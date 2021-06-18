import javax.sound.midi.SysexMessage;
import java.util.*;
import java.awt.Color;

/**
 * This class solves a clustering problem with the Prim algorithm.
 */
public class Clustering {
    EdgeWeightedGraph G;
    List<List<Integer>> clusters;
    List<List<Integer>> labeled;

    /**
     * Constructor for the Clustering class, for a given EdgeWeightedGraph and no labels.
     *
     * @param G a given graph representing a clustering problem
     */
    public Clustering(EdgeWeightedGraph G) {
        this.G = G;
        clusters = new LinkedList<List<Integer>>();
    }

    /**
     * Constructor for the Clustering class, for a given data set with labels
     *
     * @param in input file for a clustering data set with labels
     */
    public Clustering(In in) {
        int V = in.readInt();
        int dim = in.readInt();
        G = new EdgeWeightedGraph(V);
        labeled = new LinkedList<List<Integer>>();
        LinkedList labels = new LinkedList();
        double[][] coord = new double[V][dim];
        for (int v = 0; v < V; v++) {
            for (int j = 0; j < dim; j++) {
                coord[v][j] = in.readDouble();
            }
            String label = in.readString();
            if (labels.contains(label)) {
                labeled.get(labels.indexOf(label)).add(v);
            } else {
                labels.add(label);
                List<Integer> l = new LinkedList<Integer>();
                labeled.add(l);
                labeled.get(labels.indexOf(label)).add(v);
                System.out.println(label);
            }
        }

        G.setCoordinates(coord);
        for (int w = 0; w < V; w++) {
            for (int v = 0; v < V; v++) {
                if (v != w) {
                    double weight = 0;
                    for (int j = 0; j < dim; j++) {
                        weight = weight + Math.pow(G.getCoordinates()[v][j] - G.getCoordinates()[w][j], 2);
                    }
                    weight = Math.sqrt(weight);
                    Edge e = new Edge(v, w, weight);
                    G.addEdge(e);
                }
            }
        }
        clusters = new LinkedList<List<Integer>>();
    }

    /**
     * This method finds a specified number of clusters based on a MST.
     * <p>
     * It is based in the idea that removing edges from a MST will create a
     * partition into several connected components, which are the clusters.
     *
     * @param numberOfClusters number of expected clusters
     */


    public void findClusters(int numberOfClusters) {
        LinkedList<Edge> sortedMST = new LinkedList<Edge>();
        PrimMST unsortedMst = new PrimMST(this.G);
        for (Edge e : unsortedMst.edges()) {
            sortedMST.add(e);
        }
        Collections.sort(sortedMST);
        for (int i = 0; i < numberOfClusters-1; i++) {
            sortedMST.removeLast();
        }
        this.clusters = connectedComponents(sortedMST);
    }

    public List<List<Integer>> connectedComponents(LinkedList<Edge> sortedMST) {
        UF unionFinder = new UF(this.G.V());
        LinkedList<List<Integer>> clusters = new LinkedList<>();
        LinkedList<List<Integer>> clusters1 = new LinkedList<>();
        for (Edge e : sortedMST) {
            unionFinder.union(e.either(), e.other(e.either()));
        }
        LinkedList<Integer> temp = new LinkedList<Integer>();
        for (int i = 0; i < this.G.V(); i++) {
            clusters.add(i,new ArrayList<>());
            if (!temp.contains(unionFinder.find(i))) {
                temp.add(unionFinder.find(i));
            }
        }
        for (int i = 0; i < this.G.V(); i++) {
            clusters.get(unionFinder.find(i)).add(i);
        }
        for (Integer i : temp) {
            clusters1.add(clusters.get(i));
        }
        return clusters1;

    }

        /**
         * This method finds clusters based on a MST and a threshold for the coefficient of variation.
         * <p>
         * It is based in the idea that removing edges from a MST will create a
         * partition into several connected components, which are the clusters.
         * The edges are removed based on the threshold given. For further explanation see the exercise sheet.
         *
         * @param threshold for the coefficient of variation
         */
    public void findClusters(double threshold) {
        LinkedList<Edge> sortedMST = new LinkedList<Edge>();
        LinkedList<Edge> newMST = new LinkedList<Edge>();
        PrimMST unsortedMst = new PrimMST(this.G);
        for (Edge e : unsortedMst.edges()) {
            sortedMST.add(e);
        }
        Collections.sort(sortedMST);
        for (Edge a : sortedMST) {
            newMST.add(a);
            if (coefficientOfVariation(newMST) > threshold) { // Calculated new at every step
                newMST.removeLast();
            }
        }
        this.clusters = connectedComponents(newMST);
    }

    /**
     * Evaluates the clustering based on a fixed number of clusters.
     *
     * @return array of the number of the correctly classified data points per cluster
     */
    public int[] validation() {
        int[] tempArray = new int[this.clusters.size()];
        for (int i = 0; i < this.clusters.size(); i++) {
            int dimension = 0;
            for (int j = 0; j < this.labeled.get(i).size(); j++) {
                if (!this.clusters.get(i).contains(this.labeled.get(i).get(j))) {
                    continue;
                }
                dimension++;
            tempArray[i] = dimension;
            }
        }
        return tempArray;
    }

    /**
     * Calculates the coefficient of variation.
     * For the formula see exercise sheet.
     *
     * @param part list of edges
     * @return coefficient of variation
     */
    public double coefficientOfVariation(List<Edge> part) {
        if (part.isEmpty()) {
            return 0.0;
        }
        double squareSum = 0.0;
        double sum = 0.0;
        double standardDeviation = 0.0;
        double mean = 0.0;
        for (Edge a : part) {
            squareSum += Math.pow(a.weight(),2);
            sum += a.weight();
        }
        standardDeviation = Math.sqrt((1.0/part.size()*squareSum)-Math.pow(1.0/part.size()*sum,2));
        mean = (1.0/ part.size())*sum;
        return standardDeviation/mean;
    }

    /**
     * Plots clusters in a two-dimensional space.
     */
    public void plotClusters() {
        int canvas = 800;
        StdDraw.setCanvasSize(canvas, canvas);
        StdDraw.setXscale(0, 15);
        StdDraw.setYscale(0, 15);
        StdDraw.clear(new Color(0, 0, 0));
        Color[] colors = {new Color(255, 255, 255), new Color(128, 0, 0), new Color(128, 128, 128),
                new Color(0, 108, 173), new Color(45, 139, 48), new Color(226, 126, 38), new Color(132, 67, 172)};
        int color = 0;
        for (List<Integer> cluster : clusters) {
            if (color > colors.length - 1) color = 0;
            StdDraw.setPenColor(colors[color]);
            StdDraw.setPenRadius(0.02);
            for (int i : cluster) {
                StdDraw.point(G.getCoordinates()[i][0], G.getCoordinates()[i][1]);
            }
            color++;
        }
        StdDraw.show();
    }


    public static void main(String[] args) {
        Clustering c = new Clustering(new In("iris_small.txt"));
        c.findClusters(2);
        System.out.println(c.clusters.toString());
        c.plotClusters();
    }
}
/*

*/