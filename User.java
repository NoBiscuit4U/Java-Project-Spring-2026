
public class User {
	private String m_name;
	private String m_id;
	private String m_password;
	private String m_email;
	
	private PaymentInfo m_payinfo;
	
	public User(String name,String id,String password,String email) {
		this.m_name=name;
		this.m_id=id;
		this.m_password=password;
		this.m_email=email;
	}
	
	public String getInfo(String key){
		switch(key.toLowerCase()){
		default:
			return "N/A";
		case "name":
			return this.m_name;
		case "id":
			return this.m_id;
		case "password":
			return this.m_password;
		case "email":
			return this.m_email;
		}
	}
	
	public void setInfo(String key,String val){
		switch(key.toLowerCase()){
		case "name":
			this.m_name=val;
		break;
		case "id":
			this.m_id=val;
		break;
		case "password":
			this.m_password=val;
		break;
		case "email":
			this.m_email=val;
		break;
		}
	}
	
	public void setPayInfo(int cardnum,int cvv,int zipcode,String expir_date,String home_add) {
		this.m_payinfo=new PaymentInfo(cardnum,cvv,zipcode,expir_date,this.m_name,home_add);
	}
	
	public PaymentInfo getPayInfo() {
		return this.m_payinfo;
	}
}
