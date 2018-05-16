package WAVE;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import static sun.security.krb5.Confounder.bytes;


public class Wav extends CabeceraWav{

    /**
     * @return the foot
     */
    public boolean isFoot() {
        return foot;
    }

    /**
     * @param foot the foot to set
     */
    private void setFoot(boolean foot) {
        this.foot = foot;
    }
    private boolean foot;
    
    public Wav(File wav) throws IOException {
        super(wav);
    }
    //Resegra el tamaño completo de el archivo en bytes
     public long getFileSize(){
	return chartoLong(this.getChunkSize());
    }

    //Convierte a long un arreglo de 4chars    
    private long chartoLong(char chars[]){
        return  0xffffffff &( (chars[0]&0xffffffff) |  ((chars[1] << 8)&0xffffffff)  |  ((chars[2] <<16)&0xffffffff)  |  ((chars[3] <<24&0xffffffff)) );
    }
    
    //Convierte a int un arreglo de 4chars   
    private int chartoInt(char chars[]){
        return 0xffff&(chars[0]|chars[1]<<8);
    }
    
    //Convierte un long a un arreglo de 4 chars
    private char[] longtoChar(long valor){
        char a[]=new char[4];
        a[0]=(char) (valor & 0xff);
        a[1]=(char) (valor >> 8 & 0xff);
        a[2]=(char) (valor >> 16 & 0xff);
        a[3]=(char) (valor >> 24 & 0xff);
     return a;   
    }
    
    
    //Regresa el tamaño de las muestras en bytes
     public int getSampleSize(){
	return chartoInt(this.getBitsPerSample())/8;	
    }
    
    //Regresa el numero de canales;
     public int getCannalNumber(){
	return (int)this.getNumChannels()[0];
    }
    
    //Regresa el numero de muestras de audio si es momo o estero regresa la misma cantidad por lo cual en estereo se debe multiplicar por 2
     public long getNumberAudioSamples(){
         //System.out.print("Tam:"+(long)chartoLong(getSubchunk2_Size())+"\n");
         //System.out.print("Cnals:"+getCannalNumber()+"\n");
         //System.out.print("Sample:"+this.getSampleSize()+"\n");
	return (long)this.chartoLong(getSubchunk2_Size())/(this.getCannalNumber()*this.getSampleSize());
    }
     
    public long getTotalNumberSamples(){
	return this.chartoLong(getSubchunk2_Size())/this.getSampleSize();
    }

     //Regresa el tamanio total del archivo wav-8 
     public long getNumberBytesAudioInformation(){
        return this.chartoLong(getSubchunk2_Size());
    }
     //agrega un numero de muestras a la cabecera
    public void addNumberAudioSamples(long numberSamples){
        long val=this.chartoLong(this.getSubchunk2_Size())+(numberSamples*2*(int)this.getCannalNumber());
        this.setSubchunk2_Size(this.longtoChar(val));
	this.setChunkSize(this.longtoChar(this.chartoLong(this.getChunkSize())+(numberSamples*2*(int)this.getCannalNumber())));
   
    }
    //para agregar el pie del archivo wav
    private void addNumberBytes(int number){
         this.setChunkSize(this.longtoChar(this.chartoLong(this.getChunkSize())+number));  
    }
    
    //Convierte una cabecera de tipo mono a una de tipo estereo
     public boolean setAudioStereo() {
         if (this.getCannalNumber() == 1) {
             long aux = this.chartoLong(getChunkSize()) * 2;
             setChunkSize(this.longtoChar(aux));
             aux = this.chartoLong(getSubchunk2_Size()) * 2;
             setSubchunk2_ID(this.longtoChar(aux));
             this.setNumChannels0((char) 2);
             aux = this.chartoInt(getByteRate()) * 2;
             setByteRate(this.longtoChar(aux));
             aux = this.chartoInt(getBlockAlign()) * 2;
             setBlockAlign(this.longtoChar(aux));
             return true;
         }
         return false;
    }
    
    //regresa el numero de bytes del pie del archivo wav
    public long getNumberBytesFoot(){
	return this.chartoLong(getChunkSize())-36-this.chartoLong(this.getSubchunk2_Size());
    }
    
   
    public void deleteBytesFoot(){
        long numBytesFoot=getNumberBytesFoot();
       this.setChunkSize(this.longtoChar(this.chartoLong(this.getChunkSize())-numBytesFoot));
        setFoot(false);
    }
     
     
    public byte[] getBytesAudio(){
		try{	
                        FileInputStream fis=new FileInputStream(this.getFileWav());
                        byte[] Bytes=new byte[44];
                        fis.read(Bytes);
			Bytes=new byte[(int)getNumberBytesAudioInformation()];
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
    
    public void setFootInHeadLength(int number) throws IOException{
        this.deleteBytesFoot();
        this.addNumberBytes(number);
    }
    
    public byte[] getBytesFoot() throws FileNotFoundException, IOException{
	long bytesSobrantes=this.chartoLong(this.getChunkSize())-this.chartoLong(this.getSubchunk2_Size())-36;
	FileInputStream fis=new FileInputStream(this.getFileWav());
        fis.skip(44+this.chartoLong(this.getSubchunk2_Size()));
        byte[] bytes=new byte[(int)bytesSobrantes];
        fis.read(bytes);
	return bytes;
    }
 }
    




