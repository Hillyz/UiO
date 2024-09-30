import java.io.*;
import java.util.*;

public class Graph {

    public static void main(String[] args) throws Exception {
        Graph gb = new Graph();
        Map<String, String> vei = new LinkedHashMap<>();
        vei.put("nm2255973", "nm0000460");
        vei.put("nm0424060", "nm8076281");
        vei.put("nm4689420", "nm0000365");
        vei.put("nm0000288", "nm2143282");
        vei.put("nm0637259", "nm0931324");
        long starttime = System.currentTimeMillis();
        gb.buildGraph();
        System.out.println("Graphbuild time: " + (System.currentTimeMillis() - starttime) + " ms");
        long split1 = System.currentTimeMillis();
        vei.forEach((a1, a2) -> {
            gb.sixDeegreesIMDB(a1, a2);
        });
        System.out.println("Shortest path time: " + (System.currentTimeMillis() - starttime) + " ms");
        System.out.println("Split 1: " + (System.currentTimeMillis() - split1) + " ms");
        long split2 = System.currentTimeMillis();
        vei.forEach((a1, a2) -> {
            gb.chillesteVei(a1, a2);
        });
        System.out.println("Chillest path time: " + (System.currentTimeMillis() - starttime) + " ms");
        System.out.println("Split 2: " + (System.currentTimeMillis() - split2) + " ms");
        long split3 = System.currentTimeMillis();
        gb.countComponents();
        System.out.println("Counting components time: " + (System.currentTimeMillis() - starttime) + " ms");
        System.out.println("Split 3: " + (System.currentTimeMillis() - split3) + " ms");
        System.out.println("\n" + (System.currentTimeMillis() - starttime) + " ms");
    }

    private final Map<Actor, List<Edge>> graph = new HashMap<>();
    private final Map<String, Movie> movies = new HashMap<>();
    private final Map<String, Actor> actors = new HashMap<>();
    private final Map<Integer, Integer> components = new HashMap<>();

    public static class Actor {
        public final String id;
        public final String name;
        public final List<String> movies;

