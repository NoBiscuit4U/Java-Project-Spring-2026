import java.util.ArrayList;

import Constants;

public class UserManager {
    private ReadWriter m_rw;

    public UserManager(ReadWriter rw){
        m_rw=rw;
    }

    private User getUnique(int ID){
        return (User) m_rw.runQuery("select",Constants.preset_querys.m_get_unique_pass+Integer.toString(ID),
                                    Constants.table_query_cons.m_usr_table_qry,false).get(0);
    }

    private String getPassword(int ID){
        return (String) m_rw.runQuery("select_pass",Constants.preset_querys.m_get_unique_pass+Integer.toString(ID),
                                    Constants.table_query_cons.m_usr_table_qry,false).get(0);
    }

    public void updateUser(ArrayList<Object> params){
        m_rw.runQuery("update",Constants.preset_querys.m_update_user+Integer.toString((int) params.get(1)),
                    Constants.table_query_cons.m_usr_table_qry,params);
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
            m_rw.runQuery("insert into",Constants.preset_querys.m_insert_user+Integer.toString((int) params.get(1)),
                    Constants.table_query_cons.m_usr_table_qry,params);
        }

    }
}
