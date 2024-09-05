import java.util.PriorityQueue;
import java.util.Scanner;

public class BalanceHeap {

    public void algorithm(PriorityQueue<Integer> heap) {
        int num = -1;
        PriorityQueue<Integer> heap2 = new PriorityQueue<>();

        int mid = heap.size()/2;
        for (int i = 0; i <= mid; i++) {
            num = heap.poll();
            if (i < mid) heap2.offer(num);
        }
        System.out.println(num);
        if (!heap.isEmpty()) algorithm(heap);
        if (!heap2.isEmpty()) algorithm(heap2);
    }

    public static void main(String[] args) {
        BalanceHeap bh = new BalanceHeap();
        PriorityQueue<Integer> heap = new PriorityQueue<>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            int num = Integer.parseInt(line);
            heap.add(num);
        }

        bh.algorithm(heap);
    }
}
