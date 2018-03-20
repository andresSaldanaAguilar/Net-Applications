package tiendavirtual;
import java.net.*;
import java.io.*;

public class Servidor_O {

    public static void main(String[] args)throws Exception{
        
        /*looking the inventory*/
        BufferedReader br = null;
        String[] characters = new String[1024];//just an example - you have to initialize it to be big enough to hold all the lines!

        try {

            String sCurrentLine;
            br = new BufferedReader(new FileReader("data.txt"));

            int i=0;
            while ((sCurrentLine = br.readLine()) != null) {
                String[] arr = sCurrentLine.split(" ");
                //for the first line it'll print
                System.out.println("arr[0] = " + arr[0]); // h
                System.out.println("arr[1] = " + arr[1]); // Vito
                System.out.println("arr[2] = " + arr[2]); // 123
                if(arr.length == 4){
                    System.out.println("arr[3] = " + arr[3]);
                }

                //Now if you want to enter them into separate arrays
                characters[i] = arr[0];
                // and you can do the same with
                // names[1] = arr[1]
                //etc
                i++;
            }
            
            1
            

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
        ServerSocket ss = new ServerSocket(3000);
        ss.setReuseAddress(true);
        System.out.println("Servidor iniciado");
        while(true){
            Socket cl = ss.accept();
            //response
            ObjectOutputStream oos = new ObjectOutputStream(cl.getOutputStream());
            Producto ob2 = new Producto(3,3);
            oos.writeObject(ob2);
            //request
            ObjectInputStream ois = new ObjectInputStream(cl.getInputStream());
            Producto ob = (Producto)ois.readObject();
            System.out.println("Objeto recibido desde "+ cl.getInetAddress() +" en el puerto " + cl.getPort());
            System.out.println("x:"+ob.x+" y:"+ob.y);
        }
	}
}