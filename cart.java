import java.util.Scanner;
import java.util.ArrayList;

class Cart{
	
	private ArrayList<> order_items = new ArrayList<>();
	private double total_cost;
	private int cart_id;
	
	public Cart(cart_id) {
		this.cart_id = cart_id;
	}
	
	public void add_pdcts(self, product_id) {
		order_items.add(product_id);
	}
	
	public void remove_pdcts(self, product_id) {
		order_items.remove(product_id);
	}
	
	public void checkout(self) {
		
	}
	
	public void order_confirm(self) {
		
	}
	
	public void payment(self) {
		
	}
}
