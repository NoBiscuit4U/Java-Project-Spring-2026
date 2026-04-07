

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


class Cal_GUI {
	// the variables needed to define the window.
	private JFrame frame;
	private JPanel panel;
	private JPanel left_panel;
	private JPanel right_panel;
	
	private JTextField input;
	private JTextArea result; 
	
	private JButton button1;
	private JButton button2;
	private JButton button3;
	private JButton button4;
	private JButton button5;
	private JButton button6;
	private JButton button7;
	private JButton button8;
	private JButton button9;
	private JButton button0;
	private JButton button_A;
	private JButton button_S;
	private JButton button_M;
	private JButton button_D;
	private JButton button_E;
	
	private int width;
	private int height;
	
	private String x_variable;
	private String y_variable;
	private String operator;
	private double answer;
	private String equation_string = "";
	
	// Constructor
	public Cal_GUI(int w, int h) {
		// Defines the features of the window
		frame = new JFrame();
		panel = new JPanel(new GridLayout(1,2));
		left_panel = new JPanel(new GridLayout(5,3));
		right_panel = new JPanel(new GridLayout(2,1));
		
		input = new JTextField(10);
		result = new JTextArea("Hello.\nThis is a JTextArea!");
		
		button1 = new JButton("1");
		button2 = new JButton("2");
		button3 = new JButton("3");
		button4 = new JButton("4");
		button5 = new JButton("5");
		button6 = new JButton("6");
		button7 = new JButton("7");
		button8 = new JButton("8");
		button9 = new JButton("9");
		button0 = new JButton("0");
		button_A = new JButton("+");
		button_S = new JButton("-");
		button_M = new JButton("*");
		button_D = new JButton("/");
		button_E = new JButton("=");
		
		width = w;
		height = h;
	}
	
	// Defines the GUI, and creates the window
	public void setUpGUI() {
		Container cp = frame.getContentPane();
		frame.setSize(width, height);
		frame.setTitle("Calculator");
		
		cp.add(panel);
		panel.add(left_panel);
		panel.add(right_panel);
		
		right_panel.add(input);
		right_panel.add(result);
		
		left_panel.add(button1);
		left_panel.add(button2);
		left_panel.add(button3);
		left_panel.add(button4);
		left_panel.add(button5);
		left_panel.add(button6);
		left_panel.add(button7);
		left_panel.add(button8);
		left_panel.add(button9);
		left_panel.add(button0);
		left_panel.add(button_A);
		left_panel.add(button_S);
		left_panel.add(button_M);
		left_panel.add(button_D);
		left_panel.add(button_E);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public void preformOperation(String x_val, String y_val, String operator_sign) {
		double x = Double.parseDouble(x_val);
		double y = Double.parseDouble(y_val);
		
		if (operator_sign.equals("A")) {
			answer = x + y;
		}
		else if (operator_sign.equals("S")) {
			answer = x - y;
		}
		else if (operator_sign.equals("M")) {
			answer = x * y;
		}
		else if (operator_sign.equals("D")) {
			answer = x / y;
		}
	}
	
	// Determines what button was clicked, and does the appropriate action
	public void setUpButtonListeners() {
		// Creates an object without having to define a separate class in a different file
		ActionListener buttonListener = new ActionListener() {
			// Overriding the actionPreformed method to do what we want when an action occurs.
			@Override 
			// can change ae to what ever we want, but not the rest of this line.
			public void actionPerformed(ActionEvent ae) {
				// Finds out what what event occurred
				Object event_source = ae.getSource();

				if (event_source == button1) {
					
						
					input.setText(equation_string);
					
				}
				else if (event_source == button_A) {
					operator = "A";
					input.setText(equation_string);
					
				}
				else if (event_source == button_S) {
					operator = "S";
					input.setText(equation_string);
					
				}
				else if (event_source == button_M) {
					operator = "M";
					input.setText(equation_string);
					
				}
				else if (event_source == button_D) {
					operator = "D";
					input.setText(equation_string);
					
				}
				else if (event_source == button_E) {
					preformOperation(x_variable, y_variable, operator);
					String rlt = Double.toString(answer);
					result.setText(rlt);

					x_variable = "";
					y_variable = "";
					operator = "";
					equation_string = "";
					input.setText("");
					
				}
			}
		};
			// sets when action is being listened for.
			button1.addActionListener(buttonListener);
			button2.addActionListener(buttonListener);
			button3.addActionListener(buttonListener);
			button4.addActionListener(buttonListener);
			button5.addActionListener(buttonListener);
			button6.addActionListener(buttonListener);
			button7.addActionListener(buttonListener);
			button8.addActionListener(buttonListener);
			button9.addActionListener(buttonListener);
			button0.addActionListener(buttonListener);
			button_A.addActionListener(buttonListener);
			button_S.addActionListener(buttonListener);
			button_M.addActionListener(buttonListener);
			button_D.addActionListener(buttonListener);
			button_E.addActionListener(buttonListener);
			
		}
	
}


public class Calculator {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Cal_GUI gui = new Cal_GUI(640, 480);
		// Creates the window
		gui.setUpGUI();
		// makes begins listening for button click
		//gui.setUpButtonListeners();
	}

}
