package dropbocs;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ClienteDropbocs {

    public static void main(String[] args) {
        TreeNode<String> baseNode;
        File baseDir;

        try {
            int pto = 1234;
            String dst = "127.0.0.1";
            Socket cl = new Socket(dst, pto);
            System.out.println("Conexion establecida...");
            ObjectInputStream ois = new ObjectInputStream(cl.getInputStream());
            baseNode = (TreeNode<String>) ois.readObject();
            System.out.println("Objeto recibido.");
            printTree(baseNode);
            ois.close();
            cl.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printTree(TreeNode<String> node) {
        for (TreeNode<String> child : node.getChildren()) {
            System.out.println(child.getData());
            printTree(child);
        }
    }
}
