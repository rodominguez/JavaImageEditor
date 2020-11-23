package javaImageEditor.render;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Image {

	public static int[][] imageMatrix;
	
	private static int[][] original;
	
	private static int width, height;
	
	private static boolean isReady = false;
	
	/**
	 * Reads an image from a file and sets the color matrix.
	 * A second matrix with the original image is also saved.
	 * Sets isReady to true so the render and filter thread can start.
	 * @param file
	 */
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
	
	
	private static void createMatrix(BufferedImage image) {
		imageMatrix = new int[image.getWidth()][image.getHeight()];
		original = new int[image.getWidth()][image.getHeight()];

		for (int i = 0; i < image.getWidth(); i++)
			for (int j = 0; j < image.getHeight(); j++) {
				imageMatrix[i][j] = image.getRGB(i, j);
				original[i][j] = imageMatrix[i][j];
			}
	}
	
	/**
	 * Sets the pixel at [x,y].
	 * @param x
	 * @param y
	 * @param rgba
	 */
	public static void setPixelAt (int x, int y, int rgba) {
		imageMatrix[x][y] = rgba;
	}
	
	/**
	 * Returns pixel at [x,y].
	 * @param x
	 * @param y
	 * @return
	 */
	public static int getPixelAt (int x, int y) {
		return imageMatrix[x][y];
	}

	public static int getWidth() {
		return width;
	}

	public static int getHeight() {
		return height;
	}
	
	/**
	 * Reverts changes from the original.
	 */
	public static void reset () {
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				imageMatrix[i][j] = original[i][j];
	}
	
	public static boolean isReady() {
		return isReady;
	}
}
