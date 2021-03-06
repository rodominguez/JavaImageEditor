package javaImageEditor.filter;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import javaImageEditor.render.Image;

public class FilterHelperThread implements Runnable {

	private Thread thread;

	private int start, end;

	private ScriptEngineManager mgr;

	private ScriptEngine engine;

	private int[] color;

	public FilterHelperThread(int start, int end) {
		this.start = start;
		this.end = end;
		color = new int[3];
		mgr = new ScriptEngineManager();
		engine = mgr.getEngineByName("JavaScript");
		engine.put("o", this);
	}

	/**
	 * Starts helper render thread.
	 */
	public void start() {
		thread = new Thread(this);
		thread.start();
	}

	/**
	 * Joins helper render thread.
	 * @throws InterruptedException
	 */
	public void join() throws InterruptedException {
		thread.join();
	}

	@Override
	public void run() {
		try {
			for (int i = start; i < end; i++) {
				for (int j = 0; j < Image.getHeight(); j++) {
					assignColor(color, Image.imageMatrix[i][j]);
					InterpretFilter.filter(Image.imageMatrix, i, j, color[0], color[1], color[2], engine);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int find1() {
		return color[0];
	}
	
	public int find2() {
		return color[1];
	}
	
	public int find3() {
		return color[2];
	}
	
	public void test(String operation) throws ScriptException {
		engine.eval(operation);
	}
	
	private static void assignColor (int[] color, int rgb) {
		color[0] = (rgb & 0xFF0000) >> 16;
		color[1] = (rgb & 0xFF00) >> 8;
		color[2] = (rgb & 0xFF);
	}

}
