import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        BinarySearchtree bst = new BinarySearchtree();

        try {
            Scanner scanner = new Scanner(new File(args[0]));
            int operations = Integer.parseInt(scanner.nextLine());
            for (int i = 0; i < operations; i++) {
                String[] line = scanner.nextLine().split(" ");
                String method = line[0];

                if (method.equals("size")) {
                    System.out.println(bst.size());
                    continue;
                }
                int num = Integer.parseInt(line[1]);
                switch (method) {
                    case "insert":
                        bst.insert(num);
                        break;
                    case "remove":
                        bst.remove(num);
                        break;
                    case "contains":
                        System.out.println(bst.contains(num));
                        break;
                    default:
                        System.out.println("Ugyldig input: " + method);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}