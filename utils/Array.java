public class Array{
    private static int maxValue = 0;
    private static int idx = 0;
    private static int switch_pos = 0;

    public Array(){}

    // Method to sort an array;
    public static int [] sortArray(int [] arr) { 
        for(int i = 0;i<arr.length;i++){
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] > arr[j]) {
                    switch_pos = arr[i];
                    arr[i] = arr[j];
                    arr[j] = switch_pos;
                }
            }
        }  
        return arr;  
    }
    // Method to find the max value in an array;
    public static int findMaxInt(int [] arr){
         for (int i = 0; i < arr.length; i++) {
            if (arr[i] > maxValue) {
                maxValue = arr[i];
                idx = i;
            }
        }
        return maxValue;
    }
    // Method to generate an array with random number;
    public static int[] genArray(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            int n = (int) (Math.random() * 100);
            arr[i] = n;
        }
        return arr;
    }
    // Method to print an array;
    public static void printArray(int[] arr) {
        String str = "";
        for (int i = 0; i < arr.length; i++) {
            str += arr[i] + ", ";
        }
        String String = str.substring(0, str.length() - 2);
        System.out.printf("[%s]\n", String);
    }
}