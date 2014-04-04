package pso;

import problem.Room;
import problem.Target;

public class FitnessCommunication extends Fitness {
	
	Server server;
	
	public FitnessCommunication(Room room, Server server) {
		super(room);
		this.server = server;
	}
	
	public double evaluate(Particle particle) {
		
		double fitness = 0.0;
		
		// TODO: measure time between communications with server
		
		return fitness;
		
	}

}
