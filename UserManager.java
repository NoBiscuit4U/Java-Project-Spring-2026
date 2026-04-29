import java.util.ArrayList;

public class UserManager {
    private ReadWriter m_rw;
    private int session_id=0;

    public UserManager(ReadWriter rw){
        m_rw=rw;
    }

    private User getUnique(int ID,boolean getPayInfo){
        User usr=(User) m_rw.runQuery(Constants.query_cons.kselect,Constants.preset_querys.kget_unique+Integer.toString(ID),
                                    Constants.table_query_cons.kusr_table_qry,false).get(0);
        if(getPayInfo){
            try{
                usr.setPayInfo(this.getPaymentInfo(usr.getID()));
            }catch(Exception e){}
        }

        return usr;
    }

    private void deleteUser(int ID){
        m_rw.runQuery(Constants.query_cons.kdelete,Constants.preset_querys.kdelete_obj+Integer.toString(ID),
                                    Constants.table_query_cons.kusr_table_qry,false);
    }

    private String getPassword(int ID){
        return (String) m_rw.runQuery(Constants.query_cons.kselect_pass,Constants.preset_querys.kget_unique+Integer.toString(ID),
                                    Constants.table_query_cons.kusr_table_qry,false).get(0);
    }

    private PaymentInfo getPaymentInfo(int ID){
        return (PaymentInfo) m_rw.runQuery(Constants.query_cons.kselect,Constants.preset_querys.kget_unique+Integer.toString(ID),
                                    Constants.table_query_cons.kpay_info_qry,false).get(0);
    }

    public void updateUser(ArrayList<Object> params){
        m_rw.runQuery(Constants.query_cons.kupdate,Constants.preset_querys.kupdate_user+Integer.toString((int) params.get(1)),
                    Constants.table_query_cons.kusr_table_qry,params);
    }

    public boolean checkAdmin(){
        if(this.session_id!=0){
            try{
                this.m_rw.runQuery(Constants.query_cons.kselect_admin_id,Constants.preset_querys.kget_unique+Integer.toString(this.session_id),
                                        Constants.table_query_cons.kadmin_ids_qry,false).get(0);
                return true;
            }catch(Exception e){
                return false;
            }
        }else{
            return false;
        }
    }

    public boolean checkUserLogin(int ID,String pass){
        try{
            boolean check=this.getPassword(ID).equals(pass);
            this.session_id=ID;
            return check;
        }catch(Exception e){
            System.out.println(e);
            return false;
        }
    }

    public void createUser(ArrayList<Object> params){
        try{
            this.getUnique((int) params.get(1),false);
            System.out.println("USER ALREADY EXISTS ID: "+(int) params.get(1));

        }catch(Exception e){
            m_rw.runQuery(Constants.query_cons.kinsert,Constants.preset_querys.kinsert_user,
                    Constants.table_query_cons.kusr_table_qry,params);
        }

    }

    public void logout(){
        this.session_id=0;
    }
}
