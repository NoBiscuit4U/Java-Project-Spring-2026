import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

class login_page{
	// the variables needed to define the window.
	private JFrame frame;

	private JLabel idLabel;
	private JLabel passwordLabel;

	private JTextField login_id;
	private JTextField login_password;	 
	private JButton login_button;
	private JButton logout_button;

	private UserManager m_um;

	private JPanel m_adminPanel;

	private JPanel m_loginPanel;
	private JPanel m_logoutPanel;

	private JMenu m_menu;
	private JButton m_adminButton;
	private Container m_contain;

	private boolean loggedIn=false;
	
	// Constructor
	public login_page(UserManager um,Container contain,JMenu menu,JButton adminButton) {
		// Defines the features of the window
		frame = new JFrame();
		login_id = new JTextField(10);
		login_password = new JTextField(10);
		login_button = new JButton("Login");
		logout_button = new JButton("Logout");

		this.idLabel=new JLabel("User ID: ");
		this.passwordLabel=new JLabel("User Password: ");
		
		this.m_contain=contain;
		this.m_um=um;
		this.m_menu=menu;
		this.m_adminButton=adminButton;

		this.setUpLoginButtonListeners();
		this.setUpLogoutButtonListeners();
	}

	public void setUpGUIs() {
		m_loginPanel = new JPanel();
		GridBagConstraints constraints=null;
		GridBagLayout grid = new GridBagLayout();
		m_loginPanel.setLayout(grid);

		constraints=new GridBagConstraints();
		constraints.gridx=2;
		constraints.gridy=0;
		constraints.insets=new Insets(10,1,10,10);
		m_loginPanel.add(idLabel,constraints);

		constraints=new GridBagConstraints();
		constraints.gridx=2;
		constraints.gridy=0;
		constraints.insets=new Insets(10,10,1,10);
		m_loginPanel.add(login_id,constraints);

		constraints=new GridBagConstraints();
		constraints.gridx=2;
		constraints.gridy=1;
		constraints.insets=new Insets(10,1,10,10);
		m_loginPanel.add(passwordLabel,constraints);

		constraints=new GridBagConstraints();
		constraints.gridx=2;
		constraints.gridy=1;
		constraints.insets=new Insets(10,10,1,10);
		m_loginPanel.add(login_password,constraints);

		constraints=new GridBagConstraints();
		constraints.gridx=2;
		constraints.gridy=2;
		constraints.insets=new Insets(10,10,10,10);
		m_loginPanel.add(login_button,constraints);

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
			return m_loginPanel;
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

class admin_page{
	// the variables needed to define the window.
	private JFrame frame;	 
	private JButton admin_submit_button;
	private JTextField idField;
	private JTextField otherTextField;
	
	// Constructor
	public admin_page() {
		// Defines the features of the window
		frame = new JFrame();
		idField = new JTextField("ID Text Field");
		otherTextField = new JTextField("OtherD Text Field");
		admin_submit_button = new JButton("Submit");
	}

	public JPanel setUpAdminGUI() {
		JPanel loginPage = new JPanel();
		GridLayout grid = new GridLayout(2, 3);
		loginPage.setLayout(grid);
		frame.setTitle("GUI DEmo");
		loginPage.add(idField);
		loginPage.add(otherTextField);
		loginPage.add(admin_submit_button);
		return loginPage;
		
	}

	public void setUpAdminButtonListeners() {
		ActionListener adminButtonListener = new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent ae) {
				// Finds out what what event occurred
				Object event_source = ae.getSource();
				if (event_source == admin_submit_button) {
					
				}
					
			}
		};
		admin_submit_button.addActionListener(adminButtonListener);
	}
}

class product_page{
	// the variables needed to define the window.
	private JFrame frame;
	
	
	// Constructor
	public product_page() {
		// Defines the features of the window
		frame = new JFrame();
		
	}

	public JPanel setUpProductGUI() {
		JPanel productPage = new JPanel();
		GridLayout grid = new GridLayout(2, 2);
		productPage.setLayout(grid);
		frame.setTitle("GUI DEmo");
		
		return productPage;
		
	}
	
	public void setUpProductPageButtonListeners() {
		ActionListener cartButtonListener = new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent ae) {
				// Finds out what what event occurred
				Object event_source = ae.getSource();
		
			}
		};
		//login_button.addActionListener(buttonListener);
	}

	
	
}

class cart_page{
	// the variables needed to define the window.
	private JFrame frame;
	

	
	// Constructor
	public cart_page() {
		// Defines the features of the window
		frame = new JFrame();
		
	}

