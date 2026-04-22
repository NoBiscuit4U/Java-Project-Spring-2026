import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

class login_page{
	// the variables needed to define the window.
	private JFrame frame;
	private JTextField login_name;
	private JTextField login_password;	 
	private JButton login_button;

	
	private String loginUserNameMaster = "The admin";
	private String loginUserPasswordMaster = "SecurePassword123";
	
	// Constructor
	public login_page() {
		// Defines the features of the window
		frame = new JFrame();
		login_name = new JTextField("User Name");
		login_password = new JTextField("Password");
		login_button = new JButton("Login");
	}

	public JPanel setUpLoginGUI() {
		JPanel loginPage = new JPanel();
		GridLayout grid = new GridLayout(2, 2);
		loginPage.setLayout(grid);
		frame.setTitle("GUI DEmo");
		loginPage.add(login_name);
		loginPage.add(login_password);
		loginPage.add(login_button);
		return loginPage;
		
	}

	public void setUpButtonListeners() {s
		ActionListener buttonListener = new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent ae) {
				// Finds out what what event occurred
				Object event_source = ae.getSource();
				if (event_source == login_button) {
					String loginName = login_name.getText();
					String loginPassword = login_password.getText();	
					if (loginName.equals(loginUserNameMaster) && loginPassword.equals(loginUserPasswordMaster)){
						login_button.setText("true");
					}
						
					login_name.setText("");
					login_password.setText("");
						
				}
					
			}
		};
		login_button.addActionListener(buttonListener);
	}
}

class home_page{
	// the variables needed to define the window.
	private JFrame frame;
	private JTextField login_name;
	private JTextField login_password;	 
	private JButton login_button;

	
	private String loginUserNameMaster = "The admin";
	private String loginUserPasswordMaster = "SecurePassword123";
	
	// Constructor
	public home_page() {
		// Defines the features of the window
		frame = new JFrame();
		login_name = new JTextField("User Name");
		login_password = new JTextField("Password");
		login_button = new JButton("Login");
	}

	public JPanel setUpHomeGUI() {
		JPanel loginPage = new JPanel();
		GridLayout grid = new GridLayout(2, 2);
		loginPage.setLayout(grid);
		frame.setTitle("GUI DEmo");
		loginPage.add(login_name);
		loginPage.add(login_password);
		loginPage.add(login_button);
		return loginPage;
		
	}

	public void setUpButtonListeners() {
		ActionListener buttonListener = new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent ae) {
				// Finds out what what event occurred
				Object event_source = ae.getSource();
				if (event_source == login_button) {
					String loginName = login_name.getText();
					String loginPassword = login_password.getText();	
					if (loginName.equals(loginUserNameMaster) && loginPassword.equals(loginUserPasswordMaster)){
						login_button.setText("true");
					}
						
					login_name.setText("");
					login_password.setText("");
						
				}
					
			}
		};
		login_button.addActionListener(buttonListener);
	}
}


public class main_page{
	
	public static void main(String[] args) {
		//boolean logged_in = false;
		String whatPage = "login";
		//JPanel panel;
		
		JFrame frame = new JFrame("Menu Bar Example");
		Container cp = frame.getContentPane();
        
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        
        // 3. Create menu items and add them to the menu
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem exitItem = new JMenuItem("Exit");
        fileMenu.add(openItem);
        fileMenu.add(exitItem);
        
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);
        
        if (whatPage == "login") {
        	login_page lg = new login_page();
        	JPanel panel = lg.setUpLoginGUI();
        	cp.add(panel);
            frame.setSize(400, 300);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        }
        else if (whatPage == "homePage") {
        	
        }
     }
}

