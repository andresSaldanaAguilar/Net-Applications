/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import static fileMultticast.ArchivosCliente.writeFile;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;

/**
 *
 * @author andressaldana
 */
public class RecieverImg extends Thread{
    
    JEditorPane jep = null;    
    private MulticastSocket s = null;
    private InetAddress gpo;
    private int pto;
    private volatile String message;
    String id; 

    public RecieverImg(MulticastSocket s,InetAddress gpo,int pto, JEditorPane jep,String id){
        //id del dueño
        this.id = id;
        //editor asociado
        this.jep = jep;
        //socket asociado
        this.s = s;
        //grupo y puerto
        this.gpo = gpo;
        this.pto = pto;
        //mensaje que llega del grupo
        this.message = "null";
    }
    
    public void setMessage(String message){
        this.message = message;
    }
    
    public String getMessage(){
        return this.message;
    }
        
    public void append(String s) throws IOException {
        try {
            
            HTMLEditorKit editor = (HTMLEditorKit) jep.getEditorKit();
            //checando si es mensaje local o de otro

            StringReader reader;          
            reader = new StringReader("<img src=\"file:"+s+"\">"); 
       
            editor.read(reader, jep.getDocument(), jep.getDocument().getLength());

        } catch(BadLocationException exc) {
           exc.printStackTrace();
        }
    }
    
    //random unique string
    public static String generateString() {
        String uuid = UUID.randomUUID().toString();
        return "uuid = " + uuid;
    }
    
    //crea un archivo de un arreglo de bytes
    public static String writeFile(byte[] file) throws FileNotFoundException, IOException{
        String filename = "img/"+generateString()+".jpg";
        System.out.println(file.length);
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            fos.write(file);
        }
        return filename;
    }

    public void run(){
        while(true){
        String anuncio = "";
            try{
                /*empieza recibimiento de archivos*/
                DatagramPacket p = new DatagramPacket(new byte[64000],64000,gpo,pto);
                s.receive(p);
                String filename = writeFile(p.getData());           

                System.out.println("Direccion: " + p.getAddress());
                System.out.println("Tamaño: " + p.getLength());
                //le pegamos el mensaje cada vez que llegue
                append(filename);
            }catch(IOException ex){
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
