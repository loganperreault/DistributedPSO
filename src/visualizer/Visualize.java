package visualizer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Visualize extends JPanel {
	
	JFrame frame;
	int width, height;
	double scale = 1.0;
	List<Node> nodes = new ArrayList<>();
	
	public Visualize(String name, int width, int height) {
		frame = new JFrame(name);
		frame.add(this);
		this.width = width;
		this.height = height;
	}
	
	public void setScale(double scale) {
		this.scale = scale;
	}
	
	public void draw() {
		frame.setSize(scale(width), scale(height));
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void addNode(Node node) {
		nodes.add(node);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		for (Node node : nodes) {
			g2d.setColor(node.color);
			g2d.fillOval(scale(node.x), scale(node.y), scale(node.size), scale(node.size));
		}
	}
	
	private int scale(int value) {
		return (int)Math.round(scale*value);
	}
	
}
