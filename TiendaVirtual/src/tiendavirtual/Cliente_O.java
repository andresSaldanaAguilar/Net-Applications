package tiendavirtual;
import java.net.*;
import java.io.*;

public class Cliente_O {
	public static void main(String[] args)throws Exception{
		Socket cl = new Socket("localhost",3000);
		System.out.println("Conexion con servidor exitosa");
		//response
		ObjectInputStream ois = new ObjectInputStream(cl.getInputStream());
		Producto ob2 = (Producto)ois.readObject();
		System.out.println("x:"+ob2.x+" y:"+ob2.y);
		//request
		ObjectOutputStream oos = new ObjectOutputStream(cl.getOutputStream());
		Producto obj = new Producto(5,4);
		oos.writeObject(obj);
	}	
}