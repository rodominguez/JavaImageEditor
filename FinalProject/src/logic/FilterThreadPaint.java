package logic;

import java.awt.Color;
import java.awt.Graphics;

public class FilterThreadPaint implements Runnable{
	
	private Thread thread;
	
	private int start, end;
	
	private Image image;
	
	private Graphics graphics;
	
	public FilterThreadPaint(int start, int end, Image image, Graphics graphics) {
		this.start = start;
		this.end = end;
		this.image = image;
		this.graphics = graphics;
	}
	
	public void start() {
		thread = new Thread(this);
		thread.start();
	}
	
	public void join() throws InterruptedException {
		thread.join();
	}

	@Override
	public void run() {
		try {
		for (int i = start; i < end; i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				graphics.setColor(new Color(image.getPixelAt(i, j)));
				graphics.fillRect(i, j, 1, 1);
			}
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
