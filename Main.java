import java.util.ArrayList;

public class Main {
	public static void main(String[] args) {
		ReadWriter rw=new ReadWriter("root","","rst_data");
		ArrayList<Object> test=rw.runQuery("select","SELECT * FROM users",Constants.table_query_cons.m_usr_table_qry,true);

		for(Object obj:test){
			if(obj instanceof User){
				User n_obj=(User) obj;
				System.out.println(n_obj.getName());
			}
		}
	} 
}
