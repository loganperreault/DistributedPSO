package problem;

import java.awt.Color;

import tools.Tools;

public class Target {
	
	int x, y;
	double intensity = 300;
	Color color = Color.RED;
	int visibleRadius = 3;
	
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
		//this.visibleRadius = Math.max(Tools.toInt(intensity/100), 1);
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public void runIteration() {
		// Stationary target, so do nothing
	}

}
