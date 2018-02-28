package dropbocs;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorDropbocs {

    public static void main(String[] args) {
        TreeNode<String> baseNode;
        String dropPath = "/Users/andressaldana/Documents/Github/Net-Applications/Dropbocs/Dropbocs";
        File baseDir;

        try {
            baseDir = new File(dropPath);
            if (!baseDir.isDirectory()) {
                baseDir.mkdir();
            }

            baseNode = fillTree(baseDir);

            ServerSocket s = new ServerSocket(1234);
            System.out.println("Servicio iniciado...Esperando cliente");
            for (;;) {
                Socket cl = s.accept();
                System.out.println("Cliente conectado desde " + cl.getInetAddress() + ": " + cl.getPort());

                ObjectOutputStream oos = new ObjectOutputStream(cl.getOutputStream());
                oos.writeObject(baseNode);
                oos.flush();
                System.out.println("Objeto enviado...");
                oos.close();
                cl.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static TreeNode<String> fillTree(File dir) {
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
}
