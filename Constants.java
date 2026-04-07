
public class Constants {
	public static final class fp_cons{
		public static final String m_usr_strg="storage/usr_strg.dat";
		public static final String m_product_strg="storage/pdct_strg.dat";
	}

	public static final class obj_query_cons{
		public static final String m_name_qry="Name";
		public static final String m_id_qry="ID";
		public static final String m_cost_qry="ID";
		public static final String m_nutrit_qry="NutritValue";
		public static final String m_email_qry="Email";
		public static final String m_password_qry="Password";
		public static final String m_img_qry="Img";
	}

	public static final class table_query_cons{
		public static final String m_pdct_table_qry="products";
		public static final String m_usr_table_qry="users";
	}

	public static final class preset_querys{
		public static final String m_get_unique_pass="SELECT * FROM %s WHERE ID = ";
		public static final String m_delete_obj="SELECT * FROM %s WHERE ID = ";
		public static final String m_insert_user="INSERT INTO %s (`Name`, `ID`, `Email`, `Password`) VALUES (?,?,?,?)";
		public static final String m_update_user="UPDATE %s SET Name = ?, ID = ?, Email = ?, Password = ? WHERE ID = ";
	}
}