	public JPanel setUpCartGUI() {
		JPanel cartPage = new JPanel();
		GridLayout grid = new GridLayout(2, 2);
		cartPage.setLayout(grid);
		frame.setTitle("GUI DEmo");
		
		return cartPage;
		
	}
	public void setUpCartPageButtonListeners() {
		ActionListener cartButtonListener = new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent ae) {
				// Finds out what what event occurred
				Object event_source = ae.getSource();
		
			}
		};
		//login_button.addActionListener(buttonListener);
	}

}

class home_page{
	// the variables needed to define the window.
	private JFrame frame;
	private JLabel appName;
	private String userGivenAppName;
	private JTextField mainPageBlurb;
	private String userGivenMainPageBlurb;
	
	// Constructor
	public home_page() {
		// Defines the features of the window
		frame = new JFrame();
		// This is where we can have the code grab the title from the storage
		userGivenAppName = "Title";
		appName = new JLabel(userGivenAppName);
		
		userGivenMainPageBlurb = "Text Blurb";
		mainPageBlurb = new JTextField(userGivenMainPageBlurb);
	}

	public JPanel setUpHomeGUI() {
		JPanel homePage = new JPanel();
		GridLayout grid = new GridLayout(2, 2);
		homePage.setLayout(grid);
		frame.setTitle("GUI DEmo");
		homePage.add(appName);
		homePage.add(mainPageBlurb);
		
		return homePage;
		
	}

	public void setUpHomePageButtonListeners() {
		ActionListener buttonListener = new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent ae) {
				// Finds out what what event occurred
				Object event_source = ae.getSource();
		
			}
		};
		//login_button.addActionListener(buttonListener);
	}
}

public class ModularRestaurantGUI{
	private static ReadWriter m_rw=new ReadWriter("root","","rst_data");
	private static UserManager m_um=new UserManager(m_rw);
	private static ProductManager m_pm=new ProductManager(m_rw);
	public static void main(String[] args) {
		final login_page m_lg;
		final product_page m_pdpg;
		final admin_page m_adpg;
		final home_page m_hpg;
		final cart_page m_ctpg;

		JPanel m_loginPanel;
		
		JFrame frame = new JFrame("Menu Bar Example");
		Container cp = frame.getContentPane();
		
		JButton homeButton = new JButton("Home Page");
		JButton productButton = new JButton("Products");
		JButton cartButton = new JButton("Cart");
		JButton loginButton = new JButton("Login/Logout");
		JButton adminButton = new JButton("Admin");
		
		JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        
        // Declaring the home page
        m_hpg = new home_page();
    	JPanel HomePanel = m_hpg.setUpHomeGUI();
    	m_hpg.setUpHomePageButtonListeners();
    	// Initially the home panel will appear so we are doing so now. 
    	cp.add(HomePanel);
    	
    	// Declaring the cart page
        m_ctpg = new cart_page();
    	JPanel cartPanel = m_ctpg.setUpCartGUI();
    	m_ctpg.setUpCartPageButtonListeners();
    	
    	// Declaring the product page
        m_pdpg = new product_page();
    	JPanel productPanel = m_pdpg.setUpProductGUI();
    	m_pdpg.setUpProductPageButtonListeners();
    	
    	// Declaring the admin page
        m_adpg = new admin_page();
    	JPanel adminPanel = m_adpg.setUpAdminGUI();
    	m_adpg.setUpAdminButtonListeners();

		 // Declaring the login page
        m_lg = new login_page(m_um,cp,fileMenu,adminButton);
		m_lg.setUpGUIs();
		
    	// This button listen controls which page is open.
		ActionListener buttonListener = new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent ae) {
				// Finds out what what event occurred
				Object event_source = ae.getSource();
				if (event_source == loginButton) {
					cp.removeAll();
					cp.add(m_lg.getCurrentPanel());
					cp.revalidate();
					cp.repaint();
						
				}
				
				else if (event_source == homeButton) {
					cp.removeAll();
					cp.add(HomePanel);
					cp.revalidate();
					cp.repaint();
				}
				
				else if (event_source == adminButton && m_um.checkAdmin()) {
					cp.removeAll();
					cp.add(adminPanel);
					cp.revalidate();
					cp.repaint();
				}
				
				else if (event_source == productButton) {
					cp.removeAll();
					cp.add(productPanel);
					cp.revalidate();
					cp.repaint();
				}
				
				else if (event_source == cartButton) {
					cp.removeAll();
					cp.add(cartPanel);
					cp.revalidate();
					cp.repaint();
				}
			}
		};
		
		homeButton.addActionListener(buttonListener);
		productButton.addActionListener(buttonListener);
		cartButton.addActionListener(buttonListener);
		loginButton.addActionListener(buttonListener);
		adminButton.addActionListener(buttonListener);
        
        fileMenu.add(homeButton);
        fileMenu.add(productButton);
        fileMenu.add(cartButton);
        fileMenu.add(loginButton);
        
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);
        	
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        	
        
     }
}