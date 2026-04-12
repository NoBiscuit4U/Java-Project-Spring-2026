
public class PaymentInfo {
	private int m_cardnum,m_cvv,m_zipcode,m_id;
	private String m_expir_date,m_name,m_home_add;
	
	public PaymentInfo(int id,int cardnum,int cvv,int zipcode,String expir_date,String name,String home_add) {
		this.m_id=id;
		this.m_cardnum=cardnum;
		this.m_cvv=cvv;
		this.m_zipcode=zipcode;
		this.m_expir_date=expir_date;
		this.m_name=name;
		this.m_home_add=home_add;
	}

}
