import java.net.*;
import java.io.*;

public class Recibe{
    public static void main(String args[]){
        try{
            int pto = 1234, n = 0,porcentaje = 0;
            ServerSocket s = new ServerSocket(pto);
            System.out.println("Servicio iniciado... esperando archivos");

            while(true){
                Socket cl = s.accept();
                DataInputStream dis = new DataInputStream(cl.getInputStream());
                String nombre = dis.readUTF();
                long tam = dis.readLong();
                DataOutputStream dos = new DataOutputStream(new FileOutputStream(nombre));
                System.out.println("Preprarado para recibir el archivo: "+ nombre + " desde " + cl.getInetAddress() + " con " + tam + " bytes de datos.");
                long recibido = 0;
                while(recibido < tam){
                    byte[] b = new byte[1500];
                    n = dis.read(b);
                    recibido += n;
                    dos.write(b,0,n);
                    dos.flush();
                    porcentaje = (int)((recibido * 100)/tam);
                    System.out.println("Recibido el " + porcentaje + "%");
                }
                System.out.println("Archivo Recibido");
                dos.close();
                dis.close();
                cl.close();
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}