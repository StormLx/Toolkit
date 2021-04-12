package utils;
public class PrintArrays {

    public PrintArrays(){}

    public static void printArray(int[] arr) {
        String str = "";
        for (int i = 0; i < arr.length; i++) {
            str += arr[i] + ", ";
        }
        String String = str.substring(0, str.length() - 2);
        System.out.printf("[%s]\n", String);
    }

  
}
