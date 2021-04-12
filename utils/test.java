package utils;

public class test {
    
public static void main(String[] args) {
    int[] arr1 = GenArrayRand.genArray(10);
    PrintArrays.printArray(arr1);
    int max = MaxInt.findMaxInt(arr1);
    System.out.println(max);
    }

}
