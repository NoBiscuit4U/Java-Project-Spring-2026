import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shared cart grid: columns Item, Qty, Cost (line total); merges duplicate product IDs;
 * qty edits sync back to {@link Cart}.
 */
public class CartTablePanel extends JPanel {

    static final Color CARD_BG    = new Color(255, 255, 255);
    /** Header strip behind column titles (readable with black labels). */
    private static final Color HEADER_BG = new Color(242, 242, 242);

    private static final DecimalFormat FMT = new DecimalFormat("$#,##0.00");

    private final Cart cart;
    private DefaultTableModel tableModel;
    private JTable cartTable;
    private boolean syncingTable;
    private final ArrayList<Integer> rowProductIds = new ArrayList<>();
    private final ArrayList<Double> rowUnitPrices = new ArrayList<>();
    private Runnable onModified;
    private double lastSubtotal;
    private int lastItemCount;
    private int scrollHeight = 200;
    private final JScrollPane tableScroll;

    public CartTablePanel(Cart cart) {
        this.cart = cart;
        setLayout(new BorderLayout());
        setOpaque(false);

        tableModel = new DefaultTableModel(new String[] { "Item", "Qty", "Cost" }, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return c == 1;
            }

            @Override
            public Class<?> getColumnClass(int c) {
                if (c == 1) return Integer.class;
                if (c == 2) return Double.class;
                return String.class;
            }
        };

