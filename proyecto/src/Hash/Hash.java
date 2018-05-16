package Hash;

import Process.Files;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class Hash extends Files{
    public Hash(){
        
    }
 
    
    private byte[] XOR(byte digest1[], byte digest2[]){
        int lng=digest1.length;
        int lnm=digest2.length;
        boolean c=false;
        if(lng<lnm){
            lng=lnm;
            lnm=digest1.length;
            c=true;
        }
        byte a[]=new byte[lng];
        int i=0;
        for(i=0;i<lnm;i++){
            a[i]=(byte) (digest1[i] ^ digest2[i]);
        }
        if(c){
            for(i=i;i<lng;i++)
                a[i]=digest1[i];
            
        }else{
             for(i=i;i<lng;i++)
                a[i]=digest2[i];
        }
        return a;
    }
    /* Retorna un hash a partir de un tipo y un texto */
    public byte[] getHash(File file, String hashType) throws FileNotFoundException, IOException {
        Files files=new Files();
        int ite=(int)file.length()/268435456;
        StringBuffer sb = new StringBuffer();
        byte[] array;
        try {
            java.security.MessageDigest md = java.security.MessageDigest
                    .getInstance(hashType);
            if(ite==0){
                array = md.digest(files.getBytes(file));
            }else{
                FileInputStream fis=new FileInputStream(file); 
                byte[] arrayAux;
                array = md.digest(files.getBytes(fis,268435456));
                for(int k=0;k<ite;k++){
                    arrayAux = md.digest(files.getBytes(fis,268435456));
                    array=XOR(array,arrayAux);
                }
                int rest=(int)file.length()-ite*268435456;
                arrayAux = md.digest(files.getBytes(fis,rest));
                array=XOR(array,arrayAux);
                fis.close();
            }
            return array;
        } catch (java.security.NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    /* Retorna un hash MD5 a partir de un texto */
    public byte[] md5(File file) throws FileNotFoundException, IOException {
        return getHash(file, "MD5");
    }
 
    /* Retorna un hash SHA1 a partir de un texto */
    public byte[] sha1(File file) throws FileNotFoundException, IOException {
        return getHash(file, "SHA1");
    }
 
}