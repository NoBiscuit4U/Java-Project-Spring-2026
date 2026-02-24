
public class Product {
	private String m_name;
	private String m_id;
	private String m_cost;
	private String m_nutrit_value;
	
	//private PdctOptions m_options;
	
	public Product(String name,String id,String cost,String nutrit_value) {
		this.m_name=name;
		this.m_id=id;
		this.m_cost=cost;
		this.m_nutrit_value=nutrit_value;
	}
	
	public String getInfo(String key){
		switch(key.toLowerCase()){
		default:
			return "N/A";
		break;
		case "name":
			return this.m_name;
		break;
		case "id":
			return this.m_id;
		break;
		case "cost":
			return this.m_cost;
		break;
		case "nutrition":
			return this.m_nutrit_value;
		break;
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
		case "cost":
			this.m_cost=val;
		break;
		case "email":
			this.m_nutrit_value=val;
		break;
		}
	}
	
}
