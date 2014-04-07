package problem;

import java.awt.Color;

import tools.Tools;

public class Target {
	
	double x, y;
	double intensity = 300;
	Color color = Color.RED;
	int visibleRadius = 3;
	double vx, vy;
	
	public Target(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public double[] position() {
		return new double[] {x, y};
	}
	
	public void setVelocity(double vx, double vy) {
		this.vx = vx;
		this.vy = vy;
	}
	
	public double[] getVelocity() {
		return new double[] {vx, vy};
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
		x += vx;
		y += vy;
	}
	
	public void setVisibleRadius(int radius) {
		this.visibleRadius = radius;
	}

}
