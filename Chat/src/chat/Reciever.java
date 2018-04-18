/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
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
public class Reciever extends Thread{
    
    JEditorPane jep = null;    
    private MulticastSocket s = null;
    private InetAddress gpo;
    private int pto;
    private volatile String message;
    String id; 

    public Reciever(MulticastSocket s,InetAddress gpo,int pto, JEditorPane jep,String id){
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
            String [] arreglo = s.split("\t");
            StringReader reader;
            
            String path = "smilesmall.png";
        
            
            if(arreglo[1].equals(id)){
                reader = new StringReader("<b style=\"color:green\">"+arreglo[1]+": </b>"+arreglo[0]+"<br>"); 
            }
            else{
                reader = new StringReader("<b style=\"color:blue\">"+arreglo[1]+": </b>"+arreglo[0]+"<br>"); 
            }
       
            editor.read(reader, jep.getDocument(), jep.getDocument().getLength());

        } catch(BadLocationException exc) {
           exc.printStackTrace();
        }
    }

    public void run(){
        System.out.println("Escuchador de mensajes");
        while(true){
        String anuncio = "";
            try{
                //recibiendo mensaje
                DatagramPacket p = new DatagramPacket(new byte[1024],1024,gpo,pto);
                s.receive(p);
                System.out.println("Direccion: " + p.getAddress());
                anuncio = new String(p.getData(),0,p.getLength());
                System.out.println("Anuncio: " + anuncio);
                setMessage(anuncio);
                //le pegamos el mensaje cada vez que llegue
                append(anuncio);
            }catch(IOException ex){
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
