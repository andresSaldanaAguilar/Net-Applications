package WAVE;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class CabeceraWav {

    /**
     * @return the fileWav
     */
    public File getFileWav() {
        return fileWav;
    }

    /**
     * @param fileWav the fileWav to set
     */
    public void setFileWav(File fileWav) {
        this.fileWav = fileWav;
    }

    /**
     * @return the ChunkID
     */
    public char[] getChunkID() {
        return ChunkID;
    }

    /**
     * @param ChunkID the ChunkID to set
     */
    public void setChunkID(char[] ChunkID) {
        this.ChunkID = ChunkID;
    }

    /**
     * @return the ChunkSize
     */
    public char[] getChunkSize() {
        return ChunkSize;
    }

    /**
     * @param ChunkSize the ChunkSize to set
     */
    public void setChunkSize(char[] ChunkSize) {
        this.ChunkSize = ChunkSize;
    }

    /**
     * @return the Format
     */
    public char[] getFormat() {
        return Format;
    }

    /**
     * @param Format the Format to set
     */
    public void setFormat(char[] Format) {
        this.Format = Format;
    }

    /**
     * @return the Subchunk1_ID
     */
    public char[] getSubchunk1_ID() {
        return Subchunk1_ID;
    }

    /**
     * @param Subchunk1_ID the Subchunk1_ID to set
     */
    public void setSubchunk1_ID(char[] Subchunk1_ID) {
        this.Subchunk1_ID = Subchunk1_ID;
    }

    /**
     * @return the Subchunk1_Size
     */
    public char[] getSubchunk1_Size() {
        return Subchunk1_Size;
    }

    /**
     * @param Subchunk1_Size the Subchunk1_Size to set
     */
    public void setSubchunk1_Size(char[] Subchunk1_Size) {
        this.Subchunk1_Size = Subchunk1_Size;
    }

    /**
     * @return the AudioFormat
     */
    public char[] getAudioFormat() {
        return AudioFormat;
    }

    /**
     * @param AudioFormat the AudioFormat to set
     */
    public void setAudioFormat(char[] AudioFormat) {
        this.AudioFormat = AudioFormat;
    }

    /**
     * @return the NumChannels
     */
    public char[] getNumChannels() {
        return NumChannels;
    }

    /**
     * @param NumChannels the NumChannels to set
     */
    public void setNumChannels(char[] NumChannels) {
        this.NumChannels = NumChannels;
    }

    /**
     * @return the SampleRate
     */
    public char[] getSampleRate() {
        return SampleRate;
    }

    /**
     * @param SampleRate the SampleRate to set
     */
    public void setSampleRate(char[] SampleRate) {
        this.SampleRate = SampleRate;
    }

    /**
     * @return the ByteRate
     */
    public char[] getByteRate() {
        return ByteRate;
    }

    /**
     * @param ByteRate the ByteRate to set
     */
    public void setByteRate(char[] ByteRate) {
        this.ByteRate = ByteRate;
    }

    /**
     * @return the BlockAlign
     */
    public char[] getBlockAlign() {
        return BlockAlign;
    }

    /**
     * @param BlockAlign the BlockAlign to set
     */
    public void setBlockAlign(char[] BlockAlign) {
        this.BlockAlign = BlockAlign;
    }

    /**
     * @return the BitsPerSample
     */
    public char[] getBitsPerSample() {
        return BitsPerSample;
    }

    /**
     * @param BitsPerSample the BitsPerSample to set
     */
    public void setBitsPerSample(char[] BitsPerSample) {
        this.BitsPerSample = BitsPerSample;
    }

    /**
     * @return the Subchunk2_ID
     */
    public char[] getSubchunk2_ID() {
        return Subchunk2_ID;
    }

    /**
     * @param Subchunk2_ID the Subchunk2_ID to set
     */
    public void setSubchunk2_ID(char[] Subchunk2_ID) {
        this.Subchunk2_ID = Subchunk2_ID;
    }

    /**
     * @return the Subchunk2_Size
     */
    public char[] getSubchunk2_Size() {
        return Subchunk2_Size;
    }

    /**
     * @param Subchunk2_Size the Subchunk2_Size to set
     */
    public void setSubchunk2_Size(char[] Subchunk2_Size) {
        this.Subchunk2_Size = Subchunk2_Size;
    }
    
    public void setNumChannels0(char num){
        this.NumChannels[0]=num;
    }
    
    public char getNumChannels0(){
        return this.NumChannels[0];
    }


	private char ChunkID[];//4
	private char ChunkSize[];//4
	private char Format[];//4
	private char Subchunk1_ID[];//4
	private char Subchunk1_Size[];//4
	private char AudioFormat[];//2
	private char NumChannels[];//2
	private char SampleRate[];//4
	private char ByteRate[];//4
	private char BlockAlign[];//2
	private char BitsPerSample[];//2
	private char Subchunk2_ID[];//4
	private char Subchunk2_Size[];//4
        
       
        private File fileWav;
    public CabeceraWav(File wav) throws IOException {
            fileWav=wav;
            FileInputStream fis = new FileInputStream(wav);
            byte a1[]=new byte[4];
            byte a2[]=new byte[2];
            
            fis.read(a1);
            ChunkID=this.byteToChar4(a1);
            
            fis.read(a1);
            ChunkSize=this.byteToChar4(a1);
            
            fis.read(a1);
            Format=this.byteToChar4(a1);
            
            fis.read(a1);
            Subchunk1_ID=this.byteToChar4(a1);
            
            fis.read(a1);
            Subchunk1_Size=this.byteToChar4(a1);
            
            fis.read(a2);
            AudioFormat=this.byteToChar2(a2);
             
            fis.read(a2);
            NumChannels=(char [])this.byteToChar2(a2);
            
            fis.read(a1);
            SampleRate=this.byteToChar4(a1);

            fis.read(a1);
            ByteRate=this.byteToChar4(a1);

            fis.read(a2);
            BlockAlign=this.byteToChar2(a2);
            
            fis.read(a2);
            BitsPerSample=this.byteToChar2(a2);
            
            fis.read(a1);
            Subchunk2_ID=this.byteToChar4(a1);

            fis.read(a1);
            Subchunk2_Size=this.byteToChar4(a1);
            fis.close();

    }

   
    
     char[] byteToChar4(byte bytes[]){
        char a[]=new char[4];
        a[0] = (char) (bytes[0] & 0xff);
        a[1] = (char) (bytes[1] & 0xff);
        a[2] = (char) (bytes[2] & 0xff);
        a[3] = (char) (bytes[3] & 0xff);
        return a;
    }
    
     char[] byteToChar2(byte bytes[]){
        char b[] =new char[2];
        b[0] = (char) (bytes[0] & 0xff);
        b[1] = (char) (bytes[1] & 0xff);
        
        return b;
    }
     
    public byte[] charToByte2(char Chars[]){
        byte a[]=new byte[2];
        a[0]=(byte)Chars[0];
        a[1]=(byte)Chars[1];
        return a;
    }
    public byte[] charToByte4(char Chars[]){
        byte a[]=new byte[4];
        a[0]=(byte)Chars[0];
        a[1]=(byte)Chars[1];
        a[2]=(byte)Chars[2];
        a[3]=(byte)Chars[3];
        return a;
    }
    
    
}
