package logic;

import java.math.BigDecimal;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

public class Filter {

	private static String RED_OPERATION = "";

	private static String GREEN_OPERATION = "";

	private static String BLUE_OPERATION = "";

	public static boolean IS_ADDITIVE = false;

	public static boolean IS_EXPRESSION = false;

	private static Double RED_VALUE = null;

	private static Double GREEN_VALUE = null;

	private static Double BLUE_VALUE = null;

	public static void filter(int[][] image, int x, int y, int red, int green, int blue, ScriptEngine engine) {
		try {
			if (RED_OPERATION != null && !RED_OPERATION.isEmpty() && !RED_OPERATION.trim().isEmpty() && IS_EXPRESSION) {
				if (!IS_ADDITIVE)
					red = new BigDecimal(engine.eval(RED_OPERATION).toString()).intValue();
				else
					red += new BigDecimal(engine.eval(RED_OPERATION).toString()).intValue();
			} else if (!IS_EXPRESSION && RED_VALUE != null) {
				if (!IS_ADDITIVE)
					red = RED_VALUE.intValue();
				else
					red += RED_VALUE.intValue();
			}

			if (GREEN_OPERATION != null && !GREEN_OPERATION.isEmpty() && !GREEN_OPERATION.trim().isEmpty()
					&& IS_EXPRESSION) {
				if (!IS_ADDITIVE)
					green = new BigDecimal(engine.eval(GREEN_OPERATION).toString()).intValue();
				else
					green += new BigDecimal(engine.eval(GREEN_OPERATION).toString()).intValue();
			} else if (!IS_EXPRESSION && GREEN_VALUE != null) {
				if (!IS_ADDITIVE)
					green = GREEN_VALUE.intValue();
				else
					green += GREEN_VALUE.intValue();
			}

			if (BLUE_OPERATION != null && !BLUE_OPERATION.isEmpty() && !BLUE_OPERATION.trim().isEmpty()
					&& IS_EXPRESSION) {
				if (!IS_ADDITIVE)
					blue = new BigDecimal(engine.eval(BLUE_OPERATION).toString()).intValue();
				else
					blue += new BigDecimal(engine.eval(BLUE_OPERATION).toString()).intValue();
			} else if (!IS_EXPRESSION && BLUE_VALUE != null) {
				if (!IS_ADDITIVE)
					blue = BLUE_VALUE.intValue();
				else
					blue += BLUE_VALUE.intValue();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (red < 0)
			red *= -1;
		if (green < 0)
			green *= -1;
		if (blue < 0)
			blue *= -1;

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
		FilterThread test = new FilterThread(0, 0);
		test.test(operation);
	}

	public static void setOperationRed(String operation) throws Exception {
		if (operation == null || operation.isEmpty() || operation.trim().isEmpty()) {
			RED_OPERATION = operation;
			RED_VALUE = null;
			return;
		}
		if (IS_EXPRESSION) {
			operation = operation.replaceAll("r", "o.find1()");
			operation = operation.replaceAll("g", "o.find2()");
			operation = operation.replaceAll("b", "o.find3()");
			verifyOperation(operation);
			RED_OPERATION = operation;
		} else
			try {
				RED_VALUE = Double.parseDouble(operation);
			} catch (Exception e) {
				throw new Exception();
			}

	}

	public static void setOperationGreen(String operation) throws Exception {
		if (operation == null || operation.isEmpty() || operation.trim().isEmpty()) {
			GREEN_OPERATION = operation;
			GREEN_VALUE = null;
			return;
		}
		if (IS_EXPRESSION) {
			operation = operation.replaceAll("r", "o.find1()");
			operation = operation.replaceAll("g", "o.find2()");
			operation = operation.replaceAll("b", "o.find3()");
			verifyOperation(operation);
			GREEN_OPERATION = operation;
		} else
			try {
				GREEN_VALUE = Double.parseDouble(operation);
			} catch (Exception e) {
				throw new Exception();
			}
	}

	public static void setOperationBlue(String operation) throws Exception {
		if (operation == null || operation.isEmpty() || operation.trim().isEmpty()) {
			BLUE_OPERATION = operation;
			BLUE_VALUE = null;
			return;
		}
		if (IS_EXPRESSION) {
			operation = operation.replaceAll("r", "o.find1()");
			operation = operation.replaceAll("g", "o.find2()");
			operation = operation.replaceAll("b", "o.find3()");
			verifyOperation(operation);
			BLUE_OPERATION = operation;
		} else
			try {
				BLUE_VALUE = Double.parseDouble(operation);
			} catch (Exception e) {
				throw new Exception();
			}
	}

	public static void reset() {
		RED_VALUE = null;
		GREEN_VALUE = null;
		BLUE_VALUE = null;
		
		RED_OPERATION = "";
		GREEN_OPERATION = "";
		BLUE_OPERATION = "";
	}
}
