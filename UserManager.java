import java.util.ArrayList;

public class UserManager {
    private ReadWriter m_rw;

    public UserManager(ReadWriter rw){
        m_rw=rw;
    }

    private User getUnique(int ID){
        return (User) m_rw.runQuery(Constants.query_cons.kselect,Constants.preset_querys.kget_unique_pass+Integer.toString(ID),
                                    Constants.table_query_cons.kusr_table_qry,false).get(0);
    }

    private void deleteUser(int ID){
        m_rw.runQuery(Constants.query_cons.kdelete,Constants.preset_querys.kdelete_obj+Integer.toString(ID),
                                    Constants.table_query_cons.kusr_table_qry,false);
    }

    private String getPassword(int ID){
        return (String) m_rw.runQuery(Constants.query_cons.kselect_pass,Constants.preset_querys.kget_unique_pass+Integer.toString(ID),
                                    Constants.table_query_cons.kusr_table_qry,false).get(0);
    }

    public void updateUser(ArrayList<Object> params){
        m_rw.runQuery(Constants.query_cons.kupdate,Constants.preset_querys.kupdate_user+Integer.toString((int) params.get(1)),
                    Constants.table_query_cons.kusr_table_qry,params);
    }

    public boolean checkUserLogin(int ID,String pass){
        try{
            return this.getPassword(ID).equals(pass);
        }catch(Exception e){
            System.out.println(e);
            return false;
        }
    }

    public void createUser(ArrayList<Object> params){
        try{
            this.getUnique((int) params.get(1));
            System.out.println("USER ALREADY EXISTS ID: "+(int) params.get(1));

        }catch(Exception e){
            m_rw.runQuery(Constants.query_cons.kinsert,Constants.preset_querys.kinsert_user+Integer.toString((int) params.get(1)),
                    Constants.table_query_cons.kusr_table_qry,params);
        }

    }
}
