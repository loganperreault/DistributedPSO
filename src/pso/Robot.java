package pso;

public class Robot extends Particle {
	
	public double range;

	public Robot(int size, double minValue, double maxValue, double range) {
		super(size, minValue, maxValue);
		this.range = range;
		extended = true;
	}
	
	

}
