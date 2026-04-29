public class Constants {
	public static final class fp_cons{
		public static final String kusr_strg="storage/usr_strg.dat";
		public static final String kproduct_strg="storage/pdct_strg.dat";
	}

	public static final class query_cons{
		public static final String kselect="select";
		public static final String kselect_pass="select pass";
		public static final String kselect_admin_id="select admin_id";
		public static final String kinsert="insert into";
		public static final String kupdate="update";
		public static final String kinsertDynamic="insert dynamic";
		public static final String kupdateDynamic="update dynamic";
		public static final String kdelete="delete";
	}

	public static final class obj_query_cons{
		public static final String kname_qry="Name";
		public static final String kid_qry="ID";
		public static final String kcost_qry="Cost";
		public static final String knutrit_qry="NutritValue";
		public static final String kemail_qry="Email";
		public static final String kpassword_qry="Password";
		public static final String kimg_qry="Img";
		public static final String koptions_qry="Options";
		public static final String kcvv_qry="CVV";
		public static final String kcardnum_qry="CardNum";
		public static final String kzipcode_qry="Zipcode";
		public static final String kexpir_qry="Expiration";
		public static final String kaddress_qry="Address";
	}

	public static final class table_query_cons{
		public static final String kpdct_table_qry="products";
		public static final String kpdct_option_table_qry="product_options";
		public static final String kusr_table_qry="users";
		public static final String kpay_info_qry="payment_info";
		public static final String kadmin_ids_qry="admin_ids";
	}

	public static final class preset_querys{
		public static final String kget_unique="SELECT * FROM %s WHERE `ID` = ";
		public static final String kget_all="SELECT * FROM %s";
		public static final String kdelete_obj="DELETE FROM %s WHERE `ID` = ";
		public static final String kinsert_user="INSERT INTO %s (`Name`, `ID`, `Email`, `Password`) VALUES (?,?,?,?)";
		public static final String kinsert_pdct="INSERT INTO %s (`Name`, `ID`, `Cost`, `NutritValue`, `Img`) VALUES (?,?,?,?,?)";
		public static final String kupdate_user="UPDATE %s SET Name = ?, ID = ?, Email = ?, Password = ? WHERE ID = ";
		public static final String kupdate_pdct="UPDATE %s SET Name = ?, ID = ?, Cost = ?, NutritValue = ?, Img = ? WHERE ID = ";

		public static final String kupdate_pdct_dynamic="UPDATE %1$s SET %2$s WHERE ID = ";
	}

	public static final class dynamic_query{
		public static final class insert{
			public static final String kname= "`Name`";	
			public static final String kid="`ID`";	
			public static final String kcost="`Cost`";	
			public static final String knutritValue="`NutritValue`";	
			public static final String kimg="`Img`";
			public static final String kvalues=" VALUES";
		}

		public static final class update{
			public static final String kname= "`Name` = %s";	
			public static final String kid="`ID` = %s";	
			public static final String kcost="`Cost` = %s";	
			public static final String knutritValue="`NutritValue` = %s";	
			public static final String kimg="`Img` = %s";
		}
	}
}
