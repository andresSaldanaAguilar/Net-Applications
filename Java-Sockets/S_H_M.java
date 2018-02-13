import java.net.*;
import java.io.*;

public class S_H_M{

    public static void main(String[] args)throws Exception{
        //creacion del socket
        ServerSocket ss = new ServerSocket(3000);
        ss.setReuseAddress(true);
        System.out.println("Servidor iniciado");
        //conexion con el cliente
        Socket cl = ss.accept();
        System.out.println("Conexion desde "+ cl.getInetAddress() +" en el puerto " + cl.getPort());
        //escritor de respuesta
        PrintWriter pw= new PrintWriter(new OutputStreamWriter(cl.getOutputStream()));
        //escucha de peticiones
        BufferedReader br1 = new BufferedReader(new InputStreamReader(cl.getInputStream()));
        String msj;
        while(true){
            msj = br1.readLine();
            String echo = "echo-" + msj + "\n";
            System.out.println("mensaje del cliente: " + msj);
            pw.println(echo);
            pw.flush(); //vaciar el buffer de escritura
        }

	}
}