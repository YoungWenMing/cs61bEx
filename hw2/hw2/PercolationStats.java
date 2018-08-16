package hw2;


import java.util.Random;

public class PercolationStats {

    private int scale;
    private int numOfRounds;
    private PercolationFactory factory;
    private double[] pSet;

    public PercolationStats(int N, int T, PercolationFactory pf)   // perform T independent experiments on an N-by-N grid
    {
        scale = N;
        numOfRounds = T;
        factory = pf;
        pSet = new double[T];
    }


    public double mean()                                           // sample mean of percolation threshold
    {
        double sum = 0.0;
        for(int i =0; i < scale; i +=1)
            sum += pSet[i];
        return sum / scale;
    }


    public double stddev()                                         // sample standard deviation of percolation threshold
    {
        double result = 0.0;
        double average = mean();
        for(double x : pSet)
            result += Math.pow(x - average, 2);
        return result / (scale - 1);
    }

    public double confidenceLow()                                  // low endpoint of 95% confidence interval
    {
        return mean() - 1.96 * stddev() / (Math.sqrt(scale));
    }

    public double confidenceHigh()                                 // high endpoint of 95% confidence interval
    {
        return mean() + 1.96 * stddev() / (Math.sqrt(scale));
    }

    /*
    execute the mont caro experiment
     */
    private double performTest(Percolation p){
        Random rand = new Random();
        double numOfOpened = 0.0;
        for(int i = 0; i < scale * scale *10; i += 1){
            int row = rand.nextInt(scale);
            int col = rand.nextInt(scale);
            p.open(row, col);
            numOfOpened =(double) p.numberOfOpenSites();
            if(numOfOpened > scale)
                if(p.percolates()) break;
        }
        return numOfOpened / (scale * scale);
    }

    public void performTestSequence(){
        for(int i = 0; i < numOfRounds; i += 1){
            Percolation singleTest = factory.make(scale);
            pSet[i] = performTest(singleTest);
        }
    }

    public void printOutcomes(){
        for (int i = 0; i < pSet.length; i += 1){
            System.out.print(pSet[i] + " ");
        }
    }

    public static void main(String[] args){
        PercolationFactory fa = new PercolationFactory();
        PercolationStats test = new PercolationStats(30, 200, fa);
        test.performTestSequence();
        //test.printOutcomes();
        System.out.println("The mean of the experiment sequence: " + test.mean());
        System.out.println("The standard deviation of the experiment sequence: " + test.stddev());
        System.out.println("The 95% confidence interval is [ " + test.confidenceLow() + ", " + test.confidenceHigh() + " ]");
    }
}
