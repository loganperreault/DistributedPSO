package visualizer;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
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
	
	Stroke strokeDefault = new BasicStroke();
	Stroke strokeDashed = new BasicStroke(4.0f,                      // Width
            BasicStroke.CAP_SQUARE,    // End cap
            BasicStroke.JOIN_MITER,    // Join style
            10.0f,                     // Miter limit
            new float[] {16.0f,20.0f}, // Dash pattern
            0.0f);                     // Dash phase
	
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
			if (node.shape == Shape.CIRCLE)
				g2d.fillOval(scale(node.x) - scale(node.size)/2, scale(node.y) - scale(node.size)/2, scale(node.size), scale(node.size));
			else if (node.shape == Shape.SQUARE)
				g2d.fillRect(scale(node.x) - scale(node.size)/2, scale(node.y) - scale(node.size)/2, scale(node.size), scale(node.size));
			else if (node.shape == Shape.TRIANGLE || node.shape == Shape.TRIANGLE2) {
				int[] xPoints = new int[] {scale(node.x) - scale(node.size)/2, scale(node.x) + scale(node.size)/2, scale(node.x)};
				int[] yPoints = null;
				if (node.shape == Shape.TRIANGLE)
					yPoints = new int[] {scale(node.y) + scale(node.size)/2, scale(node.y) + scale(node.size)/2, scale(node.y) - scale(node.size)/2};
				else if (node.shape == Shape.TRIANGLE2)
					yPoints = new int[] {scale(node.y) - scale(node.size)/2, scale(node.y) - scale(node.size)/2, scale(node.y) + scale(node.size)/2};
				g2d.fillPolygon(xPoints, yPoints, 3);
			}
			if (node.circledRadius > 0) {
				g2d.setStroke(strokeDashed);
				g2d.drawOval(scale(node.x) - scale(node.circledRadius*2)/2, scale(node.y) - scale(node.circledRadius*2)/2, scale(node.circledRadius*2), scale(node.circledRadius*2));
				g2d.setStroke(strokeDefault);
			}
		}
	}
	
	private int scale(int value) {
		return (int)Math.round(scale*value);
	}
	
}
