import java.io.File;
import java.util.*;

public class GraphBuilder {

    private final Map<Actor, List<Map<Actor, String>>> graph = new HashMap<>();
    private final Map<String, Movie> movies = new HashMap<>();
    private final Set<Actor> actors = new HashSet<>();

    public static void main(String[] args) throws Exception {
        GraphBuilder gb = new GraphBuilder();
        gb.start();
    }
    public void start() throws Exception{
        this.initObjects();
        this.buildGraph();
        this.printNodes();
        this.printEdges();
    }
    
    public void initObjects() throws Exception{
        Scanner actorScanner = new Scanner(new File("actors.tsv"));
        Scanner movieScanner = new Scanner(new File("movies.tsv"));

        while (movieScanner.hasNextLine()) {
            String[] info = movieScanner.nextLine().split("\t");
            Movie m = new Movie(info[0], info[1], Double.parseDouble(info[2]));
            movies.put(info[0], m);
        }

        while (actorScanner.hasNextLine()) {
            String[] info = actorScanner.nextLine().split("\t");
            ArrayList<String> filmer = new ArrayList<>();
            for (int i = 2; i < info.length; i++) {
                filmer.add(info[i]);
            }
            Actor a = new Actor(info[0], info[1], filmer);
            actors.add(a);
        }
    }

    public void buildGraph() {
        HashMap<String, ArrayList<Actor>> hm = new HashMap<>(); //alle filmer med alle skuespillerne i filmen
        for (Actor a : actors) {
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
    }

    public void printNodes() {
        System.out.println("Nodes: " + this.graph.size());
    }

    public void printEdges() {
        int degrees = 0;
        for (List<Map<Actor, String>> edges : this.graph.values()) {
            degrees += edges.size();
        }
        System.out.println("Edges: " + degrees/2);
    }

    public static class Actor {
        public final String id;
        public final String name;
        public final ArrayList<String> movies;

        public Actor(String actorId, String actorName, ArrayList<String> actorMovies) {
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
}
