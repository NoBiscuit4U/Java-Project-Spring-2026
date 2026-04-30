import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
 
/**
 * ProductPage — extends JPanel, plug into RestaurantApp's CardLayout:
 *
 *   ArrayList<Product> products = new ArrayList<>();
 *   products.add(new Product("PRD-001", "Margherita Pizza", 25.00, "850 kcal", "/images/pizza.png"));
 *   ProductPage productPage = new ProductPage(products);
 *   contentPanel.add(productPage, "PRODUCTS");
 *
 * Refresh the product list at any time:
 *   productPage.setProducts(updatedList);
 */
public class ProductPage extends JPanel {
 
    // ── Shared palette ───────────────────────────────────────────────────────
    static final Color RED_ACCENT = new Color(220, 60, 60);
    static final Color LIGHT_BG   = new Color(248, 248, 248);
    static final Color CARD_BG    = new Color(255, 255, 255);
    static final Color TEXT_DARK  = new Color(30, 30, 30);
    static final Color TEXT_MUTED = new Color(120, 120, 120);
    static final Color FOOTER_BG  = new Color(210, 55, 55);
 
    // ── Internal state ───────────────────────────────────────────────────────
    private ArrayList<Product> products;
    private final JPanel       gridPanel;        // where cards are rendered
    private final int          COLS = 3;         // cards per row
 
    // ── Cart (simple in-memory) ──────────────────────────────────────────────
    private Cart m_cart;
    private JLabel cartBadge;
 
    // ─────────────────────────────────────────────────────────────────────────
 
