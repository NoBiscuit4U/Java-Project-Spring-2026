import java.util.ArrayList;

public class Main {
	public static void main(String[] args) {
		ReadWriter rw=new ReadWriter("root","","rst_data");
		UserManager um=new UserManager(rw);
		ProductManager pm=new ProductManager(rw);
			
		ArrayList<Object> test= new ArrayList<Object>(){{
				add("Johnny");
				add(2);
				add("email");
				add("3434");
			}};

		//um.updateUser(test);

	} 
}

//INSERT INTO %s (`Name`, `ID`, `Email`, `Password`) VALUES (%,%,%,%)
//SELECT * FROM %s
//
