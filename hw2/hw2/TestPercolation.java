package hw2;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class TestPercolation {

    //@Test
    public void TestPercolationConstructor(){
        try{
            Percolation testPercolation = new Percolation(1);
        }catch (IllegalArgumentException e){
            System.out.println(e);
            System.out.println("I have told you that negative integer is not allowed!");
        }
    }

    //@Test
    public void TestPerformance(){
        Percolation test = new Percolation(3);
        for(int i = 0; i< 3; i += 1)
            test.open(i, i);
        assertFalse(test.isFull(2,0));
        assertFalse(test.isFull(1, 1));
        assertFalse(test.percolates());
        assertEquals(test.numberOfOpenSites(), 3);
    }

   // @Test
    public void TestUnion(){
        Percolation test = new Percolation(3);
        test.open(0, 0);
        test.open(1, 0);
        test.open(1,1);
        test.open(2,2);
        assertFalse(test.percolates());
        assertFalse(test.isFull(2,2));
        assertTrue(test.isFull(1,1));
        assertEquals(test.numberOfOpenSites(), 4);

        test.open(1,2);
        assertTrue(test.percolates());
    }

    //@Test
    public void TestNeighborSites(){
        Percolation test = new  Percolation(5);
        test.open(2,2);
        assertTrue(test.isOpen(2, 2));
        assertEquals(test.numberOfOpenSites(), 1);
        test.open(2,3);
        //test.unionOpenedNeighbors(2,2);
    }

    //@Test
    public static void main(String[] args){
        int N = 20;
        Percolation per = new Percolation(N);
        Random rand = new Random();
        for(int i = 0; i < N * N *10; i += 1){
            int row = rand.nextInt(N);
            int col = rand.nextInt(N);
            per.open(row, col);
            PercolationVisualizer.draw(per, N);
            if(per.numberOfOpenSites() > N)
                if(per.percolates()) break;
        }
        PercolationVisualizer.draw(per, N);
    }

}
