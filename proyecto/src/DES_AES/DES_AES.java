package DES_AES;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DES_AES {
    
    public DES_AES(){
    }
    
    public byte[] encryptFoot(byte bytes[] ,String key, String instance, String mode){
        return  encrypt(this.getKey(key, instance), bytes, mode,instance);
    }
    public byte[] decryptFoot(byte bytes[] ,String key, String instance, String mode){
        return  decrypt(this.getKey(key, instance), bytes, mode,instance);
    }
    
    public void encryptFile(File file,File out,String key, String instance, String mode ) throws IOException{
        byte[] fileBytes;
        int ite=(int)(file.length()/268435456);
        byte[] encryptedBytes;
        if(ite==0){
            fileBytes = getBytes(file);
            encryptedBytes = encrypt(this.getKey(key, instance), fileBytes, mode,instance);
            saveBytes(out,encryptedBytes);
        }else{
            FileInputStream fis=new FileInputStream(file);
            FileOutputStream fos=new FileOutputStream(out);
            for(char i=0;i<ite;i++){
               fileBytes=getBytes(fis,268435456);
               encryptedBytes = encrypt(this.getKey(key, instance), fileBytes, mode,instance);
               saveBytes(fos, encryptedBytes);
            }
            int rest=(int)(file.length()-ite*268435456);
            fileBytes=getBytes(fis,rest);
         
            encryptedBytes = encrypt(this.getKey(key, instance), fileBytes, mode,instance);
            saveBytes(fos, encryptedBytes);
            fis.close();
            fos.close();
        }
    }
    
    public void decryptFile(File file,File out,String key, String instance, String mode ) throws IOException{
         byte[] fileBytes;
        int ite=(int)(file.length()/268435456);
        byte[] decryptedBytes;
        if(ite==0){
            fileBytes = getBytes(file);
            decryptedBytes = decrypt(this.getKey(key, instance), fileBytes, mode,instance);
            saveBytes(out,decryptedBytes);
        }else{
            FileInputStream fis=new FileInputStream(file);
            FileOutputStream fos=new FileOutputStream(out);
            for(char i=0;i<ite;i++){
               fileBytes=getBytes(fis,268435456);
               decryptedBytes = decrypt(this.getKey(key, instance), fileBytes, mode,instance);
               saveBytes(fos, decryptedBytes);
            }
            int rest=(int)(file.length()-ite*268435456);
            fileBytes=getBytes(fis,rest);
         
            decryptedBytes = decrypt(this.getKey(key, instance), fileBytes, mode,instance);
            saveBytes(fos, decryptedBytes);
            fis.close();
            fos.close();
        }
    }

    public void saveBytes(File out,byte[] bytes){
        try{
            FileOutputStream fos=new FileOutputStream(out);
            fos.write(bytes);
            fos.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void saveBytes(FileOutputStream fos,byte[] bytes){
        try{
            fos.write(bytes);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    
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
    
    private byte[] getBytes(FileInputStream fis,int tam){
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
    
    
    
    
    private String getMode(byte[] bytes, String mode, String instance) {
            int pad;
        if (instance.equals("DES")) {
            pad = bytes.length % 8;
        } else {
            pad = bytes.length % 16;
        }

        if (pad != 0) {
            mode = instance + "/" + mode + "/" + "PKCS5Padding";
        } else {
            mode = instance + "/" + mode + "/" + "NoPadding";
       }
        return mode;
    }

    
    public boolean checkKeyDES(String key){
        key=key.trim();
        if(key.length()!=8)
            return false;
        else
            return true;
    }
    
     public boolean checkKeyAES(String key){
        key=key.trim();
        if(key.length()!=16)
            return false;
        else
            return true;
    }
    
  
    
    private  SecretKey getKey(String key,String instance){//DES,AES
		try{
			
			byte[] keyBytes=key.getBytes("UTF-8");
                        SecretKey encryptKey;
                      if(instance.equals("DES")){
                        SecretKeyFactory keyFactory=SecretKeyFactory.getInstance(instance);
                        encryptKey=keyFactory.generateSecret(new DESKeySpec(keyBytes));
                      }else{
                          SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, "AES");
                          encryptKey=skeySpec;
                      }
                      
                        return encryptKey;
		}
		catch(Exception e){
                    e.printStackTrace();
		}		
		return null;
	}

    private byte[] encrypt(SecretKey key, byte[] bytes, String mode,String instance){
		try{
			if(mode.equals("ECB")){
				mode=getMode(bytes, mode,instance);
				Cipher cipher=Cipher.getInstance(mode);
				cipher.init(Cipher.ENCRYPT_MODE, key);
				byte[] encryptedBytes=cipher.doFinal(bytes);
				return encryptedBytes;
			}
			else{
				mode=getMode(bytes, mode,instance);
                                byte[] ivBytes;
                                if(instance.equals("DES"))
                                    ivBytes=new byte[]{0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01, 0x00};
				  else
                                    ivBytes=new byte[]{0x0F, 0x0E, 0x0D, 0x0C, 0x0B, 0x0A, 0x09, 0x08,0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01, 0x00};
				IvParameterSpec ivectorSpecv=new IvParameterSpec(ivBytes);	
				Cipher cipher=Cipher.getInstance(mode);
				cipher.init(Cipher.ENCRYPT_MODE, key, ivectorSpecv);
				byte[] encryptedBytes=cipher.doFinal(bytes);
                                return encryptedBytes;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

    private byte[] decrypt(SecretKey key, byte[] bytes, String mode,String instance){
		try{
			if(mode.equals("ECB")){
				mode=getMode(bytes, mode,instance);
				Cipher cipher=Cipher.getInstance(mode);
				cipher.init(Cipher.DECRYPT_MODE, key);

				byte[] decryptedBytes=cipher.doFinal(bytes);
				return decryptedBytes;
			}
			else{
				mode=getMode(bytes, mode,instance);
				byte[] ivBytes;
                                 if(instance.equals("DES"))
                                    ivBytes=new byte[]{0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01, 0x00};
				  else
                                    ivBytes=new byte[]{0x0F, 0x0E, 0x0D, 0x0C, 0x0B, 0x0A, 0x09, 0x08,0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01, 0x00};
                                
                                IvParameterSpec ivectorSpecv=new IvParameterSpec(ivBytes);	
				Cipher cipher=Cipher.getInstance(mode);
				cipher.init(Cipher.DECRYPT_MODE, key, ivectorSpecv);

				byte[] decryptedBytes=cipher.doFinal(bytes);
				return decryptedBytes;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
    
}
