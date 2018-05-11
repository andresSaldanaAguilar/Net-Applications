
import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import javax.imageio.ImageIO;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author andressaldana
 */
public class Getter {
    
    pathManager pm= new pathManager();
    
    void getIndex(String path){
        URL url;
        InputStream is = null;
        BufferedReader br;
        String line,root;///css/resume.min.css
        LinkedList<String> li = new LinkedList();

        try {
            url = new URL(path);
           
            root= pm.mkRoot(path);
            
            is = url.openStream(); 
            br = new BufferedReader(new InputStreamReader(is));
            pm.mkDir(root+"/");
            pm.mkFile(root+"/index.html");

            BufferedWriter writer = new BufferedWriter(new FileWriter(root+"/index.html", true));

            while ((line = br.readLine()) != null) {
                System.out.println(line); 
                
                if(line.contains("<link") || line.contains("img") || line.contains("<script")){
                    li.add(line);
                }                
                writer.append(line);            
            }
            writer.close();
            
            li.forEach((item) -> {
                System.out.println("item:" +item);
                getLink(item,path,root);
            });

        } catch (MalformedURLException mue) {
             mue.printStackTrace();
        } catch (IOException ioe) {
             ioe.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException ioe) {
                // nothing to see here
            }
        }
    }
    
    String getLink(String href,String path,String title){
        //case when is a image or script file
        if(href.contains("src")){       
            String [] aux = href.split("\"");
            for(int i = 0 ; i<aux.length ; i++){
                if(aux[i].contains("src")){
                    href = aux[i+1];
                }
            }
        }
        //case when is an CSS file
        else{
            href = href.split("\"")[1];
        }
        
        
        //ignoring https files (CDN)
        if(href.contains("https")){
            System.out.println("Ignoring: "+title+"/"+href);
        }
        //image download case
        else if(href.contains(".jpg") || href.contains(".png") || href.contains(".ico")){
            Image image = null;
            String extension = href.split("\\.")[1];
            System.out.println("Creating image: "+title+"/"+href+" with extension: "+ extension);
            try {
                pm.mkDir(title+"/"+href);
                pm.mkFile(title+"/"+href);
                URL url = new URL(path+"/"+href);
                image = ImageIO.read(url);
                ImageIO.write((RenderedImage) image, extension,new File(title+"/"+href));
            } catch (IOException e) {
            }
        }
        //text file download
        else{
            URL url;
            InputStream is = null;
            BufferedReader br;
            String line;

            try {
                url = new URL(path+"/"+href);    
                is = url.openStream();  
                br = new BufferedReader(new InputStreamReader(is));
                pm.mkDir(title+"/"+href);
                pm.mkFile(title+"/"+href);
                System.out.println("Creating: "+title+"/"+href);                

                BufferedWriter writer = new BufferedWriter(new FileWriter(title+"/"+href, true));

                while ((line = br.readLine()) != null) {         
                    writer.append(line);
                    System.out.println();
                }
                writer.close();

            } catch (MalformedURLException mue) {
                 mue.printStackTrace();
            } catch (IOException ioe) {
                 ioe.printStackTrace();
            } finally {
                try {
                    if (is != null) is.close();
                } catch (IOException ioe) {
                    // nothing to see here
                }
            }
        }
        
        return null;
    }
    
    
    public static void main(String[] args) {
        Getter g = new Getter();
        g.getIndex("https://andressaldanaaguilar.github.io/");
    }
}
