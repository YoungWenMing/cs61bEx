public class NBody{
	//the simulator of a universe
	public static double readRadius(String filename){
		//from the file get the radius of the universe
		In in = new In(filename);
		in.readInt();
		double radius = in.readDouble();
		return radius;
	}

	public static Planet[] readPlanets(String filename){
		//from the file read parameters of a single planet
		In in = new In(filename);
		int number = in.readInt();
		double radius = in.readDouble();
		Planet[] planets = new Planet[number];
		double xp;
		double yp;
		double xv;
		double yv;
		double mass;
		String photo;
		while(number > 0){
			xp = in.readDouble();
			yp = in.readDouble();
			xv = in.readDouble();
			yv = in.readDouble();
			mass = in.readDouble();
			photo = "./images/" + in.readString();
			planets[number - 1] = new Planet(xp, yp, xv, yv, mass, photo);
			number --;
		}
		return planets;
	}

	//public static void drawPlanet(int size, Planet [] pArray){

	//}


	public static void main(String[] args){
		if(args.length == 0) {
			System.out.println("Please supply T and dt, as well as data file name");
			//System.out.println("For countries with spaces, use an underscore, e.g. South_Korea");
			/* NOTE: Please don't use System.exit() in your code.
			   It will break the autograder. */
			System.exit(0);
		}

		//get all the stuff are needed
		double T = Double.parseDouble(args[0]);
		double dt = Double.parseDouble(args[1]);
		String filename = args[2];
		double radius = NBody.readRadius(filename);
		Planet[] allPlanets = NBody.readPlanets(filename);

		double t = 0;//
		int numPlanet = allPlanets.length;
		double[] xForces = new double[numPlanet];
		double[] yForces = new double[numPlanet];
		String imageToDraw = "./images/starfield.jpg";
		int j;

		StdDraw.enableDoubleBuffering();
		while(t < T){
			j = 0;
			while(j < numPlanet){
				allPlanets[j].update(dt, xForces[j], yForces[j]);
				j ++;
			}

			j = 0;
			while(j < numPlanet){
				xForces[j] = allPlanets[j].calcNetForceExertedByX(allPlanets);
				yForces[j] = allPlanets[j].calcNetForceExertedByY(allPlanets);
				j ++;
			}

			drawPlanet(imageToDraw, 100, radius, allPlanets);
			t += dt;
		}
		System.exit(0);

		//begin drawing
		/*String imageToDraw = "./images/starfield.jpg";
		int size = 100;
		StdDraw.setScale(-size, size);//set a blank board

		/* Clears the drawing window. 
		StdDraw.clear();
		StdDraw.picture(0, 0, imageToDraw, 350, 250);
		//StdDraw.picture(0, 0, allPlanets[0].imgFileName);
		int i = allPlanets.length;
		while (i > 0){
			Planet k = allPlanets[i - 1];
			k.draw();
			//k.update(0.1, 0.1, 0.1);
			i --;
		}
		StdDraw.show();*/
		//StdDraw.show();
		//StdDraw.pause(2000);

	}

	public static void drawPlanet(String backg, int size, double radius, Planet[] pA){
		StdDraw.setScale(-size, size);//set a blank board

		/* Clears the drawing window. */
		StdDraw.clear();
		StdDraw.picture(0, 0, backg, size * 2, size * 2);
		int i = pA.length;
		while (i > 0){
			Planet k = pA[i - 1];
			k.draw(radius, size);
			i --;
		}
		StdDraw.show();
		StdDraw.pause(10);
	}


/*this main function is for testing
	public static void main(String[] args){
		In in = new In("./data/planets.txt");
		int t = in.readInt();
		double test  = in.readDouble();
		System.out.println("the first integer is " + test);
	}


	public static Planet[] readSinglePlanet(String filename){
		//from the file read parameters of a single planet
		In in = new In(filename);

	}
*/
}