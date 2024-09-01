
public class Teque<E> {

    class Node<E> {
        private final E value;
        private Node<E> next;
        private Node<E> prev;

        public Node(E val) {
            value = val;
        }
    }

    private Node<E> head;
    private Node<E> tail;
    private Node<E> middle;
    private int size;

    public Teque() {
        this.head = this.tail = this.middle = null;
        this.size = 0;
    }

    public void pushBack(E x) {
        Node<E> newNode = new Node<>(x);
        if (this.tail == null) this.head = this.middle = this.tail = newNode;
        else {
            this.tail.next = newNode;
            newNode.prev = this.tail;
            this.tail = newNode;
        }
        this.size++;
        if (this.size % 2 == 0 && this.size > 1) {
            this.middle = this.middle.next;
        }
    }

    public void pushFront(E x) {
        Node<E> newNode = new Node<>(x);

        if (this.head == null) this.head = this.middle = this.tail = newNode;
        else {
            this.head.prev = newNode;
            newNode.next = this.head;
            this.head = newNode;
        }

        this.size++;
        if (this.size % 2 == 1 && this.size > 1) {
            this.middle = this.middle.prev;
        }
    }

    public void pushMiddle(E x) {
        Node<E> newNode = new Node<>(x);
        if (this.middle == null) this.middle = this.head = this.tail = newNode;
        else if (this.size % 2 == 1) {
            newNode.next = this.middle.next;
            newNode.prev = this.middle;
            if (this.middle != this.tail) {
                this.middle.next.prev = newNode;
            } else {
                this.tail = newNode;
            }
            this.middle.next = newNode;
            this.middle = newNode;
        } else if (this.size % 2 == 0) {
            newNode.next = this.middle;
            if (this.middle != this.head) {
                this.middle.prev.next = newNode;
            } else {
                this.head = newNode;
            }
            newNode.prev = this.middle.prev;
            this.middle.prev = newNode;
            this.middle = newNode;
        }
        this.size++;
    }

    public void get(int i) {
        Node<E> pointer;
        if (i <= this.size / 4) {
            pointer = this.head;
            for (int j = 0; j < i; j++) {
                pointer = pointer.next;
            }
        } else if (i <= this.size / 2) {
            pointer = this.middle;
            for (int j = this.size / 2; j > i; j--) {
                pointer = pointer.prev;
            }
        } else if (i > this.size / 2 && i < 3 * this.size / 4) {
            pointer = this.middle;
            for (int j = this.size / 2; j < i; j++) {
                pointer = pointer.next;
            }
        } else {
            pointer = this.tail;
            for (int j = this.size - 1; j > i; j--) {
                pointer = pointer.prev;
            }
        }

        System.out.println(pointer.value);
    }
}
