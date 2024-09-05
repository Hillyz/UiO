import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class AVLTree {

    private int size = 0;
    private Node root;
    class Node {
        private int value;
        public int height;
        public Node left;
        public Node right;


        public Node(int val) {
            value = val;
        }
    }

    public int getHeight(Node n) {
        if (n == null) return -1;
        return n.height;
    }
    public void setHeight(Node n) {
        if (n != null)
            n.height = 1 + Math.max(getHeight(n.left), getHeight(n.right));
    }



    private Node leftRotate(Node n) {
        Node a = n.right;
        Node T1 = a.left;
        a.left = n;
        n.right = T1;

        setHeight(n);
        setHeight(a);

        return a;
    }

    private Node rightRotate(Node n) {
        Node a = n.left;
        Node T2 = a.right;
        a.right = n;
        n.left = T2;

        setHeight(n);
        setHeight(a);

        return a;
    }

    private int balanceFactor(Node n) {
        if (n == null) {
            return 0;
        }
        return getHeight(n.left) - getHeight(n.right);
    }

    private Node balance(Node n) {
        if (balanceFactor(n) < -1) {
            if (balanceFactor(n.right) > 0) {
                n.right = rightRotate(n.right);
            }
            return leftRotate(n);
        }
        if (balanceFactor(n) > 1) {
            if (balanceFactor(n.left) < 0) {
                n.left = leftRotate(n.left);
            }
            return rightRotate(n);
        }
        return n;
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
        setHeight(n);
        return balance(n);
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
        }
        else if (x < n.value) {
            n.left = removeR(x, n.left);
        }
        else if (n.left == null) n = n.right;
        else if (n.right == null) n = n.left;
        else {
            Node u = findMin(n.right);
            n.value = u.value;
            n.right = removeR(u.value, n.right);
        }
        setHeight(n);
        return balance(n);
    }

    public int size() {
        return this.size;
    }


    public static void main(String[] args) {

        AVLTree bst = new AVLTree();

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
