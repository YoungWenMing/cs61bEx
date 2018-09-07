package byog.Core;


public class support {
    static int extractNum(String x){
        String s = "";
        for(int i = 1 ; i < x.length() - 1; i += 1){
            s += x.charAt(i);
        }
        try {
            return Integer.parseInt(s);
        }catch (Exception e){
            System.out.println("illegal input number ");
            //System.exit(-1);
            return -1;
        }
    }

    static int[][] expandArray(int[][] set){
        int[][] newSet = new int[set.length * 2][set[0].length];
        int i = 0;
        for(int[] x : set){
            newSet[i] = x;
            i += 1;
        }
        return newSet;
    }

    /*
    build a road from p1 to p2, return the points set consists the road

    static int[][] buildRoad(int[] p1, int[] p2){
        int x1 = Math.min(p1[0],  p2[0]);
        int x2 = Math.max(p1[0],  p2[0]);
        int y1 = Math.min(p1[1],  p2[1]);
        int y2 = Math.max(p1[1],  p2[1]);

        int[][] road = new int[Math.abs(len1) + Math.abs(len2)][2];
        for(int i = 0; i < len1; i += 1){

        }
    }*/

    static int[][] straightLine(int[] p1, int[] p2){
        int d;
        if(p1[0] == p2[0])
            d = 1;
        else if(p1[1] == p2[1])
            d = 0;
        else
            //throw new Exception("input of straightLine is illegal! ");
        {System.out.println("illegal input of straightLine ");
            return new int[0][0];}

        int up = Math.max(p1[d], p2[d]);
        int lo = Math.min(p1[d], p2[d]);
        int[][] result = new int[up - lo + 1][2];
        for(int i = 0; i < up - lo + 1; i += 1){
            result[i][d] = lo + i ;
            result[i][1 - d] = p1[1 - d];
        }
        return result;
    }

    /*
    give back the set of points which consists the linkage
    */
    static int[][] buildWay(int[] p1, int[] p2){
        int[] intersection = new int[]{p1[0], p2[1]};
        int[][] line1 = straightLine(intersection, p1);
        int[][] line2 = straightLine(intersection, p2);
        return linkArray(line1, line2);
    }

    static int[][] linkArray(int[][] s1, int[][] s2){
        int len = s1.length + s2.length;
        int[][] newArray = new int[len][];
        for(int i  = 0; i < len; i += 1){
            if(i < s1.length)
                newArray[i] = s1[i];
            else
                newArray[i] = s2[i - s1.length];
        }
        return newArray;
    }


    public void printline(int[] p1, int[] p2){
        int[][] line = straightLine(p1, p2);
        System.out.println("This line contains: ");
        for(int[] x : line){
            System.out.print("[" + x[0] + " " + x[1] + "]");
        }
    }

    public static void main(String[] args){
        int[] p1 = new int[]{2, 2};
        int[] p2 = new int[]{1, 4};
        support test = new support();
        test.printline(p1, p2);
    }
}
