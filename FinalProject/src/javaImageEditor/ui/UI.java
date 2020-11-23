package javaImageEditor.ui;

import java.io.File;

import javax.swing.JFrame;

import javaImageEditor.filter.Filter;
import javaImageEditor.render.Image;
import javaImageEditor.render.Renderer;

public class UI {
	
	private Renderer renderer;
	
	private Filter filter;
	
	public UI () {
		renderer = new Renderer();
	}

	/**
	 * Creates two JFrames one with the input parameters and the other with the main canvas.
	 * @throws InterruptedException
	 */
	public void init() throws InterruptedException {
		JFrame frame = new JFrame("Image Editor");       
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);
        renderer.setSize(1000, 800);
        frame.add(renderer);
        frame.setVisible(true);
        filter = new Filter();
        
        JFrame parameters = new JFrame("Parameters");
        parameters.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        parameters.setVisible(true);
        Parameters p = new Parameters();
        p.addParametersToFrame(parameters);
        
        parameters.setSize(750, 700);
        parameters.setLayout(null);
      
        
        start();
	}
	
	/**
	 * Starts filter thread and render thread.
	 * Waits for both of them to finish.
	 * @throws InterruptedException
	 */
	private void start () throws InterruptedException {
		Image.fillImage(new File("city.jpg")); //Use this line to load an image by default.
		renderer.start();
		filter.start();
		
		renderer.join();
		filter.join();
	}
}