    public ProductPage(ArrayList<Product> products,Cart cart) {
        this.products = products;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(LIGHT_BG);

        m_cart=cart;
 
        add(buildHero());
 
        // Toolbar (search + cart badge)
        add(buildToolbar());
 
        // Grid panel — rebuilt whenever products change
        gridPanel = new JPanel();
        gridPanel.setLayout(new BoxLayout(gridPanel, BoxLayout.Y_AXIS));
        gridPanel.setBackground(LIGHT_BG);
        gridPanel.setBorder(new EmptyBorder(24, 40, 40, 40));
        gridPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JScrollPane scroll = new JScrollPane(gridPanel);
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        scroll.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(1, 0, 1, 0, new Color(230, 220, 215)),
                BorderFactory.createEmptyBorder()));
        scroll.getViewport().setBackground(LIGHT_BG);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setPreferredSize(new Dimension(0, 420));
        scroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        add(scroll);
 
        add(buildFooter());
 
        renderProducts();
    }
 
    // ═══════════════════════════════════════════════════════════════════════════
    // PUBLIC API
    // ═══════════════════════════════════════════════════════════════════════════
 
    /**
     * Replace the current product list and re-render the grid.
     * Call this whenever AdminPage adds, updates, or deletes a product.
     */
    public void setProducts(ArrayList<Product> products) {
        this.products = products;
        renderProducts();
    }
    // ═══════════════════════════════════════════════════════════════════════════
    // HERO BANNER
    // ═══════════════════════════════════════════════════════════════════════════
 
    private JPanel buildHero() {
        JPanel hero = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(
                        0, 0, new Color(40, 20, 10),
                        getWidth(), getHeight(), new Color(80, 30, 10)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(255, 255, 255, 15));
                for (int x = 0; x < getWidth(); x += 30)
                    for (int y = 0; y < getHeight(); y += 30)
                        g2.fillOval(x, y, 3, 3);
            }
        };
        hero.setPreferredSize(new Dimension(0, 180));
        hero.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        hero.setAlignmentX(Component.LEFT_ALIGNMENT);
 
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
 
        JLabel title = new JLabel("Our Products");
        title.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 36));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
 
        JLabel sub = new JLabel("Fresh ingredients, bold flavours — order directly from our kitchen.");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        sub.setForeground(new Color(210, 210, 210));
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
 
        content.add(title);
        content.add(Box.createVerticalStrut(10));
        content.add(sub);
        hero.add(content);
        return hero;
    }
 
    // ═══════════════════════════════════════════════════════════════════════════
    // TOOLBAR  (search bar + cart indicator)
    // ═══════════════════════════════════════════════════════════════════════════
 
    private JPanel buildToolbar() {
        JPanel bar = new JPanel(new BorderLayout(12, 0));
        bar.setBackground(CARD_BG);
        bar.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 1, 0, new Color(230, 220, 215)),
                new EmptyBorder(12, 40, 12, 40)));
        bar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
        bar.setAlignmentX(Component.LEFT_ALIGNMENT);
 
        // Search
        JTextField search = new JTextField() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setColor(new Color(190, 180, 175));
                    g2.setFont(getFont().deriveFont(Font.ITALIC));
                    g2.drawString("🔍  Search products…", 10, getHeight() / 2 + 5);
                }
            }
        };
        search.setFont(new Font("SansSerif", Font.PLAIN, 13));
        search.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(210, 200, 195), 1),
                new EmptyBorder(6, 10, 6, 10)));
        search.setPreferredSize(new Dimension(280, 34));
        search.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                search.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(RED_ACCENT, 1),
                        new EmptyBorder(6, 10, 6, 10)));
            }
            public void focusLost(FocusEvent e) {
                search.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(210, 200, 195), 1),
                        new EmptyBorder(6, 10, 6, 10)));
            }
        });
        // Live filter on keypress
        search.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { filterProducts(search.getText().trim()); }
        });
 
        // Cart badge
        JPanel cartPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        cartPanel.setOpaque(false);
        cartBadge = new JLabel("🛒  Cart: 0 item(s)");
        cartBadge.setFont(new Font("SansSerif", Font.BOLD, 13));
        cartBadge.setForeground(TEXT_DARK);
        cartPanel.add(cartBadge);
 
        JButton viewCart = makeOutlineButton("VIEW CART");
        viewCart.setPreferredSize(new Dimension(110, 32));
        viewCart.addActionListener(e -> showCartDialog());
        cartPanel.add(viewCart);
 
        bar.add(search,    BorderLayout.WEST);
        bar.add(cartPanel, BorderLayout.EAST);
        return bar;
    }
 
    // ═══════════════════════════════════════════════════════════════════════════
    // PRODUCT GRID
    // ═══════════════════════════════════════════════════════════════════════════
 
    /** Rebuild the grid from the current product list. */
    private void renderProducts() {
        renderFiltered(products);
    }
 
    /** Rebuild the grid from a (possibly filtered) subset. */
    private void renderFiltered(ArrayList<Product> list) {
        gridPanel.removeAll();
 
        if (list == null || list.isEmpty()) {
            gridPanel.add(buildEmptyState());
        } else {
            // Chunk into rows of COLS
            for (int i = 0; i < list.size(); i += COLS) {
                int end = Math.min(i + COLS, list.size());
                ArrayList<Product> rowItems = new ArrayList<>(list.subList(i, end));
 
                JPanel row = new JPanel(new GridLayout(1, COLS, 20, 0));
                row.setOpaque(false);
                row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 320));
                row.setAlignmentX(Component.LEFT_ALIGNMENT);
 
                for (Product p : rowItems) {
                    row.add(buildProductCard(p));
                }
                // Fill empty slots in the last row so layout stays even
                for (int k = rowItems.size(); k < COLS; k++) {
                    row.add(new JPanel() {{ setOpaque(false); }});
                }
 
                gridPanel.add(row);
                gridPanel.add(Box.createVerticalStrut(20));
            }
        }
 
        gridPanel.revalidate();
        gridPanel.repaint();
    }
 
    /** Filter products by name or ID (case-insensitive). */
    private void filterProducts(String query) {
        if (query.isEmpty()) {
            renderFiltered(products);
            return;
        }
        String q = query.toLowerCase();
        ArrayList<Product> filtered = new ArrayList<>();
        for (Product p : products) {
            if (p.getName().toLowerCase().contains(q) || (Integer.toString(p.getID())).toLowerCase().contains(q)) {
                filtered.add(p);
            }
        }
        renderFiltered(filtered);
    }
 
    // ─── Empty state ─────────────────────────────────────────────────────────
 
    private JPanel buildEmptyState() {
        JPanel empty = new JPanel(new GridBagLayout());
        empty.setOpaque(false);
        empty.setPreferredSize(new Dimension(0, 300));
        empty.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
 
        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setOpaque(false);
 
        JLabel icon = new JLabel("🍽️");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 52));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);
 
        JLabel msg = new JLabel("No products yet");
        msg.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 22));
        msg.setForeground(TEXT_MUTED);
        msg.setAlignmentX(Component.CENTER_ALIGNMENT);
 
        JLabel sub = new JLabel("Add products via the Admin page to display them here.");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        sub.setForeground(new Color(170, 160, 155));
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
 
        inner.add(icon);
        inner.add(Box.createVerticalStrut(12));
        inner.add(msg);
        inner.add(Box.createVerticalStrut(6));
        inner.add(sub);
        empty.add(inner);
        return empty;
    }
 
    // ─── Product card ────────────────────────────────────────────────────────
 
    private JPanel buildProductCard(Product p) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(0, 0, 16, 0)));
 
        // ── Image area ───────────────────────────────────────────────────────
        JPanel imgPanel = buildImagePanel(p);
 
        // ── Info ─────────────────────────────────────────────────────────────
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setOpaque(false);
        info.setBorder(new EmptyBorder(14, 16, 0, 16));
 
        JLabel nameLbl = new JLabel(p.getName());
        nameLbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        nameLbl.setForeground(RED_ACCENT);
        nameLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
 
        JLabel priceLbl = new JLabel(String.format("$%.2f", p.getCost()));
        priceLbl.setFont(new Font("Serif", Font.BOLD, 24));
        priceLbl.setForeground(TEXT_DARK);
        priceLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
 
        if (p.getNuritLabel() != null && !p.getNuritLabel().isEmpty()) {
            JLabel nutLbl = new JLabel(p.getNuritLabel());
            nutLbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
            nutLbl.setForeground(TEXT_MUTED);
            nutLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
            info.add(nameLbl);
            info.add(Box.createVerticalStrut(2));
            info.add(nutLbl);
        } else {
            info.add(nameLbl);
        }
 
        info.add(Box.createVerticalStrut(6));
        info.add(priceLbl);
        info.add(Box.createVerticalStrut(12));
 
        JButton addBtn = makeRedButton("ADD TO CART");
        addBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        addBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        addBtn.addActionListener(e -> addToCart(p, addBtn));
        info.add(addBtn);
 
        card.add(imgPanel, BorderLayout.NORTH);
        card.add(info,     BorderLayout.CENTER);
        return card;
    }
 
    /**
     * Builds the image area for a card.
     * Tries to load p.img as an ImageIcon; falls back to a gradient + emoji.
     */
    private JPanel buildImagePanel(Product p) {
        JPanel imgPanel;
 
        // Attempt to load image from path/URL
        ImageIcon icon = tryLoadImage(p.getImg(), 340, 180);
 
        if (icon != null) {
            // Real image loaded
            imgPanel = new JPanel(new BorderLayout());
            imgPanel.setPreferredSize(new Dimension(0, 180));
            imgPanel.setBackground(new Color(40, 20, 10));
            JLabel imgLabel = new JLabel(icon, SwingConstants.CENTER);
            imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imgPanel.add(imgLabel, BorderLayout.CENTER);
        } else {
            // Placeholder — gradient + emoji
            imgPanel = new JPanel(new GridBagLayout()) {
                @Override protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setPaint(new GradientPaint(
                            0, 0, new Color(50, 30, 20),
                            getWidth(), getHeight(), new Color(100, 60, 30)));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                }
            };
            imgPanel.setPreferredSize(new Dimension(0, 180));
        }
 
        return imgPanel;
    }
 
    /**
     * Safely loads an ImageIcon from a file path or URL and scales it.
     * Returns null if the path is blank or the image cannot be loaded.
     */
    private ImageIcon tryLoadImage(String path, int width, int height) {
        if (path == null || path.isEmpty()) return null;
        try {
            ImageIcon raw;
            if (path.startsWith("http://") || path.startsWith("https://")) {
                raw = new ImageIcon(new java.net.URL(path));
            } else {
                java.io.File file = new java.io.File(path);
                if (!file.exists()) return null;
                raw = new ImageIcon(path);
            }
            if (raw.getIconWidth() <= 0) return null;
            Image scaled = raw.getImage()
                    .getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (Exception e) {
            return null;
        }
    }
 
    // ═══════════════════════════════════════════════════════════════════════════
    // CART
    // ═══════════════════════════════════════════════════════════════════════════
 
    private void addToCart(Product p, JButton btn) {
        m_cart.add_pdcts(p);
        int count = m_cart.cartSize();
        cartBadge.setText("🛒  Cart: " + count + " item(s)");
 
        // Brief button feedback
        String original = btn.getText();
        btn.setText("✔ Added!");
        btn.setEnabled(false);
        Timer t = new Timer(900, e -> {
            btn.setText(original);
            btn.setEnabled(true);
        });
        t.setRepeats(false);
        t.start();
    }
 
    private void showCartDialog() {
        if (m_cart.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Your cart is empty.\nBrowse products and click Add to Cart!",
                    "Your Cart", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
 
        // Build a simple summary table
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-30s %8s%n", "Product", "Price"));
        //sb.append("─".repeat(40)).append("\n");
        double total = 0;
        for (Product p : m_cart.getPdcts()) {
            sb.append(String.format("%-30s $%7.2f%n", p.getName(), p.getCost()));
            total += p.getCost();
        }
        //sb.append("─".repeat(40)).append("\n");
        sb.append(String.format("%-30s $%7.2f%n", "TOTAL", total));
 
        JTextArea area = new JTextArea(sb.toString());
        area.setFont(new Font("Monospaced", Font.PLAIN, 13));
        area.setEditable(false);
        area.setBorder(new EmptyBorder(8, 8, 8, 8));
 
        int choice = JOptionPane.showOptionDialog(
                this, new JScrollPane(area), "Your Cart",
                JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                new String[]{"Clear Cart", "Close"}, "Close");
 
        if (choice == 0) {
            m_cart.clear();
            cartBadge.setText("🛒  Cart: 0 item(s)");
        }
    }
 
    // ═══════════════════════════════════════════════════════════════════════════
    // SHARED COMPONENTS
    // ═══════════════════════════════════════════════════════════════════════════
 
    private JPanel buildFooter() {
        JPanel footer = new JPanel(new GridBagLayout());
        footer.setBackground(FOOTER_BG);
        footer.setPreferredSize(new Dimension(0, 46));
        footer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        footer.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel text = new JLabel("Restaurant WordPress Theme By Luzuk");
        text.setFont(new Font("SansSerif", Font.PLAIN, 13));
        text.setForeground(Color.WHITE);
        footer.add(text);
        return footer;
    }
 
    private JButton makeRedButton(String label) {
        JButton btn = new JButton(label) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(190, 40, 40) : RED_ACCENT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                g2.dispose();
                super.paintComponent(g);
            }
        };
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
 
    private JButton makeOutlineButton(String label) {
        JButton btn = new JButton(label) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(new Color(250, 242, 240));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                }
                g2.setColor(RED_ACCENT);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 6, 6);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("SansSerif", Font.BOLD, 11));
        btn.setForeground(RED_ACCENT);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(false);
        return btn;
    }
}