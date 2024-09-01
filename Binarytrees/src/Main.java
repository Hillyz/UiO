public class Main {
    public static void main(String[] args) {

        BinarySearchtree bst = new BinarySearchtree();
        bst.insert(10);
        bst.insert(12);
        bst.remove(10);
        System.out.println(bst.contains(12));
        System.out.println(bst.size());
    }
}