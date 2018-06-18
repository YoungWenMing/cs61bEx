public class NBody{
	//the simulator of a universe
	public static double readRadius(String filename){
		//from the file get the radius of the universe
		In in = new In(filename);
		in.readDouble();
		double radius = in.readDouble();
		return radius;
	}

}