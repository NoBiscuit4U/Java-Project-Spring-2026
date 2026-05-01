import java.util.ArrayList;

public class Product{
	private String m_name;
	private int m_id;
	private double m_cost;
	private String m_nutrit_value;
	private String m_img;
	
	public Product(String name,int id,double cost,String nutrit_value,String img){
		this.m_name=name;
		this.m_id=id;
		this.m_cost=cost;
		this.m_nutrit_value=nutrit_value;
		this.m_img=img;
	}

	public Product(ArrayList<Object>params){
		this.m_name=(String) params.get(0);
		this.m_id=(int) params.get(1);
		this.m_cost=(double) params.get(2);
		this.m_nutrit_value=(String) params.get(3);
		this.m_img=(String) params.get(4);
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

	public double getCost(){
		return this.m_cost;
	}

	public void setCost(double cost){
		this.m_cost=cost;
	}

	public String getNuritLabel(){
		return this.m_nutrit_value;
	}

	public void setNuritLabel(String nutrit_value){
		this.m_nutrit_value=nutrit_value;
	}

	public String getImg(){
		return this.m_img;
	}

	public void setImg(String img){
		this.m_img=img;
	}
}