        tableModel.addTableModelListener(e -> {
            if (syncingTable)
                return;
            if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 2)
                return;
            if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 1) {
                applyDisplayQtyChange(e.getFirstRow());
                return;
            }
            updateDerivedColumnsAndTotals();
            fireModified();
        });

        cartTable = new JTable(tableModel);
        cartTable.setRowHeight(28);
        cartTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cartTable.setBackground(CARD_BG);
        cartTable.setForeground(Color.BLACK);
        cartTable.setSelectionBackground(new Color(220, 60, 60));
        cartTable.setSelectionForeground(Color.WHITE);
        cartTable.setGridColor(new Color(220, 220, 220));
        cartTable.setShowGrid(true);

        JTableHeader header = cartTable.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 12));
        header.setOpaque(true);
        header.setBackground(HEADER_BG);
        header.setForeground(Color.BLACK);
        header.setDefaultRenderer(createHeaderRenderer(header.getFont()));
        header.setPreferredSize(new Dimension(0, 30));

        int[] widths = { 320, 64, 100 };
        for (int i = 0; i < widths.length; i++)
            cartTable.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        DefaultTableCellRenderer rightAlign = new DefaultTableCellRenderer() {
            {
                setHorizontalAlignment(RIGHT);
            }

            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                if (v instanceof Double) v = FMT.format(v);
                Component c = super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                if (!sel)
                    c.setForeground(Color.BLACK);
                return c;
            }
        };
        cartTable.getColumnModel().getColumn(2).setCellRenderer(rightAlign);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer() {
            {
                setHorizontalAlignment(SwingConstants.CENTER);
            }

            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                if (!sel)
                    c.setForeground(Color.BLACK);
                return c;
            }
        };
        cartTable.getColumnModel().getColumn(1).setCellRenderer(center);

        cartTable.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                if (!sel)
                    c.setForeground(Color.BLACK);
                return c;
            }
        });

        JSpinner spinner = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        cartTable.getColumnModel().getColumn(1).setCellEditor(new SpinnerEditor(spinner));

        tableScroll = new JScrollPane(cartTable);
        tableScroll.setPreferredSize(new Dimension(0, scrollHeight));
        tableScroll.setBorder(null);
        tableScroll.setBackground(CARD_BG);
        cartTable.setOpaque(true);

        JPanel cardWrap = new JPanel(new BorderLayout(0, 0));
        cardWrap.setBackground(CARD_BG);
        cardWrap.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(16, 16, 16, 16)));
        cardWrap.add(tableScroll, BorderLayout.CENTER);
        add(cardWrap, BorderLayout.CENTER);
    }

    public void setScrollPreferredHeight(int h) {
        this.scrollHeight = h;
        tableScroll.setPreferredSize(new Dimension(0, h));
    }

    public void setOnModified(Runnable onModified) {
        this.onModified = onModified;
    }

    public JTable getCartTable() {
        return cartTable;
    }

    public int getRowCount() {
        return tableModel.getRowCount();
    }

    public boolean isEmpty() {
        return tableModel.getRowCount() == 0;
    }

    public double getSubtotal() {
        return lastSubtotal;
    }

    public int getTotalItemCount() {
        return lastItemCount;
    }

    /** Recompute Cost cells and cached subtotal / item count. */
    public void updateDerivedColumnsAndTotals() {
        double sub = 0;
        int cnt = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            int qty = ((Number) tableModel.getValueAt(i, 1)).intValue();
            double unit = i < rowUnitPrices.size() ? rowUnitPrices.get(i) : 0;
            double line = qty * unit;
            Object cur = tableModel.getValueAt(i, 2);
            if (!(cur instanceof Double) || Double.compare((Double) cur, line) != 0)
                tableModel.setValueAt(line, i, 2);
            sub += line;
            cnt += qty;
        }
        lastSubtotal = sub;
        lastItemCount = cnt;
    }

    public void syncFromCart() {
        syncingTable = true;
        try {
            rowProductIds.clear();
            rowUnitPrices.clear();
            Map<Integer, AggLine> merged = new LinkedHashMap<>();
            for (Product p : cart.getPdcts()) {
                int id = p.getID();
                merged.computeIfAbsent(id, k -> new AggLine(p));
                merged.get(id).qty++;
            }
            tableModel.setRowCount(0);
            for (AggLine line : merged.values()) {
                rowProductIds.add(line.prototype.getID());
                int q = line.qty;
                double unit = line.prototype.getCost();
                rowUnitPrices.add(unit);
                tableModel.addRow(new Object[] { line.prototype.getName(), q, q * unit });
            }
        } finally {
            syncingTable = false;
        }
        updateDerivedColumnsAndTotals();
        fireModified();
    }

    private void fireModified() {
        if (onModified != null)
            onModified.run();
    }

    private void applyDisplayQtyChange(int row) {
        if (row < 0 || row >= rowProductIds.size())
            return;
        int pid = rowProductIds.get(row);
        Object qv = tableModel.getValueAt(row, 1);
        int newQty = qv instanceof Number ? ((Number) qv).intValue() : 1;
        if (newQty < 1)
            newQty = 1;

        Product template = null;
        for (Product p : cart.getPdcts()) {
            if (p.getID() == pid) {
                template = p;
                break;
            }
        }
        if (template == null)
            return;

        ArrayList<Product> list = cart.getPdcts();
        list.removeIf(p -> p.getID() == pid);
        for (int i = 0; i < newQty; i++)
            list.add(template);

        syncFromCart();
    }

    /** @return false if nothing selected */
    public boolean removeSelected() {
        int row = cartTable.getSelectedRow();
        if (row < 0 || row >= rowProductIds.size() || row >= rowUnitPrices.size())
            return false;
        int pid = rowProductIds.get(row);
        cart.getPdcts().removeIf(p -> p.getID() == pid);
        syncFromCart();
        return true;
    }

    private static DefaultTableCellRenderer createHeaderRenderer(final Font font) {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setForeground(Color.BLACK);
                setBackground(HEADER_BG);
                setFont(font);
                setOpaque(true);
                if (column == 1)
                    setHorizontalAlignment(SwingConstants.CENTER);
                else if (column == 2)
                    setHorizontalAlignment(SwingConstants.RIGHT);
                else
                    setHorizontalAlignment(SwingConstants.LEADING);
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)),
                        BorderFactory.createEmptyBorder(4, 8, 4, 8)));
                return this;
            }
        };
    }

    private static final class AggLine {
        final Product prototype;
        int qty;

        AggLine(Product prototype) {
            this.prototype = prototype;
            this.qty = 0;
        }
    }

    static class SpinnerEditor extends DefaultCellEditor {
        private final JSpinner spinner;

        SpinnerEditor(JSpinner s) {
            super(new JTextField());
            spinner = s;
            JTextField tf = ((JSpinner.DefaultEditor) s.getEditor()).getTextField();
            tf.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(
                JTable table, Object value, boolean isSelected, int row, int col) {
            spinner.setValue(value instanceof Integer ? value : 1);
            return spinner;
        }

        @Override
        public Object getCellEditorValue() {
            return spinner.getValue();
        }

        @Override
        public boolean stopCellEditing() {
            fireEditingStopped();
            return true;
        }
    }
}
