import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ReadWriter {
	private static String m_root_url = "jdbc:mysql://localhost:3306/";
	private String m_username;
	private String m_password;
	private String m_DBName;
	
	public ReadWriter(String username,String password,String DBName){
		this.m_username=username;
		this.m_password=password;
		this.m_DBName=DBName;
	}
	
	public Object runQuery(String query_type,String full_query,String target_table,boolean scope_all) {
		try{
			switch(query_type) {
				case "select":
					return this.selectQuery(full_query,target_table,scope_all);
				case "update":
				break;
				case "delete":
				break;
				case "insert into":
				break;
			}
		}catch(ClassNotFoundException e){
			System.out.println("CLASS EXCEPTION: "+e.getMessage());
		}catch(SQLException e){
			System.out.println("SQL EXCEPTION: "+e.getMessage());
		}catch(Exception e){
			System.out.println("RUN QUERY ERROR: "+e.getMessage());
		}
		
		return new Object();
	}
	
	private Object selectQuery(String full_query,String target_table,boolean scope_all) throws ClassNotFoundException,SQLException{
		Object returnObj=null;
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con=DriverManager.getConnection(this.m_root_url+this.m_DBName,this.m_username,this.m_password);
		System.out.println("Connection Established successfully");
		Statement st=con.createStatement();
		ResultSet rs=st.executeQuery(full_query+target_table);
		rs.next();
		
		switch(target_table){
			case "pdct":
				if(scope_all) {
					ArrayList<Object> bulk_return=new ArrayList<Object>();
					
					for(int i=0;i<rs.getFetchSize();i++) {
						rs.next();
						bulk_return.add(new Product(rs.getString(Constants.obj_query_cons.m_name_qry),rs.getInt(Constants.obj_query_cons.m_id_qry),
										rs.getDouble(Constants.obj_query_cons.m_cost_qry),rs.getString(Constants.obj_query_cons.m_nutrit_qry)));
					}
					
					returnObj=bulk_return;
				}else {
					returnObj=new Product(rs.getString(Constants.obj_query_cons.m_name_qry),rs.getInt(Constants.obj_query_cons.m_id_qry),
										rs.getDouble(Constants.obj_query_cons.m_cost_qry),rs.getString(Constants.obj_query_cons.m_nutrit_qry));
				}
				
				break;
			case "usr":
				if(scope_all) {
					ArrayList<Object> bulk_return=new ArrayList<Object>();
					
					for(int i=0;i<rs.getFetchSize();i++) {
						rs.next();
						//Make Constants for results getting
						bulk_return.add(new User(rs.getString(""),rs.getInt(""),rs.getString("")));
					}
					
					returnObj=bulk_return;
				}else {
					//Make Constants for results getting
					returnObj=new User(rs.getString(""),rs.getInt(""),rs.getString(""));
				}
				
				break;
		}
		
		return returnObj;
	}
}
