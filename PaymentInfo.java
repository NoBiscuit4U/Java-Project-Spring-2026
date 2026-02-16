
public class PaymentInfo {
	private int m_cardnum;
	private int m_cvv;
	private int m_zipcode;
	
	private String m_expir_date;
	private String m_card_name;
	private String m_home_add;
	
	public PaymentInfo(int cardnum,int cvv,int zipcode,String expir_date,String card_name,String home_add) {
		this.m_cardnum=cardnum;
		this.m_cvv=cvv;
		this.m_zipcode=zipcode;
		this.m_expir_date=expir_date;
		this.m_card_name=card_name;
		this.m_home_add=home_add;
	}
	
}
