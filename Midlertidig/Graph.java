import java.io.File;
import java.io.FileNotFoundException;
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
        vei.forEach((a1, a2) -> {
            gb.sixDeegreesIMDB(a1, a2);
        });
        vei.forEach((a1, a2) -> {
            gb.chillesteVei(a1, a2);
        });
        gb.countComponents();
        System.out.println("\n" + (System.currentTimeMillis() - starttime) + " ms");
    }

    private final Map<Actor, List<Map<Actor, String>>> graph = new HashMap<>();
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

    public static class WeightedNode implements Comparable<WeightedNode> {

        public final Actor actor;
        public double weight;
        public String movie;

        public WeightedNode(Actor actor, double weight, String movie) {
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

    public void readFile(String file) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(file));
        if (file.contains("movies")) {
            while (scanner.hasNextLine()) {
                String[] info = scanner.nextLine().split("\t");
                Movie m = new Movie(info[0], info[1], Double.parseDouble(info[2]));
                movies.put(info[0], m);
            }
        } else if (file.contains("actors")) {
            while (scanner.hasNextLine()) {
                String[] info = scanner.nextLine().split("\t");
                List<String> filmer = new ArrayList<>();
                for (int i = 2; i < info.length; i++) {
                    filmer.add(info[i]);
                }
                Actor a = new Actor(info[0], info[1], filmer);
                actors.put(info[0], a);
            }
        }
    }

    public void buildGraph() throws FileNotFoundException {
        this.readFile("movies.tsv");
        this.readFile("actors.tsv");
        HashMap<String, ArrayList<Actor>> hm = new HashMap<>(); //alle filmer med alle skuespillerne i filmen
        for (Actor a : actors.values()) {
            for (String m : a.movies) {
                if (!hm.containsKey(m)) {
                    ArrayList<Actor> aList = new ArrayList<>();
                    aList.add(a);
                    hm.put(m, aList);
                } else {
                    hm.get(m).add(a);
                }
            }
        }

        for (String movie : hm.keySet()) {
            for (Actor node : hm.get(movie)) {
                List<Map<Actor, String>> edges = new ArrayList<>(); //liste med relasjon til andre actors, med film
                // sjekk om film er relevant
                if (!movies.containsKey(movie)) {
                    if (!this.graph.containsKey(node)) {
                        this.graph.put(node, new ArrayList<>());
                    }
                    continue;
                }
                List<Actor> copy = new ArrayList<>(hm.get(movie)); // alle actors fra filmen i en liste
                copy.remove(node); // fjern seg selv for å unngå self loops

                for (Actor ax : copy) {
                    Map<Actor, String> connection = new HashMap<>();
                    connection.put(ax, movie);
                    edges.add(connection);
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
        for (List<Map<Actor, String>> edges : this.graph.values()) {
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
        for (int i = 0; i < path.size(); i++) {
            Actor node1 = path.get(i);
            if (i + 1 >= path.size()) break;
            Actor node2 = path.get(i + 1);
            String edge = "";
            for (Map<Actor, String> e : this.graph.get(node1)) {
                for (Actor a : e.keySet()) {
                    if (a.equals(node2)) {
                        edge = e.get(a);
                    }
                }
            }
            System.out.println(" ===[ " + this.movies.get(edge) + " ] ===> " + node2);
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
            for (Map<Actor, String> m : this.graph.get(u)) {
                for (Actor v : m.keySet()) {
                    if (v.equals(finish)) {
                        parents.put(v, u);
                        List<Actor> path = new ArrayList<>();

                        Actor pointer = v;
                        while (pointer != null) {
                            path.add(pointer);
                            pointer = parents.get(pointer);
                        }
                        return path.reversed();
                    }

                    if (!visited.contains(v)) {
                        visited.add(v);
                        parents.put(v, u);
                        queue.offer(v);
                    }
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
        for (int i = 1; i < weights.size(); i++) {
            WeightedNode node = weights.get(i);
            String edge = node.movie;
            System.out.println(" ===[ " + this.movies.get(edge) + " ] ===> " + node.actor);
        }
        System.out.println(weights.getLast());
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

            for (Map<Actor, String> m : this.graph.get(u.actor)) {
                for (Actor a : m.keySet()) {
                    double c = dist.get(u.actor).weight + (10 - movies.get(m.get(a)).rating);
                    WeightedNode v = new WeightedNode(a, c, m.get(a));

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
        }
        List<WeightedNode> path = new ArrayList<>();

        WeightedNode pointer = goal;
        while (pointer != null) {
            path.add(pointer);
            pointer = parents.get(pointer);
        }
        return path.reversed();
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
            for (Map<Actor, String> m : this.graph.get(u)) {
                for (Actor v : m.keySet()) {
                    if (!visited.contains(v)) {
                        visited.add(v);
                        i++;
                        queue.offer(v);
                    }
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
