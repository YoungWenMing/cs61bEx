public class Planet{

	public double xxPos;
	public double yyPos;
	public double xxVel;
	public double yyVel;
	public double mass;
	public String imgFileName;

	public static double gravityCons = 6.67e-11;

	public Planet(double xP, double yP, double xV,
					double yV, double m, String img){
		this.xxPos = xP;
		this.yyPos = yP;
		this.xxVel = xV;
		this.yyVel = yV;
		this.mass = m;
		this.imgFileName = img;
	}

	public Planet(Planet p){
		this(p.xxPos, p.yyPos, p.xxVel,p.yyVel, 
					p.mass, p.imgFileName);
		}

	public double calcDistance(Planet p){
		double deltaX = (this.xxPos - p.xxPos); 
		double deltaY = (this.yyPos - p.yyPos);
		return Math.sqrt(deltaY * deltaY + deltaX * deltaX);
	}

	public double calcForceExertedBy(Planet p){
		/*calculate the force*/
		double dist = this.calcDistance(p);
		double force = Planet.gravityCons * p.mass * this.mass / (dist * dist);
		return force;
	}

	public double calcForceExertedByX(Planet p){
		/*calculate the force p imposed on this planet
		along X axis
		*/
		double force = this.calcForceExertedBy(p);
		double dist = this.calcDistance(p);
		return force * (p.xxPos - this.xxPos) / dist;
	}

	public double calcForceExertedByY(Planet p){
		/*calculate the force p imposed on this planet
		along Y axis
		*/
		double force = this.calcForceExertedBy(p);
		double dist = this.calcDistance(p);
		return force * (p.yyPos - this.yyPos) / dist;
	}

	public double calcNetForceExertedByX(Planet[] pArray){
		/*
		sum the forces along X imposed by all the planet in
		an array
		*/
		double result = 0;
		for (Planet p : pArray){
			if (this.equals(p))
				continue;
			result += this.calcForceExertedByX(p);
		}
		return result;
	}

	public double calcNetForceExertedByY(Planet[] pArray){
		/*
		sum the forces along Y imposed by all the planet in
		an array
		*/
		double result = 0;
		for (Planet p : pArray){
			if (this.equals(p))
				continue;
			result += this.calcForceExertedByY(p);
		}
		return result;
	}

	public void update(double dt, double fx, double fy){
		/*
		update the planet's position with the forces along 
		X axis and Y axis on the interval dt
		*/
		double ax = fx / this.mass;
		double ay = fy / this.mass;
		this.xxVel += ax * dt;
		this.yyVel += ay * dt;
		this.xxPos += this.xxVel * dt;
		this.yyPos += this.yyVel * dt;

	}


	public void draw(double radius, double size){
		//draw this planet's image on the paints
		double xP = this.xxPos / radius * size;
		double yP = this.yyPos / radius * size;
		StdDraw.picture(xP, yP, this.imgFileName);
		
	}

}