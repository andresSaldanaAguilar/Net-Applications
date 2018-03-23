package tiendavirtual;
import java.net.*;
import java.io.*;
import java.util.LinkedList;

public class Servidor_O {

    public static void main(String[] args)throws Exception{
        
        /*trae todos los objetos del inventorio*/
        LinkedList<Producto> list;
        Reader r = new Reader();
        list= r.Read();
        
        ServerSocket ss = new ServerSocket(3000);
        ss.setReuseAddress(true);
        System.out.println("Servidor iniciado");
        while(true){
            Socket cl = ss.accept();
            System.out.println("Cliente conectado desde "+ cl.getInetAddress() +" en el puerto " + cl.getPort());
            
            //envio de catalogo de productos
            ObjectOutputStream oos = new ObjectOutputStream(cl.getOutputStream());       
            
            for (Producto producto: list) {
                oos.writeObject(producto);
                oos.flush();
            }
            oos.close();
                  
            //ss.close();
            //request

            //System.out.println("Objeto recibido desde "+ cl.getInetAddress() +" en el puerto " + cl.getPort());
            //System.out.println("x:"+ob.x+" y:"+ob.y);
        }
	}
}