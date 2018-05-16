
package proyecto;

import Hash.Hash;
import WAVE.Wav;
import java.io.File;
import java.io.IOException;
import Process.Process;
import java.util.Arrays;

/**
 *
 * @author miguel
 */
public class Proyecto {


    
    
    
    public static void main(String[] args) throws IOException {
        File fileWave=new File("/Users/andressaldana/Documents/Github/Net-Applications/proyecto/Music.wav");
        File fileToHide=new File("/Users/andressaldana/Documents/Github/Net-Applications/proyecto/mensaje.txt");
        File outputFile=new File("/Users/andressaldana/Documents/Github/Net-Applications/proyecto/salida.wav");
        String instance="AES";
        String mode="ECB";
        String keyAES="asdfghjklzxcvbnm";
        String keyDES="asdfghjk";
        String nombre="salida.txt";
        Process p=new Process();
        p.hide(fileWave, fileToHide, outputFile, instance, mode, keyAES,nombre);
        p.uncover(outputFile,  keyAES, instance, mode);
     }
    
}
