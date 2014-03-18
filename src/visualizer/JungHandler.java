package visualizer;

import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import graph.Edge;
import graph.Graph;
import graph.Node;

public class JungHandler {

	private Layout<Node, Edge> layout;
	private BasicVisualizationServer<Node, Edge> viewer;
	private BufferedImage image;
	private JFrame frame = new JFrame("Simple Graph View");
	private int width = 1000;
	private int height = 1000;
	private int padding = 100;
	private boolean saveImages = false;
	private String imageFolder;
	private int imageCount = 1;
	private int startVisualize = 0;

	public JungHandler() {
		
	}
	
	public void setGraph(Graph graph) {
		layout = new TreeLayout<>(graph);
		viewer = new BasicVisualizationServer<>(layout);
		
		// Map nodes to their internally stored colors
        Transformer<Node,Paint> vertexColor = new Transformer<Node,Paint>() {
			public Paint transform(Node node) {
				return node.getColor();
			}
        };
		viewer.getRenderContext().setVertexFillPaintTransformer(vertexColor);
		
		for (Node n : graph.getVertices()) {
			layout.setLocation(n, n.getLocation());
		}
		
		frame.add(viewer, BorderLayout.CENTER);
	}
	
	public void draw() {
		if (imageCount > startVisualize) {
			frame.setSize(width, height);
			if (saveImages) {
				try
		        {   
					frame.pack();
		            image = new BufferedImage(viewer.getWidth(), viewer.getHeight(), BufferedImage.TYPE_INT_RGB);
		            Graphics2D graphics2D = image.createGraphics();
		            viewer.print(graphics2D);
		            graphics2D.dispose();
		            ImageIO.write(image,"jpeg", new File(imageFolder+"/"+imageCount+".jpg"));
		        }
		        catch(Exception exception) { System.out.println("Could not save image"); }
			} else {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {	e.printStackTrace();	}
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		}
		imageCount++;
	}
	
	public void setDimensions(int width, int height) {
		this.width = width + padding*2;
		this.height = height + padding*2;
	}
	
	public void saveImagesTo(String folder) {
		File directory = new File(folder);
		if (directory.isDirectory()) {
			this.imageFolder = directory.getAbsolutePath();
			this.saveImages = true;
			frame.setUndecorated(true);
		} else {
			throw new IllegalArgumentException("Invalid Image Folder.");
		}
	}
	
	public boolean isSavingImages() {
		return saveImages;
	}
	
	public void setStartVisualize(int frame) {
		this.startVisualize = frame;
	}

}
