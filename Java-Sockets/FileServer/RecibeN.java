import java.net.*;
import java.io.*;

public class RecibeN{
    public static void main(String args[]){
        try{
            int pto = 1234, n = 0,porcentaje = 0;
            ServerSocket s = new ServerSocket(pto);
            System.out.println("Servicio iniciado... esperando archivos");

            while(true){
                Socket cl = s.accept();
                /*se manda un archivo por socket? o hay forma de enviar varios  archivos por socket (como sabe cuantos va a leer)?*/


                DataInputStream dis = new DataInputStream(cl.getInputStream());
                String nombre = dis.readUTF();
                System.out.println("nombre: "+nombre);
                long tam = dis.readLong();
                                  
                DataOutputStream dos = new DataOutputStream(new FileOutputStream("/Users/andressaldana/Documents/Github/Net-Applications/Java-Sockets/FileServer/files/"+nombre));
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
            }
                //cl.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}