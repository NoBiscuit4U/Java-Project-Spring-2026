import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

public class CartPage extends JPanel {

    // Palette (matches MainMenu / LoginPage)
    static final Color RED_ACCENT = new Color(220, 60, 60);
    static final Color DARK_BG    = new Color(30, 30, 30);
    static final Color LIGHT_BG   = new Color(248, 248, 248);
    static final Color CARD_BG    = new Color(255, 255, 255);
    static final Color TEXT_DARK  = new Color(30, 30, 30);
    static final Color TEXT_MUTED = new Color(120, 120, 120);
    static final Color FOOTER_BG  = new Color(210, 55, 55);

    private static final double DELIVERY_FEE = 3.99;
    private static final double TAX_RATE     = 0.08;
    private static final DecimalFormat FMT   = new DecimalFormat("$#,##0.00");

    // ── UI components ─────────────────────────────────────────────────────────
    private CartTablePanel    cartTablePanel;
    private JLabel            lblSubtotal, lblTax, lblTotal, lblItemCount;
    private JTextField        tfName, tfAddress, tfPhone, tfPromo;
    private JLabel            lblPromoMsg;
    private JLabel            lblStatus;

    private Cart m_cart;

    // ── Constructor ───────────────────────────────────────────────────────────
    public CartPage(Cart cart) {
        m_cart=cart;

        // Try system look-and-feel; fall back gracefully
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}

        setLayout(new BorderLayout(0, 0));
        setBackground(LIGHT_BG);
        setPreferredSize(new Dimension(780, 680));

        // ── Centre scroll area ────────────────────────────────────────────────
        JPanel centre = new JPanel();
        centre.setLayout(new BoxLayout(centre, BoxLayout.Y_AXIS));
        centre.setBackground(LIGHT_BG);
        centre.setBorder(BorderFactory.createEmptyBorder(40, 40, 50, 40));

        centre.add(buildHeaderPanel());
        centre.add(Box.createVerticalStrut(10));
        centre.add(buildCartTableSection());
        centre.add(Box.createVerticalStrut(10));
        centre.add(buildButtonRow());
        centre.add(Box.createVerticalStrut(10));
        centre.add(buildSummaryPanel());
        centre.add(Box.createVerticalStrut(10));
        centre.add(buildDeliveryPanel());
        centre.add(Box.createVerticalStrut(14));
        centre.add(buildActionRow());

