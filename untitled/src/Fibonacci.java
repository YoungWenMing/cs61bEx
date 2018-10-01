public class Fibonacci {

    public static long fn(int n){
        long answer = n == 2 || n == 1?  1 : (fn(n - 1) + fn(n - 2));
        return answer;
    }

    public static void main(String[] args){
        int n = 48;
        long startT = System.currentTimeMillis();
        long x = fn(n);
        long endT = System.currentTimeMillis();

        System.out.println("the " + n + "th fibonacci number is " + x);
        System.out.println("calculations cost " + ((endT - startT) / 1000.0) + " s");
    }
}
