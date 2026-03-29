import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
	
	public ArrayList<Object> runQuery(String query_type,String full_query,String target_table,boolean scope_all) {
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
		
		return new ArrayList<Object>();
	}
	
	private ArrayList<Object> selectQuery(String full_query,String target_table,boolean scope_all) throws ClassNotFoundException,SQLException{
		ArrayList<Object> returnObj=new ArrayList<Object>();
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con=DriverManager.getConnection(this.m_root_url+this.m_DBName,this.m_username,this.m_password);
		System.out.println("Connection Established successfully");
		PreparedStatement ps = con.prepareStatement(full_query);
		ResultSet rs=ps.executeQuery(full_query);
		rs.next();
		
		switch(target_table){
			case "products":
				if(scope_all) {
					while(rs.next()) {
						returnObj.add(new Product(rs.getString(Constants.obj_query_cons.m_name_qry),rs.getInt(Constants.obj_query_cons.m_id_qry),
										rs.getDouble(Constants.obj_query_cons.m_cost_qry),rs.getString(Constants.obj_query_cons.m_nutrit_qry)));
					}
					
				}else {
					returnObj.add(new Product(rs.getString(Constants.obj_query_cons.m_name_qry),rs.getInt(Constants.obj_query_cons.m_id_qry),
										rs.getDouble(Constants.obj_query_cons.m_cost_qry),rs.getString(Constants.obj_query_cons.m_nutrit_qry)));
				}
				
				break;
			case "users":
				if(scope_all) {
					while(rs.next()) {
						returnObj.add(new User(rs.getString(Constants.obj_query_cons.m_name_qry),rs.getInt(Constants.obj_query_cons.m_id_qry),
												rs.getString(Constants.obj_query_cons.m_email_qry)));
					}
				
				}else {
					returnObj.add(new User(rs.getString(Constants.obj_query_cons.m_name_qry),rs.getInt(Constants.obj_query_cons.m_id_qry),
											rs.getString(Constants.obj_query_cons.m_email_qry)));
				}
				
				break;
		}
		
		return returnObj;
	}
}
