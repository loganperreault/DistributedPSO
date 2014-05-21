package pso;

import java.util.ArrayList;
import java.util.List;

import problem.Room;
import problem.Target;
import tools.Tools;

public class FitnessTarget extends Fitness {
	
	List<Target> targets;
	double threshold = 1;
	
	public FitnessTarget(Room room, List<Target> targets) {
		super(room);
		this.targets = targets;
	}
	
	public FitnessTarget(Room room, Target target) {
		super(room);
		if (this.targets == null)
			this.targets = new ArrayList<>();
		targets.add(target);
	}
	
	public double evaluate(Particle particle) {
		double intensity = 0.0;
		for (Target target : targets) {
			double distance = Tools.euclidean(particle.position, target.position());
			intensity += Math.min(target.getIntensity(), target.getIntensity() / Math.pow(distance, 2));
		}
		if (intensity < threshold)
			intensity = 0;
		return intensity;
	}

}
