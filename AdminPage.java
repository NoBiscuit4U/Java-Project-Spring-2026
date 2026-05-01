import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * AdminPage — extends JPanel, plug into RestaurantApp's CardLayout:
 *
 *   contentPanel.add(new AdminPage(), "ADMIN");
 *
 * Then navigate with:
 *   cardLayout.show(contentPanel, "ADMIN");
 */
public class AdminPage extends JPanel {

    // ── Shared palette ───────────────────────────────────────────────────────
    static final Color RED_ACCENT  = new Color(220, 60, 60);
    static final Color DARK_BG     = new Color(30, 30, 30);
    static final Color LIGHT_BG    = new Color(248, 248, 248);
    static final Color CARD_BG     = new Color(255, 255, 255);
    static final Color TEXT_DARK   = new Color(30, 30, 30);
    static final Color TEXT_MUTED  = new Color(120, 120, 120);
    static final Color FOOTER_BG   = new Color(210, 55, 55);
    static final Color ADMIN_STRIPE = new Color(20, 20, 60);   // dark navy for admin hero

    // ── Product fields ───────────────────────────────────────────────────────
    private JTextField   productIdField;
    private JTextField   productNameField;
    private JTextField   productCostField;
    private JTextField   productNutritionalField;
    private JTextField   productImgField;
    private JComboBox<String> actionDropdown;

    // ── Content / site fields ────────────────────────────────────────────────
    private JTextField   m_mainTitleField;
    private JTextArea    m_headerDescArea;
    private JTextArea    m_bodyDescArea;
    private JTextField   m_mainImgField;

    private JTextArea productLog;

    private ProductManager m_pm;
    private UserManager m_um;

    public AdminPage(ProductManager pm,UserManager um) {
        m_pm=pm;
        m_um=um;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(LIGHT_BG);

        add(buildAdminHero());
        add(buildProductSection());
        add(buildDivider("Site Content"));
        add(buildContentSection());
        add(buildFooter());
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // HERO BANNER
    // ═══════════════════════════════════════════════════════════════════════════

    private JPanel buildAdminHero() {
        JPanel hero = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, ADMIN_STRIPE,
                        getWidth(), getHeight(), new Color(50, 50, 120)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Dot texture
                g2.setColor(new Color(255, 255, 255, 12));
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

        JLabel lock = new JLabel("⚙");
        lock.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 30));
        lock.setForeground(new Color(180, 180, 255));
        lock.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Admin Dashboard");
        title.setFont(new Font("Serif", Font.BOLD, 34));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Manage products and site content from one place.");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        sub.setForeground(new Color(200, 200, 220));
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        content.add(lock);
        content.add(Box.createVerticalStrut(6));
        content.add(title);
        content.add(Box.createVerticalStrut(8));
        content.add(sub);
        hero.add(content);
        return hero;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // PRODUCT MANAGEMENT SECTION
    // ═══════════════════════════════════════════════════════════════════════════

    private JPanel buildProductSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(LIGHT_BG);
        section.setBorder(new EmptyBorder(36, 48, 24, 48));
        section.setAlignmentX(Component.LEFT_ALIGNMENT);

        section.add(makeSectionHeader("⚙  Product Management"));
        section.add(Box.createVerticalStrut(6));
        JLabel hint = new JLabel("Fill in the fields below, choose an action, then press Submit.");
        hint.setFont(new Font("SansSerif", Font.PLAIN, 12));
        hint.setForeground(TEXT_MUTED);
        hint.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.add(hint);
        section.add(Box.createVerticalStrut(24));

