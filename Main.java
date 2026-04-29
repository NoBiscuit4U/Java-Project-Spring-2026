import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.HashMap;

public class Main {
    private static ReadWriter m_rw=new ReadWriter("root","","rst_data");
	private static UserManager m_um=new UserManager(m_rw);
	private static ProductManager m_pm=new ProductManager(m_rw);

    public static void main(String[] args) {
        HashMap<String,JPanel> map=new HashMap<String,JPanel>(){{
            put("Login",new Login(m_um));
        }};

		MainMenu menu=new MainMenu(m_pm,"TEST","Finely Crafted Food","TEST DESCRIPTION",map);
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
