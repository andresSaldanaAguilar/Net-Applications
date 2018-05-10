import java.io.*;
import java.util.Random;

/**
 *
 * @author escom
 */
class Productor extends Thread{
    private DataOutputStream dos;
    private Random rand = new Random();
    
    public Productor(OutputStream os){
        dos = new DataOutputStream(os);
    }//constructor
    
    public void run(){
        //while(true){
            try{
                double num = rand.nextDouble();
                dos.writeDouble(num);
                System.out.println("Productor: "+num);
                dos.flush();
                sleep(Math.abs(rand.nextInt()%1000));
            }catch(Exception e){
                e.printStackTrace();
            }//catch
        //}//while
    }//run
}//class

class Consumidor extends Thread{
    private double prom_ant=0;
    private DataInputStream dis;
    
    public Consumidor(InputStream is){
        dis = new DataInputStream(is);
    }//constructor
    
    public void run(){
        //for(;;){
            try{
                double prom = dis.readDouble();
                System.out.println("Consumidor: "+prom);
                if(Math.abs(prom-prom_ant)>0.01){
                    System.out.println("El promedio actual es: "+prom);
                    prom_ant=prom;
                }//if
            }catch(Exception e){
                e.printStackTrace();
            }//catch
        //}//for
    }//run
}//class

class Filtro extends Thread{
    private DataInputStream dis;
    private DataOutputStream dos;
    private double total=0;
    private int cuenta=0;
    
    public Filtro(InputStream is, OutputStream os){
        dis = new DataInputStream(is);
        dos = new DataOutputStream(os);
    }//constructor
    
    public void run(){
        //for(;;){
            try{
                double x = dis.readDouble();
                total+=x;
                cuenta++;
                System.out.println("Total: "+total+" Cuenta: "+cuenta);
                if(cuenta !=0){
                    dos.writeDouble(total/cuenta);
                    dos.flush();
                }//if
            }catch(IOException e){
                e.printStackTrace();
            }//catch
        //}//for
    }//run
}//class

public class PipeTest {
    public static void main(String[] args){
        try{
            PipedOutputStream pout1 = new PipedOutputStream();
            PipedInputStream pin1 = new PipedInputStream(pout1);
            PipedOutputStream pout2 = new PipedOutputStream();
            PipedInputStream pin2 = new PipedInputStream(pout2);
            Productor prod = new Productor(pout1); //envia el valor
            Filtro f = new Filtro(pin1,pout2); //recibe el valor del productor
            Consumidor cons = new Consumidor(pin2); //recive el valor del filtro
            prod.start(); 
            f.start(); 
            cons.start();
        }catch(IOException io){
            io.printStackTrace();
        }//catch
    }//main
}
