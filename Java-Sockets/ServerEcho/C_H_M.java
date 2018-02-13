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
        BufferedReader br2 = new BufferedReader(new InputStreamReader(sr.getInputStream()));
        
        //para enviar cadenas (lineas)
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(sr.getOutputStream()));
        String datos=null;
        while(true){
            datos = br1.readLine();
            //si no recibe mensaje o se manda salir, termina la  conexion
            if(datos.equals("quit") || datos.equals("")){
                break;
            }
            //de los contrario, manda el mensaje y escucha el echo
            pw.println(datos);
            pw.flush();
            String echo= br2.readLine();
            System.out.println("Echo recibido: " + echo);
        }
        br1.close();
        br2.close();
        sr.close();
	}	
}