


import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;

// Imports the classes needed for organization
import java.awt.BorderLayout;
import java.awt.Container;

// Imports code needed to "listen" for an event such as a button click. This allows user interaction.
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

// Demonstrates the basic layout of a class that sets up the GUI with some basic functions.
//This GUIDemo2 improves look by organizing the window using a flow layout.
// A flow layout arranges the elements from left to right, then shifts any extra down to the next row.
public class GUIDemo4 {
	// the variables needed to define the window.
	private JFrame frame;
	private JTextField input;
	private JTextArea ta;
	private JLabel label; 
	private JButton button1;
	private JButton button2;
	private JButton button3;
	private JButton button4;
	private int width;
	private int height;
	
	// Constructor
	public GUIDemo4(int w, int h) {
		// Defines the features of the window
		frame = new JFrame();
		label = new JLabel("Hello");
		// can optionally put in a width of the text field (in columns) in the parethasisies (which I did)
		input = new JTextField(10);
		ta = new JTextArea("Hello.\nThis is a JTextArea!");
		button1 = new JButton("CLICK ME");
		button2 = new JButton("CLICK ME TOO");
		button3 = new JButton("3");
		button4 = new JButton("4");
		width = w;
		height = h;
	}
	
	// Defines the GUI, and creates the window
	public void setUpGUI() {
		// Defines the window
		
		// accesses the innate frame of the window
		Container cp = frame.getContentPane();
		BorderLayout brdr = new BorderLayout();
		cp.setLayout(brdr);
		frame.setSize(width, height);
		frame.setTitle("GUI DEmo");
		// Adds the elements to the container not the frame this time.
		cp.add(input, BorderLayout.NORTH);
		cp.add(label, BorderLayout.SOUTH);
		cp.add(button1, BorderLayout.WEST);
		//cp.add(button2, BorderLayout.EAST);
		cp.add(ta, BorderLayout.CENTER);
		//cp.add(button3);
		//cp.add(button4);
		// This means that the program stops when we close the window. 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// This sends the above code to the window so we can see it.
		frame.setVisible(true);
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
					String s = input.getText();
					label.setText(s);
					// Clears the text field by passing an empty string
					input.setText("");
				}
				else if (event_source == button2) {
					String val = input.getText();
					// Sets val to a double
					double n = Double.parseDouble(val);
					n = n*2;
					String result = Double.toString(n);
					label.setText(result);
					// Clears the text field by passing an empty string
					input.setText("");
				}
				else if (event_source == button3) {
					System.out.println("Button3 clicked!");
				}
				else if (event_source == button4) {
					System.out.println("Button4 clicked!");
				}
			}
		};
		// sets when action is being listened for.
		button1.addActionListener(buttonListener);
		button2.addActionListener(buttonListener);
		button3.addActionListener(buttonListener);
		button4.addActionListener(buttonListener);
		
	}
	
}
