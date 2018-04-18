import java.io.*;
import java.net.*;

public class Servidor_O_UDP {
  public static void main(String[] args){


        try{
        InetAddress direccion = InetAddress.getByName("127.0.0.1");    
        int puerto = 8000;
        int aux = 0;
        String msj = "";
      
        DatagramSocket s = new DatagramSocket(puerto);
        System.out.println("Servidor UDP iniciado en el puerto "+s.getLocalPort());
        System.out.println("Recibiendo datos...");
        for(;;){
             
            DatagramPacket dp = new DatagramPacket(new byte[65100],65100);
            s.receive(dp);
            System.out.println("Datagrama recibido... extrayendo informacion");
            System.out.println("Host remoto: "+dp.getAddress().getHostAddress()+" : "+dp.getPort());
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(dp.getData()));          

            try{     
                Objeto_U objeto = (Objeto_U)ois.readObject();
                System.out.println("Datos del paquete:");
                msj = new String (objeto.getB());     
            }   
            catch(EOFException eof){
                System.out.println("excepcion");
            }
            System.out.println("mensaje recibido: " + msj);

            int tbuf = 65000;
            byte[] datos = msj.getBytes();
            //numero de datagramas
            int ndatagrama = 1; 
    
            if(datos.length > tbuf){
                ndatagrama = (datos.length)/tbuf;
                if(datos.length%tbuf > 0 ){
                    ndatagrama = ndatagrama + 1;
                }
            }
            System.out.println("ndatagrama: "+ndatagrama);
            //enviando msg
            for(int i = 0; i < ndatagrama ; i++){
            ByteArrayInputStream bais = new ByteArrayInputStream(datos);
            byte[] tmp = new byte[tbuf];
            //se leen los datos con el tamanio del buffer y lo guarda en tmp
            int n = bais.read(tmp); 
            //cuantos van, cuantos son y fraccion de mensaje
            Objeto_U objeto = new Objeto_U(i+1,ndatagrama,tmp);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(objeto);
            oos.flush();
            //arreglo de bytes a enviar
            byte[] d = baos.toByteArray();
            DatagramPacket p = new DatagramPacket(d,d.length);
            p.setAddress(direccion);
            p.setPort(puerto);
            p.setData(d);          
            s.send(p);
            
            System.out.println("Datagrama enviado con los datos:");
            System.out.println("N: "+objeto.getN());
            System.out.println("de un total de: "+objeto.getTotal());
            System.out.println("Msj: "+new String(objeto.getB()));
            oos.close();
            baos.close();
            }
            
        }//for
        //s.close();
    }catch(Exception e){
        System.err.println(e);
    }
    System.out.println("Termina el contenido del datagrama...");
  }//main
}//class