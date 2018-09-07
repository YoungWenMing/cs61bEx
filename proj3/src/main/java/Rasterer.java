import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.PI;
import static java.lang.Math.ceil;
import static java.lang.Math.floor;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {


    private int MAXDEPTH = 7;

    private static final int PIXEL = MapServer.TILE_SIZE;
    public static final double ROOT_ULLAT = MapServer.ROOT_ULLAT, ROOT_ULLON = MapServer.ROOT_ULLON,
            ROOT_LRLAT = MapServer.ROOT_LRLAT, ROOT_LRLON = MapServer.ROOT_LRLON;

    public Rasterer() {
        // YOUR CODE HERE

    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        // System.out.println(params);
        Map<String, Object> results = new HashMap<>();

        double lrlon = params.get("lrlon"), ullon = params.get("ullon"),
                ullat = params.get("ullat"), lrlat = params.get("lrlat"),
                width = params.get("w"), height = params.get("h");

        double LonDPP = getDPPS(lrlon, ullon, width);
        double LatDPP = getDPPS(ullat, lrlat, height);

        int depth = Math.max(getDepth(LonDPP, ROOT_LRLON, ROOT_ULLON), getDepth(LatDPP, ROOT_ULLAT, ROOT_LRLAT));


        int[] startIndex = startTileSpecify(LonDPP, LatDPP, ullon, ullat);
        int[] endIndex = startTileSpecify(LonDPP, LatDPP, lrlon, lrlat);

        int[] imageNum = numOfImages(startIndex, endIndex);

        String[][] fileNameArray = fileNames(imageIndexes(startIndex, imageNum), depth);

        double[] edgeLons = getTwoLon(startIndex[0], imageNum[0], LonDPP);
        double[] edgeLats = getTwoLat(startIndex[1], imageNum[1], depth);

        results.put("render_grid", fileNameArray);
        results.put("depth", depth);
        results.put("raster_ul_lon", edgeLons[0]);
        results.put("raster_lr_lon", edgeLons[1]);
        results.put("raster_ul_lat", edgeLats[0]);
        results.put("raster_lr_lat", edgeLats[1]);
        results.put("query_success", true);

        //System.out.println("Since you haven't implemented getMapRaster, nothing is displayed in "
          //                 + "your browser.");
        return results;
    }

    /*consider the ratio of screen for display
    return smallest width and height that covers the whole image
    with ratio as 4 : 3
    it worth noting that no valid output when depth is 0 or 1
     */
    public  int[] numOfImages(int[] startP, int[] endP){
        return new int[]{endP[0] - startP[0] + 1, endP[1] - startP[1] + 1};
    }



    //initialize the fixed LonDPP of different depths of levels


    //calculate the LonDPP based on longitudes and pixels along longitude
    private double getDPPS(double lowerRightLon, double upperLeftLon, double width){
        return Math.abs(lowerRightLon - upperLeftLon) / width;
    }


    //get image depth based on the LonDPP of the input
    private int getDepth(double DPP, double upper, double lower){
        for(int i = 0; i <= MAXDEPTH; i += 1)
            if(DPP > getDPPS(upper, lower, PIXEL * Math.pow(2, i)))  return i;
        return MAXDEPTH;
    }

    /*
    calculate tile number of the most northwest point
     */
    private int[] startTileSpecify(double LonDPP, double LatDPP, double ullon, double ullat){
        double x = Math.abs(ullon - ROOT_ULLON) / (PIXEL * LonDPP);
        double y = Math.abs(ullat - ROOT_ULLAT) / (PIXEL * LatDPP);
        return new int[]{(int) floor(x), (int) floor(y)};
    }

    public static int[] startTileSpecify(double LonDPP, double LatDPP, double[] coordinates, double[] base){
        double x =  Math.abs(coordinates[0] - base[0]) / (PIXEL * LonDPP);
        double y = Math.abs(coordinates[1] - base[1]) / (PIXEL * LatDPP);
        return new int[]{(int) floor(x), (int) floor(y)};
    }




    /*
    return a 2_D array contains the x indexes and y indexes of images
     */
    private int[][] imageIndexes(int[] startP, int[] endP){
        int[][] result = new int[2][];
        result[0] = new int[endP[0] - startP[0] + 1];
        for(int i = 0; i <=result[0].length; i += 1)
            result[0][i] = startP[0] + i;

        result[1] = new int[endP[1] - startP[1] + 1];
        for (int i = 0; i <= result[1].length; i +=1)
            result[1][i] = startP[1] + i;

        return result;
    }

    /*
    generate names of images which comprise the whole map
     */
    private String[][] fileNames(int[][] indexes, int depth){
        String[][] files = new String[indexes[1].length][indexes[0].length];
        for(int i = 0; i < indexes[1].length; i += 1){
            for(int j = 0; j < indexes[0].length; j += 1){
                files[i][j] = "d" + depth + "_x" + indexes[0][j] + "_y" + indexes[1][i];
            }
        }
        return files;
    }

    /*
    calculate longitudes of the two end
    every image is of PIXEL pixels, and every pixel is of LonDPP longitudes
     */
    private double[] getTwoLon(int start, int num, double LonDPP){
        return new double[]{LonDPP * PIXEL * start + ROOT_ULLON, LonDPP * PIXEL * (start + num) + ROOT_ULLON};
    }

    /*
    calculate two latitudes in a simple way
     */
    private double[] getTwoLat(int start, int num,int depth){
        double interval = (ROOT_ULLAT - ROOT_LRLAT) / (256 * Math.pow(2, depth));
        return new double[]{start * interval, (start + num) * interval};
    }

    public static void main(String[] args){


        Rasterer rasterer = new Rasterer();




   ;

        System.out.println("3.0 / 4 = " + (3 * 1.0) / 4);

        int[][] XYsets = rasterer.imageIndexes(new int[]{0,0}, new int[]{4, 3});
        String[][] filenames = rasterer.fileNames(XYsets, 3);
        printArray(XYsets[0]);
        printArray(XYsets[1]);
        for(String[] x : filenames)
            printString(x);
    }

    public  static void printArray(int[] toDisplay){
        for(int x : toDisplay)
            System.out.print(x + " ");
    }

    public static void printString(String[] toDisplay){
        System.out.println(" ");
        for(String x : toDisplay)
            System.out.print(x + " ");
    }
}
