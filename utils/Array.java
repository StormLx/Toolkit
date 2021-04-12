public class Array{
    private static int maxValue = 0;
    private static int idx = 0;
    private static int switch_pos = 0;

    public Array(){}

    public static int [] sortArray(int [] genArr) {
        for(int i = 0;i<genArr.length;i++){
            for (int j = i + 1; j < genArr.length; j++) {
                if (genArr[i] > genArr[j]) {
                    switch_pos = genArr[i];
                    genArr[i] = genArr[j];
                    genArr[j] = switch_pos;
                }
            }
        }  
        return genArr;  
    }
    public static int findMaxInt(int [] arr){

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > maxValue) {
                maxValue = arr[i];
                idx = i;
            }
        }
        return maxValue;
    }

    public static int[] genArray(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            int n = (int) (Math.random() * 100);
            arr[i] = n;
        }
        return arr;
    }

    public static void printArray(int[] arr) {
        String str = "";
        for (int i = 0; i < arr.length; i++) {
            str += arr[i] + ", ";
        }
        String String = str.substring(0, str.length() - 2);
        System.out.printf("[%s]\n", String);
    }
}