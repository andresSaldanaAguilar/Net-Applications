package downloadaccelerator;

//package hello;

import java.io.File;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    private Client() {}

    public static void main(String[] args) {

	String host = (args.length < 1) ? null : args[0];
	try {
		
	    Registry registry = LocateRegistry.getRegistry(host);	
	    
	    Dir stub = (Dir) registry.lookup("Dir");
            FileObj file = stub.itExists("12.455.67","prin");
            System.out.println("Checksum: "+file.checksum+" Name:"+file.file.getName()+" Server:"+file.id);
            
	    
            
	} catch (Exception e) {
	    System.err.println("Client exception: " + e.toString());
	    e.printStackTrace();
	}
    }
}
