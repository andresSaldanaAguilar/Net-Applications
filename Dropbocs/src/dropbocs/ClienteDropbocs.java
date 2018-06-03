package dropbocs;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream; 
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;
import dropbocs.AES;

public class ClienteDropbocs {
    int pto = 1111;
    String dst = "10.100.75.206";
    Socket cl = null;
    TreeNode<String> baseNode = null;
    File baseDir = null;
    VentanaDropbocs v = null;
    //---------------------------------------
    DIFFIE dhA = new DIFFIE("90000049","5683","10002");
    String A = dhA.GenerateKeyPart();
    String B = "";
    //---------------------------------------

    public ClienteDropbocs() {
        try {
            cl = new Socket(dst, pto);
            System.out.println("Conexion establecida...");
            
            System.out.println("Recibiendo parametro de llave..");
            ObjectInputStream oisKey = new ObjectInputStream(cl.getInputStream());
            String keyB = (String) oisKey.readObject();
            B = dhA.GenerateSharedKey(keyB);
            
            System.out.println("Compartiendo parametro de llave..");
            ObjectOutputStream oosKey = new ObjectOutputStream(cl.getOutputStream());
            oosKey.writeObject(A);
            oosKey.flush();           
            
            ObjectInputStream ois = new ObjectInputStream(cl.getInputStream());
            baseNode = (TreeNode<String>) ois.readObject();
            System.out.println("Árbol recibido. Construyendo interfaz.");
            //printTree(baseNode);
            v = new VentanaDropbocs(baseNode, this);
        } catch (Exception e) {
            System.err.println("Error: Servidor no localizado.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void printTree(TreeNode<String> node) {
        for (TreeNode<String> child : node.getChildren()) {
            System.out.println(child.getData());
            printTree(child);
        }
    }
    
    public void requestFiles(File destPath, List<String> filePaths, String relPath) {
        try{
            ObjectOutputStream oos = new ObjectOutputStream(cl.getOutputStream());
            oos.writeObject("Download");
            oos.flush();
            oos.writeObject(relPath);
            oos.flush();
            oos.writeObject(filePaths);
            oos.flush();
            
            System.out.println("Se solicitarán los siguientes archivos a la ruta '" + destPath.getAbsolutePath() + "':");
            for(int i = 0; i < filePaths.size(); i++)
                System.out.println(" - " + filePaths.get(i));
            
            Socket newCl = new Socket(dst, pto);
            DataInputStream dis = new DataInputStream(newCl.getInputStream());
            int n = 0, porcentaje = 0;
            String nombre = dis.readUTF();
            long tam = dis.readLong();
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(destPath.getAbsolutePath() + File.separator + nombre));
            System.out.println("Preprarado para recibir el archivo: "+ nombre + " desde " + cl.getInetAddress() + " con " + tam + " bytes de datos.");
            long recibido = 0;
            while(recibido < tam){
                byte[] b = new byte[1500];
                n = dis.read(b);
                recibido += n;
                dos.write(b,0,n);
                dos.flush();
                porcentaje = (int)((recibido * 100)/tam);
               System.out.println("\rRecibido el " + porcentaje + "% del archivo.");
            }
            System.out.println("Archivo Recibido.");
            dos.close();
            dis.close();
            //------------------------------------------------------------------
            AES aes = new AES(B);
            String hashI = aes.getHash(destPath.getAbsolutePath() + File.separator + nombre);
            aes.AESIt(destPath.getAbsolutePath() + File.separator + nombre, 2);
            String hashO = aes.hashFile(destPath.getAbsolutePath() + File.separator + nombre);
            System.out.println("hash:\n"+hashI+"\n"+hashO);
            //------------------------------------------------------------------
            unZipIt(destPath.getAbsolutePath() + File.separator + nombre, destPath);
            new File(destPath.getAbsolutePath() + File.separator + nombre).delete();
            if(hashI.equals(hashO)){
                JOptionPane.showMessageDialog(v.panel, "Archivo(s) descargado(s) correctamente.");                
            }
            else{
                JOptionPane.showMessageDialog(v.panel, "Error, el archivo esta corrupto.");                
            }
        }
        catch(Exception e){
            if(e.getMessage().contains("Connection reset by peer")){
                System.err.println("El servidor cerró la conexión.");
                JOptionPane.showMessageDialog(v.panel, "El servidor cerró la conexión.", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
            System.err.println("Error de socket: requestFiles.");
            e.printStackTrace();
        }
    }
    
    public void sendFiles(File[] newFiles, String relPath) {
        try{
            ObjectOutputStream oos = new ObjectOutputStream(cl.getOutputStream());
            oos.writeObject("Upload");
            oos.flush();
            oos.writeObject(relPath);
            oos.flush();
            String basePath = newFiles[0].getParent();
            String zipFile = basePath + File.separator + "zipper.zip";
            List<File> fileList = new ArrayList<>();
            for(File f : newFiles)
                fileList.addAll(addToList(f));
            System.out.println("Se cargarán los siguientes archivos:");
            for(int i = 0; i < fileList.size(); i++)
                System.out.println(" - " + fileList.get(i));
            zipIt(zipFile, fileList, basePath);
            //------------------------------------------------------------------
            AES aes = new AES(B);
            aes.AESIt(zipFile, 1);
            //------------------------------------------------------------------
            Socket newCl = new Socket(dst, pto);
            DataOutputStream dos = new DataOutputStream(newCl.getOutputStream());
            File f = new File(zipFile);
            if(f.exists()){
                int n = 0, porcentaje = 0;
                String nombre = f.getName();
                long tam = f.length(), enviados=0;
                String path = f.getAbsolutePath();

                System.out.println("\tInicia transferencia de archivo: " + path);

                DataInputStream dis = new DataInputStream(new FileInputStream(path));

                dos.writeUTF(nombre);
                dos.flush();
                dos.writeLong(tam);
                dos.flush();

                byte[] b = new byte[1500];

                while(enviados < tam){
                    n = dis.read(b);
                    enviados += n;
                System.out.println(new String(b,0,n));
                    dos.write(b,0,n);
                    dos.flush();
                    porcentaje = (int)((enviados*100)/tam);
                    //System.out.print("\r\tTransmitiendo el " + porcentaje + "% del archivo...");
                }
                System.out.println("\n\tArchivo transmitido.");
                dis.close();
                dos.close();
                newCl.close();
                System.out.println("Terminó la transmisión.");
                f.delete();
                
                ObjectInputStream ois = new ObjectInputStream(cl.getInputStream());
                baseNode = (TreeNode<String>) ois.readObject();
                System.out.println("Árbol recibido. Construyendo interfaz.");
                v.f.dispose();
                v = new VentanaDropbocs(baseNode, this);
            }
        }
        catch(Exception e){
            if(e.getMessage().contains("Connection reset by peer")){
                System.err.println("El servidor cerró la conexión.");
                JOptionPane.showMessageDialog(v.panel, "El servidor cerró la conexión.", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
            System.err.println("Error de socket: requestFiles.");
            e.printStackTrace();
        }
    }
    
    public void zipIt(String zipFile, List<File> fileList, String basePath){
        byte[] buffer = new byte[1024];
        try{
            FileOutputStream fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);

            System.out.println("Comprimiendo al archivo: " + zipFile);
            
            for(File file : fileList){
                    System.out.println("Comprimiendo: " + file.getAbsolutePath());
                    ZipEntry ze = new ZipEntry(file.getAbsolutePath().replace(basePath + File.separator, ""));
                    zos.putNextEntry(ze);
                    //if(new File(dropPath + file).isDirectory()){
                    //    Files.copy(Paths.get(dropPath + file), zos);
                    //    continue;
                    //}
                    FileInputStream in = new FileInputStream(file);
                    int len;
                    while ((len = in.read(buffer)) > 0)
                        zos.write(buffer, 0, len);
                    in.close();
            }

            zos.closeEntry();
            zos.close();

            System.out.println("Compresión exitosa.");
        }
        catch(IOException ex){
           ex.printStackTrace();   
        }
   }
    
    public List<File> addToList(File file){
        List<File> files = new ArrayList<>();
            if(file.isDirectory())
                for(String sub : file.list())
                    files.addAll(addToList(new File(file, sub)));
            else
                files.add(file);
        return files;
    }
    
    
    public void unZipIt(String zipFile, File outputFolder){
        byte[] buffer = new byte[1024];

        try{
            ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
            ZipEntry ze = zis.getNextEntry();

            while(ze != null){

                String fileName = ze.getName();
                File newFile = new File(outputFolder + File.separator + fileName);

                System.out.println("Descomprimiendo: "+ newFile.getAbsoluteFile());
                new File(newFile.getParent()).mkdirs();

                FileOutputStream fos = new FileOutputStream(newFile);             

                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }

                fos.close();   
                ze = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();

            System.out.println("Descompresión terminada.");

        }
        catch(IOException ex){
          ex.printStackTrace(); 
        }
    }

   

        public void closeCon() {
           try{
               cl.close();
           }
           catch(Exception e){
               System.err.println("Error al cerrar conexión.");
               e.printStackTrace();
           }
        }
    }
