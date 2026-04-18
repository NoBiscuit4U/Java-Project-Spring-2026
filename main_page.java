import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

class login_page{
	// the variables needed to define the window.
	private JFrame frame;
	private JTextField login_id;
	private JTextField login_password;	 
	private JButton login_button;
	private UserManager m_um;
	
	// Constructor
	public login_page(UserManager um) {
		m_um=um;
		// Defines the features of the window
		frame = new JFrame();
		login_id = new JTextField("User ID");
		login_password = new JTextField("Password");
		login_button = new JButton("Login");
	}

	public JPanel setUpLoginGUI() {
		JPanel loginPage = new JPanel();
		GridLayout grid = new GridLayout(2, 2);
		loginPage.setLayout(grid);
		frame.setTitle("GUI DEmo");
		loginPage.add(login_id);
		loginPage.add(login_password);
		loginPage.add(login_button);
		this.setUpButtonListeners();
		return loginPage;
		
	}

	public void setUpButtonListeners() {
		ActionListener buttonListener = new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent ae) {
				// Finds out what what event occurred
				Object event_source = ae.getSource();

				if (event_source == login_button) {
					int loginID = Integer.parseInt(login_id.getText());
					String loginPassword = login_password.getText();	
					if(m_um.checkUserLogin(loginID,loginPassword)){
						login_button.setText("true");
					}
						
					login_id.setText("");
					login_password.setText("");
						
				}
					
			}
		};
		login_button.addActionListener(buttonListener);
	}
}


public class main_page{
	private static ReadWriter m_rw=new ReadWriter("root","","rst_data");
	private static UserManager m_um=new UserManager(m_rw);
	private static ProductManager m_pm=new ProductManager(m_rw);
	
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
        	login_page lg = new login_page(m_um);
        	JPanel panel = lg.setUpLoginGUI();

        	cp.add(panel);
            frame.setSize(400, 300);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        }
        else if (whatPage == "mainPage") {
        	
        }
     }
}

