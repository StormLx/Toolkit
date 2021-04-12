import time.*;
public class Test {
    public static void main(String[] args) {
        Time.print();
        time.Stopwatch s = new time.Stopwatch();
        
        s.start();      
        Array.printArray(Array.genArray(1000));
        s.stop();

        System.out.println(s);
    }
}
