package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private WeightedQuickUnionUF base;
    private int gridScale;
    private int[] sitesStatus;
    private int openSites;

    public Percolation(int N)                // create N-by-N grid, with all sites initially blocked
    {
        if(N < 0) throw new IllegalArgumentException("the input must be beyond 0");
        base = new WeightedQuickUnionUF(N * N);
        gridScale = N;
        sitesStatus = new int[N * N];
    }

    /*
    set the status of corresponding position in the sitesStatus to 1
    and union it with its opened neighbor
    furthermore, refactoring the neighbor's group is also necessary
     */
    public void open(int row, int col)       // open the site (row, col) if it is not open already
    {
        sitesStatus[posTransformation(row, col)] = 1;
        openSites += 1;

    }

    /*
    union the site with its neighbors
     */
    public void unionNeighbors(int row, int col){
        int b1, b2;
        if(row % gridScale == 0) b1 = 0;
        else                     b1 = 2;

        if(row % gridScale == 0) b2 = 0;
        else                     b2 = 2;

        for(int i = -1; i < b1;i += 2){
            for(int j = -1;j < b2; b2 += 2 ){

            }
        }
    }

    public boolean isOpen(int row, int col)  // is the site (row, col) open?
    {
        int pos = posTransformation(row, col);
        return pos == 1;
    }

    /*
    check whether the position is connected to a open site which is in the top row
     */
    public boolean isFull(int row, int col)  // is the site (row, col) full?
    {
        int pos = posTransformation(row, col);
        for(int i = 0; i < gridScale; i += 1){
            int p = posTransformation(0, i);
            boolean x = isOpen(0, i)  &&  base.connected(p, pos);
            if(x) return true;
        }
        return false;
    }


    public int numberOfOpenSites()           // number of open sites
    {
        return 0;
    }

    public boolean percolates()              // does the system percolate?
    {
        return false;
    }

    public static void main(String[] args)   // use for unit testing (not required)
    {

    }

    private boolean checkValidation(int n){
        boolean b = n < gridScale && n >= 0;
        if(! b) throw new IllegalArgumentException("the number of either row or column must between 0 and " + gridScale);
        return b;
    }

    /*
    return the actual position in the disjoint set based on
    the row number and the column
     */
    public int posTransformation(int row, int col){
        checkValidation(row);
        checkValidation(col);

        return row * gridScale + col;
    }
}
