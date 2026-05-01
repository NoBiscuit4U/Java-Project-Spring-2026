import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.ArrayList;
import java.util.Arrays;

public class LoginPage extends JPanel{
    private UserManager m_um;

    private JPanel m_loginCard;
    private JPanel m_logoutCard;
    private JPanel m_createAccountCard;

    // ── Shared palette (matches RestaurantApp) ───────────────────────────────
    static final Color RED_ACCENT=new Color(220,60,60);
    static final Color DARK_BG=new Color(30,30,30);
    static final Color LIGHT_BG=new Color(248,248,248);
    static final Color CARD_BG=new Color(255,255,255);
    static final Color TEXT_DARK=new Color(30,30,30);
    static final Color TEXT_MUTED=new Color(120,120,120);
    static final Color FOOTER_BG=new Color(210,55,55);

    public LoginPage(UserManager um){
        m_um=um;
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        setBackground(LIGHT_BG);

        m_loginCard=buildLoginCard();
        m_logoutCard=buildLogoutCard();
        m_logoutCard.setVisible(false);
        m_createAccountCard=buildCreateAccountCard();
        m_createAccountCard.setVisible(false);

        add(m_loginCard); 
        add(m_logoutCard); 
        add(m_createAccountCard); 
        add(buildFooter());
    }

