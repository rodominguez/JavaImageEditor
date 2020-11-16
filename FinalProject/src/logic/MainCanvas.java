package logic;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;

public class MainCanvas extends JPanel implements Runnable, MouseWheelListener, MouseMotionListener, MouseListener{

	private static final long serialVersionUID = 4524214773112363954L;

	private Thread thread;
	
	private boolean isMousePressed;
	
	private int mouseStartPosX, mouseStartPosY;

	public MainCanvas() {
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
			Image.paint(g, this);
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
		Image.addZoom(e.getWheelRotation());
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (isMousePressed) {
			int offsetX = (int)((mouseStartPosX - e.getXOnScreen()) * -0.08);
			int offsetY = (int)((mouseStartPosY - e.getYOnScreen()) * -0.08);
			Image.addOffset(offsetX, offsetY);
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
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
