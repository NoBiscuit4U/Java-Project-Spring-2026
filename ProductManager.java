import java.util.ArrayList;


public class ProductManager {
    private ReadWriter m_rw;

    private ArrayList<Product> m_productlist=new ArrayList<Product>();

    public ProductManager(ReadWriter rw){
        m_rw=rw;
        m_productlist=this.getAllProducts();
    }

    public ArrayList<Product> getAllProducts(){
        ArrayList<Product> pdct_arr=new ArrayList<Product>();
        ArrayList<Object> obj_pdcts=m_rw.runQuery(Constants.query_cons.kselect,Constants.preset_querys.kget_all_products,
                                                    Constants.table_query_cons.kpdct_table_qry,true);
        
        for(Object obj_pdct:obj_pdcts){
            Product pdct=(Product) obj_pdct;
        }

        return pdct_arr;
    }
}