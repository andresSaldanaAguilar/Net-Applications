import java.net.*;
import java.io.*;

public class Servidor {

    public static void main(String[] args)throws Exception{
        ServerSocket ss = new ServerSocket(8888);
        ss.setReuseAddress(true);
        System.out.println("Servidor iniciado");
        while(true){
            Socket cl = ss.accept();
            System.out.println("Cliente conectado desde "+ cl.getInetAddress() +" en el puerto " + cl.getPort());
            //response
            ObjectOutputStream oos = new ObjectOutputStream(cl.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(cl.getInputStream());
            Dato d1 = (Dato)ois.readObject();
            System.out.println("objeto recibido con los datos: \n " + d1.getNombre() + " "+ d1.getEdad() + " " + d1.getTelefono() + " " + d1.getSueldo());
            Dato d2 = new Dato("Pepillo Orijel",45,"55552233",10.8f);
            oos.writeObject(d2);
            oos.flush();
            System.out.println("Objeto enviado");
            ois.close();
            oos.close();
            cl.close();
        }
	}
}