package utils;
public class GenArrayRand {
    
    public static int[] genArray(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            int n = (int) (Math.random() * 100);
            arr[i] = n;
        }
        return arr;
    }
}
