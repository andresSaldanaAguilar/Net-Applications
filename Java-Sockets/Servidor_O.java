import java.net.*;
import java.io.*;

public class Servidor_O {

    public static void main(String[] args)throws Exception{
        ServerSocket ss = new ServerSocket(3000);
        ss.setReuseAddress(true);
        System.out.println("Servidor iniciado");
        while(true){
            Socket cl = ss.accept();
            //response
            ObjectOutputStream oos = new ObjectOutputStream(cl.getOutputStream());
            Objeto ob2 = new Objeto(3,3);
            oos.writeObject(ob2);
            //request
            ObjectInputStream ois = new ObjectInputStream(cl.getInputStream());
            Objeto ob = (Objeto)ois.readObject();
            System.out.println("Objeto recibido desde "+ cl.getInetAddress() +" en el puerto " + cl.getPort());
            System.out.println("x:"+ob.x+" y:"+ob.y);
        }
	}
}