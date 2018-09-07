package mergesort;
import org.junit.Test;
import static org.junit.Assert.*;


public class TestMerge {
    @Test
    public void Testmergearray() {
        int[] x = new int[]{1, 2, 4, 5, 6, 8};
        int[] y = new int[]{5, 7, 10};

        int[] union = merge.mergeArray(x, y);
        for(int i : union){
            System.out.print(" " + i);
        }
        System.out.println("");
    }

    @Test
    public void TestCount(){
        merge sample = new merge();
        int test = 1024;
        sample.count(test);
        sample.printC(test);
    }
}
