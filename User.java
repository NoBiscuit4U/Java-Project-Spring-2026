
public class User{
	private String m_name;
	private int m_id;
	private String m_email;
	
	private PaymentInfo m_payinfo;
	
	public User(String name,int id,String email) {
		this.m_name=name;
		this.m_id=id;
		this.m_email=email;
	}

	public String getName(){
		return this.m_name;
	}

	public void setName(String name){
		this.m_name=name;
	}

	public int getID(){
		return this.m_id;
	}

	public void setID(int id){
		this.m_id=id;
	}

	public String getEmail(){
		return this.m_email;
	}

	public void setEmail(String email){
		this.m_email=email;
	}
	
	public void setPayInfo(PaymentInfo payInfo){
		this.m_payinfo=payInfo;
	}
	
	public PaymentInfo getPayInfo(){
		return this.m_payinfo;
	}
}
