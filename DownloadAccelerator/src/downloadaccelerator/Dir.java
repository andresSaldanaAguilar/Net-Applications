package downloadaccelerator;

//package hello;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Dir extends Remote {
    File itExists(String ip, String fileName) throws RemoteException;
}
