import java.net.*;
import java.io.*;
public class Cliente{
    public static void main(String args[]){
        try{
            int pto = 8888;
            String dst = "127.0.0.1";
            Socket cl = new Socket(dst,pto);
            System.out.println("Conexion establecida... produciendo objeto");
            Dato d1  = new Dato("Juan",20,"555555555",20.5f); 
            System.out.println("Se enviara el objeto con los datos: ");
            System.out.println(d1.getNombre() + " "+ d1.getEdad() + " " + d1.getTelefono() + " " + d1.getSueldo());
            ObjectOutputStream oos = new ObjectOutputStream(cl.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(cl.getInputStream());
            oos.writeObject(d1);
            oos.flush();
            System.out.println("objeto enviado.. prepardo para recibir un Objeto");
            Dato d2 = (Dato)ois.readObject();
            System.out.println("objeto recibido con los datos: \n " + d2.getNombre() + " "+ d2.getEdad() + " " + d2.getTelefono() + " " + d2.getSueldo());
            ois.close();
            oos.close();
            cl.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

//para los atributos sin valor:
// primitivos = 0
// objetos = null
