/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package downloadaccelerator;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author andressaldana
 */
public class MulticastServer extends Thread{
    int port;
    
    public MulticastServer(int port){
        this.port = port;
    }
    
    public void run(){
        
        System.setProperty("java.net.preferIPv4Stack", "true");
        InetAddress gpo=null;
        try{
            //Setting up server
            MulticastSocket s= new MulticastSocket(port);
            s.setReuseAddress(true);
            s.setTimeToLive(0);
            try{
                gpo = InetAddress.getByName("228.1.1.1");
            }catch(UnknownHostException u){
                System.err.println("Direccion no valida");
            }
            s.joinGroup(gpo);
            System.out.println(port+" Esperando por mensaje...");
            //waiting for packages
            for(;;){
                DatagramPacket p = new DatagramPacket(new byte[10],10);
                s.receive(p);
                String msj = new String(p.getData());
                System.out.println(port+" Datagrama recibido: "+msj);
            }
        }catch(Exception e){
            Logger.getLogger(MulticastServer.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
