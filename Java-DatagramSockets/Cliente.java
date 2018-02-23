import java.net.*;
import java.io.*;

public class Cliente{
    public static void main(String[] args){
        try{
            DatagramSocket cl = new DatagramSocket(1234);
            System.out.println("Cliente iniciado escuchando anuncios..");
            for(;;){
                DatagramPacket p = new DatagramPacket(new byte[65535],65535);
                cl.receive(p);
                //p.getlength ajusta el tamaño del mensaje
                String msj = new String(p.getData(),0,p.getLength());
                System.out.println("Mensaje recibido desde" + p.getAddress() + 
                "Con el mensaje: " + msj);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}