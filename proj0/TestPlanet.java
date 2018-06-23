/*to test the planet class*/
public class TestPlanet{

	public static double[] clacForce(Planet p1, Planet p2){
		//calculate force between the two planet
		double[] force = new double[2];
		force[0] = p1.calcForceExertedByX(p2);
		force[1] = p1.calcForceExertedByY(p2);
		return force;
	}

	public static void main(String[] args){
		Planet p1 = new Planet(0.0, 0, 1, 1, 10e10, "./no1.fig");
		Planet p2 = new Planet(3, 3, 2, 0, 5e10, "./no2.fig");
		double[] force = clacForce(p1, p2);
		System.out.println(force[0]);
	}
}