import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(new File(args[0]));
            Teque<Integer> teque = new Teque<>();

            int operations = Integer.parseInt(scanner.nextLine());

            for (int i = 0; i < operations; i++) {
                String input = scanner.nextLine();
                String[] inputs = input.split(" ");
                String method = inputs[0];
                int number = Integer.parseInt(inputs[1]);

                switch (method) {
                    case "push_back":
                        teque.pushBack(number);
                        break;
                    case "push_front":
                        teque.pushFront(number);
                        break;
                    case "push_middle":
                        teque.pushMiddle(number);
                        break;
                    case "get":
                        teque.get(number);
                        break;
                    default:
                        System.out.println("Ugyldig input");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}