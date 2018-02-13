import java.net.*;
import java.io.*;

public class C_H_M {

public static void main(String[] args)throws Exception{
        int pto=3000;
        String dir="localhost";
        //para leer de consola
        BufferedReader br1= new BufferedReader(new InputStreamReader(System.in)); 
        //creacion del socket
        Socket sr = new Socket(dir,pto);
        System.out.println("Conexion establecida");

        //para leer cadenas
        BufferedReader br2= new BufferedReader(new InputStreamReader(sr.getInputStream()));
        
        //para enviar cadenas (lineas)
        PrintWriter pw= new PrintWriter(new OutputStreamWriter(sr.getOutputStream()));
        
        while(true){
            String datos = br1.readLine();
            pw.println(datos);
            pw.flush();
            String echo= br2.readLine();
            System.out.println("Echo recibido: " + echo);
        }
        // br2.close();
        // br1.close();
        // sr.close();
	}	
}