
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
public class Getter extends Thread{
    
    pathManager pm= new pathManager();
    LinkedList<String> externURL = new LinkedList();
    String path = "";
    
    public Getter(String path){
        this.path = path;
    }
    
    void getIndex(){
        
        URL url;
        //for the entrant url
        InputStream is = null;
        BufferedReader br;
        //line is the current line that is being readed
        //root is the main directory of the page
        String line,root;
        //this stores the dependencies ofr
        LinkedList<String> li = new LinkedList();

        try {
            url = new URL(path);
            root= pm.mkRoot(path,1);
            BufferedWriter writer = null;
            
            //if we're dealing with one of this kind of files
            if(path.contains("pdf") || path.contains("txt")){
                String fileroot = pm.mkRoot(path,2);
                pm.mkDir(fileroot);
                pm.mkFile(root);
                writer = new BufferedWriter(new FileWriter(root, true));
            }
            //if not
            else{            
                pm.mkDir(root+"/");
                pm.mkFile(root+"/index.html");
                writer = new BufferedWriter(new FileWriter(root+"/index.html", true));
            }
            
            is = url.openStream(); 
            br = new BufferedReader(new InputStreamReader(is));


            while ((line = br.readLine()) != null) {                
                if(line.contains("<link") || line.contains("<img") || line.contains("<script")){
                    li.add(line);
                }     
                //add extern page to waiting list
                if(line.contains("<a")){
                    String [] aux = line.split("\"");
                    for(int i = 0 ; i<aux.length ; i++){
                        if(aux[i].contains("href")){
                            if(aux[i+1].contains("#")){
                                //ignore internal references
                            }
                            //when is a reference outside the folder page
                            else if(aux[i+1].contains("https")){
                                //validating to avoid duplicates
                                if(!aux[i+1].equals(path) && !externURL.contains(aux[i+1])){
                                    externURL.add(aux[i+1]);
                                }                                                  
                                //if it's the main index
                                if(aux[i+1].contains(root)){
                                    //checar que se envie la verdadera raiz
                                    line = line.replaceAll(aux[i+1], "index.html");
                                }
                                //if is external
                                else{
                                    line = line.replaceAll(aux[i+1], "../"+pm.mkRoot(aux[i+1],1)+"/index.html");
                                }                                
                                System.out.println("https "+aux[i+1]);
                            }
                            //when is a reference inside the folder page
                            else{
                                
                                if(!aux[i+1].equals(path) && !externURL.contains(aux[i+1])){
                                    externURL.add(path+aux[i+1]);
                                }
 
                                if(line.contains("/")){
                                    //if it is this kind of file
                                    if(line.contains("pdf") || line.contains("txt")){
                                    }
                                    else{
                                        line = line.replaceAll(aux[i+1],"../"+aux[i+1].replaceAll("/", "")+"/index.html");
                                    }
                                }
                                else{
                                    line = line.replaceAll(aux[i+1],"../"+aux[i+1].replaceAll("/", "")+"/index.html");
                                }
                            }
                        }
                    }
                }
                System.out.println(line);
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
                //System.out.println("src: "+href);
            }
        }
        //case when is an CSS file
        else{
            String [] aux = href.split("\"");
            for(int i = 0 ; i<aux.length ; i++){
                if(aux[i].contains("href")){
                    href = aux[i+1];
                }
            }
        }     
        //ignoring https files (CDN)
        if(href.contains("https")){
            //System.out.println("Ignoring: "+title+"/"+href);            
        }
        //image download case
        else if(href.contains(".jpg") || href.contains(".png") || href.contains(".ico")){
            Image image = null;
            String extension = href.split("\\.")[1];
            //System.out.println("Creating image: "+title+"/"+href+" with extension: "+ extension);
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
                url = new URL(path);    //aquiiii url = new URL(path+"/"+href);
                is = url.openStream();  
                br = new BufferedReader(new InputStreamReader(is));
                pm.mkDirSpecial(pm.mkRoot(path,3));
                pm.mkFile(pm.mkRoot(path,3)+"/index.html");
                
                BufferedWriter writer = new BufferedWriter(new FileWriter(pm.mkRoot(path,3)+"/index.html", true));

                while ((line = br.readLine()) != null) {         
                    writer.append(line);
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
    
    public LinkedList<String> getQueue(){
        return this.externURL;
    }
    
    public void run(){
        getIndex();
    }
    
    
    public static void main(String[] args) {
        //Getter g = new Getter();
        //g.getIndex("https://github.com/");
    }
}
