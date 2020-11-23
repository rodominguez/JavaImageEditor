package javaImageEditor.render;

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
	
	public static int renderThreads = 1;
	
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

	/**
	 * Start the render thread.
	 */
	public void start() {
		thread = new Thread(this);
		thread.start();
	}
	
	/**
	 * Wait for the render thread to end.
	 * @throws InterruptedException
	 */
	public void join () throws InterruptedException {
		thread.join();
	}

	/**
	 * Paints the image on the Graphics.
	 */
	public void paint(Graphics g) {
		g.fillRect(0, 0, 10000, 10000);
		if (Image.isReady())
			Renderer.paintParallel(g, this);
	}
	
	/**
	 * Creates a new BufferedImage and extracts the buffer.
	 * The buffer is divided into blocks and assigned to helper render threads.
	 * Waits for the threads to end and then draws the image into the graphics.
	 * @param graphics
	 * @param component
	 */
	public static void paintParallel (Graphics graphics, Component component) {
		int width, height;
		width = (int)Math.ceil(Image.getWidth() / zoom);
		height = (int)Math.ceil(Image.getHeight() / zoom);
		
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		
		divideIntoThreads(pixels, width, height);
		startThreads();
		joinThreads();
				
		graphics.drawImage(image, offsetX, offsetY, component);
	}
	
	/**
	 * Divides the buffer into blocks and assigns a helper render thread to each one.
	 * @param pixels
	 * @param width
	 * @param height
	 */
	private static void divideIntoThreads(int[] pixels, int width, int height) {
		int blocks = height / renderThreads;
		threads = new ArrayList<>();
		for (int i = 0;; i += blocks) {
			if (i + blocks >= height) {
				threads.add(new RenderHelperThread(i, height, pixels, width, zoom));
				return;
			} else
				threads.add(new RenderHelperThread(i, i + blocks, pixels, width, zoom));
		}
	}
	
	/**
	 * Starts all the helper render threads.
	 */
	private static void startThreads() {
		threads.forEach(m -> m.start());
	}
	
	/**
	 * Joins all the helper render threads.
	 */
	private static void joinThreads() {
		threads.forEach(m -> {
			try {
				m.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Calculates zoom of the image.
	 * @param zoom
	 */
	public static void addZoom(int zoom) {
		double newZoom = Renderer.zoom + zoom * 0.02f;
		if (newZoom <= MIN_ZOOM && newZoom >= MAX_ZOOM)
			Renderer.zoom = newZoom;
	}
	
	/**
	 * Moves the image left, right, up or down.
	 * @param offsetX
	 * @param offsetY
	 */
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
