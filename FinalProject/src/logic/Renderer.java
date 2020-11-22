package logic;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class Renderer extends JPanel implements Runnable, MouseWheelListener, MouseMotionListener, MouseListener{

	private static final long serialVersionUID = 4524214773112363954L;
	
	private static final double MAX_ZOOM = 1d;
	
	private static final double MIN_ZOOM = 10d;
	
	public static final int MAX_RENDER_THREADS = 3;
	
	public static int RENDER_THREADS = 1;
	
	private static List<RenderHelperThread> threads;
	
	private static int offsetX, offsetY;
	
	private static double zoom = 1.0d;

	private Thread thread;
	
	private boolean isMousePressed;
	
	private int mouseStartPosX, mouseStartPosY;

	public Renderer() {
		addMouseWheelListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	public void start() {
		thread = new Thread(this);
		thread.start();
	}
	
	public void join () throws InterruptedException {
		thread.join();
	}

	public void paint(Graphics g) {
		g.fillRect(0, 0, 10000, 10000);
		if (Image.isReady())
			Renderer.paintParallel(g, this);
	}
	
	public static void paintParallel (Graphics g, Component c) {
		int width, height;
		width = (int)Math.ceil(Image.getWidth() / zoom);
		height = (int)Math.ceil(Image.getHeight() / zoom);
		
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		
		/*
		for (posY = 0; posY< height; posY++)
			for (posX = 0; posX < width; posX++)
				pixels[posY * width + posX] = imageMatrix[(int)(posX * zoom)][(int)(posY * zoom)];
		*/
		
		divideIntoThreads(pixels, width, height);
		startThreads();
		joinThreads();
				
		g.drawImage(image, offsetX, offsetY, c);
	}
	
	private static void joinThreads() {
		threads.forEach(m -> {
			try {
				m.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
	}

	private static void startThreads() {
		threads.forEach(m -> m.start());
	}

	private static void divideIntoThreads(int[] pixels, int width, int height) {
		int blocks = height / RENDER_THREADS;
		threads = new ArrayList<>();
		for (int i = 0;; i += blocks) {
			if (i + blocks >= height) {
				threads.add(new RenderHelperThread(i, height, pixels, width, zoom));
				return;
			} else
				threads.add(new RenderHelperThread(i, i + blocks, pixels, width, zoom));
		}
	}
	
	public static void addZoom(int zoom) {
		double newZoom = Renderer.zoom + zoom * 0.02f;
		if (newZoom <= MIN_ZOOM && newZoom >= MAX_ZOOM)
			Renderer.zoom = newZoom;
	}
	
	public static void addOffset (int offsetX, int offsetY) {
		Renderer.offsetX += offsetX;
		Renderer.offsetY += offsetY;
	}

	@Override
	public void run() {
		try {
			while (true) {
				Thread.sleep(1);
				repaint();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		addZoom(e.getWheelRotation());
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (isMousePressed) {
			int offsetX = (int)((mouseStartPosX - e.getXOnScreen()) * -0.08);
			int offsetY = (int)((mouseStartPosY - e.getYOnScreen()) * -0.08);
			addOffset(offsetX, offsetY);
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		return;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		return;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		return;
	}

	@Override
	public void mouseExited(MouseEvent e) {
		return;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		isMousePressed = true;
		mouseStartPosX = e.getXOnScreen();
		mouseStartPosY = e.getYOnScreen();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		isMousePressed = false;
	}
}
