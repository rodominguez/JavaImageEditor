package logic;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class Image {
	
	private static final double MAX_ZOOM = 1d;
	
	private static final double MIN_ZOOM = 10d;

	public static int[][] imageMatrix;
	
	private static int[][] original;
	
	private static int width, height;
	
	private static int offsetX, offsetY;
	
	private static double zoom = 1.0d;
	
	private static List<FilterThreadPaint> threads;
	
	public static int RENDER_THREADS = 1;
	
	private static boolean isReady = false;
	
	public static void fillImage(File file){
		isReady = false;
		BufferedImage image;
		try {
			image = ImageIO.read(file);
			width = image.getWidth();
			height = image.getHeight();
			createMatrix(image);
			isReady = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void paint (Graphics g, Component c) {
		int width, height;
		width = (int)Math.ceil(Image.width / zoom);
		height = (int)Math.ceil(Image.height / zoom);
		
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
				threads.add(new FilterThreadPaint(i, height, pixels, width, zoom));
				return;
			} else
				threads.add(new FilterThreadPaint(i, i + blocks, pixels, width, zoom));
		}
	}
	
	private static void createMatrix(BufferedImage image) {
		imageMatrix = new int[image.getWidth()][image.getHeight()];
		original = new int[image.getWidth()][image.getHeight()];

		for (int i = 0; i < image.getWidth(); i++)
			for (int j = 0; j < image.getHeight(); j++) {
				imageMatrix[i][j] = image.getRGB(i, j);
				original[i][j] = imageMatrix[i][j];
			}
	}
	
	public static void export(String fileName) throws IOException {
		if (fileName == null || fileName.isEmpty())
			fileName = "result.jpg";
		File file = new File(fileName + ".jpg");
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		setImage(image, imageMatrix);
		ImageIO.write(image, "JPG", file);
	}
	
	private static void setImage(BufferedImage image, int[][] matrix) {
		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix[i].length; j++)
				image.setRGB(i, j, matrix[i][j]);
	}
	
	public static void setPixelAt (int x, int y, int rgba) {
		imageMatrix[x][y] = rgba;
	}
	
	public static int getPixelAt (int x, int y) {
		return imageMatrix[x][y];
	}

	public static int getWidth() {
		return width;
	}

	public static int getHeight() {
		return height;
	}

	public static void addZoom(int zoom) {
		double newZoom = Image.zoom + zoom * 0.02f;
		if (newZoom <= MIN_ZOOM && newZoom >= MAX_ZOOM)
			Image.zoom = newZoom;
	}
	
	public static void addOffset (int offsetX, int offsetY) {
		Image.offsetX += offsetX;
		Image.offsetY += offsetY;
	}
	
	public static void reset () {
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				imageMatrix[i][j] = original[i][j];
	}
	
	public static boolean isReady() {
		return isReady;
	}
}
