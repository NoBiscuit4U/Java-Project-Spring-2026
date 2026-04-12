import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Constants;

public class ReadWriter {
	private static String m_root_url = "jdbc:mysql://localhost:3306/";
	private String m_username;
	private String m_password;
	private String m_DBName;

	private Connection m_con;
	
	public ReadWriter(String username,String password,String DBName){
		this.m_username=username;
		this.m_password=password;
		this.m_DBName=DBName;

		try{
			Class.forName("com.mysql.cj.jdbc.Driver");
			m_con=DriverManager.getConnection(this.m_root_url+this.m_DBName,this.m_username,this.m_password);
			System.out.println("Connection Established successfully");
		}catch(ClassNotFoundException e){
			System.out.println(e);
		}catch(SQLException e){
			System.out.println(e);
		}
	}

	public ArrayList<Object> runQuery(String query_type,String full_query,String target_table,boolean scope_all) {
		try{
			switch(query_type) {
				case "select_pass":
					return this.singleQueryPass(full_query,target_table,scope_all);
				case "select":
					return this.singleQuery(full_query,target_table,scope_all);
				case "delete":
					return this.singleQuery(full_query,target_table,scope_all);
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

	public ArrayList<Object> runQuery(String query_type,String full_query,String target_table,ArrayList<Object> params) {
		try{
			switch(query_type) {
				case "update":
					return this.paramQuery(full_query,target_table,params);
				case "insert into":
					return this.paramQuery(full_query,target_table,params);
			}
		}catch(ClassNotFoundException e){
			System.out.println("CLASS EXCEPTION: "+e.getMessage());
		}catch(SQLException e){
			System.out.println("SQL EXCEPTION: "+e.getMessage());
		}catch(Exception e){
			System.out.println("RUN QUERY ERROR: "+e.getMessage()+" ");
			e.printStackTrace();
		}
		
		return new ArrayList<Object>();
	}

	private ArrayList<Object> paramQuery(String full_query,String target_table,ArrayList<Object> params) throws ClassNotFoundException,SQLException{
		ArrayList<Object> returnObj=new ArrayList<Object>();

		String format_query=String.format(full_query,target_table);
		System.out.println(format_query);
		PreparedStatement ps=m_con.prepareStatement(format_query);

		for(Object obj:params){
			if(obj instanceof String){
				String n_obj=(String) obj;
				
				ps.setString(params.indexOf(obj)+1,n_obj);
			}else if(obj instanceof Integer){
				Integer n_obj=(Integer) obj;

				ps.setInt(params.indexOf(obj)+1,n_obj);
			}
		}

		ps.executeUpdate();

		return returnObj;
	}
	
	private ArrayList<Object> singleQuery(String full_query,String target_table,boolean scope_all) throws ClassNotFoundException,SQLException{
		ArrayList<Object> returnObj=new ArrayList<Object>();
		
		String format_query=String.format(full_query,target_table);

		PreparedStatement ps = m_con.prepareStatement(format_query);
		ResultSet rs=ps.executeQuery(format_query);
		rs.next();
		
		switch(target_table){
			case "products":
				if(scope_all) {
					while(rs.next()) {
						returnObj.add(new Product(rs.getString(Constants.obj_query_cons.m_name_qry),rs.getInt(Constants.obj_query_cons.m_id_qry),
										rs.getDouble(Constants.obj_query_cons.m_cost_qry),rs.getString(Constants.obj_query_cons.m_nutrit_qry),
										rs.getString(Constants.obj_query_cons.m_img_qry)));
					}
					
				}else {
					returnObj.add(new Product(rs.getString(Constants.obj_query_cons.m_name_qry),rs.getInt(Constants.obj_query_cons.m_id_qry),
										rs.getDouble(Constants.obj_query_cons.m_cost_qry),rs.getString(Constants.obj_query_cons.m_nutrit_qry),
										rs.getString(Constants.obj_query_cons.m_img_qry)));
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

	private ArrayList<Object> singleQueryPass(String full_query,String target_table,boolean scope_all) throws ClassNotFoundException,SQLException{
		ArrayList<Object> returnObj=new ArrayList<Object>();
		
		String format_query=String.format(full_query,target_table);
		
		PreparedStatement ps=m_con.prepareStatement(format_query);
		ResultSet rs=ps.executeQuery(format_query);
		rs.next();
		
		returnObj.add(rs.getString(Constants.obj_query_cons.m_password_qry));
		return returnObj;
	}
}
