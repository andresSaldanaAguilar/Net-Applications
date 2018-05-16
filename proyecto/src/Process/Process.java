/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Process;

import DES_AES.DES_AES;
import Hash.Hash;
import WAVE.Wav;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author miguel
 */
public class Process {   
    
    private Wav wav;
    private DES_AES cipher;
    private File cFile, wFile;
    private Files files;
    
    public Process(){
        cipher = new DES_AES();
        files=new Files();
    }
        
    ///metodos a modificar en el dominio que el usuario regrese un booleano al preguntar
    public boolean userQuestion1(){
        System.out.print("Si se convierte a estereo el audio el tamaño alcanzará\n Desea continuar?");
        //
        return true;
    }
    public boolean userQuestion2(){
        System.out.print("Desea repetir el audio hasta cubrir el tamaño a ocultar");
        //
        return true;
    }
    public void userMessage1(){
        System.out.print("Por favor introduzca un archivo de audio 4 veces mayor al que desea ocultar");
        
    }
    public void userMessage2(boolean Complete){
        if(Complete){
                System.out.print("Sucessfull ... Download!\n");
        }else{
            System.out.print("Error ... Password, intance or mode incorrect!\n");
        }
        
    }
    
    public void userMessage3(){
        System.out.print("Error El archivo de audio está dañado o no contiene inormacion ");
        
    }
    
    ///
    private boolean size_wav_file_is_valid(long totalSamples,long fileToHideLength) throws IOException{
        if(totalSamples*2<fileToHideLength*4)
            return false;
        return true;
    }
   
    
    private long round(double number){
        long val=(long)number;
        if((double)((double)number-(double)val)==0)
            return (long) number;
        else
            return ++val;
    }
    
    private long sizeCryptedFile(String instance,long ln){
         if(instance.equals("AES"))
            ln=(long) (round((double)ln/16)*16);
        else
            ln=(long) (round((double)ln/8)*8);
        return ln;
    }
    
    public boolean hide(File fileWave, File fileToHide, File outputFile, String instance, String mode, String key, String nombre) throws IOException {
        if (this.is_valid_wav(fileWave, false)) {
            Wav wav = new Wav(fileWave);
            long ln = sizeCryptedFile(instance, fileToHide.length());
            // se verifica que el archivo de audio sea mayor que el archivo a ocultar
            boolean valid = size_wav_file_is_valid(wav.getTotalNumberSamples(), ln);
            //si el archivo de audio no es valido en tamanio
            boolean sucess = false;
            if (!valid) {
                //si aunque se convierta a estereo o ya es estereo y no alcanza se le pregunta si desea repetir al audio
                valid = userQuestion2();
                if (valid) {
                    sucess = hide(fileWave, fileToHide, outputFile, key, instance, mode, (char) 3, nombre);
                } else {
                    userMessage1();
                    fileWave.delete();
                    sucess = false;
                }

            } else {
                sucess = hide(fileWave, fileToHide, outputFile, key, instance, mode, (char) 1, nombre);
            }
            this.userMessage2(sucess);
            if (sucess) {
                fileWave.delete();
                fileToHide.delete();
                cFile.delete();
                wFile.delete();
            } else {
                cFile.delete();
                wFile.delete();
            }
            return sucess;
        } else {
            this.userMessage3();
            return false;
        }
    }

    
    private byte[] mix_bytes_wav_and_encrypted_file(byte bytesSamples[],byte encrypted[]){
        int ln=encrypted.length;
        for(int i=0;i<ln;i++){
            bytesSamples[4*i]=(byte) ((bytesSamples[4*i]&0xf0)|(encrypted[i] & 0x0f));
            bytesSamples[4*(i+1)-2]=(byte) ((bytesSamples[4*(i+1)-2]&0xf0)|(encrypted[i]>>4 & 0x0f));
        }
        return bytesSamples;
    }
    
