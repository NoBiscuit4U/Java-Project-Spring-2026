import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

public class CartPage extends JFrame {

    // ── Data ──────────────────────────────────────────────────────────────────
    private final Object[][] menuData = {
        { "Margherita Pizza",    "Pizza",   1, 13.99 },
        { "Caesar Salad",        "Salad",   2,  7.49 },
        { "Spaghetti Carbonara", "Pasta",   1, 15.99 },
        { "Garlic Bread",        "Sides",   3,  3.49 },
        { "Tiramisu",            "Dessert", 1,  6.99 },
    };

    private final Object[][] extraItems = {
        { "Bruschetta",       "Starters", 1,  5.99 },
        { "Lasagne al Forno", "Pasta",    1, 14.49 },
        { "Panna Cotta",      "Dessert",  1,  5.49 },
        { "Sparkling Water",  "Drinks",   2,  2.49 },
    };
    private int extraIdx = 0;

    private static final double DELIVERY_FEE = 3.99;
    private static final double TAX_RATE     = 0.08;
    private static final DecimalFormat FMT   = new DecimalFormat("$#,##0.00");

    // ── UI components ─────────────────────────────────────────────────────────
    private DefaultTableModel tableModel;
    private JTable            cartTable;
    private JLabel            lblSubtotal, lblTax, lblTotal, lblItemCount;
    private JTextField        tfName, tfAddress, tfPhone, tfPromo;
    private JLabel            lblPromoMsg;
    private JLabel            lblStatus;

    // ── Constructor ───────────────────────────────────────────────────────────
    public CartPage() {
        super("🍽  Bistro Bella — Shopping Cart");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(780, 680);
        setMinimumSize(new Dimension(680, 580));
        setLocationRelativeTo(null);

        // Try system look-and-feel; fall back gracefully
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}

        // ── Root panel ────────────────────────────────────────────────────────
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(new Color(245, 241, 232));
        setContentPane(root);

        // ── Menu bar ──────────────────────────────────────────────────────────
        setJMenuBar(buildMenuBar());

