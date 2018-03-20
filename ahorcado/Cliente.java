import java.net.*;
import java.io.*;
public class Cliente{
    public static void main(String args[]){
        try{
            int pto = 9999;
            String dir = "::1";
            InetAddress gpo = null;
            try{
                gpo = InetAddress.getByName(dir);
            }catch(UnknownHostException u){
                System.err.println("Dir no valida");
                System.exit(1);
            }
            Socket cl = new Socket(dir,pto);
            //para enviar cadenas (lineas)
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(cl.getOutputStream()));
            String datos=null;
            //lee de consola
            BufferedReader br1= new BufferedReader(new InputStreamReader(System.in));
            //para leer cadenas
            BufferedReader br2 = new BufferedReader(new InputStreamReader(cl.getInputStream()));
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
            
            cl.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

//para los atributos sin valor:
// primitivos = 0
// objetos = null
