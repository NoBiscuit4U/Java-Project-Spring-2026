import java.util.Scanner;
import java.util.ArrayList;

class Cart{
	
	private ArrayList<Product> order_items = new ArrayList<>();
	private double total_cost;
	private int cart_id;
	
	public Cart(int cart_id) {
		this.cart_id = cart_id;
	}
	
	public void add_pdcts(Product pdct) {
		order_items.add(pdct);
	}
	
	public void remove_pdcts(int product_id) {
		//order_items.remove(product_id);
	}
	
	public void checkout() {
		
	}
	
	public void order_confirm() {
		
	}
	
	public void payment() {
		
	}
}
