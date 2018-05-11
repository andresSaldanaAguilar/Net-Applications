
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
    
    public void imgtest() throws FileNotFoundException, IOException, URISyntaxException{
        String line;
        

                //mkDir(title+"/"+href);
                mkFile("/Users/andressaldana/Documents/Github/andresSaldanaAguilar.github.io/img/profile3.js");
                //System.out.println("Creating: "+title+"/"+href);                

                /*BufferedWriter writer = new BufferedWriter(new FileWriter("/Users/andressaldana/Documents/Github/andresSaldanaAguilar.github.io/img/profile2.jpg", true));

                while ((line = br.readLine()) != null) {         
                    writer.append(line);
                    System.out.println(line);
                }
                writer.close();*/
                //URL url = this.getClass().getResource("https://andressaldanaaguilar.github.io/img/profile.jpg");
                URL url = new URL("https://andressaldanaaguilar.github.io/img/profile.jpg"); 
                File file = new File(url.toURI());
                FileInputStream input = new FileInputStream(file);
                byte[] buffer = new byte[1024];
                int bytesRead;
                FileOutputStream output = new FileOutputStream("/Users/andressaldana/Documents/Github/andresSaldanaAguilar.github.io/img/profile3.jpg");
                while ((bytesRead = input.read(buffer)) != -1)
                {
                    output.write( buffer , 0 , bytesRead );
                }
                output.close();
    }
    
    
    public static void main(String args[]) throws IOException, FileNotFoundException, URISyntaxException{
        pathManager pm = new pathManager();
        pm.imgtest();
        //pm.mkFile("vendor/bootstrap/js/bootstrap.bundle.min.js");
    }
}
