package problem;

public class Target {
	
	int x, y;
	double intensity = 300;
	
	public Target(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public double[] position() {
		return new double[] {x, y};
	}
	
	public double getIntensity() {
		return intensity;
	}

}
