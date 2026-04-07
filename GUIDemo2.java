

// Basic frame
import javax.swing.JFrame;
// Button
import javax.swing.JButton;
// Allows for the creation of labels + text fields
import javax.swing.JLabel;
import javax.swing.JTextField;

// Imports the classes needed for organization
import java.awt.FlowLayout;
import java.awt.Container;

// Imports code needed to "listen" for an event such as a button click. This allows user interaction.
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

// Demonstrates the basic layout of a class that sets up the GUI with some basic functions.
//This GUIDemo2 improves look by organizing the window using a flow layout.
// A flow layout aranges the elements from left to right, then shifts any extra down to the next row.
public class GUIDemo2 {
	// the variables needed to define the window.
	private JFrame frame;
	private JTextField input;
	private JLabel label;
	private JButton button1;
	private JButton button2;
	private JButton button3;
	private JButton button4;
	private int width;
	private int height;
	
	// Constructor
	public GUIDemo2(int w, int h) {
		// Defines the features of the window
		frame = new JFrame();
		label = new JLabel("Hello");
		// can optionally put in a width of the text field (in columns) in the parethasisies (which I did)
		input = new JTextField(10);
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
		
		// accesses the inate frame of the window
		Container cp = frame.getContentPane();
		FlowLayout flow = new FlowLayout();
		cp.setLayout(flow);
		frame.setSize(width, height);
		frame.setTitle("GUI DEmo");
		// Adds the elements to the container not the frame this time.
		cp.add(input);
		cp.add(label);
		cp.add(button1);
		cp.add(button2);
		cp.add(button3);
		cp.add(button4);
		// This means that the program stops when we close the window. 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// This sends the above code to the window so we can see it.
		frame.setVisible(true);
	}
	
	public void setUpButtonListeners() {
		// Creates an object without having to define a separate class in a different file
		ActionListener buttonListener1 = new ActionListener() {
			// Overriding the actionPreformed method to do what we want when an action occurs.
			@Override 
			// can change ae to what ever we want, but not the rest of this line.
			public void actionPerformed(ActionEvent ae) {
				System.out.println("Button1 clicked!");
			}
		};
		ActionListener buttonListener2 = new ActionListener() {
			// Overriding the actionPreformed method to do what we want when an action occurs.
			@Override 
			// can change ae to what ever we want, but not the rest of this line.
			public void actionPerformed(ActionEvent ae) {
				System.out.println("Button2 clicked!");
			}
		};
		// sets when action is being listened for.
		button1.addActionListener(buttonListener1);
		button2.addActionListener(buttonListener2);
		
	}
	
	// this version of setUpButtonListeners allows for only one buttonListener used across multiple buttons.
	public void setUpButtonListenersMultiButton() {
		// Creates an object without having to define a separate class in a different file
		ActionListener buttonListener = new ActionListener() {
			// Overriding the actionPreformed method to do what we want when an action occurs.
			@Override 
			// can change ae to what ever we want, but not the rest of this line.
			public void actionPerformed(ActionEvent ae) {
				// Finds out what what event occurred
				Object o = ae.getSource();
				if (ae.getSource() == button3) {
					System.out.println("Button3 clicked!");
				}
				// alternatly use the object
				else if (o == button4) {
					System.out.println("Button4 clicked!");
				}
			}
		};
		// sets when action is being listened for.
		button3.addActionListener(buttonListener);
		button4.addActionListener(buttonListener);
		
	}
	
}
