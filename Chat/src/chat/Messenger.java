/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author andressaldana
 */
public class Messenger{
    
    MulticastSocket s = null;
    InetAddress gpo;
    int pto;
    
    public Messenger(MulticastSocket s,InetAddress gpo,int pto){
        this.s = s;
        this.gpo = gpo;
        this.pto = pto;
    }
    
    //crea un arreglo de bytes para mandar el archivo
    public static byte[] readFile(String fullpath) throws IOException{
        Path path = Paths.get(fullpath);
        byte[] data = Files.readAllBytes(path);
        return data;
    }
    
    public void sendMsg(String msj) {
        try{           
            //enviando mensaje
            byte[] b = msj.getBytes();
            DatagramPacket p1 = new DatagramPacket(b,b.length,gpo,pto);
            s.send(p1);


            try{
                Thread.currentThread().sleep(1000);
            }catch(InterruptedException ie){
            }
        }catch(IOException ex){
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendImg(String path) throws IOException{
        byte[] b = readFile(path);
        DatagramPacket p1 = new DatagramPacket(b,b.length,gpo,pto+1);
        s.send(p1);   
    }
    
    public static LinkedList<Sala> getRooms() throws IOException, ClassNotFoundException{
        Socket socket = new Socket("localhost",3000);
        ObjectOutputStream dOut = new ObjectOutputStream(socket.getOutputStream());
        // Send the action
        dOut.writeByte(1);
        dOut.flush();
        //get de dbs
        ObjectInputStream dIn = new ObjectInputStream(socket.getInputStream());
        LinkedList<Sala> salas = (LinkedList<Sala>) dIn.readObject();
        
        dIn.close();
        dOut.close();
        return salas; 
    }
    
    public static void setRoom(String data) throws IOException, ClassNotFoundException{
        Socket socket = new Socket("localhost",3000);
        ObjectOutputStream dOut = new ObjectOutputStream(socket.getOutputStream());
        // Send the action
        dOut.writeByte(2);
        dOut.writeUTF(data);
        dOut.flush();
        dOut.close();
    }

}
