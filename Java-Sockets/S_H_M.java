import java.net.*;
import java.io.*;

public class S_H_M{

    public static void main(String[] args)throws Exception{
        //creacion del socket
        ServerSocket ss = new ServerSocket(3000);
        ss.setReuseAddress(true);
        System.out.println("Servidor iniciado");
        while(true){
            String msj;
            //conexion con el cliente
            Socket cl = ss.accept();
            System.out.println("Conexion desde "+ cl.getInetAddress() +" en el puerto " + cl.getPort());
            //escritor de respuesta
            PrintWriter pw= new PrintWriter(new OutputStreamWriter(cl.getOutputStream()));
            //escuchador de peticiones
            BufferedReader br1 = new BufferedReader(new InputStreamReader(cl.getInputStream()));
            
            //estara cachando los mensajes de un cliente hasta que este se desconecte
            try{
                while ((msj = br1.readLine()) != null) {
                    String echo = "echo --" + msj;
                    System.out.println("mensaje del cliente: " + msj);
                    pw.println(echo);
                    pw.flush(); //vaciar el buffer de escritura
                }
            }catch(Exception e){
                pw.close();
                br1.close();
                cl.close();
            }
        }
	}
}