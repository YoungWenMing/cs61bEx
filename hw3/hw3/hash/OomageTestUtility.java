package hw3.hash;

import java.util.List;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        /* TODO:
         * Write a utility function that returns true if the given oomages
         * have hashCodes that would distribute them fairly evenly across
         * M buckets. To do this, convert each oomage's hashcode in the
         * same way as in the visualizer, i.e. (& 0x7FFFFFFF) % M.
         * and ensure that no bucket has fewer than N / 50
         * Oomages and no bucket has more than N / 2.5 Oomages.
         */
        int[] spread = new int[M];
        int N = oomages.size();
        double upperB = N / 2.5;
        double lowerB = N / 50;
        for(Oomage o : oomages){
            int bucketNum = (o.hashCode() & 0x7FFFFFFF) % M;
            spread[bucketNum] += 1;
        }
        for(int num: spread){
            if(num > upperB || num < lowerB)
                return false;
        }
        return true;
    }
}
