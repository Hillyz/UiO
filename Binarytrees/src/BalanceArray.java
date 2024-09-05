import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class BalanceArray {


    public void algorithm(int[] arr) {
        int half = (int) (Math.floor(arr.length)/2);
        int pointer = arr[half];
        System.out.println(pointer);
        int[] left = Arrays.copyOfRange(arr, 0, half);
        int[] right =Arrays.copyOfRange(arr, half+1, arr.length);
        if (right.length != 0) algorithm(right);
        if (left.length != 0) algorithm(left);
    }


    public static void main(String[] args) {
        BalanceArray idk = new BalanceArray();
        ArrayList<Integer> arrayList = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            int num = Integer.parseInt(line);
            arrayList.add(num);
        }
        int[] arr = new int[arrayList.size()];
        int i = 0;
        for (int n : arrayList) {
            arr[i] = n;
            i++;
        }
        idk.algorithm(arr);
    }
}