        JScrollPane scroll = new JScrollPane(centre);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        // ── Status bar ────────────────────────────────────────────────────────
        add(buildStatusBar(), BorderLayout.SOUTH);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                cartTablePanel.syncFromCart();
            }
        });

        cartTablePanel.syncFromCart();
    }

    /** Rebuilds the grid from {@link #m_cart}; safe to call after items are added on other pages. */
    public void refreshFromCart() {
        cartTablePanel.syncFromCart();
    }

    /** For embedding in a {@link JFrame}; call {@code frame.setJMenuBar(panel.createMenuBar())}. */
    public JMenuBar createMenuBar() {
        return buildMenuBar();
    }

    // ── Menu bar ──────────────────────────────────────────────────────────────
    private JMenuBar buildMenuBar() {
        JMenuBar mb = new JMenuBar();
        mb.setOpaque(true);
        mb.setBackground(LIGHT_BG);
        mb.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));
        String[] menus = { "File", "Edit", "Order", "Help" };
        for (String m : menus) {
            JMenu menu = new JMenu(m);
            menu.setMnemonic(m.charAt(0));
            mb.add(menu);
        }

        // File → Exit
        JMenu fileMenu = (JMenu) mb.getMenu(0);
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(e -> System.exit(0));
        fileMenu.add(exit);

        // Order → Place Order
        JMenu orderMenu = (JMenu) mb.getMenu(2);
        JMenuItem place = new JMenuItem("Place Order");
        place.addActionListener(e -> placeOrder());
        orderMenu.add(place);
        orderMenu.addSeparator();
        JMenuItem clear = new JMenuItem("Clear Cart");
        clear.addActionListener(e -> clearAll());
        orderMenu.add(clear);

        return mb;
    }

    // ── Header ────────────────────────────────────────────────────────────────
    private JPanel buildHeaderPanel() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);

        JLabel title = new JLabel("Your Order");
        title.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 30));
        title.setForeground(RED_ACCENT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel ornament = new JLabel("— ✦ —");
        ornament.setFont(new Font("SansSerif", Font.PLAIN, 14));
        ornament.setForeground(TEXT_MUTED);
        ornament.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblItemCount = new JLabel("0 items");
        lblItemCount.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblItemCount.setForeground(TEXT_MUTED);
        lblItemCount.setAlignmentX(Component.CENTER_ALIGNMENT);

        p.add(title);
        p.add(Box.createVerticalStrut(6));
        p.add(ornament);
        p.add(Box.createVerticalStrut(8));
        p.add(lblItemCount);
        return p;
    }

    // ── Cart table (shared with ProductPage cart dialog) ─────────────────────
    private JPanel buildCartTableSection() {
        cartTablePanel = new CartTablePanel(m_cart);
        cartTablePanel.setScrollPreferredHeight(200);
        cartTablePanel.setOnModified(this::refreshTotals);
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.add(cartTablePanel, BorderLayout.CENTER);
        return p;
    }

    // ── Buttons below table ───────────────────────────────────────────────────
    private JPanel buildButtonRow() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        p.setOpaque(false);

        JButton btnRemove = makeOutlineButton("Remove Selected");
        btnRemove.addActionListener(e -> removeSelected());

        JButton btnClear = makeOutlineButton("Clear Cart");
        btnClear.addActionListener(e -> clearAll());

        p.add(btnRemove);
        p.add(btnClear);
        return p;
    }

    // ── Summary panel ─────────────────────────────────────────────────────────
    private JPanel buildSummaryPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(titledBorder("Order Summary"));
        p.setBackground(CARD_BG);

        GridBagConstraints l = new GridBagConstraints();
        l.anchor = GridBagConstraints.WEST;
        l.insets = new Insets(3, 8, 3, 4);
        l.gridx = 0; l.gridy = 0; l.weightx = 1.0; l.fill = GridBagConstraints.HORIZONTAL;

        GridBagConstraints r = new GridBagConstraints();
        r.anchor = GridBagConstraints.EAST;
        r.insets = new Insets(3, 4, 3, 12);
        r.gridx = 1; r.gridy = 0;

        Font labelFont = new Font("SansSerif", Font.PLAIN, 13);
        Font valueFont = new Font("SansSerif", Font.PLAIN, 13);
        Font totalFont = new Font("Serif", Font.BOLD, 22);

        String[] labels = { "Subtotal:", "Delivery fee:", "Tax (8%):", "Total:" };
        lblSubtotal = moneyLabel(valueFont);
        JLabel lblDelivery = new JLabel(FMT.format(DELIVERY_FEE));
        lblDelivery.setFont(valueFont);
        lblDelivery.setForeground(TEXT_DARK);
        lblTax = moneyLabel(valueFont);
        lblTotal = moneyLabel(totalFont);
        lblTotal.setForeground(RED_ACCENT);

        JLabel[] values = { lblSubtotal, lblDelivery, lblTax, lblTotal };

        for (int i = 0; i < labels.length; i++) {
            l.gridy = i;
            r.gridy = i;
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(i == 3 ? new Font("SansSerif", Font.BOLD, 13) : labelFont);
            lbl.setForeground(i == 3 ? TEXT_DARK : TEXT_MUTED);
            if (i == 3) {
                // Separator row before Total label/value pair
                GridBagConstraints sl = new GridBagConstraints();
                sl.gridx = 0; sl.gridy = 3; sl.gridwidth = 2;
                sl.fill = GridBagConstraints.HORIZONTAL;
                sl.insets = new Insets(2, 8, 2, 8);
                p.add(new JSeparator(), sl);
                l.gridy = 4; r.gridy = 4;
            }
            p.add(lbl,      l);
            p.add(values[i], r);
        }
        return p;
    }

    // ── Delivery panel ────────────────────────────────────────────────────────
    private JPanel buildDeliveryPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(titledBorder("Delivery Details"));
        p.setBackground(CARD_BG);

        GridBagConstraints lc = new GridBagConstraints();
        lc.anchor = GridBagConstraints.WEST;
        lc.insets = new Insets(4, 8, 4, 4);

        GridBagConstraints fc = new GridBagConstraints();
        fc.fill = GridBagConstraints.HORIZONTAL;
        fc.weightx = 1.0;
        fc.insets = new Insets(4, 0, 4, 12);

        Font lblFont = new Font("SansSerif", Font.PLAIN, 13);
        Font fldFont = new Font("SansSerif", Font.PLAIN, 13);

        String[] lbs = { "Name:", "Address:", "Phone:" };
        String[] dv  = { "John Smith", "123 Main Street, Apt 4B", "(555) 867-5309" };
        JTextField[] fields = new JTextField[3];
        tfName    = new JTextField(dv[0], 24); fields[0] = tfName;
        tfAddress = new JTextField(dv[1], 32); fields[1] = tfAddress;
        tfPhone   = new JTextField(dv[2], 16); fields[2] = tfPhone;

        for (int i = 0; i < 3; i++) {
            lc.gridx = 0; lc.gridy = i;
            fc.gridx = 1; fc.gridy = i;
            JLabel lbl = new JLabel(lbs[i]);
            lbl.setFont(lblFont);
            lbl.setForeground(TEXT_MUTED);
            fields[i].setFont(fldFont);
            styleFormField(fields[i]);
            p.add(lbl,      lc);
            p.add(fields[i], fc);
        }
        return p;
    }

    // ── Action row ────────────────────────────────────────────────────────────
    private JPanel buildActionRow() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        p.setOpaque(false);

        JButton btnCancel = makeOutlineButton("Cancel");
        btnCancel.addActionListener(e -> {
            int ok = JOptionPane.showConfirmDialog(this,
                "Discard your order and close?", "Cancel Order",
                JOptionPane.YES_NO_OPTION);
            if (ok != JOptionPane.YES_OPTION)
                return;
            Window w = SwingUtilities.getWindowAncestor(this);
            if (w != null)
                w.dispose();
            else
                System.exit(0);
        });

        JButton btnPlace = makeRedButton("Place Order");
        btnPlace.addActionListener(e -> placeOrder());

        p.add(btnCancel);
        p.add(btnPlace);
        return p;
    }

    // ── Status bar ────────────────────────────────────────────────────────────
    private JPanel buildStatusBar() {
        JPanel p = new JPanel(new BorderLayout(8, 0));
        p.setBackground(FOOTER_BG);
        p.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        p.setPreferredSize(new Dimension(0, 46));

        lblStatus = new JLabel("  Ready");
        lblStatus.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblStatus.setForeground(Color.WHITE);
        p.add(lblStatus, BorderLayout.WEST);

        JLabel version = new JLabel("Bistro Bella POS v1.0  ");
        version.setFont(new Font("SansSerif", Font.PLAIN, 11));
        version.setForeground(new Color(255, 255, 255, 200));
        p.add(version, BorderLayout.EAST);
        return p;
    }

    // ── Data helpers ──────────────────────────────────────────────────────────

    private void refreshTotals() {
        double sub = cartTablePanel.getSubtotal();
        int cnt = cartTablePanel.getTotalItemCount();
        double tax = sub * TAX_RATE;
        double tot = sub + tax + DELIVERY_FEE;
        lblSubtotal.setText(FMT.format(sub));
        lblTax.setText(FMT.format(tax));
        lblTotal.setText(FMT.format(tot));
        lblItemCount.setText(cnt + " item" + (cnt != 1 ? "s" : "") + " in cart");
        lblStatus.setText("  Cart updated — " + cartTablePanel.getRowCount() + " product(s)");
    }

    private void removeSelected() {
        if (cartTablePanel.removeSelected())
            setStatus("Item removed.");
        else
            JOptionPane.showMessageDialog(this,
                "Please select a row to remove.", "No selection",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void clearAll() {
        int ok = JOptionPane.showConfirmDialog(this,
            "Remove all items from cart?", "Clear Cart",
            JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            m_cart.clear();
            cartTablePanel.syncFromCart();
            setStatus("Cart cleared.");
        }
    }

    private void placeOrder() {
        if (cartTablePanel.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Your cart is empty!", "Nothing to order",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        String name = tfName.getText().trim();
        String addr = tfAddress.getText().trim();
        if (name.isEmpty() || addr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please fill in name and address.", "Missing details",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this,
            "Order placed for " + name + "!\n"
            + "Delivering to: " + addr + "\n"
            + "Estimated time: 30–45 minutes. Enjoy your meal!",
            "Order Confirmed", JOptionPane.INFORMATION_MESSAGE);
        setStatus("  Order placed successfully for " + name + "!");
    }

    // ── Utilities ─────────────────────────────────────────────────────────────
    private void setStatus(String msg) { lblStatus.setText("  " + msg); }

    private JLabel moneyLabel(Font f) {
        JLabel l = new JLabel("$0.00");
        l.setFont(f);
        l.setForeground(TEXT_DARK);
        l.setHorizontalAlignment(SwingConstants.RIGHT);
        return l;
    }

    private void styleFormField(JTextField field) {
        field.setForeground(TEXT_DARK);
        field.setBackground(new Color(252, 249, 247));
        field.setCaretColor(TEXT_DARK);
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(210, 200, 195), 1),
                new EmptyBorder(10, 12, 10, 12)));
    }

    private Border titledBorder(String title) {
        return BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                title,
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 12),
                TEXT_MUTED);
    }

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
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(170, 38));
        btn.setOpaque(false);
        return btn;
    }

    private JButton makeOutlineButton(String label) {
        JButton btn = new JButton(label) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover())
                    g2.setColor(new Color(250, 242, 240));
                else
                    g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.setColor(RED_ACCENT);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 6, 6);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setForeground(RED_ACCENT);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(160, 38));
        btn.setOpaque(false);
        return btn;
    }

}