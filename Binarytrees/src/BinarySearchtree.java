
public class BinarySearchtree {

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
}
