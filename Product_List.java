import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.*;

class Product_List extends JPanel implements ListSelectionListener{
    private JList<String> displayList;
    private JScrollPane scrollPane;

    public Product_List(ArrayList<String> data){
        GridBagConstraints gridConstraints=null;
        GridLayout layout=new GridLayout();
        displayList = new JList(data.toArray());
        displayList.addListSelectionListener(this);

        scrollPane = new JScrollPane(displayList);

        gridConstraints=new GridBagConstraints();
        gridConstraints.gridx=0;
        gridConstraints.gridy=0;
        gridConstraints.insets=new Insets(10,10,10,10);

        setLayout(layout);

        add(scrollPane,gridConstraints);
    }

    @Override
    public void valueChanged(ListSelectionEvent event){
        if (!event.getValueIsAdjusting()) {
            String selected = this.displayList.getSelectedValue();
            System.out.println("Selected: " + selected);
        }
    }
}
