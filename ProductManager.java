import java.util.ArrayList;

public class ProductManager {
    private ReadWriter m_rw;

    private ArrayList<Product> m_productlist=new ArrayList<Product>();

    public ProductManager(ReadWriter rw){
        m_rw=rw;
        m_productlist=this.getAllProducts();
    }

    private ProductOptions getProductOptions(int ID){
        return (ProductOptions) m_rw.runQuery(Constants.query_cons.kselect,Constants.preset_querys.kget_unique+Integer.toString(ID),
                                                    Constants.table_query_cons.kpdct_table_qry,false).get(0);
    }

    private ArrayList<Product> getAllProducts(){
        ArrayList<Product> pdct_arr=new ArrayList<Product>();
        ArrayList<Object> obj_pdcts=m_rw.runQuery(Constants.query_cons.kselect,Constants.preset_querys.kget_all,
                                                    Constants.table_query_cons.kpdct_table_qry,true);
        
        for(Object obj_pdct:obj_pdcts){
            Product pdct=(Product) obj_pdct;
            try{
                ProductOptions pdct_opt=this.getProductOptions(pdct.getID());

                pdct.setPdctOptions(pdct_opt);
                pdct_arr.add(pdct);
            }catch(Exception e) {
                pdct_arr.add(pdct);
            }
        }

        return pdct_arr;
    }
}