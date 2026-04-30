import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

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
			System.out.println("CLASS ERROR: "+e);
			e.printStackTrace();
		}catch(SQLException e){
			System.out.println("SQL ERROR: "+e);
			e.printStackTrace();
		}
	}

	public ArrayList<Object> runQuery(String query_type,String full_query,String target_table,boolean scope_all) {
		try{
			switch(query_type) {
				case Constants.query_cons.kselect_pass:
					return this.singleQueryPass(full_query,target_table);
				case Constants.query_cons.kselect_admin_id:
					return this.singleQueryAdminID(full_query,target_table);
				case Constants.query_cons.kselect:
					return this.singleQueryExecute(full_query,target_table,scope_all);
				case Constants.query_cons.kdelete:
					return this.singleQueryUpdate(full_query,target_table);
			}
		}catch(ClassNotFoundException e){
			System.out.println("CLASS EXCEPTION: "+e.getMessage());
			e.printStackTrace();
		}catch(SQLException e){
			System.out.println("SQL EXCEPTION: "+e.getMessage());
			e.printStackTrace();
		}catch(Exception e){
			System.out.println("RUN QUERY ERROR: "+e.getMessage());
			e.printStackTrace();
		}
		
		return new ArrayList<>();
	}

	public ArrayList<Object> runQuery(String query_type,String full_query,String target_table,ArrayList<Object> params) {
		try{
			switch(query_type) {
				case "update":
					return this.paramQuery(full_query,target_table,params);
				case "insert into":
					return this.paramQuery(full_query,target_table,params);
				case "update dynamic":
					return this.paramQuery(full_query,target_table,params);
			}
		}catch(ClassNotFoundException e){
			System.out.println("CLASS EXCEPTION: "+e.getMessage());
			e.printStackTrace();
		}catch(SQLException e){
			System.out.println("SQL EXCEPTION: "+e.getMessage());
			e.printStackTrace();
		}catch(Exception e){
			System.out.println("RUN QUERY ERROR: "+e.getMessage()+" ");
			e.printStackTrace();
		}
		
		return new ArrayList<>();
	}

	public ArrayList<Object> runQuery(String query_type,String full_query,String target_table,HashMap<String,Object> params) {
		try{
			switch(query_type){
				case "update dynamic":
					return this.paramQueryDynamic(full_query,target_table,params);
			}
		}catch(ClassNotFoundException e){
			System.out.println("CLASS EXCEPTION: "+e.getMessage());
			e.printStackTrace();
		}catch(SQLException e){
			System.out.println("SQL EXCEPTION: "+e.getMessage());
			e.printStackTrace();
		}catch(Exception e){
			System.out.println("RUN QUERY ERROR: "+e.getMessage()+" ");
			e.printStackTrace();
		}
		
		return new ArrayList<>();
	}

	private ArrayList<Object> paramQuery(String full_query,String target_table,ArrayList<Object> params) throws ClassNotFoundException,SQLException{
		ArrayList<Object> returnObj=new ArrayList<Object>();

		String format_query=String.format(full_query,target_table);
		PreparedStatement ps=m_con.prepareStatement(format_query);

		for(int i = 0; i < params.size(); i++) {
			Object obj = params.get(i);
			int paramIndex = i + 1;
			if(obj instanceof String){
				ps.setString(paramIndex, (String) obj);
			}else if (obj instanceof Integer){
				ps.setInt(paramIndex, (Integer) obj);
			}else if (obj instanceof Double){
				ps.setDouble(paramIndex, (Double) obj);
			}else{
				ps.setObject(paramIndex, obj);
			}
		}

		ps.executeUpdate();

		return returnObj;
	}

	private ArrayList<Object> paramQueryDynamic(String full_query,String target_table,HashMap<String,Object> params) throws ClassNotFoundException,SQLException{
		ArrayList<Object> returnObj=new ArrayList<Object>();
		StringBuilder targetFields=new StringBuilder();

		for(String key:params.keySet()){
			String updatePattern=null;
			switch(key){
				case Constants.obj_query_cons.kname_qry:
					updatePattern=Constants.dynamic_query.update.kname;
				break;
				case Constants.obj_query_cons.kcost_qry:
					updatePattern=Constants.dynamic_query.update.kcost;
				break;
				case Constants.obj_query_cons.knutrit_qry:
					updatePattern=Constants.dynamic_query.update.knutritValue;
				break;
				case Constants.obj_query_cons.kimg_qry:
					updatePattern=Constants.dynamic_query.update.kimg;
				break;
				case Constants.obj_query_cons.kmainTitle_qry:
					updatePattern=Constants.dynamic_query.update.kmainTitle;
				break;
				case Constants.obj_query_cons.kheaderDesc_qry:
					updatePattern=Constants.dynamic_query.update.kheaderDesc;
				break;
				case Constants.obj_query_cons.kbodyDesc_qry:
					updatePattern=Constants.dynamic_query.update.kbodyDesc;
				break;
				case Constants.obj_query_cons.kbannerImg_qry:
					updatePattern=Constants.dynamic_query.update.kbannerImg;
				break;
			}
			if(updatePattern==null){
				continue;
			}
			if(targetFields.length()>0){
				targetFields.append(", ");
			}

			Object obj=params.get(key);
			String n_obj;
			if(obj instanceof String){
				String escaped=((String) obj).replace("'", "''");
				n_obj="'"+escaped+"'";
			}else{
				n_obj=String.valueOf(obj);
			}
			targetFields.append(String.format(updatePattern,n_obj));
		}

		String format_query=String.format(full_query,target_table,targetFields.toString());
		PreparedStatement ps=m_con.prepareStatement(format_query);

		ps.executeUpdate();
		
		return returnObj;
	}

	private ArrayList<Object> singleQueryUpdate(String full_query,String target_table) throws ClassNotFoundException,SQLException{
		ArrayList<Object> returnObj=new ArrayList<Object>();

		String format_query=String.format(full_query,target_table);
		PreparedStatement ps=m_con.prepareStatement(format_query);

		ps.executeUpdate();

		return returnObj;
	}
	
	private ArrayList<Object> singleQueryExecute(String full_query,String target_table,boolean scope_all) throws ClassNotFoundException,SQLException{
		ArrayList<Object> returnObj=new ArrayList<Object>();
		
		String format_query=String.format(full_query,target_table);

		PreparedStatement ps = m_con.prepareStatement(format_query);
		ResultSet rs=ps.executeQuery(format_query);
		rs.next();
		
		switch(target_table){
			case Constants.table_query_cons.kpdct_table_qry:
				if(scope_all){
					while(rs.next()){
						returnObj.add(new Product(rs.getString(Constants.obj_query_cons.kname_qry),rs.getInt(Constants.obj_query_cons.kid_qry),
										rs.getDouble(Constants.obj_query_cons.kcost_qry),rs.getString(Constants.obj_query_cons.knutrit_qry),
										rs.getString(Constants.obj_query_cons.kimg_qry)));
					}
					
				}else{
					returnObj.add(new Product(rs.getString(Constants.obj_query_cons.kname_qry),rs.getInt(Constants.obj_query_cons.kid_qry),
										rs.getDouble(Constants.obj_query_cons.kcost_qry),rs.getString(Constants.obj_query_cons.knutrit_qry),
										rs.getString(Constants.obj_query_cons.kimg_qry)));
				}
				
			break;
			case Constants.table_query_cons.kusr_table_qry:
				if(scope_all){
					while(rs.next()){
						returnObj.add(new User(rs.getString(Constants.obj_query_cons.kname_qry),rs.getInt(Constants.obj_query_cons.kid_qry),
												rs.getString(Constants.obj_query_cons.kemail_qry)));
					}
				
				}else{
					returnObj.add(new User(rs.getString(Constants.obj_query_cons.kname_qry),rs.getInt(Constants.obj_query_cons.kid_qry),
											rs.getString(Constants.obj_query_cons.kemail_qry)));
				}
				
			break;
			case Constants.table_query_cons.kpdct_option_table_qry:
				if(scope_all){
					while(rs.next()){
						returnObj.add(new ProductOptions(rs.getInt(Constants.obj_query_cons.kid_qry),rs.getString(Constants.obj_query_cons.koptions_qry)));
					}
				
				}else{
					returnObj.add(new ProductOptions(rs.getInt(Constants.obj_query_cons.kid_qry),rs.getString(Constants.obj_query_cons.koptions_qry)));
				}
				
			break;
			case Constants.table_query_cons.kpay_info_qry:
				if(scope_all){
					while(rs.next()){
						returnObj.add(new PaymentInfo(rs.getInt(Constants.obj_query_cons.kid_qry),rs.getInt(Constants.obj_query_cons.kcardnum_qry),
														rs.getInt(Constants.obj_query_cons.kcvv_qry),rs.getInt(Constants.obj_query_cons.kzipcode_qry),
														rs.getString(Constants.obj_query_cons.kexpir_qry),rs.getString(Constants.obj_query_cons.kname_qry),
														rs.getString(Constants.obj_query_cons.kaddress_qry)));
					}
				
				}else{
					returnObj.add(new PaymentInfo(rs.getInt(Constants.obj_query_cons.kid_qry),rs.getInt(Constants.obj_query_cons.kcardnum_qry),
														rs.getInt(Constants.obj_query_cons.kcvv_qry),rs.getInt(Constants.obj_query_cons.kzipcode_qry),
														rs.getString(Constants.obj_query_cons.kexpir_qry),rs.getString(Constants.obj_query_cons.kname_qry),
														rs.getString(Constants.obj_query_cons.kaddress_qry)));
				}
				
			break;
			case Constants.table_query_cons.ksitecontent_qry:
				returnObj.add(rs.getString(Constants.obj_query_cons.kmainTitle_qry));
				returnObj.add(rs.getString(Constants.obj_query_cons.kheaderDesc_qry));
				returnObj.add(rs.getString(Constants.obj_query_cons.kbodyDesc_qry));
				returnObj.add(rs.getString(Constants.obj_query_cons.kbannerImg_qry));
			break;
		}
		
		return returnObj;
	}

	private ArrayList<Object> singleQueryPass(String full_query,String target_table) throws ClassNotFoundException,SQLException{
		ArrayList<Object> returnObj=new ArrayList<Object>();
		
		String format_query=String.format(full_query,target_table);
		
		PreparedStatement ps=m_con.prepareStatement(format_query);
		ResultSet rs=ps.executeQuery(format_query);
		rs.next();

		returnObj.add(rs.getString(Constants.obj_query_cons.kpassword_qry));
		return returnObj;
	}

	private ArrayList<Object> singleQueryAdminID(String full_query,String target_table) throws ClassNotFoundException,SQLException{
		ArrayList<Object> returnObj=new ArrayList<Object>();
		
		String format_query=String.format(full_query,target_table);
		
		PreparedStatement ps=m_con.prepareStatement(format_query);
		ResultSet rs=ps.executeQuery(format_query);
		rs.next();

		returnObj.add(rs.getInt(Constants.obj_query_cons.kid_qry));
		return returnObj;
	}
}
