import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.*;

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


    /*
    the logic chain of this method is:
    1 get raw LonDPP, based on which we can get the depth by comparison with standard LonDPP
    2 calculate proper LonDPP & LatDPP according to depth
    3 specify the upper-left and lower-right tile, remember to ignore those part beyond the scope
    4 generate all files' name according to two extreme position
    5 calculate edges' longitude & latitude
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        // System.out.println(params);
        Map<String, Object> results = new HashMap<>();

        double lrlon = params.get("lrlon"), ullon = params.get("ullon"),
                ullat = params.get("ullat"), lrlat = params.get("lrlat"),
                width = params.get("w"), height = params.get("h");

        double LonDPP = getDPPS(lrlon, ullon, width);

        //int depth = Math.max(getDepth(LonDPP, ROOT_LRLON, ROOT_ULLON), getDepth(LatDPP, ROOT_ULLAT, ROOT_LRLAT));

        int depth = getDepth(LonDPP, ROOT_LRLON, ROOT_ULLON);

        LonDPP = getDPPS(ROOT_ULLON, ROOT_LRLON, PIXEL * Math.pow(2, depth));
        double LatDPP = getDPPS(ROOT_ULLAT, ROOT_LRLAT, PIXEL * Math.pow(2, depth));

        int[] startIndex = TileSpecify(LonDPP, LatDPP, ullon, ullat, depth);
        int[] endIndex = TileSpecify(LonDPP, LatDPP, lrlon, lrlat, depth);

        String[][] fileNameArray = fileNames(imageIndexes(startIndex, endIndex), depth);

        double[] edgeLons = getTwoLon(startIndex[0], endIndex[0], LonDPP);
        double[] edgeLats = getTwoLat(startIndex[1], endIndex[1], LatDPP);

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
    private int[] TileSpecify(double LonDPP, double LatDPP, double ullon, double ullat, int depth){
        double x = (ullon - ROOT_ULLON) / (PIXEL * LonDPP);
        double y = (ROOT_ULLAT - ullat) / (PIXEL * LatDPP);
        return new int[]{edgeCheck(x, depth), edgeCheck(y, depth)};
    }

    /*
    ensure that all image indexes are inside the scope
    the front end may send u some data which is beyond the whole map's scope sometime
    so we need to abandon those part
     */
    private int edgeCheck(double x , int depth){
        if(x <= 0)
            return 0;
        else if(x >= pow(2, depth))
            return (int) pow(2, depth) - 1;
        else
            return (int) floor(x);
    }

    /*
    return a 2_D array contains the x indexes and y indexes of images
     */
    private int[][] imageIndexes(int[] startP, int[] endP){
        int[][] result = new int[2][];
        result[0] = new int[endP[0] - startP[0] + 1];       //add all x values
        for(int i = 0; i < result[0].length; i += 1)
            result[0][i] = startP[0] + i;

        result[1] = new int[endP[1] - startP[1] + 1];       // add all y vallues
        for (int i = 0; i < result[1].length; i +=1)
            result[1][i] = startP[1] + i;

        return result;
    }

    /*
    generate names of images which comprise the whole map
    parameter indexes is a two-D array, indexes[0] is all possible x value of necessary images
    and indexes[1] is about y
    every x should match all ys
     */
    private String[][] fileNames(int[][] indexes, int depth){
        String[][] files = new String[indexes[1].length][indexes[0].length];
        for(int i = 0; i < indexes[1].length; i += 1){
            for(int j = 0; j < indexes[0].length; j += 1){
                files[i][j] = "d" + depth + "_x" + indexes[0][j] + "_y" + indexes[1][i] + ".png";
            }
        }
        return files;
    }

    /*
    calculate longitudes of the two end
    every image is of PIXEL pixels, and every pixel is of LonDPP longitudes
    NOTE that when calculating end's longitude, we need another 1 to reach the real end of this image
    rather than the last image's upper-left position
     */
    private double[] getTwoLon(int start, int end, double LonDPP){
        return new double[]{LonDPP * PIXEL * start + ROOT_ULLON, LonDPP * PIXEL * (end+1) + ROOT_ULLON};
    }

    /*
    calculate two latitudes in a simple way
     */
    private double[] getTwoLat(int start, int end, double LatDPP) {
        return new double[]{ROOT_ULLAT - start * PIXEL * LatDPP, ROOT_ULLAT - (end+1) * PIXEL * LatDPP};
    }

}
