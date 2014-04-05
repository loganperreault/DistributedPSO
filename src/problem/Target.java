package problem;

import java.awt.Color;

public class Target {
	
	int x, y;
	double intensity = 300;
	Color color = Color.RED;
	
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
	
	public void setIntensity(double intensity) {
		this.intensity = intensity;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}

}
