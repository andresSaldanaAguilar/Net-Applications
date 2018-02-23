import java.net.*;
import java.io.*;
import javax.swing.JFileChooser;
import java.util.*;

public class EnviaN{
	public static void main(String[] args){
		try{
			//conexion
			int pto = 1234, n = 0,procentaje = 0;
			String host="127.0.0.1";

			//seleccion de archivos
			JFileChooser jf = new JFileChooser();
			jf.requestFocus(); 
			jf.setFileSelectionMode(JFileChooser.FILES_ONLY);
			jf.setMultiSelectionEnabled(true);
 			int r = jf.showOpenDialog(null);

			if (r==JFileChooser.APPROVE_OPTION) {

				File [] f = jf.getSelectedFiles();
				List<String> nombre = new ArrayList<String>();
				List<String> path = new ArrayList<String>();
				List<Long> tam = new ArrayList<Long>();
				
				/*Realizar un for para que vaya leyendo cada uno de los datos del arreglo*/
				for (int i = 0; i < f.length ; i++) {
					nombre.add(f[i].getName());	
					tam.add(f[i].length());
					path.add(f[i].getAbsolutePath());
				}
				
				/*se ocupan data streams porque mandaremos diferentes tipos de dato*/
				for (int i = 0; i < f.length ; i++) {

					Socket cl = new Socket(host,pto);
					DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
					DataInputStream dis = new DataInputStream(new FileInputStream(path.get(i)));
					System.out.println("Conexion establecida, Inicia transferencia de archivo.");
					
					int enviados = 0;
					dos.writeUTF(nombre.get(i));
					dos.flush();
					dos.writeLong(tam.get(i));
					dos.flush();

					byte[] b = new byte[1500];

					while(enviados < tam.get(i)){
						n = dis.read(b);
						enviados += n;
						dos.write(b,0,n);
						dos.flush();
						procentaje = (int)((enviados*100)/tam.get(i));
						System.out.println("\r Transmitiendo el "+procentaje+"% del archivo.");
					}
					System.out.println("Archivo transmitido");
					dis.close();
					dos.close();
				}
				cl.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}