        // ── Centre scroll area ────────────────────────────────────────────────
        JPanel centre = new JPanel();
        centre.setLayout(new BoxLayout(centre, BoxLayout.Y_AXIS));
        centre.setBackground(new Color(245, 241, 232));
        centre.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));

        centre.add(buildHeaderPanel());
        centre.add(Box.createVerticalStrut(10));
        centre.add(buildCartPanel());
        centre.add(Box.createVerticalStrut(10));
        centre.add(buildButtonRow());
        centre.add(Box.createVerticalStrut(10));
        centre.add(buildSummaryPanel());
        centre.add(Box.createVerticalStrut(10));
        centre.add(buildPromoPanel());
        centre.add(Box.createVerticalStrut(10));
        centre.add(buildDeliveryPanel());
        centre.add(Box.createVerticalStrut(14));
        centre.add(buildActionRow());

        JScrollPane scroll = new JScrollPane(centre);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        root.add(scroll, BorderLayout.CENTER);

        // ── Status bar ────────────────────────────────────────────────────────
        root.add(buildStatusBar(), BorderLayout.SOUTH);

        // Load initial data and compute totals
        loadInitialData();
        refreshTotals();

        setVisible(true);
    }

    // ── Menu bar ──────────────────────────────────────────────────────────────
    private JMenuBar buildMenuBar() {
        JMenuBar mb = new JMenuBar();
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
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);

        JLabel title = new JLabel("  Your Order");
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(new Color(60, 40, 10));

        lblItemCount = new JLabel("0 items");
        lblItemCount.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblItemCount.setForeground(new Color(120, 90, 40));
        lblItemCount.setHorizontalAlignment(SwingConstants.RIGHT);

        p.add(title,      BorderLayout.WEST);
        p.add(lblItemCount, BorderLayout.EAST);
        return p;
    }

    // ── Cart table ────────────────────────────────────────────────────────────
    private JPanel buildCartPanel() {
        String[] cols = { "Item", "Category", "Qty", "Unit Price", "Line Total" };
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == 2; }
            @Override public Class<?> getColumnClass(int c) {
                return c == 2 ? Integer.class : (c >= 3 ? Double.class : String.class);
            }
        };
        tableModel.addTableModelListener(e -> refreshTotals());

        cartTable = new JTable(tableModel);
        cartTable.setRowHeight(28);
        cartTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cartTable.setSelectionBackground(new Color(196, 150, 60));
        cartTable.setSelectionForeground(Color.WHITE);
        cartTable.setGridColor(new Color(220, 210, 190));
        cartTable.setShowGrid(true);

        // Header style
        JTableHeader header = cartTable.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 12));
        header.setBackground(new Color(80, 50, 15));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 28));

        // Column widths
        int[] widths = { 220, 100, 60, 90, 90 };
        for (int i = 0; i < widths.length; i++)
            cartTable.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        // Right-align numeric columns
        DefaultTableCellRenderer rightAlign = new DefaultTableCellRenderer() {
            { setHorizontalAlignment(RIGHT); }
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                if (v instanceof Double) v = FMT.format(v);
                return super.getTableCellRendererComponent(t, v, sel, foc, row, col);
            }
        };
        cartTable.getColumnModel().getColumn(3).setCellRenderer(rightAlign);
        cartTable.getColumnModel().getColumn(4).setCellRenderer(rightAlign);

        // Center-align qty column
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        cartTable.getColumnModel().getColumn(2).setCellRenderer(center);

        // Spinner editor for Qty
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        cartTable.getColumnModel().getColumn(2).setCellEditor(new SpinnerEditor(spinner));

        JScrollPane sp = new JScrollPane(cartTable);
        sp.setPreferredSize(new Dimension(0, 170));
        sp.setBorder(BorderFactory.createLineBorder(new Color(160, 130, 80), 1));

        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.add(sp, BorderLayout.CENTER);
        return p;
    }

    // ── Buttons below table ───────────────────────────────────────────────────
    private JPanel buildButtonRow() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        p.setOpaque(false);

        JButton btnRemove = styledButton("Remove Selected", new Color(180, 60, 40));
        btnRemove.addActionListener(e -> removeSelected());

        JButton btnAdd = styledButton("+ Add Item", new Color(60, 130, 60));
        btnAdd.addActionListener(e -> addNextItem());

        JButton btnClear = styledButton("Clear Cart", new Color(100, 80, 40));
        btnClear.addActionListener(e -> clearAll());

        p.add(btnRemove);
        p.add(btnAdd);
        p.add(btnClear);
        return p;
    }

    // ── Summary panel ─────────────────────────────────────────────────────────
    private JPanel buildSummaryPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(titledBorder("Order Summary"));
        p.setBackground(new Color(255, 252, 245));

        GridBagConstraints l = new GridBagConstraints();
        l.anchor = GridBagConstraints.WEST;
        l.insets = new Insets(3, 8, 3, 4);
        l.gridx = 0; l.gridy = 0; l.weightx = 1.0; l.fill = GridBagConstraints.HORIZONTAL;

        GridBagConstraints r = new GridBagConstraints();
        r.anchor = GridBagConstraints.EAST;
        r.insets = new Insets(3, 4, 3, 12);
        r.gridx = 1; r.gridy = 0;

        Font labelFont = new Font("SansSerif", Font.PLAIN, 13);
        Font valueFont = new Font("Monospaced", Font.PLAIN, 13);
        Font totalFont = new Font("SansSerif", Font.BOLD, 14);

        String[] labels = { "Subtotal:", "Delivery fee:", "Tax (8%):", "Total:" };
        lblSubtotal = moneyLabel(valueFont);
        JLabel lblDelivery = new JLabel(FMT.format(DELIVERY_FEE));
        lblDelivery.setFont(valueFont);
        lblTax   = moneyLabel(valueFont);
        lblTotal = moneyLabel(totalFont);
        lblTotal.setForeground(new Color(160, 80, 0));

        JLabel[] values = { lblSubtotal, lblDelivery, lblTax, lblTotal };

        for (int i = 0; i < labels.length; i++) {
            l.gridy = i;
            r.gridy = i;
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(i == 3 ? totalFont : labelFont);
            if (i == 3) {
                JSeparator sep = new JSeparator();
                GridBagConstraints sc = new GridBagConstraints();
                sc.gridx = 0; sc.gridy = i; sc.gridwidth = 2;
                sc.fill = GridBagConstraints.HORIZONTAL;
                sc.insets = new Insets(4, 8, 4, 8);
                // insert separator row before Total
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

    // ── Promo panel ───────────────────────────────────────────────────────────
    private JPanel buildPromoPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        p.setBorder(titledBorder("Promo Code"));
        p.setBackground(new Color(255, 252, 245));

        tfPromo = new JTextField(14);
        tfPromo.setFont(new Font("Monospaced", Font.PLAIN, 13));
        tfPromo.setToolTipText("Try: BELLA10");

        JButton btnApply = styledButton("Apply", new Color(60, 100, 160));
        btnApply.addActionListener(e -> applyPromo());

        lblPromoMsg = new JLabel("");
        lblPromoMsg.setFont(new Font("SansSerif", Font.ITALIC, 12));

        p.add(new JLabel("Code:"));
        p.add(tfPromo);
        p.add(btnApply);
        p.add(lblPromoMsg);
        return p;
    }

    // ── Delivery panel ────────────────────────────────────────────────────────
    private JPanel buildDeliveryPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(titledBorder("Delivery Details"));
        p.setBackground(new Color(255, 252, 245));

        GridBagConstraints lc = new GridBagConstraints();
        lc.anchor = GridBagConstraints.WEST;
        lc.insets = new Insets(4, 8, 4, 4);

        GridBagConstraints fc = new GridBagConstraints();
        fc.fill = GridBagConstraints.HORIZONTAL;
        fc.weightx = 1.0;
        fc.insets = new Insets(4, 0, 4, 12);

        Font f = new Font("SansSerif", Font.PLAIN, 13);
        Font tf = new Font("Monospaced", Font.PLAIN, 13);

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
            lbl.setFont(f);
            fields[i].setFont(tf);
            p.add(lbl,      lc);
            p.add(fields[i], fc);
        }
        return p;
    }

    // ── Action row ────────────────────────────────────────────────────────────
    private JPanel buildActionRow() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        p.setOpaque(false);

        JButton btnCancel = styledButton("Cancel", new Color(130, 60, 40));
        btnCancel.addActionListener(e -> {
            int ok = JOptionPane.showConfirmDialog(this,
                "Discard your order and close?", "Cancel Order",
                JOptionPane.YES_NO_OPTION);
            if (ok == JOptionPane.YES_OPTION) System.exit(0);
        });

        JButton btnPlace = new JButton("Place Order  ▶");
        btnPlace.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnPlace.setBackground(new Color(80, 140, 60));
        btnPlace.setForeground(Color.WHITE);
        btnPlace.setFocusPainted(false);
        btnPlace.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(50, 100, 40), 1),
            BorderFactory.createEmptyBorder(6, 18, 6, 18)));
        btnPlace.addActionListener(e -> placeOrder());

        p.add(btnCancel);
        p.add(btnPlace);
        return p;
    }

    // ── Status bar ────────────────────────────────────────────────────────────
    private JPanel buildStatusBar() {
        JPanel p = new JPanel(new BorderLayout(8, 0));
        p.setBackground(new Color(220, 210, 190));
        p.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(180, 160, 120)));
        p.setPreferredSize(new Dimension(0, 24));

        lblStatus = new JLabel("  Ready");
        lblStatus.setFont(new Font("SansSerif", Font.PLAIN, 12));
        p.add(lblStatus, BorderLayout.WEST);

        JLabel version = new JLabel("Bistro Bella POS v1.0  ");
        version.setFont(new Font("SansSerif", Font.PLAIN, 11));
        version.setForeground(new Color(120, 100, 60));
        p.add(version, BorderLayout.EAST);
        return p;
    }

    // ── Data helpers ──────────────────────────────────────────────────────────
    private void loadInitialData() {
        for (Object[] row : menuData) addRow(row);
    }

    private void addRow(Object[] row) {
        String name  = (String)  row[0];
        String cat   = (String)  row[1];
        int    qty   = ((Number) row[2]).intValue();
        double price = ((Number) row[3]).doubleValue();
        tableModel.addRow(new Object[]{ name, cat, qty, price, price * qty });
    }

    private void refreshTotals() {
        double sub = 0;
        int    cnt = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            int    qty   = (Integer) tableModel.getValueAt(i, 2);
            double price = (Double)  tableModel.getValueAt(i, 3);
            double line  = qty * price;
            tableModel.setValueAt(line, i, 4);   // update line total silently
            sub += line;
            cnt += qty;
        }
        double tax = sub * TAX_RATE;
        double tot = sub + tax + DELIVERY_FEE;
        lblSubtotal.setText(FMT.format(sub));
        lblTax.setText(FMT.format(tax));
        lblTotal.setText(FMT.format(tot));
        lblItemCount.setText(cnt + " item" + (cnt != 1 ? "s" : "") + " in cart");
        lblStatus.setText("  Cart updated — " + tableModel.getRowCount() + " product(s)");
    }

    private void removeSelected() {
        int row = cartTable.getSelectedRow();
        if (row >= 0) {
            tableModel.removeRow(row);
            setStatus("Item removed.");
        } else {
            JOptionPane.showMessageDialog(this,
                "Please select a row to remove.", "No selection",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void addNextItem() {
        Object[] item = extraItems[extraIdx % extraItems.length];
        extraIdx++;
        // check if already in cart
        String name = (String) item[0];
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 0).equals(name)) {
                int qty = (Integer) tableModel.getValueAt(i, 2) + 1;
                tableModel.setValueAt(qty, i, 2);
                setStatus("\"" + name + "\" qty increased.");
                return;
            }
        }
        addRow(item);
        setStatus("\"" + name + "\" added to cart.");
    }

    private void clearAll() {
        int ok = JOptionPane.showConfirmDialog(this,
            "Remove all items from cart?", "Clear Cart",
            JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            tableModel.setRowCount(0);
            setStatus("Cart cleared.");
        }
    }

    private void applyPromo() {
        String code = tfPromo.getText().trim().toUpperCase();
        if (code.equals("BELLA10")) {
            lblPromoMsg.setForeground(new Color(0, 120, 0));
            lblPromoMsg.setText("10% discount applied!");
        } else if (code.isEmpty()) {
            lblPromoMsg.setForeground(new Color(180, 0, 0));
            lblPromoMsg.setText("Please enter a code.");
        } else {
            lblPromoMsg.setForeground(new Color(180, 0, 0));
            lblPromoMsg.setText("Invalid code. Try BELLA10.");
        }
    }

    private void placeOrder() {
        if (tableModel.getRowCount() == 0) {
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
        l.setHorizontalAlignment(SwingConstants.RIGHT);
        return l;
    }

    private Border titledBorder(String title) {
        return BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(180, 150, 90), 1),
            title,
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("SansSerif", Font.BOLD, 12),
            new Color(100, 70, 20));
    }

    private JButton styledButton(String text, Color fg) {
        JButton b = new JButton(text);
        b.setFont(new Font("SansSerif", Font.PLAIN, 12));
        b.setForeground(fg);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(fg.darker(), 1),
            BorderFactory.createEmptyBorder(4, 12, 4, 12)));
        return b;
    }

    // ── Spinner cell editor ───────────────────────────────────────────────────
    static class SpinnerEditor extends DefaultCellEditor {
        private final JSpinner spinner;
        private final JTextField tf;

        SpinnerEditor(JSpinner s) {
            super(new JTextField());
            spinner = s;
            tf = ((JSpinner.DefaultEditor) s.getEditor()).getTextField();
            tf.addActionListener(this::stopCellEditing);
        }

        private void stopCellEditing(ActionEvent e) { fireEditingStopped(); }

        @Override public Component getTableCellEditorComponent(
                JTable table, Object value, boolean isSelected, int row, int col) {
            spinner.setValue(value instanceof Integer ? value : 1);
            return spinner;
        }

        @Override public Object getCellEditorValue() { return spinner.getValue(); }
        @Override public boolean stopCellEditing() { fireEditingStopped(); return true; }
    }

    // ── Main ──────────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        SwingUtilities.invokeLater(RestaurantCart::new);
    }
}