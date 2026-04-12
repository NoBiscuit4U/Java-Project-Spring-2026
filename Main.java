import java.util.ArrayList;

public class Main {
	public static void main(String[] args) {
		ReadWriter rw=new ReadWriter("root","","rst_data");
		UserManager um=new UserManager(rw);

		//System.out.println(um.checkUserLogin(1,"12345"));

		//ArrayList<Object> test=rw.runQuery("select","SELECT * FROM %s",Constants.table_query_cons.m_usr_table_qry,true);
			
		ArrayList<Object> test= new ArrayList<Object>(){{
				add("Johnny");
				add(2);
				add("email");
				add("3434");
			}};

		um.updateUser(test);

		/*for(Object obj:test){
		/	if(obj instanceof User){
				User n_obj=(User) obj;
				System.out.println(n_obj.getName());
			}
		}*/
	} 
}

//INSERT INTO %s (`Name`, `ID`, `Email`, `Password`) VALUES (%,%,%,%)
//SELECT * FROM %s
//
