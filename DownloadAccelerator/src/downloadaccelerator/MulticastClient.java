/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package downloadaccelerator;
import java.io.IOException;
import sun.net.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author andressaldana
 */
public class MulticastClient {
    public static void main(String[] args ){
        System.setProperty("java.net.preferIPv4Stack", "true");
        InetAddress gpo=null;
        int pto =9999;
        try{
            MulticastSocket cl= new MulticastSocket(9999);
            System.out.println("Cliente escuchando puerto "+ cl.getLocalPort());
            cl.setReuseAddress(true);
            try{
                gpo = InetAddress.getByName("228.1.1.1");
            }catch(UnknownHostException u){
                System.err.println("Direccion no valida");
            }//catch
            cl.joinGroup(gpo);
            System.out.println("Unido al grupo");
            for(;;){
                DatagramPacket p = new DatagramPacket(new byte[10],10);
                cl.receive(p);
                System.out.println("Datagrama recibido..");
                String msj = new String(p.getData());
                
                System.out.println("Servidor descubierto: "+p.getAddress());
               
            }//for
            
        }catch(Exception e){
            Logger.getLogger(MulticastServer.class.getName()).log(Level.SEVERE, null, e);
        }//catch
    }//main
}
