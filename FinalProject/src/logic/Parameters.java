package logic;

import java.awt.Color;

import javax.script.ScriptException;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

public class Parameters {

	private JTextField red, green, blue;
	private JLabel redError, greenError, blueError;

	public void addParametersToFrame(JFrame frame) {
		setDelay(frame);
		setThreads(frame);
		setUniThread(frame);
		setColorModifierRed(frame);
		setColorModifierGreen(frame);
		setColorModifierBlue(frame);
		setReset(frame);
	}

	private void setDelay(JFrame frame) {
		SpinnerModel model = new SpinnerNumberModel(1, 0, 30, 1);
		JSpinner delay = new JSpinner(model);
		delay.setBounds(10, 30, 95, 30);
		delay.addChangeListener(e -> Modifier.delay = (int) delay.getValue());

		JLabel l = new JLabel("Modify delay: ");
		l.setLabelFor(delay);
		l.setBounds(10, 5, 85, 30);

		frame.add(l);
		frame.add(delay);
	}

	private void setThreads(JFrame frame) {
		SpinnerModel model = new SpinnerNumberModel(1, 1, 5, 1);
		JSpinner spinner = new JSpinner(model);
		spinner.setBounds(10, 85, 95, 30);
		spinner.addChangeListener(e -> Modifier.NUM_THREADS = (int) spinner.getValue());

		JLabel l = new JLabel("Modify threds: ");
		l.setLabelFor(spinner);
		l.setBounds(10, 60, 85, 30);

		frame.add(l);
		frame.add(spinner);
	}

	private void setUniThread(JFrame frame) {
		JCheckBox uniThread = new JCheckBox("All in one thread");
		uniThread.setBounds(10, 120, 130, 30);
		uniThread.addActionListener(event -> Image.IS_UNI_THREAD = !Image.IS_UNI_THREAD);

		frame.add(uniThread);
	}

	private void setColorModifierRed(JFrame frame) {
		JLabel title = new JLabel("Red Modifier");
		title.setBounds(10, 150, 200, 30);

		red = new JTextField();
		red.setBounds(10, 180, 200, 30);

		redError = new JLabel("Invalid Operation!");
		redError.setBounds(10, 210, 200, 30);
		redError.setForeground(Color.RED);
		redError.setVisible(false);

		frame.add(title);
		frame.add(red);
		frame.add(redError);
	}

	private void setColorModifierGreen(JFrame frame) {
		JLabel title = new JLabel("Green Modifier");
		title.setBounds(10, 250, 200, 30);

		green = new JTextField();
		green.setBounds(10, 280, 200, 30);

		greenError = new JLabel("Invalid Operation!");
		greenError.setBounds(10, 310, 200, 30);
		greenError.setForeground(Color.RED);
		greenError.setVisible(false);

		frame.add(title);
		frame.add(green);
		frame.add(greenError);
	}

	private void setColorModifierBlue(JFrame frame) {
		JLabel title = new JLabel("Green Modifier");
		title.setBounds(10, 350, 200, 30);

		blue = new JTextField();
		blue.setBounds(10, 380, 200, 30);

		blueError = new JLabel("Invalid Operation!");
		blueError.setBounds(10, 410, 200, 30);
		blueError.setForeground(Color.RED);
		blueError.setVisible(false);

		frame.add(title);
		frame.add(blue);
		frame.add(blueError);
	}

	private void setReset(JFrame frame) {
		JButton reset = new JButton("Reset");
		reset.setBounds(10, 450, 100, 30);
		reset.addActionListener(event -> {
			red.setText("");
			blue.setText("");
			green.setText("");
			
			redError.setVisible(false);
			greenError.setVisible(false);
			blueError.setVisible(false);
			
			Filter.reset();
			Image.reset();
		});

		JButton perform = new JButton("Perform");
		perform.setBounds(130, 450, 110, 30);
		perform.addActionListener(event -> {
			try {
				Filter.setOperationRed(red.getText());
				redError.setVisible(false);
			} catch (ScriptException e) {
				redError.setVisible(true);
			}
			try {
				Filter.setOperationGreen(green.getText());
				greenError.setVisible(false);
			} catch (ScriptException e) {
				greenError.setVisible(true);
			}
			try {
				Filter.setOperationBlue(blue.getText());
				blueError.setVisible(false);
			} catch (ScriptException e) {
				blueError.setVisible(true);
			}
		});

		frame.add(reset);
		frame.add(perform);
	}
}
