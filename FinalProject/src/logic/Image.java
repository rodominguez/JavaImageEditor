package logic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Image {
	
	public static boolean IS_UNI_THREAD = false;
	
	private static final float MAX_ZOOM = 0.9f;
	
	private static final float MIN_ZOOM = 3f;

	public static int[][] imageMatrix;
	
	private static int[][] original;
	
	private static int width, height;
	
	private static int offsetX, offsetY;
	
	private static float zoom = 1.0f;
	
	private static Filter filter = new Filter();
	
	private static boolean isReady = false;
	
	public static void fillImage(){
		BufferedImage image;
		try {
			image = ImageIO.read(new File("city.jpg"));
			width = image.getWidth();
			height = image.getHeight();
			createMatrix(image);
			isReady = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void paint (Graphics g) {
		if (IS_UNI_THREAD)
			for (int i = 0; i < width; i++)
				for (int j = 0; j < height; j++)
					filter.filter(imageMatrix,i,j);
		
		for (float i = 0, posX = 0; (int)i < width; i += zoom, posX++)
			for (float j = 0, posY = 0; (int)j < height; j += zoom, posY++) {
				g.setColor(new Color(imageMatrix[(int)i][(int)j]));
				g.fillRect((int)posX + offsetX, (int)posY + offsetY, 1, 1);
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
		float newZoom = Image.zoom + (zoom / 100.0f) * 2;
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