    // ─── LOGIN CARD ──────────────────────────────────────────────────────────
    private JPanel buildLoginCard(){
        // Warm gradient outer panel — mirrors the hero banner style
        JPanel outer=new JPanel(new GridBagLayout()){
            @Override protected void paintComponent(Graphics g){
                super.paintComponent(g);
                Graphics2D g2=(Graphics2D) g;
                g2.setPaint(new GradientPaint(
                        0,0,new Color(245,238,232),getWidth(),getHeight(),new Color(235,220,210)));
                g2.fillRect(0,0,getWidth(),getHeight());
                // Subtle dot texture
                g2.setColor(new Color(180,140,120,40));
                for (int x=0; x<getWidth(); x+=28)
                    for (int y=0; y<getHeight(); y+=28)
                        g2.fillOval(x,y,3,3);
            }
        };
        // Let the outer fill all available space above the footer
        outer.setMaximumSize(new Dimension(Integer.MAX_VALUE,Integer.MAX_VALUE));
        outer.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ── White card ──
        JPanel card=new JPanel();
        card.setLayout(new BoxLayout(card,BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220,210,205),1,true),new EmptyBorder(36,40,36,40)));
        card.setPreferredSize(new Dimension(360,500));

        // Logo mark
        JLabel icon=new JLabel("✦");
        icon.setFont(new Font("Serif",Font.PLAIN,32));
        icon.setForeground(RED_ACCENT);
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Title
        JLabel title=new JLabel("Welcome Back");
        title.setFont(new Font("Serif",Font.BOLD|Font.ITALIC,26));
        title.setForeground(TEXT_DARK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle=new JLabel("Sign in to your account");
        subtitle.setFont(new Font("SansSerif",Font.PLAIN,13));
        subtitle.setForeground(TEXT_MUTED);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Divider
        JSeparator sep=new JSeparator();
        sep.setForeground(new Color(235,225,220));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE,1));

        // ID field
        JLabel idLabel=makeFieldLabel("User ID");
        idLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField idField=makeTextField("Enter your user ID");
        idField.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Password field
        JLabel pwLabel=makeFieldLabel("Password");
        pwLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField pwField=makeTextField("Enter your password");
        pwField.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Sign in button
        JButton submitBtn=makeRedButton("SIGN IN");
        submitBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE,42));
        submitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitBtn.addActionListener(e->handleLogin(idField,pwField));

        // OR divider
        JPanel orRow=buildOrDivider();

        // Create account button
        JButton createBtn=makeOutlineButton("Create New Account");
        createBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE,42));
        createBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        createBtn.addActionListener(e->openAccountCreation());

        // Assemble
        card.add(icon);
        card.add(Box.createVerticalStrut(10));
        card.add(title);
        card.add(Box.createVerticalStrut(4));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(24));
        card.add(sep);
        card.add(Box.createVerticalStrut(24));
        card.add(idLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(idField);
        card.add(Box.createVerticalStrut(18));
        card.add(pwLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(pwField);
        card.add(Box.createVerticalStrut(6));
        card.add(Box.createVerticalStrut(24));
        card.add(submitBtn);
        card.add(Box.createVerticalStrut(20));
        card.add(orRow);
        card.add(Box.createVerticalStrut(20));
        card.add(createBtn);

        outer.add(card);
        return outer;
    }

    private JPanel buildCreateAccountCard(){
        // Warm gradient outer panel — mirrors the hero banner style
        JPanel outer=new JPanel(new GridBagLayout()){
            @Override protected void paintComponent(Graphics g){
                super.paintComponent(g);
                Graphics2D g2=(Graphics2D) g;
                g2.setPaint(new GradientPaint(
                        0,0,new Color(245,238,232),getWidth(),getHeight(),new Color(235,220,210)));
                g2.fillRect(0,0,getWidth(),getHeight());
                // Subtle dot texture
                g2.setColor(new Color(180,140,120,40));
                for (int x=0; x<getWidth(); x+=28)
                    for (int y=0; y<getHeight(); y+=28)
                        g2.fillOval(x,y,3,3);
            }
        };
        // Let the outer fill all available space above the footer
        outer.setMaximumSize(new Dimension(Integer.MAX_VALUE,Integer.MAX_VALUE));
        outer.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ── White card ──
        JPanel card=new JPanel();
        card.setLayout(new BoxLayout(card,BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220,210,205),1,true),new EmptyBorder(36,40,36,40)));
        card.setPreferredSize(new Dimension(360,550));

        // Logo mark
        JLabel icon=new JLabel("✦");
        icon.setFont(new Font("Serif",Font.PLAIN,32));
        icon.setForeground(RED_ACCENT);
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle=new JLabel("Create your account");
        subtitle.setFont(new Font("SansSerif",Font.PLAIN,13));
        subtitle.setForeground(TEXT_MUTED);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSeparator sep=new JSeparator();
        sep.setForeground(new Color(235,225,220));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE,1));

        JLabel idLabel=makeFieldLabel("User ID");
        idLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField idField=makeTextField("Enter your user ID");
        idField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameLabel=makeFieldLabel("User Name");
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField nameField=makeTextField("Enter your Name");
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel emailLabel=makeFieldLabel("Email");
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField emailField=makeTextField("Enter your Email");
        emailField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel pwLabel=makeFieldLabel("Password");
        pwLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField pwField=makeTextField("Enter your Password");
        pwField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton submitBtn=makeRedButton("Create Account");
        submitBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE,42));
        submitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitBtn.addActionListener(e->handleAccountCreation(idField,pwField,nameField,emailField));

        card.add(icon);
        card.add(Box.createVerticalStrut(10));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(24));
        card.add(sep);
        card.add(Box.createVerticalStrut(24));
        card.add(idLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(idField);
        card.add(Box.createVerticalStrut(18));
        card.add(nameLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(nameField);
        card.add(Box.createVerticalStrut(18));
        card.add(emailLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(emailField);
        card.add(Box.createVerticalStrut(18));
        card.add(pwLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(pwField);
        card.add(Box.createVerticalStrut(24));
        card.add(submitBtn);
        card.add(Box.createVerticalStrut(20));

        outer.add(card);
        return outer;
    }

    private JPanel buildLogoutCard(){
        // Warm gradient outer panel — mirrors the hero banner style
        JPanel outer=new JPanel(new GridBagLayout()){
            @Override protected void paintComponent(Graphics g){
                super.paintComponent(g);
                Graphics2D g2=(Graphics2D) g;
                g2.setPaint(new GradientPaint(
                        0,0,new Color(245,238,232),getWidth(),getHeight(),new Color(235,220,210)));
                g2.fillRect(0,0,getWidth(),getHeight());
                // Subtle dot texture
                g2.setColor(new Color(180,140,120,40));
                for (int x=0; x<getWidth(); x+=28)
                    for (int y=0; y<getHeight(); y+=28)
                        g2.fillOval(x,y,3,3);
            }
        };
        // Let the outer fill all available space above the footer
        outer.setMaximumSize(new Dimension(Integer.MAX_VALUE,Integer.MAX_VALUE));
        outer.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ── White card ──
        JPanel card=new JPanel();
        card.setLayout(new BoxLayout(card,BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220,210,205),1,true),new EmptyBorder(36,40,36,40)));
        card.setPreferredSize(new Dimension(360,440));
        card.setMaximumSize(new Dimension(360,440));

        JButton logoutBtn=makeRedButton("Logout");
        logoutBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE,42));
        logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutBtn.addActionListener(e->handleLogout());

        card.add(logoutBtn);

        outer.add(card);
        return outer;
    }

    // ─── FOOTER ──────────────────────────────────────────────────────────────

    private JPanel buildFooter(){
        JPanel footer=new JPanel(new GridBagLayout());
        footer.setBackground(FOOTER_BG);
        footer.setPreferredSize(new Dimension(0,46));
        footer.setMaximumSize(new Dimension(Integer.MAX_VALUE,46));
        footer.setAlignmentX(Component.LEFT_ALIGNMENT);
        return footer;
    }

    // ─── OR DIVIDER ──────────────────────────────────────────────────────────

    private JPanel buildOrDivider(){
        JPanel row=new JPanel(new BorderLayout(10,0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE,20));
        JSeparator left=new JSeparator(); left.setForeground(new Color(220,210,205));
        JSeparator right=new JSeparator(); right.setForeground(new Color(220,210,205));
        JLabel or=new JLabel("OR");
        or.setFont(new Font("SansSerif",Font.BOLD,11));
        or.setForeground(TEXT_MUTED);
        or.setHorizontalAlignment(SwingConstants.CENTER);
        row.add(left,BorderLayout.WEST);
        row.add(or,BorderLayout.CENTER);
        row.add(right,BorderLayout.EAST);
        return row;
    }

    // ─── ACTION HANDLERS ─────────────────────────────────────────────────────

    private void handleLogin(JTextField idField,JTextField pwField){
        String id=idField.getText().trim();
        String pw=new String(pwField.getText()).trim();
        if (id.isEmpty()||pw.isEmpty()){
            JOptionPane.showMessageDialog(this,"Please enter both your User ID and Password.","Missing Fields",JOptionPane.WARNING_MESSAGE);
            return;
        }else{
            if(m_um.checkUserLogin(Integer.parseInt(id),pw)){
                m_loginCard.setVisible(false);
                m_logoutCard.setVisible(true);
                this.revalidate();
                this.repaint();

                idField.setText("");
                pwField.setText("");
            }
        }
    }
    
    private void openAccountCreation(){
        m_loginCard.setVisible(false);
        m_createAccountCard.setVisible(true);
        this.revalidate();
        this.repaint();
    }

    private void handleAccountCreation(JTextField idField,JTextField pwField,JTextField nameField,JTextField emailField){
        String id=idField.getText().trim();
        String pw=new String(pwField.getText()).trim();
        String name=nameField.getText().trim();
        String email=emailField.getText().trim();
        if (id.isEmpty()||pw.isEmpty()||name.isEmpty()||email.isEmpty()){
            JOptionPane.showMessageDialog(this,"Please enter all the fields.","Missing Fields",JOptionPane.WARNING_MESSAGE);
            return;
        }else{
            if(m_um.createUser(new ArrayList<Object>(Arrays.asList(name,Integer.parseInt(id),email,pw)))){
                m_loginCard.setVisible(true);
                m_createAccountCard.setVisible(false);
                this.revalidate();
                this.repaint();
            }else{
                JOptionPane.showMessageDialog(this,"User ID already exists.","User ID Already Exists",JOptionPane.WARNING_MESSAGE);
            }
        }
    }


    private void handleLogout(){
        m_loginCard.setVisible(true);
        m_logoutCard.setVisible(false);
        this.revalidate();
        this.repaint();

        m_um.logout();
    }

    private void handleCreateAccount(){
        // TODO: navigate to a registration page or open a dialog
        JOptionPane.showMessageDialog(this,"Registration coming soon!","Create Account",JOptionPane.INFORMATION_MESSAGE);
    }

    // ─── FIELD HELPERS ───────────────────────────────────────────────────────

    private JLabel makeFieldLabel(String text){
        JLabel lbl=new JLabel(text);
        lbl.setFont(new Font("SansSerif",Font.BOLD,12));
        lbl.setForeground(TEXT_MUTED);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JTextField makeTextField(String placeholder){
        JTextField field=new JTextField(){
            @Override protected void paintComponent(Graphics g){
                super.paintComponent(g);
                if (getText().isEmpty()&&!isFocusOwner()){
                    Graphics2D g2=(Graphics2D) g;
                    g2.setColor(new Color(190,180,175));
                    g2.setFont(getFont().deriveFont(Font.ITALIC));
                    g2.drawString(placeholder,10,getHeight()/2+5);
                }
            }
        };
        styleTextField(field,placeholder);
        return field;
    }

    private void styleTextField(JTextField field,String placeholder){
        field.setFont(new Font("SansSerif",Font.PLAIN,13));
        field.setForeground(TEXT_DARK);
        field.setBackground(new Color(252,249,247));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(210,200,195),1),new EmptyBorder(10,12,10,12)));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE,42));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.addFocusListener(new FocusAdapter(){
            public void focusGained(FocusEvent e){
                field.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(RED_ACCENT,1),new EmptyBorder(10,12,10,12)));
            }
            public void focusLost(FocusEvent e){
                field.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(210,200,195),1),new EmptyBorder(10,12,10,12)));
            }
        });
    }

    // ─── BUTTON HELPERS ──────────────────────────────────────────────────────

    private JButton makeRedButton(String label){
        JButton btn=new JButton(label){
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(190,40,40) : RED_ACCENT);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),6,6);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("SansSerif",Font.BOLD,13));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(160,42));
        btn.setOpaque(false);
        return btn;
    }

    private JButton makeOutlineButton(String label){
        JButton btn=new JButton(label){
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()){
                    g2.setColor(new Color(250,242,240));
                    g2.fillRoundRect(0,0,getWidth(),getHeight(),6,6);
                }
                g2.setColor(RED_ACCENT);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1,1,getWidth()-2,getHeight()-2,6,6);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("SansSerif",Font.BOLD,13));
        btn.setForeground(RED_ACCENT);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(160,42));
        btn.setOpaque(false);
        return btn;
    }
}
