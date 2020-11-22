package logic;

import java.util.ArrayList;
import java.util.List;

public class Filter implements Runnable {
	
	public static final int MAX_DELAY = 30;
	
	public static final int MAX_FILTER_THREADS = 4;

	private Thread thread;

	public static int NUM_THREADS = 1; // Number of threads to run in parallel

	public static boolean IS_LOOP = false;

	public static boolean NEED_TO_PERFORM = false;

	private List<FilterHelperThread> threads;

	public static int delay = 1;

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
			while (true) {
				if (delay != 0)
					Thread.sleep(delay);
				else
					Thread.sleep(0, 1);

				if (Image.isReady())
					if (IS_LOOP || NEED_TO_PERFORM) {
						long start = System.nanoTime();
						divideIntoThreads();
						startThreads();
						joinThreads();
						NEED_TO_PERFORM = false;
						long end = System.nanoTime();
						long total = (end - start) / 1000000000;
						System.out.println("Total filtering time = " + total + " s.");
					}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void joinThreads() {
		threads.forEach(m -> {
			try {
				m.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
	}

	private void startThreads() {
		threads.forEach(m -> m.start());
	}

	private void divideIntoThreads() {
		int blocks = Image.getWidth() / NUM_THREADS;
		threads = new ArrayList<>();
		for (int i = 0;; i += blocks) {
			if (i + blocks >= Image.getWidth()) {
				threads.add(new FilterHelperThread(i, Image.getWidth()));
				return;
			} else
				threads.add(new FilterHelperThread(i, i + blocks));
		}
	}

}
