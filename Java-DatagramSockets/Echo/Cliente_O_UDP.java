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
            int n = bais.read(tmp); //problema aqui
            Objeto_U objeto = new Objeto_U(i+1,ndatagrama,tmp);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(objeto);
            oos.flush();
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

    }
    }catch(Exception e){
        System.err.println(e);
    }
  }//main
}//class