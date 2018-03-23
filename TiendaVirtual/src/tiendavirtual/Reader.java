/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiendavirtual;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

/**
 *
 * @author andressaldana: esta clase lee los registros en el archivo y retorna una lista de objetos
 * 
 */
public class Reader {
        /*looking the inventory*/
    
    LinkedList<Producto> Read(){
        BufferedReader br = null;

        try{
            //leyendo registros del catalogo
            String sCurrentLine;
            br = new BufferedReader(new FileReader("data.txt"));   
            LinkedList<String> catalogue= new LinkedList();
            LinkedList<LinkedList> li = new LinkedList(); 
            while ((sCurrentLine = br.readLine()) != null) {
                String[] arr = sCurrentLine.split("/n");
                //for the first line it'll print
                for(int i= 0; i < arr.length; i++){
                   catalogue.add(arr[i]); 
                }
            }
            
            //lista con productos del catalogo
            LinkedList<Producto> objectLi = new LinkedList();
            for(int i= 0; i < catalogue.size(); i++) {
                String[] arr = catalogue.get(i).split("_");
                //for the first line it'll print
                Producto p = new Producto(arr[0],arr[1],Float.parseFloat(arr[2]),Integer.parseInt(arr[4]),Float.parseFloat(arr[3]),arr[5]);
                objectLi.add(p);
            }  
            
            return objectLi;
            
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
//    public static void main(String args[]){
//        Reader r= new Reader();
//        LinkedList<Producto>list= r.Read();        
//    }
}
