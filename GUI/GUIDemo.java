package GUI;


import javax.swing.JFrame;
import javax.swing.JButton;

// Imports code needed to "listen" for an event such as a button click. This allows user interaction.
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

// Demonstrates the basic layout of a class that sets up the GUI with some basic functions.
public class GUIDemo {
	// the variables needed to define the window.
	private JFrame frame;
	private JButton button;
	private int width;
	private int height;
	
	// Constructor
	public GUIDemo(int w, int h) {
		// Defines the features of the window
		frame = new JFrame();
		button = new JButton("CLICK ME");		
		width = w;
		height = h;
	}
	
	// Defines the GUI, and creates the window
	public void setUpGUI() {
		// Defines the window
		frame.setSize(width, height);
		frame.setTitle("GUI DEmo");
		// this add method allows us to add a feature such as a button to the window. 
		frame.add(button);
		// This means that the program stops when we close the window. 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// This sends the above code to the window so we can see it.
		frame.setVisible(true);
	}
	
	public void setUpButtonListeners() {
		// Creates an object without having to define a separate class in a different file
		ActionListener buttonListener = new ActionListener() {
			// Overriding the actionPreformed method to do what we want when an action occurs.
			@Override 
			// can change ae to what ever we want, but not the rest of this line.
			public void actionPerformed(ActionEvent ae) {
				System.out.println("Button clicked!");
			}
		};
		// sets when action is being listened for.
		button.addActionListener(buttonListener);
	}
	
}
