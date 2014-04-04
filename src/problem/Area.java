package problem;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pso.PSO;
import pso.Particle;
import tools.Tools;
import visualizer.Node;
import visualizer.Visualize;

public class Area {
	
	double width, height;
	List<Target> targets = new ArrayList<>();
	PSO swarm;
	Visualize vis;
	int visualizerSize = 800;
	Map<Particle, Node> particleMap = new HashMap<>();
	Map<Target, Node> targetMap = new HashMap<>();
	int timestep = 0;
	boolean draw = true;
	int pause = 1000;
	
	public Area(double size) {
		initializeArea(size);
	}
	
	private void initializeArea(double size) {
		this.width = size;
		this.height = size;
		vis = new Visualize("Area", (int)width, (int)height);
		vis.setScale(visualizerSize/size);
		vis.draw();
	}
	
	public void addSwarm(PSO swarm) {
		this.swarm = swarm;
		for (Particle particle : swarm.particles) {
			Node node = new Node(Tools.toInt(particle.getPosition(0)), Tools.toInt(particle.getPosition(1)), 2, Color.BLUE);
			particleMap.put(particle, node);
			vis.addNode(node);
		}
	}
	
	public void addTarget(Target target) {
		if (target.x > width || target.x < 0 ||
			target.y > height || target.y < 0) {
			System.out.println("ERROR: Target is out of bounds");
			System.exit(1);
		}
		targets.add(target);
		Node node = new Node(target.x, target.y, 3, Color.RED);
		targetMap.put(target, node);
		vis.addNode(node);
	}
	
	public void runIterations(int timesteps) {
		for (timestep = 0; timestep < timesteps; timestep++) {
			nextTimestep();
		}
	}
	
	private void nextTimestep() {
		swarm.runIteration();
		updateArea();
		draw();
		try {	
			Thread.sleep(pause);	
		} catch (InterruptedException e) {	e.printStackTrace();	}
	}
	
	private void updateArea() {
		System.out.println(swarm.get(0));
		for (Particle particle : swarm.particles) {
			Node node = particleMap.get(particle);
			node.setPosition(Tools.toInt(particle.getPosition(0)), Tools.toInt(particle.getPosition(1)));
		}
		for (Target target : targets) {
			Node node = targetMap.get(target);
			node.setPosition(Tools.toInt(target.x), Tools.toInt(target.y));
		}
	}
	
	public void draw() {
		vis.removeAll();
		vis.updateUI();
		vis.repaint();
	}

}
