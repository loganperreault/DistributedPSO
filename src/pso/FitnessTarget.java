package pso;

import java.util.ArrayList;
import java.util.List;

import problem.Room;
import problem.Target;
import tools.Tools;

public class FitnessTarget extends Fitness {
	
	List<Target> targets;
	
	public FitnessTarget(Room room, List<Target> targets) {
		super(room);
		this.targets = targets;
	}
	
	public FitnessTarget(Room room, Target target) {
		super(room);
		this.targets = new ArrayList<>();
		targets.add(target);
	}
	
	public double evaluate(Particle particle) {
		double intensity = 0.0;
		for (Target target : targets) {
			double distance = Tools.euclidean(particle.position, target.position());
			intensity += target.getIntensity() / Math.pow(distance, 2);
		}
		return intensity;
	}

}
