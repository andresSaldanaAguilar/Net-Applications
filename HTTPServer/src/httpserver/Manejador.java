package httpserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;

public class Manejador extends Thread {
    protected Socket socket;
    protected DataInputStream dis;
    protected DataOutputStream dos;
    protected String FileName;
    protected String ServerFolder = "serverfiles";

    public Manejador(Socket _socket) throws Exception{
        this.socket = _socket;
    }

    public void run(){
        try{
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
            byte[] b = new byte[1024];
            int n = 0;
            String request = "";
            while((n = dis.read(b)) != -1)
                request += new String(b, 0, n);
            //for(int i = 0; i<6; i++)
            //    line2 += br.readLine() + "\r\n";
            String line = request.substring(0, request.indexOf("\r\n"));
            if(line == null){
                    dos.writeChars("<html><head><title>Servidor WEB");
                    dos.writeChars("</title><body bgcolor=\"#AACCFF\"<br>Linea Vacia</br>");
                    dos.writeChars("</body></html>");
                    socket.close();
                    return;
            }
            System.out.println("\nCliente Conectado desde: " + socket.getInetAddress());
            System.out.println("Por el puerto: " + socket.getPort());
            System.out.println("Datos: " + line + "\r\n");
            System.out.println(request + "\r\n\r\n");

            String command = line.split("[\t ]+")[0].toUpperCase();
            switch(command){
                case "GET":
                    if(line.contains("?")){
                                        StringTokenizer tokens = new StringTokenizer(line, "?");
                                        String req_a = tokens.nextToken();
                                        String req = tokens.nextToken();
                                        System.out.println("Token1: " + req_a + "\r\n\r\n");
                                        System.out.println("Token2: " + req + "\r\n\r\n");
                                        dos.writeChars("HTTP/1.0 200 OK\r\n");
                                        dos.flush();
                                        dos.writeChars("\r\n");
                                        dos.flush();
                                        dos.writeChars("<html><head><title>SERVIDOR WEB");
                                        dos.flush();
                                        dos.writeChars("</title></head><body bgcolor=\"#AACCFF\"><center><h1><br>Parametros Obtenidos..</br></h1>");
                                        dos.flush();
                                        dos.writeChars("<h3><b>" + req + "</b></h3>");
                                        dos.flush();
                                        dos.writeChars("</center></body></html>");
                                        dos.flush();
                    }
                    else{
                        getArch(line);
                        if(FileName.compareTo("") == 0)
                            SendA("index.html");
                        else
                            SendA(FileName);
                        System.out.println(FileName);
                    }
                    break;
                case "POST":
                                                getArch(line);
                                                if(FileName.compareTo("") == 0)
                                                    SendA("index.html");
                                                else
                                                    SendA(FileName);
                                                System.out.println(FileName);
                    break;
                case "HEAD":
                    
                    break;
                case "DELETE":
                    
                    break;
            }
            
            
            
            
            
            /*
            if(!line.contains("?")){
                getArch(line);
                if(FileName.compareTo("") == 0)
                    SendA("index.html");
                else
                    SendA(FileName);
                System.out.println(FileName);
            }
            
            
            
            
            else if(line.toUpperCase().startsWith("GET")){
                StringTokenizer tokens = new StringTokenizer(line, "?");
                String req_a = tokens.nextToken();
                String req = tokens.nextToken();
                System.out.println("Token1: " + req_a + "\r\n\r\n");
                System.out.println("Token2: " + req + "\r\n\r\n");
                pw.println("HTTP/1.0 200 OK");
                pw.flush();
                pw.println();
                pw.flush();
                pw.print("<html><head><title>SERVIDOR WEB");
                pw.flush();
                pw.print("</title></head><body bgcolor=\"#AACCFF\"><center><h1><br>Parametros Obtenidos..</br></h1>");
                pw.flush();
                pw.print("<h3><b>" + req + "</b></h3>");
                pw.flush();
                pw.print("</center></body></html>");
                pw.flush();
            }
            else{
                pw.println("HTTP/1.0 501 Not Implemented");
                pw.println();
            }*/
            dos.flush();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        try{
            socket.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void getArch(String line){
        int i;
        int f;
        if(line.toUpperCase().startsWith("GET")){
            i = line.indexOf("/");
            f = line.indexOf(" ", i);
            FileName = line.substring(i + 1, f);
        }
    }
    public void SendA(String fileName, Socket sc){
        //System.out.println(fileName);
        int fSize = 0;
        byte[] buffer = new byte[4096];
        try{
            DataOutputStream out = new DataOutputStream(sc.getOutputStream());

            //sendHeader();
            FileInputStream f = new FileInputStream(fileName);
            int x = 0;
            while((x = f.read(buffer)) > 0){
    //		System.out.println(x);
                    out.write(buffer,0,x);
            }
            out.flush();
            f.close();
        }
        catch(FileNotFoundException e){
                //msg.printErr("Transaction::sendResponse():1", "El archivo no existe: " + fileName);
        }
        catch(IOException e){
//			System.out.println(e.getMessage());
                //msg.printErr("Transaction::sendResponse():2", "Error en la lectura del archivo: " + fileName);
        }
    }
    public void SendA(String arg){
        try{
            int b_leidos = 0;
            BufferedInputStream bis2 = new BufferedInputStream(new FileInputStream(ServerFolder + File.separator + arg));
            byte[] buf = new byte[1024];
            int tam_bloque = 0;
            if(bis2.available() >= 1024)
                tam_bloque = 1024;
            else
                bis2.available();

            int tam_archivo = bis2.available();
            /***********************************************/
            String sb = "";
            sb = sb+ "HTTP/1.0 200 ok\n";
            sb = sb + "Server: Oswaldo Server/1.0 \n";
            sb = sb + "Date: " + new Date() +" \n";
            sb = sb + "Content-Type: text/html \n";
            sb = sb + "Content-Length: "+ tam_archivo + " \n";
            sb = sb + "\n";
            dos.write(sb.getBytes());
            dos.flush();

            //out.println("HTTP/1.0 200 ok");
            //out.println("Server: Axel Server/1.0");
            //out.println("Date: " + new Date());
            //out.println("Content-Type: text/html");
            //out.println("Content-Length: " + mifichero.length());
            //out.println("\n");

            /***********************************************/

            while((b_leidos = bis2.read(buf)) != -1){
                System.out.println("Leidos: "+b_leidos );
                System.out.println(""+socket.isConnected());
                dos.write(buf, 0, b_leidos);
            dos.flush();
            }
            
            //bis2.close();

        }
        catch(Exception e){
                e.printStackTrace();
        }

    }
}