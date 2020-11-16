package logic;

public class FilterThreadPaint implements Runnable {

	private Thread thread;

	private int start, end;

	private int width;

	private int[] pixels;

	private double zoom;

	public FilterThreadPaint(int start, int end, int[] pixels, int width, double zoom) {
		this.start = start;
		this.end = end;
		this.pixels = pixels;
		this.zoom = zoom;
		this.width = width;
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
			int i, j;
			for (i = start; i < end; i++)
				for (j = 0; j < width; j++)
					pixels[i * width + j] = Image.imageMatrix[(int) (j * zoom)][(int) (i * zoom)];
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
