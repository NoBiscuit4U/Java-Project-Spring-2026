import java.util.ArrayList;

class Cart{
	
	private ArrayList<Product> order_items = new ArrayList<Product>();
	private double total_cost;
	private int cart_id;
	
	public Cart(int cart_id) {
		this.cart_id = cart_id;
	}
	
	public void add_pdcts(Product pdct){
		order_items.add(pdct);
	}

	public ArrayList<Product> getPdcts(){
		return this.order_items;
	}
	
	public void remove_pdcts(int row){
		order_items.remove(row);
	}

	public int cartSize(){
		return this.order_items.size();
	}

	public boolean isEmpty(){
		return this.cartSize()==0;
	}

	public void clear(){
		this.order_items=new ArrayList<Product>();
	}
	
	public void checkout() {
		
	}
	
	public void order_confirm() {
		
	}
	
	public void payment() {
		
	}
}
