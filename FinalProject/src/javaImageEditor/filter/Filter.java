package javaImageEditor.filter;

import java.util.ArrayList;
import java.util.List;

import javaImageEditor.render.Image;

public class Filter implements Runnable {
	
	public static final int MAX_DELAY = 30;
	
	public static final int MAX_FILTER_THREADS = 4;

	public static int filterThreads = 1;
	
	public static int delay = 1;

	public static boolean IS_LOOP = false;

	public static boolean NEED_TO_PERFORM = false;

	private List<FilterHelperThread> threads;
	
	private Thread thread;

	/**
	 * Start helper filter thread.
	 */
	public void start() {
		thread = new Thread(this);
		thread.start();
	}

	/**
	 * Joins helper filter thread.
	 * @throws InterruptedException
	 */
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
						divideIntoThreads();
						startThreads();
						joinThreads();
						NEED_TO_PERFORM = false;				}
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
		int blocks = Image.getWidth() / filterThreads;
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
