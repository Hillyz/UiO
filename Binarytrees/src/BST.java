import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class BST {

    private Node root = null;
    private int size = 0;

    class Node {
        private int value;
        public Node left;
        public Node right;

        public Node(int val) {
            value = val;
        }
    }

    public boolean contains(int x){
        return containsR(x, this.root);
    }

    private boolean containsR(int x, Node n) {
        if (n == null) return false;
        if (x > n.value) return containsR(x, n.right);
        else if (x < n.value) return containsR(x, n.left);
        return true;
    }

    public void insert(int x) {
        if (this.contains(x)) return;
        this.root = this.insertR(x, this.root);
        this.size++;
    }

    private Node insertR(int x, Node n) {
        if (n == null) {
            n = new Node(x);
        }
        else if (x > n.value) n.right = this.insertR(x, n.right);
        else if (x < n.value) n.left = this.insertR(x, n.left);
        return n;
    }

    public void remove(int x) {
        if (!this.contains(x)) return;
        this.root = removeR(x, this.root);
        this.size--;
    }

    private Node findMin(Node n) {
        if (n.left == null) return n;
        return findMin(n.left);
    }

    private Node removeR(int x, Node n) {
        if (n == null) return null;
        if (x > n.value) {
            n.right = removeR(x, n.right);
            return n;
        } else if (x < n.value) {
            n.left = removeR(x, n.left);
            return n;
        }
        if (n.left == null) return n.right;
        if (n.right == null) return n.left;

        Node u = findMin(n.right);
        n.value = u.value;
        n.right = removeR(u.value, n.right);
        return n;
    }

    public int size() {
        return this.size;
    }

    public static void main(String[] args) {

        BST bst = new BST();

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
