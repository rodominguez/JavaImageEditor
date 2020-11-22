package logic;

import java.io.File;

import javax.swing.JFrame;

public class UI {
	
	private Renderer canvas;
	
	private Filter modifier;
	
	public UI () {
		canvas = new Renderer();
	}

	public void init() throws InterruptedException {
		JFrame frame = new JFrame("Image Editor");       
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);
        canvas.setSize(1000, 800);
        frame.add(canvas);
        frame.setVisible(true);
        modifier = new Filter();
        
        JFrame parameters = new JFrame("Parameters");
        parameters.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        parameters.setVisible(true);
        Parameters p = new Parameters();
        p.addParametersToFrame(parameters);
        
        parameters.setSize(750, 700);
        parameters.setLayout(null);
      
        
        start();
	}
	
	public void start () throws InterruptedException {
		//Image.fillImage(new File("city.jpg"));
		canvas.start();
		modifier.start();
		
		canvas.join();
		modifier.join();
	}
}
