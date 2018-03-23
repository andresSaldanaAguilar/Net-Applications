package tiendavirtual;
import java.net.*;
import java.io.*;
import java.util.LinkedList;

public class Cliente_O {
	public static void main(String[] args)throws Exception{
            Socket cl = new Socket("localhost",3000);
            System.out.println("Conexion con servidor exitosa");

            //leemos el catalogo de productos
            ObjectInputStream ois = new ObjectInputStream(cl.getInputStream());                
            LinkedList<Producto> list = new LinkedList();
                
            try{    
                while(true){  
                    list.add((Producto)ois.readObject());
                }                   
            }catch(EOFException e){}
            
            Interfaz gui = new Interfaz(list);
            gui.setVisible(true);
            gui.showItems();            
        }
}