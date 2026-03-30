import java.util.ArrayList;

public class Main {
	public static void main(String[] args) {
		ReadWriter rw=new ReadWriter("root","","rst_data");
		UserManager um=new UserManager(rw);

		System.out.println(um.checkUserLogin(1,"12345"));

		//ArrayList<Object> test=rw.runQuery("select","SELECT * FROM %s",Constants.table_query_cons.m_usr_table_qry,true);

		/*ArrayList<Object> test2=rw.runQuery("insert into","INSERT INTO %1$s (Name, ID, Email, Password) VALUES (?, ?, ?, ?)",Constants.table_query_cons.m_usr_table_qry,4,
			new ArrayList<Object>(){{
				add(new String("Test"));
				add(new Integer(001));
				add(new String("email"));
				add(new String("1231"));
			}}
		);*/

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
