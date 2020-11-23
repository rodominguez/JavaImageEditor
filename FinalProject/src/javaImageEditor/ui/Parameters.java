package javaImageEditor.ui;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import javax.activation.MimetypesFileTypeMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import javaImageEditor.export.Exporter;
import javaImageEditor.filter.Filter;
import javaImageEditor.filter.InterpretFilter;
import javaImageEditor.render.Image;
import javaImageEditor.render.Renderer;

public class Parameters {

	private JTextField red, green, blue;
	private JLabel redError, greenError, blueError;

	/**
	 * Adds every input to the parameters JFrame.
	 * @param frame
	 */
	public void addParametersToFrame(JFrame frame) {
		setUploadFile(frame);
		setDelay(frame);
		setFilterThreads(frame);
		setRenderThreads(frame);
		setExpressionAdditive(frame);
		setColorModifierRed(frame);
		setColorModifierGreen(frame);
		setColorModifierBlue(frame);
		setReset(frame);
		setExport(frame);
	}

	private void setDelay(JFrame frame) {
		SpinnerModel model = new SpinnerNumberModel(1, 1, Filter.MAX_DELAY, 1);
		JSpinner delay = new JSpinner(model);
		delay.setBounds(10, 30, 110, 30);
		delay.addChangeListener(e -> Filter.delay = (int) delay.getValue());

		JLabel l = new JLabel("Modify delay (ms): ");
		l.setLabelFor(delay);
		l.setBounds(10, 5, 150, 30);

		frame.add(l);
		frame.add(delay);
	}

	private void setFilterThreads(JFrame frame) {
		SpinnerModel model = new SpinnerNumberModel(1, 1, Filter.MAX_FILTER_THREADS, 1);
		JSpinner spinner = new JSpinner(model);
		spinner.setBounds(10, 85, 110, 30);
		spinner.addChangeListener(e -> Filter.filterThreads = (int) spinner.getValue());

		JLabel l = new JLabel("Filter threads: ");
		l.setLabelFor(spinner);
		l.setBounds(10, 60, 110, 30);

		frame.add(l);
		frame.add(spinner);
	}
	
	private void setRenderThreads(JFrame frame) {
		SpinnerModel model = new SpinnerNumberModel(1, 1, Renderer.MAX_RENDER_THREADS, 1);
		JSpinner spinner = new JSpinner(model);
		spinner.setBounds(160, 85, 120, 30);
		spinner.addChangeListener(e -> Renderer.renderThreads = (int) spinner.getValue());

		JLabel l = new JLabel("Render threads: ");
		l.setLabelFor(spinner);
		l.setBounds(160, 60, 110, 30);

		frame.add(l);
		frame.add(spinner);
	}
	
	private void setExpressionAdditive (JFrame frame) {
		JCheckBox expression = new JCheckBox("Expression");
		expression.setBounds(10, 115, 110, 20);
		expression.addActionListener(event -> InterpretFilter.IS_EXPRESSION = !InterpretFilter.IS_EXPRESSION);
		
		JCheckBox additive = new JCheckBox("Additive");
		additive.setBounds(10, 135, 110, 20);
		additive.addActionListener(event -> InterpretFilter.IS_ADDITIVE = !InterpretFilter.IS_ADDITIVE);
		
		frame.add(expression);
		frame.add(additive);
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
		JLabel title = new JLabel("Blue Modifier");
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
			
			InterpretFilter.reset();
			Image.reset();
		});

		JButton perform = new JButton("Perform");
		perform.setBounds(130, 450, 110, 30);
		perform.addActionListener(event -> {
			boolean isValid = true;
			try {
				InterpretFilter.setOperationRed(red.getText());
				redError.setVisible(false);
			} catch (Exception e) {
				redError.setVisible(true);
				isValid = false;
			}
			try {
				InterpretFilter.setOperationGreen(green.getText());
				greenError.setVisible(false);
			} catch (Exception e) {
				greenError.setVisible(true);
				isValid = false;
			}
			try {
				InterpretFilter.setOperationBlue(blue.getText());
				blueError.setVisible(false);
			} catch (Exception e) {
				blueError.setVisible(true);
				isValid = false;
			}
			if (isValid)
				Filter.NEED_TO_PERFORM = true;
		});
		
		JCheckBox loop = new JCheckBox("Loop");
		loop.setBounds(240, 450, 110, 30);
		loop.addActionListener(event -> Filter.IS_LOOP = !Filter.IS_LOOP);

		frame.add(loop);
		frame.add(reset);
		frame.add(perform);
	}
	
	private void setUploadFile (JFrame frame) {
		JFileChooser file = new JFileChooser();
		file.setBounds(300, 10, 400, 400);
		file.setFileSelectionMode(JFileChooser.FILES_ONLY);
		file.addActionListener(event -> {
			if (file.getSelectedFile().isFile()) {
				File f = file.getSelectedFile();
				String mimetype= new MimetypesFileTypeMap().getContentType(f);
		        String type = mimetype.split("/")[0];
		        if(type.equals("image")) {
		        	InterpretFilter.reset();
		        	Image.fillImage(file.getSelectedFile());
		        }
			}
		});
		
		frame.add(file);
	}
	
	private void setExport(JFrame frame) {
		JTextField fileName = new JTextField();
		fileName.setBounds(10, 490, 200, 30);
		
		JButton export = new JButton("Export");
		export.setBounds(220, 490, 100, 30);
		export.addActionListener(event -> {
			try {
				Exporter.export(fileName.getText());
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		
		frame.add(fileName);
		frame.add(export);
	}
	
}
