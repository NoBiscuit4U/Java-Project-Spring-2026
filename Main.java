public class Main {
	public static void main(String[] args) {
		ReadWriter rw=new ReadWriter("root","","rst_data");
		System.out.println(rw.runQuery("select","select *from ",Constants.table_query_cons.m_usr_table_qry,true));
	} 
}
