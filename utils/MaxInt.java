package utils;

public class MaxInt {
    private static int maxValue = 0;
    private static int idx = 0;

    public MaxInt(){}

    public static int findMaxInt(int [] arr){

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > maxValue) {
                maxValue = arr[i];
                idx = i;
            }
        }
        return maxValue;
    }
}