        public Actor(String actorId, String actorName, List<String> actorMovies) {
            this.id = actorId;
            this.name = actorName;
            this.movies = actorMovies;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    public static class Movie {
        public final String id;
        public final String name;
        public final double rating;

        public Movie(String movieId, String movieName, double movieRating) {
            this.id = movieId;
            this.name = movieName;
            this.rating = movieRating;
        }

        @Override
        public String toString() {
            return this.name + " (" + this.rating + ")";
        }
    }

    public static class Edge {
        public final Actor actor;
        public Movie movie;


        public Edge(Actor actor, Movie movie) {
            this.actor = actor;
            this.movie = movie;
        }
    }

    public static class WeightedNode implements Comparable<WeightedNode> {

        public final Actor actor;
        public double weight;
        public Movie movie;

        public WeightedNode(Actor actor, double weight, Movie movie) {
            this.actor = actor;
            this.weight = weight;
            this.movie = movie;
        }

        @Override
        public String toString() {
            return String.format("Total weight: %.1f", this.weight);
        }

        @Override
        public int compareTo(WeightedNode o) {
            return (int) (this.weight - o.weight);
        }
    }

    public void readFile(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        if (file.contains("movies")) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] info = line.split("\t");
                Movie m = new Movie(info[0], info[1], Double.parseDouble(info[2]));
                movies.put(info[0], m);
            }
        } else if (file.contains("actors")) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] info = line.split("\t");
                List<String> filmer = new ArrayList<>();
                for (int i = 2; i < info.length; i++) {
                    filmer.add(info[i]);
                }
                Actor a = new Actor(info[0], info[1], filmer);
                actors.put(info[0], a);
            }
        }
    }

    public void buildGraph() throws IOException {
        this.readFile("movies.tsv");
        this.readFile("actors.tsv");
        HashMap<String, List<Actor>> hm = new HashMap<>(); //alle filmer med alle skuespillerne i filmen
        for (Actor a : actors.values()) {
            for (String m : a.movies) {
                if (!hm.containsKey(m)) {
                    List<Actor> aList = new ArrayList<>();
                    aList.add(a);
                    hm.put(m, aList);
                } else {
                    hm.get(m).add(a);
                }
            }
        }

        for (String movie : hm.keySet()) {
            for (Actor node : hm.get(movie)) {
                List<Edge> edges = new ArrayList<>();
                // sjekk om film er relevant
                if (!movies.containsKey(movie)) {
                    if (!this.graph.containsKey(node)) {
                        this.graph.put(node, edges);
                    }
                    continue;
                }
                List<Actor> copy = new ArrayList<>(hm.get(movie)); // alle actors fra filmen i en liste
                copy.remove(node); // fjern seg selv for å unngå self loops

                for (Actor ax : copy) {
                    edges.add(new Edge(ax, movies.get(movie)));
                }
                if (this.graph.containsKey(node)) {
                    this.graph.get(node).addAll(edges);
                } else
                    this.graph.put(node, edges);
            }
        }
        this.printNodes();
        this.printEdges();
    }

    public void printNodes() {
        System.out.println("Nodes: " + this.graph.size());
    }

    public void printEdges() {
        int degrees = 0;
        for (List<Edge> edges : this.graph.values()) {
            degrees += edges.size();
        }
        System.out.println("Edges: " + degrees / 2);
    }

    public void sixDeegreesIMDB(String id1, String id2) {
        Actor start = this.actors.get(id1);
        Actor goal = this.actors.get(id2);

        List<Actor> path = findShortestPath(start, goal, new HashSet<>());
        System.out.println("\n Fastest from " + start + " to " + goal);

        System.out.println(start);
        for (int i = path.size() - 1; i >= 0; i--) {
            Actor node1 = path.get(i);
            if (i - 1 < 0) break;
            Actor node2 = path.get(i - 1);
            Movie edge = null;
            for (Edge e : this.graph.get(node1)) {
                if (e.actor.equals(node2)) {
                    edge = e.movie;
                }
            }
            System.out.println(" ===[ " + edge + " ] ===> " + node2);
        }
    }

    private List<Actor> findShortestPath(Actor start, Actor finish, Set<Actor> visited) {
        visited.add(start);
        Queue<Actor> queue = new LinkedList<>();
        queue.add(start);
        Map<Actor, Actor> parents = new HashMap<>();

        while (!queue.isEmpty()) {
            Actor u = queue.poll();
            //gå gjennom kanter
            for (Edge e : this.graph.get(u)) {
                Actor v = e.actor;
                if (v.equals(finish)) {
                    parents.put(v, u);
                    List<Actor> path = new ArrayList<>();

                    Actor pointer = v;
                    while (pointer != null) {
                        path.add(pointer);
                        pointer = parents.get(pointer);
                    }
                    return path;
                }

                if (!visited.contains(v)) {
                    visited.add(v);
                    parents.put(v, u);
                    queue.offer(v);
                }
            }
        }
        return null;
    }

    public void chillesteVei(String id1, String id2) {
        Actor start = this.actors.get(id1);
        Actor goal = this.actors.get(id2);

        System.out.println("\nMost enjoyable from " + start + " to " + goal);
        List<WeightedNode> weights = this.dijkstras(start, goal);
        System.out.println(start);
        for (int i = weights.size() - 1; i >= 1; i--) {
            WeightedNode node = weights.get(i - 1);
            Movie edge = node.movie;
            System.out.println(" ===[ " + edge + " ] ===> " + node.actor);
        }
        System.out.println(weights.get(0));
    }

    private List<WeightedNode> dijkstras(Actor start, Actor finish) {
        Map<WeightedNode, WeightedNode> parents = new HashMap<>();
        Map<Actor, WeightedNode> dist = new HashMap<>();
        Queue<WeightedNode> queue = new PriorityQueue<>();
        WeightedNode s = new WeightedNode(start, 0, null);
        WeightedNode goal = new WeightedNode(finish, Double.MAX_VALUE, null);
        queue.add(s);
        dist.put(start, s);

        while (!queue.isEmpty()) {
            WeightedNode u = queue.poll();

            for (Edge e : this.graph.get(u.actor)) {
                Actor a = e.actor;
                double c = dist.get(u.actor).weight + (10 - e.movie.rating);
                WeightedNode v = new WeightedNode(a, c, e.movie);

                if (dist.get(a) == null || c < dist.get(a).weight) {
                    dist.put(a, v);
                    queue.offer(v);
                    parents.put(v, u);
                }
                if (a.equals(finish) && v.weight < goal.weight) {
                    goal.weight = v.weight;
                    goal.movie = v.movie;
                    parents.put(goal, u);
                }
            }
        }
        List<WeightedNode> path = new ArrayList<>();

        WeightedNode pointer = goal;
        while (pointer != null) {
            path.add(pointer);
            pointer = parents.get(pointer);
        }
        return path;
    }

    private void BFSfull() {
        Set<Actor> visited = new HashSet<>();
        for (Actor node : this.graph.keySet()) {
            int size = 0;
            if (!visited.contains(node)) {
                size = BFSvisit(node, visited);
            }
            if (this.components.containsKey(size)) {
                this.components.put(size, this.components.get(size) + 1);
            } else if (size != 0) {
                this.components.put(size, 1);
            }
        }
    }

    private int BFSvisit(Actor startNode, Set<Actor> visited) {
        visited.add(startNode);
        Queue<Actor> queue = new LinkedList<>();
        queue.offer(startNode);
        int i = 1;
        while (!queue.isEmpty()) {
            Actor u = queue.poll();
            for (Edge edge : this.graph.get(u)) {
                Actor v = edge.actor;
                if (!visited.contains(v)) {
                    visited.add(v);
                    i++;
                    queue.offer(v);
                }
            }
        }
        return i;
    }

    public void countComponents() {
        this.BFSfull();
        this.components.forEach((size, comps) -> {
            System.out.println("There are " + comps + " components of size " + size);
        });
    }

}
