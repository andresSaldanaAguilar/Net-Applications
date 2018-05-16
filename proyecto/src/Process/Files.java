/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Process;

import java.io.File;
import java.io.FileInputStream;

/**
 *
 * @author miguel
 */
public class Files {
    
       public byte[] getBytes(File file){
		try{			
                    FileInputStream fis=new FileInputStream(file);
                    byte[] Bytes=new byte[(int) file.length()];
                    fis.read(Bytes);
                    fis.close();
                    return Bytes;
		}
		catch(Exception e){
                    e.printStackTrace();
		}
		return null;
    }
    
    public byte[] getBytes(FileInputStream fis,int tam){
		try{			
			byte[] Bytes=new byte[tam];
			fis.read(Bytes);
			return Bytes;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
    }

    public byte[] longtoChar(long valor){
        byte a[]=new byte[4];
        a[0]=(byte) (valor & 0xff);
        a[1]=(byte) (valor >> 8 & 0xff);
        a[2]=(byte) (valor >> 16 & 0xff);
        a[3]=(byte) (valor >> 24 & 0xff);
     return a;   
    }
    

    public long chartoLong(char chars[]){
        return  0xffffffff &( (chars[0]&0xffffffff) |  ((chars[1] << 8)&0xffffffff)  |  ((chars[2] <<16)&0xffffffff)  |  ((chars[3] <<24&0xffffffff)) );
    }
}
