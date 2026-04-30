import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import java.util.Arrays;

public class MainMenu extends JFrame {

    // Color palette
    static final Color RED_ACCENT=new Color(220, 60, 60);
    static final Color DARK_BG=new Color(30, 30, 30);
    static final Color LIGHT_BG=new Color(248, 248, 248);
    static final Color CARD_BG=new Color(255, 255, 255);
    static final Color TEXT_DARK=new Color(30, 30, 30);
    static final Color TEXT_MUTED=new Color(120, 120, 120);
    static final Color FOOTER_BG=new Color(210, 55, 55);

    private ProductManager m_pm;
    private UserManager m_um;

    private CardLayout m_cardLayout;
    private JPanel m_contentPanel;
    private JPanel m_navLinksPanel;
    private LinkedHashMap<String, JPanel> m_pages;
    private JPanel m_root;
    private JPanel m_adminPanel;

    public MainMenu(ProductManager pm,UserManager um,String title,String header_descrip,String body_descrip) {
        m_pm=pm;
        m_um=um;

        m_navLinksPanel=new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        m_navLinksPanel.setOpaque(false);

        m_pages=new LinkedHashMap<>();
        m_cardLayout=new CardLayout();
        m_contentPanel=new JPanel(m_cardLayout);

        JPanel homePanel=buildBody(header_descrip,body_descrip);
        m_adminPanel=new AdminPage(m_pm,m_um);
        addPage("Home",homePanel);
        addPage("Login",new LoginPage(m_um));

        setTitle(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1050,780);
        setMinimumSize(new Dimension(900, 650));
        setLocationRelativeTo(null);

        m_root=new JPanel(new BorderLayout());
        m_root.setBackground(LIGHT_BG);

        m_root.add(buildNavBar(title),BorderLayout.NORTH);

        JScrollPane scroll=new JScrollPane(m_contentPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        m_root.add(scroll, BorderLayout.CENTER);

        setContentPane(m_root);
        setVisible(true);

        runUpdate();
    }

    private void runUpdate(){
        while(true){
            if(m_um.checkAdmin()){
                if(!Arrays.asList(m_contentPanel.getComponents()).contains(m_adminPanel)){
                    addPage("Admin",m_adminPanel);
                }
            }else{
                if(Arrays.asList(m_contentPanel.getComponents()).contains(m_adminPanel)){
                    removePage("Admin");
                }
            }
        }
    }

    // ─── NAV BAR ────────────────────────────────────────────────────────────────

    private JPanel buildNavBar(String title) {
        JPanel nav=new JPanel(new BorderLayout());
        nav.setBackground(DARK_BG);
        nav.setPreferredSize(new Dimension(0, 56));
        nav.setBorder(new EmptyBorder(0, 24, 0, 24));

        // Logo
        JLabel logo=new JLabel("✦ "+title);
        logo.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 20));
        logo.setForeground(Color.WHITE);
        nav.add(logo, BorderLayout.WEST);
        
        nav.add(m_navLinksPanel, BorderLayout.CENTER);
        refreshNavLinks();

        // Social icons (text stand-ins)
        JPanel socials = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        socials.setOpaque(false);
        for (String s : new String[]{"f", "t", "in", "yt"}) {
            JLabel icon = new JLabel(s.toUpperCase());
            icon.setFont(new Font("SansSerif", Font.BOLD, 11));
            icon.setForeground(Color.WHITE);
            icon.setOpaque(true);
            icon.setBackground(new Color(80, 80, 80));
            icon.setPreferredSize(new Dimension(26, 26));
            icon.setHorizontalAlignment(SwingConstants.CENTER);
            icon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            socials.add(icon);
        }
        nav.add(socials, BorderLayout.EAST);

