import time.*;
public class Test {
    public static void main(String[] args) {
        
        time.Stopwatch timer1 = new time.Stopwatch();
        int [] arr =  Array.genArray(1000);
        Array.printArray(arr);
        double time1 = timer1.elapsedTime();
        System.out.printf("Elapsed time to generate this array: %1.3fs%n", time1);
        Time.print();
        Time.printFR();
    }
}
