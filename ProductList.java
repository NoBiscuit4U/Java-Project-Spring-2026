import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.*;

class ProductList extends JPanel implements ListSelectionListener{
    private DefaultListModel model;
    private JList<String> displayList;
    private JScrollPane scrollPane;

    public ProductList(ArrayList<String> data){
        GridBagConstraints gridConstraints=null;
        GridLayout layout=new GridLayout();

        this.model=new DefaultListModel();
        this.displayList = new JList(model);
        this.displayList.addListSelectionListener(this);

        for(String str:data){
            this.model.addElement(str);
        }

        this.displayList.ensureIndexIsVisible(model.getSize() - 1);

        this.scrollPane = new JScrollPane(displayList);

        gridConstraints=new GridBagConstraints();
        gridConstraints.gridx=0;
        gridConstraints.gridy=0;
        gridConstraints.insets=new Insets(10,10,10,10);

        setLayout(layout);

        add(scrollPane,gridConstraints);
    }

    public void update(ArrayList<String> data){
        this.model.removeAllElements();
        for(String str:data){
            this.model.addElement(str);
        }

        this.displayList.ensureIndexIsVisible(model.getSize() - 1);
    }

    @Override
    public void valueChanged(ListSelectionEvent event){
        if (!event.getValueIsAdjusting()) {
            String selected = this.displayList.getSelectedValue();
            System.out.println("Selected: " + selected);
        }
    }
}
