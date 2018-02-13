import java.net.*;
import java.io.*;

public class Cliente{

public static void main(String[] args)throws Exception{
        int pto=3000;
        BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("\n Escribe la IP del servidor:");
        String dir = br1.readLine();
        System.out.println("\n Intentando conectar con el servidor...");
        Socket cl = new Socket(dir,pto);
        System.out.println("\n Escribe el mensaje para el servidor:");
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(cl.getOutputStream()));
        String msj = br1.readLine();
        
        BufferedReader br2 = new BufferedReader(new InputStreamReader(cl.getInputStream()));
        String echo = br2.readLine();
        System.out.println(echo);
        pw.println(msj);
        pw.close();
        br1.close();
        cl.close();
	}	
}