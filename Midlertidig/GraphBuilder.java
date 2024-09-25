import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class GraphBuilder {

    public void buildGraph() {
        HashMap<String, HashMap<String, String>> movies = new HashMap<>(); //vil koble tt-verdier til filmnavn og rating
        HashMap<String, String> actors = new HashMap<>(); //vil koble nm id til actors navn
        HashMap<String, ArrayList<String>> connections = new HashMap<>(); //koble actors til movies
        HashMap<String, ArrayList<String>> graph = new HashMap<>(); //koble actors til actors
        int noder = 0;
        int kanter = 0;
        Scanner actorScanner = new Scanner("marvel_actors.tsv");
        Scanner movieScanner = new Scanner("marvel_movies.tsv");

        while (movieScanner.hasNextLine()) {
            String[] info = movieScanner.nextLine().split("\t");
            HashMap<String, String> ratings = new HashMap<>();
            ratings.put(info[1], info[2]);
            movies.put(info[0], ratings);
        }

        while (actorScanner.hasNextLine()) {
            noder++;
            String[] info = actorScanner.nextLine().split("\t");
            actors.put(info[0], info[1]);
            ArrayList<String> actingMovies = new ArrayList<>(); //filmer actor har spilt i
            for (int i = 2; i < info.length; i++) {
                if (!movies.keySet().contains(info[i])) continue;
                actingMovies.add(info[i]);
            }
            connections.put(info[0], actingMovies);
        }

        for (String mv : movies.keySet()) {
            ArrayList<String> edge = new ArrayList<>();
            for (String ac : connections.keySet()) {
                if (connections.get(ac).contains(mv)) edge.add()
            }
        }

    }
}
