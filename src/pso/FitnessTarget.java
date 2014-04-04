package pso;

import problem.Room;
import problem.Target;
import tools.Tools;

public class FitnessTarget extends Fitness {
	
	Target target;
	
	public FitnessTarget(Room room, Target target) {
		super(room);
		this.target = target;
	}
	
	public double evaluate(Particle particle) {
		double distance = Tools.euclidean(particle.position, target.position());
		return target.getIntensity() / Math.pow(distance, 2);
	}

}
