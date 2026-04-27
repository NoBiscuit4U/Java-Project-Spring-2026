import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;

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

class admin_page extends JPanel{
	// the variables needed to define the window.
	private JFrame frame;	 
	private JButton admin_submit_button;
	private JTextField idField;
	private JTextField imgField;
	private JTextField nameField;
	private JTextField costField;
	private JTextField nutritValField;
	private GridBagConstraints gbc = new GridBagConstraints();
	private JComboBox<String> selectOptions;

	private ProductManager m_pm;
	
	// Constructor
	public admin_page(ProductManager pm) {
		m_pm=pm;

		// Defines the features of the window
		frame = new JFrame();
		setLayout(new GridBagLayout());
		idField = new JTextField("ID Text Field");
		imgField = new JTextField("Image Link");
		nutritValField = new JTextField("Nutritional Value");
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
		adminPage.add(nutritValField, gbc);
		
		gbc.gridx = 4;
		gbc.gridy = 1;
		adminPage.add(imgField, gbc);
		
		gbc.gridx = 3;
		gbc.gridy = 2;
		adminPage.add(admin_submit_button, gbc);
		
		return adminPage;
		
	}

	public HashMap<String,Object> getMap(){
		HashMap<String,Object> map=new HashMap<String,Object>();

		if(!nameField.getText().equals("")){
			map.put(Constants.obj_query_cons.kname_qry,nameField.getText());
		}
		if(!costField.getText().equals("")){
			map.put(Constants.obj_query_cons.kcost_qry,Double.parseDouble(costField.getText()));
		}
		if(!nutritValField.getText().equals("")){
			map.put(Constants.obj_query_cons.knutrit_qry,nutritValField.getText());
		}
		if(!imgField.getText().equals("")){
			map.put(Constants.obj_query_cons.kimg_qry,imgField.getText());
		}

		return map;
	}

	public void setUpAdminButtonListeners() {
		ActionListener adminButtonListener = new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent ae) {
				// Finds out what what event occurred
				Object event_source = ae.getSource();
				if (event_source == admin_submit_button) {
					if (selectOptions.getSelectedItem().equals("Add")) {
						if(!nameField.getText().equals("") && !idField.getText().equals("") && !costField.getText().equals("")
							&& !nutritValField.getText().equals("") && !imgField.getText().equals("")){                   
							m_pm.createProduct(new ArrayList<Object>(){{
								add(nameField.getText());
								add(Integer.parseInt(idField.getText()));
								add(Double.parseDouble(costField.getText()));
								add(nutritValField.getText());
								add(imgField.getText());
							}});
						}
					}
					else if (selectOptions.getSelectedItem().equals("Update")) {
						m_pm.runDynamicUpdate(getMap(),Integer.parseInt(idField.getText()));
					}
					else if (selectOptions.getSelectedItem().equals("Delete")) {
						
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
	private GridBagConstraints gbc = new GridBagConstraints();
	private JTextArea searchProducts;
	private ProductList allProductsList;
	private ProductList filteredProductsList;
	private JLabel allProducts;

	private ProductManager m_pm;
	
	// Constructor
	public home_page(ProductManager pm) {
		m_pm=pm;

		// Defines the features of the window
		frame = new JFrame();
		setLayout(new GridBagLayout());
		
		searchProducts = new JTextArea(1,15);
		filteredProductsList = new ProductList(new ArrayList<String>());
		
		// This is where we can have the code grab the title from the storage
		userGivenAppName = "Title";
		appName = new JLabel(userGivenAppName);
		
		userGivenMainPageBlurb = "Text Blurb";
		mainPageBlurb = new JTextField(userGivenMainPageBlurb);
		mainPageBlurb.setEditable(false);
		
		searchButton = new JButton("Search");
		allProducts=new JLabel("All Products");

		allProductsList = new ProductList(this.m_pm.getDisplayListAll());

		setUpButtonListeners();
	}

	public JPanel setUpGUI() {
		JPanel homePage = new JPanel(new GridBagLayout());
		frame.setTitle("GUI Demo");
		
		gbc.insets = new Insets(5,5,5,5);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		homePage.add(appName, gbc);
		
		gbc = new GridBagConstraints();
		gbc.insets = new Insets(5,5,5,5);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		homePage.add(mainPageBlurb, gbc);
		
		gbc = new GridBagConstraints();
		gbc.insets = new Insets(5,5,5,5);
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		homePage.add(allProducts,gbc);
		
		gbc = new GridBagConstraints();
		gbc.insets = new Insets(5,5,0,5);
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		homePage.add(searchButton, gbc);

		gbc = new GridBagConstraints();
		gbc.insets = new Insets(5,5,0,5);
		gbc.gridx = 1;
		gbc.gridy = 2;
		homePage.add(searchProducts, gbc);
		
		gbc = new GridBagConstraints();
		gbc.insets = new Insets(0,5,5,5);
		gbc.gridx = 1;
		gbc.gridy = 4;
		homePage.add(filteredProductsList,gbc);

		gbc = new GridBagConstraints();
		gbc.insets = new Insets(0,5,5,5);
		gbc.gridx = 0;
		gbc.gridy = 4;
		homePage.add(allProductsList,gbc);
		
		return homePage;
		
	}

	public void updateAllProducts(){
		this.allProductsList.update(this.m_pm.getDisplayListAll());
	}

	public void setUpButtonListeners() {
		ActionListener buttonListener = new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent ae) {
				// Finds out what what event occurred
				Object event_source = ae.getSource();
				if(event_source == searchButton) {
					if(!searchProducts.getText().equals("")){
						System.out.println(m_pm.getDisplayListSearch(searchProducts.getText()));
						filteredProductsList.update(m_pm.getDisplayListSearch(searchProducts.getText()));
					}
				}
			}
		};
		searchButton.addActionListener(buttonListener);
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
        m_hpg = new home_page(m_pm);
    	JPanel HomePanel = m_hpg.setUpGUI();
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
        m_adpg = new admin_page(m_pm);
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

					m_hpg.updateAllProducts();
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