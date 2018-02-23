import java.net.*;
import java.io.*;

public class Servidor{
    public static void main(String [] args){
        try{
            DatagramSocket s= new DatagramSocket(5678);
            System.out.println("Servicio iniciado... preparando mensaje..");
            String msj = "Hoña";
            byte[] b = msj.getBytes();
            InetAddress dst = null;
            try{
                //checando validez de direccion
                dst = InetAddress.getByName("localhost");
            }catch(Exception  u){
                u.printStackTrace();
            }
        DatagramPacket p = new DatagramPacket(b,b.length,dst,1234);

        for(;;){
            s.send(p);
            try{
                Thread.currentThread().sleep(3000);
            }catch(Exception e){
                //no hacemos na'    
            }
        }

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}