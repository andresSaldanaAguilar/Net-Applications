/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package downloadaccelerator;

import java.io.File;
import java.io.Serializable;

/**
 *
 * @author andressaldana
 */
public class FileList implements Serializable{
    String folder;
    public File itExists(String ip, String fileName){
        File folder = new File(System.getProperty("user.dir")+"/"+ip);
        File[] listOfFiles = folder.listFiles();
        File file = null;
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                if(listOfFiles[i].getName().contains(fileName)){
                    file = new File(System.getProperty("user.dir")+"/"+ip+"/"+listOfFiles[i].getName());
                }
            } else if (listOfFiles[i].isDirectory()) {
            }
        }
        return file;
    }
    public static void main(String args[]){
        FileList fl = new FileList();
        File file = fl.itExists("12.455.67","prin");
        System.out.println(file.length());
    }
}
