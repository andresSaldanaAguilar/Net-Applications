package downloadaccelerator;

//package hello;
import java.io.File;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Server implements Dir {

    public Server() {
    }

    public File itExists(String ip, String fileName) throws RemoteException{
        File folder = new File(System.getProperty("user.dir") + "/" + ip);
        File[] listOfFiles = folder.listFiles();
        File file = null;
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                if (listOfFiles[i].getName().contains(fileName)) {
                    file = new File(System.getProperty("user.dir") + "/" + ip + "/" + listOfFiles[i].getName());
                }
            }
            else if (listOfFiles[i].isDirectory()) {
            }
        }
        return file;
    }

    public static void main(String args[]) {
        try {
            java.rmi.registry.LocateRegistry.createRegistry(1099); //puerto default del rmiregistry
            System.out.println("RMI registry ready.");
        } catch (Exception e) {
            System.out.println("Exception starting RMI registry:");
            e.printStackTrace();
        }//catch

        try {
            System.setProperty("java.rmi.server.codebase", "http://8.25.100.18/clases/"); ///file:///f:\\redes2\\RMI\\RMI2
            Server obj = new Server();
            Dir stub = (Dir) UnicastRemoteObject.exportObject(obj, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("Dir", stub);

            System.err.println("Servidor listo...");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
