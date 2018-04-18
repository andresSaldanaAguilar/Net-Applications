package chat;

import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
public class Cliente{

    //id del cliente
    String id;
    //puerto
    int pto;

    public Cliente(int pto){
        this.pto = pto;
    }
    
    public MulticastSocket joinGeneralChat(String id){
        System.setProperty("java.net.preferIPv4Stack", "true");
        MulticastSocket s = null;
        try{
            //id del cliente
            this.id = id;
            String dir = "228.1.1.1";
            s = new MulticastSocket(pto);
            s.setTimeToLive(0);
            s.setReuseAddress(true);
            
            System.out.println("Cliente iniciado..");
            InetAddress gpo = null;
            try{
                gpo = InetAddress.getByName(dir);
            }catch(UnknownHostException u){
                System.err.println("Dir multicast no valida");
                System.exit(1);
            }            
            s.joinGroup(gpo);                                             
            
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            return s;
        }    
    }
    
    public static void main(String[] args) throws UnknownHostException, IOException{
        //Cliente cl = new Cliente();        
        //MulticastSocket m = cl.joinGeneralChat();
        String msj = "";
        while(!msj.equals("bye")){
            //Messenger msgr = new Messenger(m, InetAddress.getByName("228.1.1.1") , 5677);
 
            //Reciever rcvr = new Reciever(m, InetAddress.getByName("228.1.1.1") , 5677);
           
        }       
    }
}

//cliente, al pto2 y usar recieve