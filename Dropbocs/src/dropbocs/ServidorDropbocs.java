package dropbocs;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ServidorDropbocs {
    ServerSocket s = null;
    TreeNode<String> baseNode = null;
    String dropDir = File.separator + "dropbocs" + File.separator;
    String dropPath = System.getProperty("user.dir") + dropDir;
    String zipPath = System.getProperty("user.dir") + File.separator + "zipper.zip";
    File baseDir = null;
    
    public ServidorDropbocs() {
        System.out.println("Directorio base: " + dropPath);
        try {
            baseDir = new File(dropPath);
            if (!baseDir.isDirectory()) {
                System.out.println("No se encontró el nodo base. Generando directorio.");
                baseDir.mkdir();
            }
            else
                System.out.println("Nodo base localizado.");
        }
        catch (Exception e) {
            System.err.println("Error en directorios.");
            e.printStackTrace();
            System.exit(1);
        }
        
        try{
            s = new ServerSocket(1111);
            System.out.println("Servicio iniciado.");
            for (;;) {
            System.out.println("Esperando clientes...");
                Socket cl = s.accept();
                System.out.println("Cliente conectado desde " + cl.getInetAddress() + ": " + cl.getPort());

                baseNode = fillTree(baseDir);
                ObjectOutputStream oos = new ObjectOutputStream(cl.getOutputStream());
                oos.writeObject(baseNode);
                oos.flush();
                System.out.println("Arbol enviado.");
                getRequests(cl);
            }
        }
        catch (Exception e) {
            if(e.getMessage().contains("Connection reset"))
                
            System.err.println("Error de socket.");
            e.printStackTrace();
        }
    }
    
    public void getRequests(Socket cl){
        for(;;){
            try{
                ObjectInputStream ois = new ObjectInputStream(cl.getInputStream());
                String opt = (String)ois.readObject();
                String relPath = (String) ois.readObject();
                if(opt.equals("Download")){
                    List<String> filePaths = (List<String>) ois.readObject();
                    System.out.println("Solicitud recibida.");
                    int originalSize = filePaths.size();
                    for(int i = 0; i < originalSize; i++){
                        File f = new File(dropPath + filePaths.get(i));
                        if(f.isDirectory())
                            filePaths.set(i, filePaths.get(i) + File.separator);
                        filePaths.addAll(generateFileList(f, false));
                    }
                    
                    zipIt(zipPath, filePaths , relPath);
                    Socket newCl = s.accept();
                    DataOutputStream dos = new DataOutputStream(newCl.getOutputStream());
                    File f = new File(zipPath);
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
                            dos.write(b,0,n);
                            dos.flush();
                            porcentaje = (int)((enviados*100)/tam);
                            System.out.print("\r\tTransmitiendo el " + porcentaje + "% del archivo...");
                        }
                        System.out.println("\n\tArchivo transmitido.");
                        dis.close();
                        dos.close();
                        newCl.close();
                        System.out.println("Terminó la transmisión.");
                        f.delete();
                    }
                }
                else{
                    Socket newCl = s.accept();
                    DataInputStream dis = new DataInputStream(newCl.getInputStream());
                    int n = 0, porcentaje = 0;
                    String nombre = dis.readUTF();
                    long tam = dis.readLong();
                    DataOutputStream dos = new DataOutputStream(new FileOutputStream(dropPath + relPath + nombre));
                    System.out.println("Preprarado para recibir el archivo: "+ nombre + " desde " + cl.getInetAddress() + " con " + tam + " bytes de datos.");
                    System.out.println("Se creará el archivo " + dropPath.replace(dropDir, "") + File.separator + nombre);
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
                    //----------------------------------------------------------------------
                    System.out.println("dropPath: "+dropPath + relPath + nombre);
                    AES aes = new AES();
                    aes.AESIt(dropPath + relPath + nombre, 2); 
                    //-----------------------------------------------------------------------------------------
                                        //---------------------------------------------------
                    unZipIt(dropPath + relPath, nombre);
                    
                    new File(dropPath + relPath, nombre).delete();
                    
                    baseNode = fillTree(baseDir);
                    ObjectOutputStream oos = new ObjectOutputStream(cl.getOutputStream());
                    oos.writeObject(baseNode);
                    oos.flush();
                    System.out.println("Arbol enviado.");
                }
            }
            catch (Exception e) {
                if(e.getMessage().contains("Connection reset")){
                    System.out.println("El cliente ha cerrado la conexión.");
                    break;
                }
                else{
                    System.err.println("Error de socket.");
                    e.printStackTrace();
                }
            }
        }
    }

    public TreeNode<String> fillTree(File dir) {
        TreeNode<String> node = new TreeNode<>(dir.getName());

        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                node.addChild(fillTree(f));
            } else {
                node.addChild(f.getName());
            }
        }
        return node;
    }
    
    
    
    public void zipIt(String zipFile, List<String> fileList, String relPath){
        byte[] buffer = new byte[1024];
    	
        try{
            FileOutputStream fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);

            System.out.println("Comprimiendo al archivo: " + zipFile);

            for(String file : fileList){
                    if ((file.substring(file.length() - 1)).equals(File.separator))
                        continue;
                    System.out.println("Comprimiendo: " + file);
                    ZipEntry ze = new ZipEntry(file.replace(relPath, ""));
                    zos.putNextEntry(ze);
                    //if(new File(dropPath + file).isDirectory()){
                    //    Files.copy(Paths.get(dropPath + file), zos);
                    //    continue;
                    //}
                    FileInputStream in = new FileInputStream(dropPath + file);
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
    
    public void unZipIt(String zipPath, String zipName){
        byte[] buffer = new byte[1024];

        try{
            ZipInputStream zis = new ZipInputStream(new FileInputStream(zipPath + zipName));
            ZipEntry ze = zis.getNextEntry();

            while(ze != null){

                String fileName = ze.getName();
                File newFile = new File(zipPath + fileName);

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
    
    public List<String> generateFileList(File node, boolean shouldAdd){
        List<String> newFiles = new ArrayList<>();
        if(shouldAdd){
            if(node.isDirectory())
                newFiles.add(generateEntry(node.getAbsoluteFile().toString()) + File.separator);
            else
                newFiles.add(generateEntry(node.getAbsoluteFile().toString()));
        }
        if(node.isDirectory()){
            for(String filename : node.list())
                newFiles.addAll(generateFileList(new File(node, filename), true));
        }
        return newFiles;
    }

    private String generateEntry(String file){
    	return file.substring(dropPath.length(), file.length());
    }
    
    
    
    
    
    
    
    
    
    
    public void closeCon() {
        try{
            s.close();
        }
        catch(Exception e){
            System.err.println("Error al cerrar conexión.");
            e.printStackTrace();
        }
    }
}













































