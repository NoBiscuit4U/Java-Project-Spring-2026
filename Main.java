public class Main {
	public static void main(String[] args) {
		ReadWriter rw=new ReadWriter(Constants.fp_cons.m_product_strg);

		rw.appendFile(new Product("TEST1","1000","1000","NUTRITION"));
		
		System.out.println(rw.readFile());
	} 
}