        return nav;
    }

    private void refreshNavLinks() {
        m_navLinksPanel.removeAll();
        for (String page : m_pages.keySet()) {
            JLabel lbl = new JLabel(page);
            lbl.setForeground(Color.WHITE);
            lbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            lbl.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    showPage(page);
                }
            });
            m_navLinksPanel.add(lbl);
        }
        m_navLinksPanel.revalidate();
        m_navLinksPanel.repaint();
    }

    public void addPage(String pageName, JPanel pagePanel) {
        if (pageName == null || pageName.equals("") || pagePanel == null || m_pages.containsKey(pageName)) {
            return;
        }
        m_pages.put(pageName, pagePanel);
        m_contentPanel.add(pagePanel, pageName);
        refreshNavLinks();
        m_contentPanel.revalidate();
        m_contentPanel.repaint();
    }

    public void removePage(String pageName) {
        if (pageName == null || pageName.equals("") || "Home".equals(pageName)) {
            return;
        }
        JPanel panel = m_pages.remove(pageName);
        if (panel == null) {
            return;
        }
        m_contentPanel.remove(panel);
        refreshNavLinks();
        showPage("Home");
        m_contentPanel.revalidate();
        m_contentPanel.repaint();
    }

    public void showPage(String pageName) {
        if (pageName == null || !m_pages.containsKey(pageName)) {
            return;
        }
        m_cardLayout.show(m_contentPanel, pageName);
    }

    private void handleAddToCart(String name){
        //cart.addm_pm.getProduct(name)
    }

    // ─── BODY ───────────────────────────────────────────────────────────────────

    private JPanel buildBody(String header_descrip,String body_descrip) {
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(LIGHT_BG);

        body.add(buildHeroBanner(header_descrip,body_descrip));
        body.add(buildFeaturedSection());
        body.add(buildFooter());

        return body;
    }

    // ─── HERO BANNER ────────────────────────────────────────────────────────────

    private JPanel buildHeroBanner(String header_descrip,String body_descrip) {
        // Gradient panel standing in for a background photo
        JPanel hero = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                // Dark gradient to simulate a dimmed food photo background
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(40, 20, 10),
                        getWidth(), getHeight(), new Color(80, 30, 10));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Subtle texture dots
                g2.setColor(new Color(255, 255, 255, 15));
                for (int x = 0; x < getWidth(); x += 30)
                    for (int y = 0; y < getHeight(); y += 30)
                        g2.fillOval(x, y, 3, 3);
            }
        };
        hero.setPreferredSize(new Dimension(0, 320));
        hero.setMaximumSize(new Dimension(Integer.MAX_VALUE, 320));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel(header_descrip);
        title.setFont(new Font("Serif", Font.BOLD, 38));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel(body_descrip);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(new Color(220, 220, 220));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        content.add(title);
        content.add(Box.createVerticalStrut(14));
        content.add(subtitle);
        content.add(Box.createVerticalStrut(22));

        hero.add(content);
        return hero;
    }

    // ─── FEATURED SECTION ───────────────────────────────────────────────────────

    private JPanel buildFeaturedSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(LIGHT_BG);
        section.setBorder(new EmptyBorder(40, 40, 50, 40));

        // Decorative script heading
        JLabel heading = new JLabel("Featured Products");
        heading.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 30));
        heading.setForeground(RED_ACCENT);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Ornament
        JLabel ornament = new JLabel("— ✦ —");
        ornament.setFont(new Font("SansSerif", Font.PLAIN, 14));
        ornament.setForeground(TEXT_MUTED);
        ornament.setAlignmentX(Component.CENTER_ALIGNMENT);

        section.add(Box.createVerticalStrut(4));
        section.add(heading);
        section.add(Box.createVerticalStrut(6));
        section.add(ornament);
        section.add(Box.createVerticalStrut(28));

        // Cards row
        JPanel cards = new JPanel(new GridLayout(1, 3, 20, 0));
        cards.setOpaque(false);
        cards.setMaximumSize(new Dimension(Integer.MAX_VALUE, 340));
        String[] dishes=new String[3];
        String[] imgs=new String[3];
        double[] prices=new double[3];

        List<Product> pdct_list=m_pm.getProductList().subList(0,3);
        for(int i=0;i<pdct_list.size();i++){
            Product pdct=pdct_list.get(i);
            dishes[i]=pdct.getName();
            imgs[i]=pdct.getImg();
            prices[i]=pdct.getCost();
        }

        for (int i = 0; i < 3; i++) {
            cards.add(buildProductCard(imgs[i],dishes[i], prices[i]));
        }

        section.add(cards);
        return section;
    }

    // ─── PRODUCT CARD ───────────────────────────────────────────────────────────

    private JPanel buildProductCard(String img, String name, double price) {
        JPanel card = new JPanel(new BorderLayout(0, 0));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(0, 0, 16, 0)));

        JPanel imgPlaceholder = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                try{
                    File file=new File(img);
                    BufferedImage buff_img=ImageIO.read(file);
                    g.drawImage(buff_img,0,0,getWidth(),getHeight(),this);
                }catch(IOException e){
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g;
                    GradientPaint gp = new GradientPaint(
                            0, 0, new Color(50, 30, 20),
                            getWidth(), getHeight(), new Color(100, 60, 30));
                    g2.setPaint(gp);
                    g2.fillRect(0, 0, getWidth(), getHeight());
                }

            }
        };

        imgPlaceholder.setPreferredSize(new Dimension(0, 180));

        // Info panel
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setOpaque(false);
        info.setBorder(new EmptyBorder(14, 16, 0, 16));

        JLabel nameLbl = new JLabel(name);
        nameLbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        nameLbl.setForeground(RED_ACCENT);
        nameLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel priceLbl = new JLabel(String.format("$%.0f", price));
        priceLbl.setFont(new Font("Serif", Font.BOLD, 24));
        priceLbl.setForeground(TEXT_DARK);
        priceLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton addBtn = makeRedButton("ADD TO CART");
        addBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        addBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        addBtn.addActionListener(e -> handleAddToCart(name));

        info.add(nameLbl);
        info.add(Box.createVerticalStrut(4));
        info.add(priceLbl);
        info.add(Box.createVerticalStrut(12));
        info.add(addBtn);

        card.add(imgPlaceholder, BorderLayout.NORTH);
        card.add(info, BorderLayout.CENTER);

        return card;
    }

    // ─── FOOTER ─────────────────────────────────────────────────────────────────

    private JPanel buildFooter() {
        JPanel footer = new JPanel(new GridBagLayout());
        footer.setBackground(FOOTER_BG);
        footer.setPreferredSize(new Dimension(0, 46));
        footer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));

        return footer;
    }

    // ─── HELPERS ────────────────────────────────────────────────────────────────

    private JButton makeRedButton(String label) {
        JButton btn = new JButton(label) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(190, 40, 40) : RED_ACCENT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setText(label);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(160, 38));
        btn.setOpaque(false);
        return btn;
    }
}