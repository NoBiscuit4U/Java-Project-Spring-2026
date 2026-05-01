
public class PaymentInfo {
	private String m_phone,m_name,m_home_add;
	private int m_id;
	
	public PaymentInfo(int id,String phone,String name,String home_add) {
		this.m_id=id;
		this.m_name=name;
		this.m_home_add=home_add;
		this.m_phone=phone;
	}

	public String getName(){
		return this.m_name;
	}

	public String getAddress(){
		return this.m_home_add;
	}

	public String getPhone(){
		return this.m_phone;
	}

	public int getID(){
		return m_id;
	}

}
