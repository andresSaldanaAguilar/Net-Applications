
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
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
        new File("/Users/andressaldana/Documents/Github/Net-Applications/URL_constructor/"+dir).mkdirs();
        return null;
    }

    public String mkDirSpecial(String dir){
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
    
    public String mkRoot(String path, int opt){
        //this option gives the url title
        String result = "";
        if(opt == 1){
            result = path.split("//")[1];
        }
        //this option separates the directory from the archive
        if(opt == 2){
            result = path.substring(0, path.lastIndexOf("/")+1).split("//")[1];
        }
        if(opt == 3){
            result = path.substring(8, path.length());
        }
        return result;
    }
    
    
    
    public static void main(String args[]) throws IOException, FileNotFoundException, URISyntaxException{
        pathManager pm = new pathManager();
        //pm.imgtest();
        //pm.mkFile("vendor/bootstrap/js/bootstrap.bundle.min.js");
    }
}
