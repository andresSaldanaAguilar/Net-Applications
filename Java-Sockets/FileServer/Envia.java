import java.net.*;
import java.io.*;
import javax.swing.JFileChooser;

public class Envia{
	public static void main(String[] args){
		try{
			int pto = 1234, n=0,procentaje=0;
			String host="127.0.0.1";

			JFileChooser jf = new JFileChooser();
			jf.requestFocus();  /*Lo manda a primer plano*/
			/*jf.setMultiSelectionEnabled(true) ... (1) */
 			int r = jf.showOpenDialog(null);

			if (r==JFileChooser.APPROVE_OPTION) {
				/*File[] f = jf.getSelectedFiles()*/
				File f = jf.getSelectedFile();
				/*Realizar un for para que vaya leyendo cada uno de los datos del arreglo*/
				String nombre = f.getName();
				long tam = f.length(), enviados=0;
				String path = f.getAbsolutePath();
				Socket cl = new Socket(host,pto);
				/*Asumiendo que ya está conectado*/
				System.out.println("Conexion establecida.\n"+
							   "Inicia transferencia de archivo: "+path);

				/*se ocupan data streams porque mandaremos diferentes tipos de dato*/
				DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
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
					procentaje = (int)((enviados*100)/tam);
					System.out.print("\r Transmitiendo el "+procentaje+"% del archivo.");
				}//ciera while
				System.out.println("Archivo transmitido");
				dis.close();
				dos.close();
				cl.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}