    public byte[] stringToBytes(String string){
        int ln=string.length();
        char tam=43;
        if(ln>tam){
            string=string.substring(ln-tam, ln);
        }
        ln=string.length();
        byte a[]=new byte[tam+1];
        int i=0;
        for(i=0;i<ln;i++){
            a[i]=(byte)string.charAt(i);
        }
        a[i]=(char)'|';
        
        for(i=i+1;i<tam;i++){
            a[i]=(byte)0; 
        }
 
        return a;
    }
    
    public byte[] getFootToSave(String nombre,long numMuestras,byte[] md5,String key,String instance,String mode) throws IOException{
        byte ln[]=files.longtoChar(numMuestras);
        byte name[]=stringToBytes(nombre);
         char tam=64;
        byte result[]=new byte[tam];
        int i=0;
        for(i=0;i<4;i++){
            result[i]=ln[i];
        }
        for(i=i;i<20;i++){
            result[i]=md5[i-4];
        }
        for(i=i;i<tam;i++){
            result[i]=name[i-20];
        }
        
       return cipher.encryptFoot(result, key, instance,mode);
    }
    
    
    private boolean mix(File wavFile, File encryptedFile, File outputWav, String nombre, String key, String instance, String mode, char option, long originalFileLn, byte md5Hash[]) throws IOException {
        try {
            Wav wav = new Wav(wavFile);
            FileOutputStream fos = new FileOutputStream(outputWav);
            byte f[] = getFootToSave(nombre, originalFileLn, md5Hash, key, instance, mode);
            wav.setFootInHeadLength(f.length);
            this.writeHeaderWav(fos, wav);

            int ite = (int) wav.getTotalNumberSamples() / 134217728;
            int iteC = (int) encryptedFile.length() / 67108864;
            byte bytesAudio[];
            byte encryptedBytes[];
            if (iteC == 0 && ite == 0) {
                bytesAudio = wav.getBytesAudio();
                encryptedBytes = files.getBytes(encryptedFile);
                fos.write(mix_bytes_wav_and_encrypted_file(bytesAudio, encryptedBytes));
            } else {
                int restEncryptedBytes, restBytesAudio;
                FileInputStream fis, fise;
                fis = new FileInputStream(wav.getFileWav());
                fise = new FileInputStream(encryptedFile);
                bytesAudio = new byte[44];
                fis.read(bytesAudio);
                int j = 0;
                for (j = 0; j < iteC; j++) {
                    bytesAudio = wav.getBytes(fis, 268435456);
                    encryptedBytes = files.getBytes(fise, 67108864);
                    fos.write(mix_bytes_wav_and_encrypted_file(bytesAudio, encryptedBytes));
                }
                restEncryptedBytes = (int) (encryptedFile.length() - iteC * 67108864);
                bytesAudio = wav.getBytes(fis, restEncryptedBytes * 4);
                encryptedBytes = files.getBytes(fise, restEncryptedBytes);
                fos.write(mix_bytes_wav_and_encrypted_file(bytesAudio, encryptedBytes));
                if (option != 3) {
                    restBytesAudio = (int) (wav.getTotalNumberSamples() * 4 - iteC * 268435456);
                    fos.write(wav.getBytes(fis, restBytesAudio - (restEncryptedBytes * 4)));
                }
                fis.close();
            }
            fos.write(f);
            fos.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    private void writeHeaderWav( FileOutputStream fos,Wav wav) throws IOException{
        fos.write(wav.charToByte4(wav.getChunkID()));
        fos.write(wav.charToByte4(wav.getChunkSize()));
        fos.write(wav.charToByte4(wav.getFormat()));
        fos.write(wav.charToByte4(wav.getSubchunk1_ID()));
        fos.write(wav.charToByte4(wav.getSubchunk1_Size()));
        fos.write(wav.charToByte2(wav.getAudioFormat()));
        fos.write(wav.charToByte2(wav.getNumChannels()));
        fos.write(wav.charToByte4(wav.getSampleRate()));
        fos.write(wav.charToByte4(wav.getByteRate()));
        fos.write(wav.charToByte2(wav.getBlockAlign()));
        fos.write(wav.charToByte2(wav.getBitsPerSample()));
        fos.write(wav.charToByte4(wav.getSubchunk2_ID()));
        fos.write(wav.charToByte4(wav.getSubchunk2_Size()));    
    }
    

 
    private void repeatWave(File fileInWav,int iterations,int remainingBytes) throws IOException{
        Wav wav=new Wav(fileInWav);
        Wav wav2=new Wav(fileInWav);
        wFile=new File("waux"+System.currentTimeMillis()+".wav");
        FileOutputStream fos=new FileOutputStream(wFile);
        wav.deleteBytesFoot();
        int remainingSamples=remainingBytes/(2*wav.getCannalNumber());
        wav.addNumberAudioSamples((iterations-1)*wav2.getNumberAudioSamples()+remainingSamples);
        writeHeaderWav(fos,wav);

        //////////
       int ite=(int)wav2.getTotalNumberSamples()/134217728;
       byte bytesAudio[];
       FileInputStream fis;
       if(ite==0){
            for (int j = 0; j < iterations; j++) {
                bytesAudio=wav2.getBytesAudio();
                fos.write(bytesAudio);
            }
            fis=new FileInputStream(wav2.getFileWav());
            bytesAudio = new byte[44];
            fis.read(bytesAudio);
            bytesAudio = wav2.getBytes(fis,remainingBytes);
            fos.write(bytesAudio);
            fis.close();
           
       }else{
           int rest;
           for (int i = 0; i < iterations; i++) {
               fis=new FileInputStream(wav2.getFileWav());
               bytesAudio = new byte[44];
               fis.read(bytesAudio);
               for (int j = 0; j < ite; j++) {
                   bytesAudio = wav2.getBytes(fis, 268435456);
                   fos.write(bytesAudio);
               }
               rest = (int) (wav2.getTotalNumberSamples()*2 - ite * 268435456);
               bytesAudio = wav2.getBytes(fis, rest);
               fos.write(bytesAudio);
               fis.close();
           }
            fis=new FileInputStream(wav2.getFileWav());
            bytesAudio = new byte[44];
            fis.read(bytesAudio);
            if(remainingBytes<268435456){
                bytesAudio = wav2.getBytes(fis, remainingBytes);
                fos.write(bytesAudio);
            }else{
                ite=remainingBytes/(268435456);
                for (int j = 0; j < ite; j++) {
                    bytesAudio = wav2.getBytes(fis, 268435456);
                    fos.write(bytesAudio);
                }    
                rest = (int) (remainingBytes - ite * 268435456);
                bytesAudio = wav2.getBytes(fis, rest);
                fos.write(bytesAudio);
                fis.close();
            }
           
           
       }
       fos.close();
       
    }
    
    private boolean hide(File fileWave, File fileToHide, File outputFile, String key, String instance, String mode, char option, String nombre) throws IOException {
        cFile = new File("caux" + System.currentTimeMillis());
        wav = new Wav(fileWave);
        cipher.encryptFile(fileToHide, cFile, key, instance, mode);
        Hash hash = new Hash();
        if (option == 1) {
            wFile = fileWave;
        } else {
            int repetitions = (int) (cFile.length() * 2 / wav.getTotalNumberSamples());
            int remainingBytes = (int) (cFile.length() * 4 - repetitions * wav.getTotalNumberSamples() * 2);
            if (repetitions != 0) {
                repeatWave(fileWave, repetitions, remainingBytes);
            }
        }
        return mix(wFile, cFile, outputFile, nombre, key, instance, mode, option, fileToHide.length(), hash.getHash(fileToHide, "MD5"));
        
    }
    
    
    private long getLnFoot(byte footBytes[]){
        char bytesln[]=new char[4];
        bytesln[0]=(char)footBytes[0];
        bytesln[1]=(char)footBytes[1];
        bytesln[2]=(char)footBytes[2];
        bytesln[3]=(char)footBytes[3];
        return files.chartoLong(bytesln);
    }
    
    private byte[] getHashBytes(byte footBytes[]){
        byte hashBytes[]=new byte[16];
        for(char i=0;i<16;i++){
            hashBytes[i]=footBytes[i+4];
        }
        return hashBytes;
    }
    private String getFileName(byte footBytes[]){
        String name="";
        char i=20;
        char c=(char)footBytes[20];
        while(c!='|' && i<63){
            name+=c;
            i++;
            c=(char)footBytes[i]; 
        };
        return name;
    }
    
    private File uncover(File fileWave,long numSamples) throws FileNotFoundException, IOException{
        FileInputStream fis=new FileInputStream(fileWave);
        File cFile=new File("daux"+System.currentTimeMillis());
        FileOutputStream fos=new FileOutputStream(cFile);
        fis.skip(44);
        byte sample1[]=new byte[2];
        byte sample2[]=new byte[2];
        byte out;
        
        for(long i=0;i<numSamples;i++){
            fis.read(sample1);
            fis.read(sample2);
            out=(byte)((sample1[0]&0x0f) | (sample2[0]&0x0f)<<4);
            fos.write(out);
        }
        fos.close();
        return cFile;
    }
    
    private void plainFileFinal(File decryptedFile,File finalFile,long fileLength) throws FileNotFoundException, IOException{
        FileInputStream fis=new FileInputStream(decryptedFile);
        FileOutputStream fos=new FileOutputStream(finalFile);
        int ite=(int)Math.floor(fileLength/268435456);
        byte bytes[];
        if(ite==0){
            bytes=new byte[(int)fileLength];
            fis.read(bytes);
            fos.write(bytes);
        }else{
            for(char i=0;i<ite;i++){
                bytes=new byte[268435456];
                fis.read(bytes);
                fos.write(bytes);
            }
            bytes=new byte[(int)(fileLength-ite*268435456)];
            fis.read(bytes);
            fos.write(bytes);
        }
        fis.close();
        fos.close();
        
    }
    
    private boolean equalHash(byte hash1[], byte hash2[]){
        try{
            for(char i=0;i<16;i++){
                if(hash1[i]!=hash2[i])
                    return false;
            }
            return true;
        }catch(Exception e){
            return false;
        }
    }
    /*
        Si regresa true, todo salio bien
        si regresa false la instancia o modo es o contraseña estan mal 
    
    se podria modificar a que regrese un char y dependiendo el numero es lo que se le muestra al usauario
    */
            
    public boolean uncover(File fileWave, String key, String instance, String mode) throws IOException {
        if (this.is_valid_wav(fileWave, true)) {
                Wav wav = new Wav(fileWave);
                byte footBytes[] = wav.getBytesFoot();
                footBytes = cipher.decryptFoot(footBytes, key, instance, mode);
                long lna = getLnFoot(footBytes);
                byte hashBytes[] = getHashBytes(footBytes);
                String name = getFileName(footBytes);
                File file= file = new File("daux" + System.currentTimeMillis());
                File ffile = new File(name);
            try {
              long ln = sizeCryptedFile(instance, lna);
                cFile = uncover(fileWave, ln);
                
                cipher.decryptFile(cFile, file, key, instance, mode);
                plainFileFinal(file, ffile, lna);
                Hash hash = new Hash();
                boolean complete = equalHash(hash.getHash(ffile, "MD5"), hashBytes);
                if (complete) {
                    fileWave.delete();
                    cFile.delete();
                    file.delete();
                } else {
                    cFile.delete();
                    file.delete();
                    ffile.delete();
                }
                this.userMessage2(complete);
                return complete;
            } catch (Exception e) {
                this.userMessage2(false);
                 cFile.delete();
                    file.delete();
                    ffile.delete();
                return false;
            }
        } else {
            this.userMessage3();
            return false;
        }
    }
    
    
    private boolean is_valid_wav(File fileWav,boolean decrypt) throws IOException{
        long lnFile=fileWav.length();
        if(lnFile<48)
            return false;
        Wav wav=new Wav(fileWav);
        long ln=files.chartoLong(wav.getChunkSize());
        if(lnFile!=ln+8)
            return false;
        ln=wav.getTotalNumberSamples();
        long lnfoot=wav.getNumberBytesFoot();
        if(ln*2+lnfoot+44!=lnFile)
            return false;
        if(decrypt){
            if(wav.getNumberBytesFoot()!=64)
                return false;
        }
        return true;
    }
}
