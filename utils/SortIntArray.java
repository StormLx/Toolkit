package utils;

public class SortIntArray {

    static private int switch_pos = 0;

    public SortIntArray(){};

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
}
