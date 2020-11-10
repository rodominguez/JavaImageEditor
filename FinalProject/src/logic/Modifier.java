package logic;

import java.util.ArrayList;
import java.util.List;

public class Modifier implements Runnable {

	private Thread thread;

	public static int NUM_THREADS = 1; // Number of threads to run in parallel

	private List<FilterThread> threads;

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

				if (!Image.IS_UNI_THREAD) {
					divideIntoThreads();
					startThreads();
					joinThreads();
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
				threads.add(new FilterThread(i, Image.getWidth()));
				return;
			} else
				threads.add(new FilterThread(i, i + blocks));
		}
	}

}
