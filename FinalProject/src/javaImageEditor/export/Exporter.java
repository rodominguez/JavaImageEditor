package javaImageEditor.export;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javaImageEditor.render.Image;

public class Exporter {

	/**
	 * Exports the current image with the given name.
	 * If no name is given the name 'result' is given.
	 * If !Image.isReady() then it will not export.
	 * @param fileName
	 * @throws IOException
	 */
	public static void export(String fileName) throws IOException {
		if (!Image.isReady()) return;
		if (fileName == null || fileName.isEmpty())
			fileName = "result";
		File file = new File(fileName + ".jpg");
		BufferedImage image = new BufferedImage(Image.getWidth(), Image.getHeight(), BufferedImage.TYPE_INT_RGB);
		setImage(image, Image.imageMatrix);
		ImageIO.write(image, "JPG", file);
	}
	
	/**
	 * Sets each pixel into the buffered image.
	 * @param image
	 * @param matrix
	 */
	private static void setImage(BufferedImage image, int[][] matrix) {
		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix[i].length; j++)
				image.setRGB(i, j, matrix[i][j]);
	}
	
}
