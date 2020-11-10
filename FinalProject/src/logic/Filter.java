package logic;

import java.math.BigDecimal;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Filter {

	private static String RED_OPERATION = "";

	private static String GREEN_OPERATION = "";

	private static String BLUE_OPERATION = "";

	private static ScriptEngineManager mgr = new ScriptEngineManager();

	private static ScriptEngine engine = mgr.getEngineByName("JavaScript");

	public static void filter(int[][] image, int x, int y) {
		int red = (image[x][y] & 0xFF0000) >> 16;
		int green = (image[x][y] & 0xFF00) >> 8;
		int blue = (image[x][y] & 0xFF);
		red++;
		blue++;
		green++;
		try {
			//engine.put("r", red);
			//engine.put("g", green);
			//engine.put("b", blue);
			if (RED_OPERATION != null && !RED_OPERATION.isEmpty() && !RED_OPERATION.trim().isEmpty()) {
				red = new BigDecimal(engine.eval(RED_OPERATION).toString()).intValue();
			}

			if (GREEN_OPERATION != null && !GREEN_OPERATION.isEmpty() && !GREEN_OPERATION.trim().isEmpty()) {
				green = new BigDecimal(engine.eval(GREEN_OPERATION).toString()).intValue();
			}

			if (BLUE_OPERATION != null && !BLUE_OPERATION.isEmpty() && !BLUE_OPERATION.trim().isEmpty()) {
				blue = new BigDecimal(engine.eval(BLUE_OPERATION).toString()).intValue();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (red < 0) red *= -1;
		if (green < 0) green *= -1;
		if (blue < 0) blue *= -1;
		
		red %= 256;
		green %= 256;
		blue %= 256;

		int color;
        color = red;
        color = (color << 8) | (int) green;
        color = (color << 8) | (int) blue;
        
		image[x][y] = color;
	}

	private static void verifyOperation(String operation) throws ScriptException {
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = mgr.getEngineByName("JavaScript");
		engine.put("r", 1);
		engine.put("g", 1);
		engine.put("b", 1);
		engine.eval(operation);
	}

	public static void setOperationRed(String operation) throws ScriptException {
		verifyOperation(operation);
		RED_OPERATION = operation;
	}

	public static void setOperationGreen(String operation) throws ScriptException {
		verifyOperation(operation);
		GREEN_OPERATION = operation;
	}

	public static void setOperationBlue(String operation) throws ScriptException {
		verifyOperation(operation);
		BLUE_OPERATION = operation;
	}
	
	public static void reset () {
		RED_OPERATION = "";
		GREEN_OPERATION = "";
		BLUE_OPERATION = "";
	}
}
