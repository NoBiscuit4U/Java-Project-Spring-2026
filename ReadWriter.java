import java.io.*;
import java.util.ArrayList;

public class ReadWriter {
	private String m_fp;
	
	
	public ReadWriter(String fp){
		this.m_fp=fp;
		
		try(FileOutputStream fop=new FileOutputStream(m_fp);){
			fop.close();
		}catch(Exception e) {
			System.out.println("NO FILE FOUND, ERROR: "+e);
		}
	}
	
	public void appendFile(Object obj){
		ArrayList<Object> old_objs=this.readFile();

		try(ObjectOutputStream oop=new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(m_fp,true)))){
			oop.writeObject(obj);

			for(Object old_obj:old_objs){
				oop.writeObject(old_obj);
			}

			oop.flush();	
			oop.close();
			
		}catch(Exception e) {
			System.out.println("WRITE OBJ FAILED, ERROR: "+e);
		}
	}
	

	public ArrayList<Object> readFile(){
		ArrayList<Object> returnArr=new ArrayList<Object>();
		
			try(ObjectInputStream oip=new ObjectInputStream(new BufferedInputStream(new FileInputStream(m_fp)))){
				while(true) {
						try{
							Object obj=oip.readObject();

							if(obj instanceof Product){
								returnArr.add((Product) obj);
							}else {
								returnArr.add((User) obj);
							}
						}catch (EOFException e) {
							System.out.println("FINISHED "+e);
							oip.close();
							break;
						}
	
				}
			}catch(Exception e) {
				System.out.println(e);
			}
			
		return(returnArr);
	}
}
