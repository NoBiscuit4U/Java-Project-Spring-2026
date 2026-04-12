package GUI;

import java.util.Scanner;

public class GUITester {
	
	public static void main(String[] args) {
		int which_version;
		Scanner scnr = new Scanner(System.in);
		which_version = 6;
		
		// this version has one button
		if (which_version == 1) {
			GUIDemo gd = new GUIDemo(640, 480);
			// Creates the window
			gd.setUpGUI();
			// makes begins listening for button click
			gd.setUpButtonListeners();
		}
		
		// multiple buttons and the first layout design
		else if (which_version == 2) {
			// For the second GUIDemo
			GUIDemo2 gd2 = new GUIDemo2(640, 480);
			// Creates the window
			gd2.setUpGUI();
			// makes begins listening for button click
			gd2.setUpButtonListeners();
			gd2.setUpButtonListenersMultiButton();
		}
		
		// multiple buttons and a text field + labels
		else if (which_version == 3) {
			// For the second GUIDemo
			GUIDemo3 gd3 = new GUIDemo3(640, 480);
			// Creates the window
			gd3.setUpGUI();
			// makes begins listening for button click
			gd3.setUpButtonListeners();
		}
		
		// Java BorderLayout
		else if (which_version == 4) {
			// For the second GUIDemo
			GUIDemo4 gd4 = new GUIDemo4(640, 480);
			// Creates the window
			gd4.setUpGUI();
			// makes begins listening for button click
			gd4.setUpButtonListeners();
		}
		
		// Grid layout
				else if (which_version == 5) {
					// For the second GUIDemo
					GUIDemo5 gd5 = new GUIDemo5(640, 480);
					// Creates the window
					gd5.setUpGUI();
					// makes begins listening for button click
					gd5.setUpButtonListeners();
				}
		
		// JPanels
				else if (which_version == 6) {
					// For the second GUIDemo
					GUIDemo6 gd6 = new GUIDemo6(640, 480);
					// Creates the window
					gd6.setUpGUI();
					// makes begins listening for button click
					gd6.setUpButtonListeners();
				}
		
		else {
			System.out.println("Not a valid option");
		}
		
	}

}
