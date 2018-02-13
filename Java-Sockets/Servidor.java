import java.net.*;

import java.io.*;

public class Servidor{

    public static void main(String[] args)throws Exception{
        ServerSocket ss = new ServerSocket(3000);
        ss.setReuseAddress(true);
        System.out.println("Servidor iniciado");
        while(true){
            Socket cl = ss.accept();
            BufferedReader bis = new BufferedReader(new InputStreamReader(cl.getInputStream()));
            String client_msj = bis.readLine();
            System.out.println("Mensaje del cliente: " +client_msj);
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(cl.getOutputStream()));
            pw.println("echo: "+client_msj);
            bis.close();
            cl.close();
        }
	}
}