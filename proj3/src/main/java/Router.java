import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.IndexMinPQ;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    /**
     * Return a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination
     * location.
     * @param g The graph to use.
     * @param stlon The longitude of the start location.
     * @param stlat The latitude of the start location.
     * @param destlon The longitude of the destination location.
     * @param destlat The latitude of the destination location.
     * @return A list of node id's in the order visited on the shortest path.
     */
    public static List<Long> shortestPath(GraphDB g, double stlon, double stlat,
                                          double destlon, double destlat) {
        //return null; // FIXME
        GraphDB.vertex startP = g.getVertex(g.closest(stlon, stlat));
        GraphDB.vertex endP = g.getVertex(g.closest(destlon, destlat));

        PriorityQueue<GraphDB.vertex> pq = new PriorityQueue<>(100, new VertexComparator());
        HashMap<Long, Double>   distTo = new HashMap<>();
        HashMap<Long, Long>     edgeTo = new HashMap<>();

        //initiation
        distTo.put(startP.id, 0.0);
        edgeTo.put(startP.id, null);
        startP.distToX = g.distance(startP.id, endP.id);
        pq.add(startP);         //maintain a set to check whether a node is in the queue

        while (!pq.isEmpty()){
            GraphDB.vertex here = pq.poll();
            if(here.id == endP.id){
                return findRoute(startP.id, endP.id, edgeTo);               //reach the destination
            }


            for(long v : here.neighbors){

                /*step 1: get the vertex object of here's neighbor*/
                GraphDB.vertex Vx = g.getVertex(v);

                /* calculate its g(v) + h(v)*/
                double distanceYet = distTo.get(here.id) + g.distance(here.id, v);
                double newD = distanceYet + g.distance(v, endP.id);

                /*update distToX field if necessary. Only when a shorter path to this node be found, the vertex's distToX should be modified.
                * note that after a node is removed from the queue, no shorter path to it will be found. so, the vertex is absolutely in the queue when we enter this "if"*/
                if(Vx.distToX > newD) {
                    /*step 2: update the edgeTo array */
                    edgeTo.put(v, here.id);
                    distTo.put(v, distanceYet);
                    Vx.distToX = newD;
                    /*refactor the priority queue if Vx is in it*/
                    pq.remove(Vx);          //if a node is excluded from the queue, there would never be a new shorter path to it
                    pq.add(Vx);
                }
                if(here.id == endP.id)      return findRoute(startP.id, endP.id, edgeTo);
            }
        }
        return null;
    }

    /*
    static class vertexD{
        long id;
        double distance;

        public vertexD(long id, double distance){
            this.id = id;
            this.distance = distance;
        }

        public void changeD(double x){
            this.distance = x;
        }
    }*/
    private static List<Long> findRoute(long startId, long endId, HashMap<Long, Long> edgeTo){
        Stack<Long> route = new Stack<>();
        while(edgeTo.get(endId) != null){
            long x = edgeTo.get(endId);
            route.add(endId);
            endId = x;
        }
        ArrayList<Long> realRoute = new ArrayList<>();
        realRoute.add(startId);
        while (!route.empty())      realRoute.add(route.pop());
        return realRoute;
    }



    static class VertexComparator implements Comparator{
        @Override
        public int compare(Object arg0, Object arg1){
            double d = ((GraphDB.vertex) arg0).distToX - ((GraphDB.vertex) arg1).distToX;
            if(d > 0)     return 1;
            else if(d < 0)  return -1;
            else            return 0;
        }

        VertexComparator(){}
    }

    /**
     * Create the list of directions corresponding to a route on the graph.
     * @param g The graph to use.
     * @param route The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of NavigatiionDirection objects corresponding to the input
     * route.
     */
    public static List<NavigationDirection> routeDirections(GraphDB g, List<Long> route) {
        //return null; // FIXME
        ArrayList<NavigationDirection> navi = new ArrayList<>();
        Iterator<Long> routeIter = route.iterator();
        long startP = routeIter.next(), secondP = routeIter.next();
        double bearingInit = g.bearing(startP, secondP);
        double distance =g.distance(startP, secondP);
        int direction = 0;
        long wayId = getWayId(startP, secondP, g);

        while(routeIter.hasNext()){
            long thirdP = routeIter.next();
            double tempBearing = g.bearing(secondP, thirdP);
            int tempDirection = getDirection(tempBearing - bearingInit);
            long tempWayId = getWayId(startP, secondP, g);
            if(wayId != tempWayId){
                navi.add(generateNavi(direction, getWayName(tempWayId, g), distance));            //when we need to take a turn, we finish the last
                direction = tempDirection;                                                      //path and add the navigation information to list
                distance  = 0;
                wayId = tempWayId;
            }
            distance += g.distance(secondP, thirdP);
            startP = secondP;
            secondP = thirdP;
            bearingInit = tempBearing;
        }
        /*get destination, accomplish integral route*/
        navi.add(generateNavi(direction, getWayName(secondP, g), distance));

        return navi;
    }


    //get the initial way, a little complicated in case that the start point is at an
    static long getWayId(long v1, long v2, GraphDB g){
        ArrayList<Long> roadList1 = g.getVertex(v1).roadIdList;
        ArrayList<Long> roadList2 = g.getVertex(v2).roadIdList;
        for(long x : roadList1){
            if (roadList2.contains(x))      return x;
        }
        return -1;
    }

    //from id of a node to get its road's name, if no name return "unnamed road"
    private static String getWayName(long id, GraphDB g){
        GraphDB.edge way = g.getEdge(id);           //get vertex object first, and then the road number
        return (way == null || !way.extraInfo.containsKey("name"))? NavigationDirection.UNKNOWN_ROAD: way.extraInfo.get("name");
    }

    //identify direction according to relative bearing
    private static int getDirection(double relativeBearing){
        double bearingAbs = Math.abs(relativeBearing);
        if(bearingAbs <= 15)
            return 1;
        if(bearingAbs <= 30)
            return relativeBearing < 0? 2:3;
        else if(bearingAbs <= 100)
            return relativeBearing < 0? 4:5;
        else
            return relativeBearing < 0? 6:7;
    }


    /*generate navigation element*/
    static NavigationDirection generateNavi(int direction, String way, double distance){
        NavigationDirection X = new NavigationDirection();
        X.direction = direction;
        X.way       = way;
        X.distance  = distance;
        return X;
    }

    /**
     * Class to represent a navigation direction, which consists of 3 attributes:
     * a direction to go, a way, and the distance to travel for.
     */
    public static class NavigationDirection {

        /** Integer constants representing directions. */
        public static final int START = 0;
        public static final int STRAIGHT = 1;
        public static final int SLIGHT_LEFT = 2;
        public static final int SLIGHT_RIGHT = 3;
        public static final int RIGHT = 4;
        public static final int LEFT = 5;
        public static final int SHARP_LEFT = 6;
        public static final int SHARP_RIGHT = 7;

        /** Number of directions supported. */
        public static final int NUM_DIRECTIONS = 8;

        /** A mapping of integer values to directions.*/
        public static final String[] DIRECTIONS = new String[NUM_DIRECTIONS];

        /** Default name for an unknown way. */
        public static final String UNKNOWN_ROAD = "unknown road";
        
        /** Static initializer. */
        static {
            DIRECTIONS[START] = "Start";
            DIRECTIONS[STRAIGHT] = "Go straight";
            DIRECTIONS[SLIGHT_LEFT] = "Slight left";
            DIRECTIONS[SLIGHT_RIGHT] = "Slight right";
            DIRECTIONS[LEFT] = "Turn left";
            DIRECTIONS[RIGHT] = "Turn right";
            DIRECTIONS[SHARP_LEFT] = "Sharp left";
            DIRECTIONS[SHARP_RIGHT] = "Sharp right";
        }

        /** The direction a given NavigationDirection represents.*/
        int direction;
        /** The name of the way I represent. */
        String way;
        /** The distance along this way I represent. */
        double distance;

        /**
         * Create a default, anonymous NavigationDirection.
         */
        public NavigationDirection() {
            this.direction = STRAIGHT;
            this.way = UNKNOWN_ROAD;
            this.distance = 0.0;
        }

        public String toString() {
            return String.format("%s on %s and continue for %.3f miles.",
                    DIRECTIONS[direction], way, distance);
        }

        /**
         * Takes the string representation of a navigation direction and converts it into
         * a Navigation Direction object.
         * @param dirAsString The string representation of the NavigationDirection.
         * @return A NavigationDirection object representing the input string.
         */
        public static NavigationDirection fromString(String dirAsString) {
            String regex = "([a-zA-Z\\s]+) on ([\\w\\s]*) and continue for ([0-9\\.]+) miles\\.";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(dirAsString);
            NavigationDirection nd = new NavigationDirection();
            if (m.matches()) {
                String direction = m.group(1);
                if (direction.equals("Start")) {
                    nd.direction = NavigationDirection.START;
                } else if (direction.equals("Go straight")) {
                    nd.direction = NavigationDirection.STRAIGHT;
                } else if (direction.equals("Slight left")) {
                    nd.direction = NavigationDirection.SLIGHT_LEFT;
                } else if (direction.equals("Slight right")) {
                    nd.direction = NavigationDirection.SLIGHT_RIGHT;
                } else if (direction.equals("Turn right")) {
                    nd.direction = NavigationDirection.RIGHT;
                } else if (direction.equals("Turn left")) {
                    nd.direction = NavigationDirection.LEFT;
                } else if (direction.equals("Sharp left")) {
                    nd.direction = NavigationDirection.SHARP_LEFT;
                } else if (direction.equals("Sharp right")) {
                    nd.direction = NavigationDirection.SHARP_RIGHT;
                } else {
                    return null;
                }

                nd.way = m.group(2);
                try {
                    nd.distance = Double.parseDouble(m.group(3));
                } catch (NumberFormatException e) {
                    return null;
                }
                return nd;
            } else {
                // not a valid nd
                return null;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof NavigationDirection) {
                return direction == ((NavigationDirection) o).direction
                    && way.equals(((NavigationDirection) o).way)
                    && distance == ((NavigationDirection) o).distance;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, way, distance);
        }
    }
}
