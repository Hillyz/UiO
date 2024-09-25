import java.io.File;
import java.util.*;

public class GraphBuilder {

    private HashMap<Actor, List<Actor>> naboListe = new HashMap<>();
    private HashMap<String, Movie> movies = new HashMap<>();
    private HashSet<Actor> actors = new HashSet<>();

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
        HashMap<String, ArrayList<Actor>> hm = new HashMap<>();
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

        for (String m : hm.keySet()) {
            for (Actor a : hm.get(m)) {
                if (!movies.containsKey(m)) {
                    this.naboListe.put(a, new ArrayList<>());
                } else {
                    List<Actor> lst = hm.get(m).subList(0, hm.get(m).size());
                    this.naboListe.put(a, lst);
                    System.out.println(this.naboListe.get(a).add(new Actor("test", "test", new ArrayList<>())));
                }
            }
        }
        for (Actor a : this.naboListe.keySet()) {

        }
    }

    public void printNodes() {
        System.out.println("Nodes: " + this.naboListe.keySet().size());
    }

    public void printEdges() {
        int edges = 0;
        for (List<Actor> l : this.naboListe.values()) {
            for (Actor a : l) {
                edges++;
            }
        }
        System.out.println("Edges: " + edges/2);
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
            return "Movie{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", rating=" + rating +
                    '}';
        }
    }
}
