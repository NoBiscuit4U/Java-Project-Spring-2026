import java.io.*;

public class ReadWriter {
	private String m_fp;
	
	private ObjectOutputStream m_oop;
	private FileOutputStream m_fop;
	
	private ObjectInputStream m_oip;
	private FileInputStream m_fip;
	
	public ReadWriter(String fp) throws FileNotFoundException {
		this.m_fp=fp;
		
		try{
			this.m_fop=new FileOutputStream(m_fp);
			this.m_oop=new ObjectOutputStream(m_fop);
			
			this.m_fip=new FileInputStream(m_fp);
			this.m_oip=new ObjectInputStream(m_fip);
			
		}catch(FileNotFoundException e) {
			System.out.println("NO FILE FOUND, ERROR: "+e);
		}
	}
	
	
	private void decodeBytes(){
		
	}
}
