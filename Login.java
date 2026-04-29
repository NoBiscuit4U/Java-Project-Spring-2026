import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;

class Login extends JPanel{
	// the variables needed to define the window.
	private JLabel idLabel;
	private JLabel passwordLabel;

	private JTextField login_id;
	private JTextField login_password;	 
	private JButton login_button;
	private JButton logout_button;

	private UserManager m_um;

	private JPanel m_logoutPanel;

	private JMenu m_menu;
	private JButton m_adminButton;
	private Container m_contain;

	private boolean loggedIn=false;
	
	// Constructor
	public Login(UserManager um) {
		// Defines the features of the window
		login_id = new JTextField(10);
		login_password = new JTextField(10);
		login_button = new JButton("Login");
		logout_button = new JButton("Logout");

		this.idLabel=new JLabel("User ID: ");
		this.passwordLabel=new JLabel("User Password: ");
		
		//this.m_contain=contain;
		this.m_um=um;
		//this.m_menu=menu;
		//this.m_adminButton=adminButton;

		this.setUpLoginButtonListeners();
		this.setUpLogoutButtonListeners();
	}

	public void setUpGUIs() {
		GridBagConstraints constraints=null;
		GridBagLayout grid = new GridBagLayout();
		this.setLayout(grid);

		constraints=new GridBagConstraints();
		constraints.gridx=2;
		constraints.gridy=0;
		constraints.insets=new Insets(10,1,10,10);
	    this.add(idLabel,constraints);

		constraints=new GridBagConstraints();
		constraints.gridx=2;
		constraints.gridy=0;
		constraints.insets=new Insets(10,10,1,10);
		this.add(login_id,constraints);

		constraints=new GridBagConstraints();
		constraints.gridx=2;
		constraints.gridy=1;
		constraints.insets=new Insets(10,1,10,10);
		this.add(passwordLabel,constraints);

		constraints=new GridBagConstraints();
		constraints.gridx=2;
		constraints.gridy=1;
		constraints.insets=new Insets(10,10,1,10);
		this.add(login_password,constraints);

		constraints=new GridBagConstraints();
		constraints.gridx=2;
		constraints.gridy=2;
		constraints.insets=new Insets(10,10,10,10);
		this.add(login_button,constraints);

		m_logoutPanel = new JPanel();
		grid=new GridBagLayout();
		m_logoutPanel.setLayout(grid);

		constraints=new GridBagConstraints();
		constraints.gridx=0;
		constraints.gridy=0;
		constraints.insets=new Insets(10,10,10,10);
		m_logoutPanel.add(logout_button,constraints);
	}

	public JPanel getCurrentPanel(){
		if(!loggedIn){
			return this;
		}else{
			return m_logoutPanel;
		}
	}

	public void setUpLoginButtonListeners() {
		ActionListener loginButtonListener = new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent ae) {
				// Finds out what what event occurred
				Object event_source = ae.getSource();
				if (event_source == login_button) {
					int loginID = Integer.parseInt(login_id.getText());
					String loginPassword = login_password.getText();	
					if(m_um.checkUserLogin(loginID,loginPassword)){
						if(m_um.checkAdmin()){
							m_menu.add(m_adminButton);
						}
						
						loggedIn=true;

						m_contain.removeAll();
						m_contain.add(getCurrentPanel());
						m_contain.revalidate();
						m_contain.repaint();

						login_id.setText("");
						login_password.setText("");
					}	
				}	
			}
		};
		login_button.addActionListener(loginButtonListener);
	}

	public void setUpLogoutButtonListeners() {
		ActionListener logoutButtonListener = new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent ae) {
				// Finds out what what event occurred
				Object event_source = ae.getSource();
				if (event_source == logout_button) {
					loggedIn=false;

					m_menu.remove(m_adminButton);

					m_contain.removeAll();
					m_contain.add(getCurrentPanel());
					m_contain.revalidate();
					m_contain.repaint();

				}	
			}
		};
		logout_button.addActionListener(logoutButtonListener);
	}
}
