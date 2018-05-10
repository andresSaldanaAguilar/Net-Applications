
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author andressaldana
 */
public class pathManager{
    
    public String mkDir(String dir){
        dir = dir.substring(0, dir.lastIndexOf('/'));
        System.out.println(dir);
        new File("/Users/andressaldana/Documents/Github/Net-Applications/URL_constructor/"+dir).mkdirs();
        return null;
    }
    
    public String mkFile(String dir){
        File file = new File(dir);
        try {
            file.createNewFile();
        } catch (IOException ex) {
            System.err.println("Problem on creating archive");
        }
        return null;
    }
    
    public String mkRoot(String path){
        String title = path.split("/")[2].split("\\.")[0];
        return title;
    }
    
    
    public static void main(String args[]){
        pathManager pm = new pathManager();
        //pm.mkFile("vendor/bootstrap/js/bootstrap.bundle.min.js");
    }
}
