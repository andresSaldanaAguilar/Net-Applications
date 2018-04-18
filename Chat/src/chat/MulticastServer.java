/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import com.apple.eio.FileManager;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;

/**
 *
 * @author andressaldana
 */
public class MulticastServer{
    
    public static void main(String[] args)throws Exception{
            int pto =5000;
            /*server config*/
            ServerSocket listener = new ServerSocket(3000);
            System.out.println("Server Running");
            LinkedList<Sala> salas = new LinkedList();
            Sala primersala = new Sala("Global","",pto);
            salas.add(primersala);
            
            
            while(true){
            Socket socket = listener.accept();
            System.out.println("Client connection");
            ObjectInputStream dIn = new ObjectInputStream(socket.getInputStream());
            
            boolean done = false;
            while(!done){
                
            byte messageType = -1;
            /*handling the EOF exception*/
            try{
                messageType = dIn.readByte();
            }catch(EOFException e){}   
            
            switch(messageType)
                {
                    //caso mandar salas
                    case 1:                    
                        ObjectOutputStream dout = new ObjectOutputStream(socket.getOutputStream());
                        dout.writeObject(salas);
                        dout.flush();
                        System.out.println("Data Sended");
                        break;
                    case 2:                    
                        String data = dIn.readUTF();
                        String [] arr = data.split(" ");
                        Sala sala = new Sala(arr[0],arr[1],pto+2);
                        salas.add(sala);
                        System.out.println("Data Saved");
                        break;
                    default:
                        done = true;
                }            
            }
            //dout.close();
            dIn.close();
        }
    }
}
