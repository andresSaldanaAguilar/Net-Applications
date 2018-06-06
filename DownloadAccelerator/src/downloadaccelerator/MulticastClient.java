/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package downloadaccelerator;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import sun.net.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author andressaldana
 */
public class MulticastClient {
    public static void main(String[] args ) throws IOException{
        System.setProperty("java.net.preferIPv4Stack", "true");
        InetAddress gpo=null;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Number of servers to search:");
        //number of servers
        int nos = Integer.parseInt(br.readLine());
        
        try{
            MulticastSocket cl= new MulticastSocket();
            cl.setReuseAddress(true);
            cl.setTimeToLive(0);
            String msj ="hola";
            byte[] b = msj.getBytes();
            try{
                gpo = InetAddress.getByName("228.1.1.1");
            }catch(UnknownHostException u){
                System.err.println("Direccion no valida");
            }//catch
            cl.joinGroup(gpo);
            System.out.println("Unido al grupo");
            
            for(int i = 0; i < nos; i++){
                DatagramPacket p = new DatagramPacket(b,b.length,gpo,9990+i);
                cl.send(p);
                System.out.println("Enviando mensaje: "+msj+ " al puerto: "+ 9990+i);            
            }
            cl.close();
                
        }catch(Exception e){
            Logger.getLogger(MulticastServer.class.getName()).log(Level.SEVERE, null, e);
        }//catch
    }//main
}
