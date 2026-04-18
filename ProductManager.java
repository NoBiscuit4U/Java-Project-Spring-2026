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

    private Product getUnique(int ID){
        Product pdct=(Product) m_rw.runQuery(Constants.query_cons.kselect,Constants.preset_querys.kget_unique+Integer.toString(ID),
                                    Constants.table_query_cons.kpdct_table_qry,false).get(0);
        try{
            pdct.setPdctOptions(this.getProductOptions(pdct.getID()));
        }catch(Exception e){}

        return pdct;
    }

    public void createProduct(ArrayList<Object> params){
        try{
            this.getUnique((int) params.get(1));
            System.out.println("PRODUCT ALREADY EXISTS ID: "+(int) params.get(1));

        }catch(Exception e){
            m_rw.runQuery(Constants.query_cons.kinsert,Constants.preset_querys.kinsert_pdct+Integer.toString((int) params.get(1)),
                    Constants.table_query_cons.kpdct_table_qry,params);

            this.m_productlist=this.getAllProducts();
        }
    }

    public void updateProduct(ArrayList<Object> params){
        m_rw.runQuery(Constants.query_cons.kupdate,Constants.preset_querys.kupdate_pdct+Integer.toString((int) params.get(1)),
                Constants.table_query_cons.kpdct_table_qry,params);

        this.m_productlist=this.getAllProducts();
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

    public ArrayList<String> getDisplayListAll(){
        ArrayList<String> returnList=new ArrayList<String>();
        this.m_productlist.forEach(pdct -> returnList.add(pdct.getName()));
        return returnList;
    }

    public ArrayList<String> getDisplayListSearch(String req){
        ArrayList<String> returnList=new ArrayList<String>();
        
        return returnList;
    }
}