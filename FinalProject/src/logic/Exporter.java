package logic;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Exporter {

	public static void export(String fileName) throws IOException {
		if (!Image.isReady()) return;
		if (fileName == null || fileName.isEmpty())
			fileName = "result.jpg";
		File file = new File(fileName + ".jpg");
		BufferedImage image = new BufferedImage(Image.getWidth(), Image.getHeight(), BufferedImage.TYPE_INT_RGB);
		setImage(image, Image.imageMatrix);
		ImageIO.write(image, "JPG", file);
	}
	
	private static void setImage(BufferedImage image, int[][] matrix) {
		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix[i].length; j++)
				image.setRGB(i, j, matrix[i][j]);
	}
	
}
