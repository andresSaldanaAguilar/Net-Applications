/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileMultticast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

/**
 *
 * @author andressaldana
 */
public class ArchivosCliente {
    
    public static byte[] readFile(String fullpath) throws IOException{
        Path path = Paths.get(fullpath);
        byte[] data = Files.readAllBytes(path);
        return data;
    }
    
    public static void writeFile(byte[] file) throws FileNotFoundException, IOException{
        System.out.println(file.length);
        try (FileOutputStream fos = new FileOutputStream("smile2.png")) {
            fos.write(file);
        }
    }
    
    public static void main(String args[]) throws IOException{
        System.setProperty("java.net.preferIPv4Stack", "true");
        //byte[] file = readFile("smile.png");

        System.setProperty("java.net.preferIPv4Stack", "true");
        MulticastSocket s = null;
        try{
            //id del cliente
            int pto =5799;
            String dir = "228.1.1.1";
            s = new MulticastSocket(pto);
            s.setTimeToLive(0);
            s.setReuseAddress(true);
            
            System.out.println("Cliente iniciado..");
            InetAddress gpo = null;
            try{
                gpo = InetAddress.getByName(dir);
            }catch(UnknownHostException u){
                System.err.println("Dir multicast no valida");
                System.exit(1);
            }            
            s.joinGroup(gpo); 
            
            /*empieza escritura de bytes*/
            byte[] b = readFile("smilesmall.png");
            DatagramPacket p1 = new DatagramPacket(b,b.length,gpo,pto);
            s.send(p1);            
            
            /*empieza recibimiento de archivos*/
            DatagramPacket p = new DatagramPacket(new byte[64000],64000,gpo,pto);
            s.receive(p);
            writeFile(p.getData());           
            String anuncio;
            System.out.println("Direccion: " + p.getAddress());
            System.out.println("Tamaño: " + p.getLength());

            
        }
        catch(Exception e){
            e.printStackTrace();
        }
 
    }
}
