import java.io.*;

public class ReadWriter {
	private String m_fp;
	
	private FileOutputStream m_fop;
	private FileInputStream m_fip;
	
	public ReadWriter(String fp) throws FileNotFoundException {
		this.m_fp=fp;
		
		try{
			this.m_fop=new FileOutputStream(m_fp,true);
			this.m_fip=new FileInputStream(m_fp);
			
		}catch(FileNotFoundException e) {
			System.out.println("NO FILE FOUND, ERROR: "+e);
		}
	}
	
	
	private void decodeBytes(){
		
	}
}
