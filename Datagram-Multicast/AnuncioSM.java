import java.net.*;
import java.io.*;
public class AnuncioSM{

    
    public static void main(String[] args){
        System.setProperty("java.net.preferIPv4Stack", "true"); //necesarios solo en MacOS
        try{
            int pto1 = 1234, pto2 = 5678;
            String dir = "228.1.1.1";
            MulticastSocket s = new MulticastSocket(pto1);
            s.setTimeToLive(255);
            s.setReuseAddress(true);
            
            System.out.println("SErvicio de anuncios iniciado..");
            String msj = "Un mensaje multicast";
            byte[] b = msj.getBytes();
            InetAddress gpo = null;
            try{
                gpo = InetAddress.getByName(dir);
            }catch(UnknownHostException u){
                System.err.println("Dir multicast no valida");
                System.exit(1);
            }
            s.joinGroup(gpo);
            for(;;){
                DatagramPacket p = new DatagramPacket(b,b.length,gpo,pto2); //pto2, puerto de clientes
                s.send(p);
                try{
                    Thread.currentThread().sleep(1000);
                }catch(InterruptedException ie){
                }
            }
        }
            catch(Exception e){
                e.printStackTrace();
            }
    }
}

//cliente, al pto2 y usar recieve