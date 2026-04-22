package GUI_Test;

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

	
	private String loginUserNameMaster = "1";  //"The admin";
	private String loginUserPasswordMaster = "1"; //"SecurePassword123";
	
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

	public void setUpLoginButtonListeners() {
		ActionListener loginButtonListener = new ActionListener() {
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
		login_button.addActionListener(loginButtonListener);
	}
}

class admin_page extends JPanel{
	// the variables needed to define the window.
	private JFrame frame;	 
	private JButton admin_submit_button;
	private JTextField idField;
	private JTextField imageLinkField;
	private JTextField nameField;
	private JTextField costField;
	private JTextField nutritionalValField;
	private GridBagConstraints gbc = new GridBagConstraints();
	private JComboBox<String> selectOptions;
	
	// Constructor
	public admin_page() {
		// Defines the features of the window
		frame = new JFrame();
		setLayout(new GridBagLayout());
		idField = new JTextField("ID Text Field");
		imageLinkField = new JTextField("Image Link");
		nutritionalValField = new JTextField("Nutritional Value");
		nameField = new JTextField("Name");
		costField = new JTextField("Price/Cost");
		admin_submit_button = new JButton("Submit");
		
		String[] options = {"Add", "Update", "Delete"};
        selectOptions = new JComboBox<>(options);
	}

	public JPanel setUpAdminGUI() {
		JPanel adminPage = new JPanel(new GridBagLayout());
		frame.setTitle("GUI DEmo");
		
		gbc.insets = new Insets(5,5,5,5);
		
		gbc.gridx = 3;
		gbc.gridy = 0;
		adminPage.add(selectOptions, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		adminPage.add(idField, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		adminPage.add(nameField, gbc);
		
		gbc.gridx = 2;
		gbc.gridy = 1;
		adminPage.add(costField, gbc);
		
		gbc.gridx = 3;
		gbc.gridy = 1;
		adminPage.add(nutritionalValField, gbc);
		
		gbc.gridx = 4;
		gbc.gridy = 1;
		adminPage.add(imageLinkField, gbc);
		
		gbc.gridx = 3;
		gbc.gridy = 2;
		adminPage.add(admin_submit_button, gbc);
		
		return adminPage;
		
	}

	public void setUpAdminButtonListeners() {
		ActionListener adminButtonListener = new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent ae) {
				// Finds out what what event occurred
				Object event_source = ae.getSource();
				if (event_source == admin_submit_button) {
					
				}
				else if (event_source == selectOptions) {
					if (selectOptions.equals("Add")) {
						
					}
					else if (selectOptions.equals("Update")) {
						
					}
					else if (selectOptions.equals("Delete")) {
						
					}
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

class home_page extends JPanel{
	// the variables needed to define the window.
	private JFrame frame;
	private JLabel appName;
	private String userGivenAppName;
	private JTextField mainPageBlurb;
	private String userGivenMainPageBlurb;
	private JButton searchButton;
	private JButton goToProductsButton;
	private GridBagConstraints gbc = new GridBagConstraints();
	private JTextArea searchProducts;
	private JScrollPane scrollPane;
	
	// Constructor
	public home_page() {
		// Defines the features of the window
		frame = new JFrame();
		setLayout(new GridBagLayout());
		
		searchProducts = new JTextArea(10, 15);
		scrollPane = new JScrollPane(searchProducts);
		searchProducts.setEditable(false);
		
		// This is where we can have the code grab the title from the storage
		userGivenAppName = "Title";
		appName = new JLabel(userGivenAppName);
		
		userGivenMainPageBlurb = "Text Blurb";
		mainPageBlurb = new JTextField(userGivenMainPageBlurb);
		
		searchButton = new JButton("Search");
		goToProductsButton = new JButton("Search All Products");
		
	}

	public JPanel setUpHomeGUI() {
		JPanel homePage = new JPanel(new GridBagLayout());
		frame.setTitle("GUI Demo");
		
		gbc.insets = new Insets(5,5,5,5);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 3;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		homePage.add(appName, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 3;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		homePage.add(mainPageBlurb, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		homePage.add(goToProductsButton, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 2;
		homePage.add(searchButton, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 3;
		homePage.add(scrollPane, gbc);
		
		return homePage;
		
	}

	public void setUpHomePageButtonListeners() {
		ActionListener buttonListener = new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent ae) {
				// Finds out what what event occurred
				Object event_source = ae.getSource();
				if (event_source == searchButton) {
					
				}
			}
		};
		searchButton.addActionListener(buttonListener);
	}
}

public class ModularRestaurantGUI{
	
	public static void main(String[] args) {
	
		boolean logged_in = true;
		boolean is_admin = false;

		//JPanel panel;
		
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
        home_page hpg = new home_page();
    	JPanel HomePanel = hpg.setUpHomeGUI();
    	hpg.setUpHomePageButtonListeners();
    	// Initially the home panel will appear so we are doing so now. 
    	cp.add(HomePanel);
        
        // Declaring the login page
        login_page lg = new login_page();
    	JPanel LoginPanel = lg.setUpLoginGUI();
    	lg.setUpLoginButtonListeners();
    	
    	// Declaring the cart page
        cart_page ctpg = new cart_page();
    	JPanel cartPanel = ctpg.setUpCartGUI();
    	ctpg.setUpCartPageButtonListeners();
    	
    	// Declaring the product page
        product_page pdpg = new product_page();
    	JPanel productPanel = pdpg.setUpProductGUI();
    	pdpg.setUpProductPageButtonListeners();
    	
    	// Declaring the admin page
        admin_page adpg = new admin_page();
    	JPanel adminPanel = adpg.setUpAdminGUI();
    	adpg.setUpAdminButtonListeners();
		
    	// This button listen controls which page is open.
		ActionListener buttonListener = new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent ae) {
				// Finds out what what event occurred
				Object event_source = ae.getSource();
				if (event_source == loginButton) {
					cp.removeAll();
					cp.add(LoginPanel);
					cp.revalidate();
					cp.repaint();
						
				}
				
				else if (event_source == homeButton) {
					cp.removeAll();
					cp.add(HomePanel);
					cp.revalidate();
					cp.repaint();
				}
				
				else if (event_source == adminButton) {
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
        fileMenu.add(adminButton);
        
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);
        	
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        	
        
     }
}