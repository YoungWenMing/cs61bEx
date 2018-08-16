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
        openSites = 0;
    }

    /*
    set the status of corresponding position in the sitesStatus to 1
    and union it with its opened neighbor
    furthermore, refactoring the neighbor's group is also necessary
     */
    public void open(int row, int col)       // open the site (row, col) if it is not open already
    {
        if(!checkValidation(row, col)) throw new IllegalArgumentException();
        if(isOpen(row, col)){
            //System.out.println("[ " +row + " " + col + "]" + "is already opened.");
            return;
        }
        sitesStatus[posTransformation(row, col)] = 1;
        openSites += 1;
        unionOpenedNeighbors(row, col);
    }

    /*
    union the site with its neighbors
     */
    private void unionOpenedNeighbors(int row, int col){
        int nthrow, nthcol;
        int pos = posTransformation(row, col);

        for(int i = -1 ; i < 2; i += 2){
            nthrow = row + i;
            nthcol = col + i;
            if(checkValidation(nthrow, col) && isOpen(nthrow, col))          //if the neighbor site is valid and opened
                base.union(posTransformation(nthrow, col), pos);

            if(checkValidation(row, nthcol) && isOpen(row, nthcol))
                base.union(posTransformation(row, nthcol), pos);
        }

    }

    public boolean isOpen(int row, int col)  // is the site (row, col) open?
    {
        int pos = posTransformation(row, col);
        return sitesStatus[pos] == 1;
    }

    /*
    check whether the position is connected to a open site which is in the top row
     */
    public boolean isFull(int row, int col)  // is the site (row, col) full?
    {
        if(!isOpen(row, col)) return false;

        int pos = posTransformation(row, col);
        for(int i = 0; i < gridScale; i += 1){
            int p = posTransformation(0, i);
            if( isOpen(0, i))
                if(base.connected(p, pos)) return true;
        }
        return false;
    }


    public int numberOfOpenSites()           // number of open sites
    {
        return openSites;
    }

    public boolean percolates()              // does the system percolate?
    {
        for(int i =0;i < gridScale; i += 1) {
            if (isFull(gridScale - 1, i)){
                //System.out.print("it percolates.");
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args)   // use for unit testing (not required)
    {

    }

    private boolean checkValidation(int row, int col){
        //if(! b) throw new IllegalArgumentException("the number of either row or column must between 0 and " + gridScale);
        return row < gridScale && row >= 0 && col < gridScale && col >= 0;
    }

    /*
    return the actual position in the disjoint set based on
    the row number and the column
     */
    private int posTransformation(int row, int col){
        return row * gridScale + col;
    }
}
