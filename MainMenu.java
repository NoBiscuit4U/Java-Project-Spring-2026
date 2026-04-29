import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.border.*;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class MainMenu extends JFrame {

    // Color palette
    static final Color RED_ACCENT   = new Color(220, 60, 60);
    static final Color DARK_BG      = new Color(30, 30, 30);
    static final Color LIGHT_BG     = new Color(248, 248, 248);
    static final Color CARD_BG      = new Color(255, 255, 255);
    static final Color TEXT_DARK    = new Color(30, 30, 30);
    static final Color TEXT_MUTED   = new Color(120, 120, 120);
    static final Color FOOTER_BG    = new Color(210, 55, 55);

    private ProductManager m_pm;

    public MainMenu(ProductManager pm,String title,String header_descrip,String body_descrip,HashMap<String,JPanel> map) {
        m_pm=pm;
        CardLayout cardLayout = new CardLayout();
        JPanel contentPanel = new JPanel(cardLayout);
        map.put("Home",buildBody(header_descrip,body_descrip));

        for(String key:map.keySet()){
            contentPanel.add(map.get(key),key);
        }
        

        setTitle(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1050, 780);
        setMinimumSize(new Dimension(900, 650));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(LIGHT_BG);

        root.add(buildNavBar(cardLayout,contentPanel,title,map.keySet().toArray()),BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(contentPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        root.add(scroll, BorderLayout.CENTER);

        setContentPane(root);
        setVisible(true);
    }

    // ─── NAV BAR ────────────────────────────────────────────────────────────────

    private JPanel buildNavBar(CardLayout cardLayout,JPanel contentPanel,String title,Object[] pages) {
        JPanel nav = new JPanel(new BorderLayout());
        nav.setBackground(DARK_BG);
        nav.setPreferredSize(new Dimension(0, 56));
        nav.setBorder(new EmptyBorder(0, 24, 0, 24));

        // Logo
        JLabel logo = new JLabel("✦ "+title);
        logo.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 20));
        logo.setForeground(Color.WHITE);
        nav.add(logo, BorderLayout.WEST);

        // Nav links
        JPanel links = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        links.setOpaque(false);
        for (int i = 0; i < pages.length; i++) {
            JLabel lbl = new JLabel((String) pages[i]);

            final String page = (String) pages[i];
            lbl.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    cardLayout.show(contentPanel, page);
                }
            });
            links.add(lbl);
        }
        nav.add(links, BorderLayout.CENTER);

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

        // Sub-heading
        JLabel sub = new JLabel("Best of Photos");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        sub.setForeground(TEXT_MUTED);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Decorative script heading
        JLabel heading = new JLabel("Feature Products");
        heading.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 30));
        heading.setForeground(RED_ACCENT);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Ornament
        JLabel ornament = new JLabel("— ✦ —");
        ornament.setFont(new Font("SansSerif", Font.PLAIN, 14));
        ornament.setForeground(TEXT_MUTED);
        ornament.setAlignmentX(Component.CENTER_ALIGNMENT);

        section.add(sub);
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
                    g.drawImage(buff_img,getWidth(),getHeight(),this);
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