        // ── Card shell ───────────────────────────────────────────────────────
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 230), 1),
                new EmptyBorder(28, 32, 28, 32)));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        // Row 1 — Product ID | Name
        JPanel row1 = makeFieldRow(
                "Product ID",   productIdField   = makePlainField("e.g. PRD-001"),
                "Product Name", productNameField = makePlainField("e.g. Margherita Pizza"));
        card.add(row1);
        card.add(Box.createVerticalStrut(16));

        // Row 2 — Cost | Nutritional Value
        JPanel row2 = makeFieldRow(
                "Cost ($)",          productCostField        = makePlainField("e.g. 25.00"),
                "Nutritional Value", productNutritionalField = makePlainField("e.g. 850 kcal"));
        card.add(row2);
        card.add(Box.createVerticalStrut(16));

        // Row 3 — Image path | Action dropdown
        JPanel row3 = makeFieldRow(
                "Image Path / URL", productImgField = makePlainField("e.g. /images/pizza.png"),
                "Action",           buildActionDropdownPanel());
        card.add(row3);
        card.add(Box.createVerticalStrut(24));

        // Submit button + feedback log side by side
        JPanel bottom = new JPanel(new BorderLayout(20, 0));
        bottom.setOpaque(false);
        bottom.setAlignmentX(Component.LEFT_ALIGNMENT);
        bottom.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JButton submitBtn = makeRedButton("SUBMIT");
        submitBtn.setPreferredSize(new Dimension(160, 42));
        submitBtn.addActionListener(e -> handleProductSubmit());

        productLog = new JTextArea(4, 0);
        productLog.setEditable(false);
        productLog.setFont(new Font("Monospaced", Font.PLAIN, 11));
        productLog.setBackground(new Color(245, 245, 250));
        productLog.setForeground(new Color(60, 60, 100));
        productLog.setBorder(new CompoundBorder(
                new LineBorder(new Color(210, 210, 225), 1),
                new EmptyBorder(8, 10, 8, 10)));
        productLog.setText("Action log will appear here...");
        JScrollPane logScroll = new JScrollPane(productLog);
        logScroll.setBorder(null);

        JPanel leftBtn = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftBtn.setOpaque(false);
        leftBtn.add(submitBtn);

        bottom.add(leftBtn,   BorderLayout.WEST);
        bottom.add(logScroll, BorderLayout.CENTER);

        card.add(bottom);
        section.add(card);
        return section;
    }

    // ── Action dropdown wrapped in a panel so makeFieldRow works cleanly ─────
    private JPanel buildActionDropdownPanel() {
        actionDropdown = new JComboBox<>(new String[]{"Add", "Update", "Delete"});
        actionDropdown.setFont(new Font("SansSerif", Font.PLAIN, 13));
        actionDropdown.setBackground(CARD_BG);
        actionDropdown.setForeground(TEXT_DARK);
        actionDropdown.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        actionDropdown.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        // Style the border to match text fields
        actionDropdown.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(210, 200, 195), 1),
                new EmptyBorder(4, 8, 4, 8)));

        JPanel wrap = new JPanel();
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        wrap.setOpaque(false);
        wrap.add(actionDropdown);
        return wrap;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // SITE CONTENT SECTION
    // ═══════════════════════════════════════════════════════════════════════════

    private JPanel buildContentSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(LIGHT_BG);
        section.setBorder(new EmptyBorder(8, 48, 36, 48));
        section.setAlignmentX(Component.LEFT_ALIGNMENT);

        section.add(makeSectionHeader("✏  Site Content"));
        section.add(Box.createVerticalStrut(6));
        JLabel hint = new JLabel("Edit the main page title and descriptions shown to visitors.");
        hint.setFont(new Font("SansSerif", Font.PLAIN, 12));
        hint.setForeground(TEXT_MUTED);
        hint.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.add(hint);
        section.add(Box.createVerticalStrut(24));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 230), 1),
                new EmptyBorder(28, 32, 28, 32)));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));


        // Main Img(single line)
        card.add(makeFieldLabel("Banner Img"));
        card.add(Box.createVerticalStrut(6));
        m_mainImgField = makePlainField("e.g. /dir/img");
        m_mainImgField.setFont(new Font("Serif", Font.BOLD, 15));
        card.add(m_mainImgField);
        card.add(Box.createVerticalStrut(20));

        // Main Title (single line)
        card.add(makeFieldLabel("Main Title"));
        card.add(Box.createVerticalStrut(6));
        m_mainTitleField = makePlainField("e.g. Welcome to LZ Restaurant");
        m_mainTitleField.setFont(new Font("Serif", Font.BOLD, 15));
        card.add(m_mainTitleField);
        card.add(Box.createVerticalStrut(20));

        // Header Description (short, ~2 lines)
        card.add(makeFieldLabel("Header Description"));
        card.add(Box.createVerticalStrut(6));
        m_headerDescArea = makeTextArea(3,"");
        card.add(scrollWrap(m_headerDescArea, 80));
        card.add(Box.createVerticalStrut(20));

        // Body Description (longer, ~5 lines)
        card.add(makeFieldLabel("Body Description"));
        card.add(Box.createVerticalStrut(6));
        m_bodyDescArea = makeTextArea(6,"");
        card.add(scrollWrap(m_bodyDescArea, 130));
        card.add(Box.createVerticalStrut(24));

        // Save button
        JButton saveBtn = makeRedButton("SAVE CONTENT");
        saveBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        saveBtn.addActionListener(e -> handleContentSave());
        card.add(saveBtn);

        section.add(card);
        return section;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // SUBMIT HANDLERS
    // ═══════════════════════════════════════════════════════════════════════════

    private HashMap<String,Object> getProductMap(){
        HashMap<String,Object> map=new HashMap<String,Object>();
        String id       = productIdField.getText().trim();
        String name     = productNameField.getText().trim();
        String cost     = productCostField.getText().trim();
        String nutritVal = productNutritionalField.getText().trim();
        String img      = productImgField.getText().trim();

        if(!name.equals("")){
			map.put(Constants.obj_query_cons.kname_qry,name);
		}
		if(!cost.equals("")){
			map.put(Constants.obj_query_cons.kcost_qry,Double.parseDouble(cost));
		}
		if(!nutritVal.equals("")){
			map.put(Constants.obj_query_cons.knutrit_qry,nutritVal);
		}
		if(!img.equals("")){
			map.put(Constants.obj_query_cons.kimg_qry,img);
		}

        return map;
    }

    private void handleProductSubmit() {
        String id       = productIdField.getText().trim();
        String name     = productNameField.getText().trim();
        String cost     = productCostField.getText().trim();
        String nutritVal = productNutritionalField.getText().trim();
        String img      = productImgField.getText().trim();
        String action   = (String) actionDropdown.getSelectedItem();

        // ── Validation ───────────────────────────────────────────────────────
        if (id.isEmpty()) {
            log("⚠  Product ID is required for all operations");
            return;
        }

        // ── Simulate DB operations ───────────────────────────────────────────
        switch (action) {
            case "Add":
                if(!id.equals("") && !name.equals("") && !cost.equals("")
                    && !nutritVal.equals("") && !img.equals("")){                   
                    m_pm.createProduct(new ArrayList<Object>(){{
                        add(name);
                        add(Integer.parseInt(id));
                        add(Double.parseDouble(cost));
                        add(nutritVal);
                        add(img);
                    }});

                    log("✔  Added Product, ID: ("+id+") Name: ("+name+")");
                }else{
                    log("⚠  All Fields must be populated");
                }
                clearProductFields();
            break;
            case "Update":
                m_pm.runDynamicUpdate(getProductMap(),Integer.parseInt(id));
                log("✔  Updated Product, ID: ("+id+")");
            break;
            case "Delete":
                m_pm.deleteProduct(Integer.parseInt(id));
                log("✔  Deleted Product, ID: ("+id+")");
            break;
        }
    }

    private void handleContentSave(){
        m_um.runDynamicUpdate(getSiteContentMap(),1);
    }

    private HashMap<String,Object> getSiteContentMap(){
        HashMap<String,Object> map=new HashMap<String,Object>();
        String mainTitle   = m_mainTitleField.getText().trim();
        String headerDesc  = m_headerDescArea.getText().trim();
        String bodyDesc    = m_bodyDescArea.getText().trim();
        String mainImg=m_mainImgField.getText().trim();

        if(!mainTitle.isEmpty()) {
            map.put(Constants.obj_query_cons.kmainTitle_qry,mainTitle);
        }
        if(!headerDesc.isEmpty()){
            map.put(Constants.obj_query_cons.kheaderDesc_qry,headerDesc);
        }
        if(!bodyDesc.isEmpty()){
            map.put(Constants.obj_query_cons.kbodyDesc_qry,bodyDesc);
        }
        if(!mainImg.isEmpty()){
            map.put(Constants.obj_query_cons.kbannerImg_qry,mainImg);
        }

        return map;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // HELPERS
    // ═══════════════════════════════════════════════════════════════════════════

    /** Append a line to the action log area. */
    private void log(String message) {
        String current = productLog.getText();
        if (current.equals("Action log will appear here...")) current = "";
        productLog.setText((current.isEmpty() ? "" : current + "\n") + message);
        productLog.setCaretPosition(productLog.getDocument().getLength());
    }

    /** Clear all product input fields after a successful operation. */
    private void clearProductFields() {
        productIdField.setText("");
        productNameField.setText("");
        productCostField.setText("");
        productNutritionalField.setText("");
        productImgField.setText("");
        actionDropdown.setSelectedIndex(0);
    }

    /** Two-column field row with labels above each field. */
    private JPanel makeFieldRow(String label1, JComponent field1,
                                 String label2, JComponent field2) {
        JPanel row = new JPanel(new GridLayout(1, 2, 20, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        row.add(wrapWithLabel(label1, field1));
        row.add(wrapWithLabel(label2, field2));
        return row;
    }

    private JPanel wrapWithLabel(String labelText, JComponent field) {
        JPanel wrap = new JPanel();
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        wrap.setOpaque(false);
        wrap.add(makeFieldLabel(labelText));
        wrap.add(Box.createVerticalStrut(5));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrap.add(field);
        return wrap;
    }

    private JLabel makeFieldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        lbl.setForeground(TEXT_MUTED);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JTextField makePlainField(String placeholder) {
        JTextField field = new JTextField() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setColor(new Color(190, 180, 175));
                    g2.setFont(getFont().deriveFont(Font.ITALIC));
                    g2.drawString(placeholder, 10, getHeight() / 2 + 5);
                }
            }
        };
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setForeground(TEXT_DARK);
        field.setBackground(new Color(252, 249, 247));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(210, 200, 195), 1),
                new EmptyBorder(10, 12, 10, 12)));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(RED_ACCENT, 1),
                        new EmptyBorder(10, 12, 10, 12)));
            }
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(210, 200, 195), 1),
                        new EmptyBorder(10, 12, 10, 12)));
            }
        });
        return field;
    }

    private JTextArea makeTextArea(int rows, String placeholder) {
        JTextArea area = new JTextArea(rows, 0);
        area.setFont(new Font("SansSerif", Font.PLAIN, 13));
        area.setForeground(TEXT_DARK);
        area.setBackground(new Color(252, 249, 247));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(210, 200, 195), 1),
                new EmptyBorder(10, 12, 10, 12)));
        // Placeholder via focus
        area.setForeground(new Color(190, 180, 175));
        area.setText(placeholder);
        area.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (area.getText().equals(placeholder)) {
                    area.setText("");
                    area.setForeground(TEXT_DARK);
                }
                area.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(RED_ACCENT, 1),
                        new EmptyBorder(10, 12, 10, 12)));
            }
            public void focusLost(FocusEvent e) {
                if (area.getText().isEmpty()) {
                    area.setForeground(new Color(190, 180, 175));
                    area.setText(placeholder);
                }
                area.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(210, 200, 195), 1),
                        new EmptyBorder(10, 12, 10, 12)));
            }
        });
        return area;
    }

    private JScrollPane scrollWrap(JTextArea area, int height) {
        JScrollPane sp = new JScrollPane(area);
        sp.setBorder(null);
        sp.setAlignmentX(Component.LEFT_ALIGNMENT);
        sp.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
        sp.setPreferredSize(new Dimension(0, height));
        return sp;
    }

    /** Visual divider with a centred label between two sections. */
    private JPanel buildDivider(String label) {
        JPanel row = new JPanel(new BorderLayout(16, 0));
        row.setBackground(LIGHT_BG);
        row.setBorder(new EmptyBorder(8, 48, 0, 48));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        JSeparator left  = new JSeparator(); left.setForeground(new Color(210, 200, 220));
        JSeparator right = new JSeparator(); right.setForeground(new Color(210, 200, 220));
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        lbl.setForeground(TEXT_MUTED);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);

        row.add(left,  BorderLayout.WEST);
        row.add(lbl,   BorderLayout.CENTER);
        row.add(right, BorderLayout.EAST);
        return row;
    }

    private JLabel makeSectionHeader(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 24));
        lbl.setForeground(ADMIN_STRIPE);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JPanel buildFooter() {
        JPanel footer = new JPanel(new GridBagLayout());
        footer.setBackground(FOOTER_BG);
        footer.setPreferredSize(new Dimension(0, 46));
        footer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        footer.setAlignmentX(Component.LEFT_ALIGNMENT);
        return footer;
    }

    private JButton makeRedButton(String label) {
        JButton btn = new JButton(label) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(190, 40, 40) : RED_ACCENT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(160, 42));
        btn.setOpaque(false);
        return btn;
    }
}