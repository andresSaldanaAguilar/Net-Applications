import java.net.*;
import java.io.*;
public class Cliente{

    
    public static void main(String[] args){
        System.setProperty("java.net.preferIPv4Stack", "true");
        try{
            int pto = 5678;
            String dir = "228.1.1.1";
            MulticastSocket s = new MulticastSocket(pto);
            s.setTimeToLive(255);
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
            for(;;){
                DatagramPacket p = new DatagramPacket(new byte[1024],1024,gpo,pto); //pto2, puerto de clientes
                s.receive(p);
                System.out.println("Direccion: " + p.getAddress());
                String anuncio = new String(p.getData(),0,p.getLength());
                System.out.println("Anuncio: " + anuncio);

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