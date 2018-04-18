import java.io.*;
import java.net.*;

public class Cliente_O_UDP {
  public static void main(String[] args){
    int puerto = 8000;
    int tbuf = 65000;

    try{
    InetAddress direccion = InetAddress.getByName("127.0.0.1");
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    for(;;){
        DatagramSocket cl = new DatagramSocket();
        String msj = br.readLine();
        byte[] datos = msj.getBytes();
        //numero de datagramas
        int ndatagrama = 1; 

        if(datos.length > tbuf){
            ndatagrama = (datos.length)/tbuf;
            if(datos.length%tbuf > 0 ){
                ndatagrama = ndatagrama + 1;
            }
        }

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
            cl.send(p);
            
            System.out.println("Datagrama enviado con los datos:");
            System.out.println("N: "+objeto.getN());
            System.out.println("de un total de: "+objeto.getTotal());
            System.out.println("Msj: "+new String(objeto.getB()));
            oos.close();
            baos.close();

        }
        System.out.println("Paquete enviado");

        DatagramPacket dp = new DatagramPacket(new byte[65100],65100);
        cl.receive(dp);
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

    }
    }catch(Exception e){
        System.err.println(e);
    }
  }//main
}//class