package httpserver;

import java.net.*;
import java.io.*;
import java.util.*;

public class HTTPServer{
    public static final int PUERTO=8080;
    ServerSocket ss;
		
    public HTTPServer() throws Exception{
        System.out.println("Iniciando Servidor.......");
        this.ss=new ServerSocket(PUERTO);
        System.out.println("Servidor iniciado:---OK");
        System.out.println("Esperando por Cliente....");
        for(;;){
            Socket accept = ss.accept();
            new Manejador(accept).start();
        }
    }
		
    public static void main(String[] args) throws Exception{
            HTTPServer s = new HTTPServer();
    }
	
}