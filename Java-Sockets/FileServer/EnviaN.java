import java.net.*;
import java.io.*;
import javax.swing.JFileChooser;

public class EnviaN{
	public static void main(String[] args){
		try{
			int pto = 1234, n = 0,procentaje = 0;
			String host="127.0.0.1";

			JFileChooser jf = new JFileChooser();
			jf.requestFocus();  /*Lo manda a primer plano*/
			jf.setFileSelectionMode(JFileChooser.FILES_ONLY);
			jf.setMultiSelectionEnabled(true);
 			int r = jf.showOpenDialog(null);

			if (r==JFileChooser.APPROVE_OPTION) {

				File [] f = jf.getSelectedFiles();
				String nombre [] = new String[10];
				String path [] = new String[10];
				long tam [] = new long[10]; 
				/*Realizar un for para que vaya leyendo cada uno de los datos del arreglo*/

				for (int i = 0; i < f.length ; i++) {
					nombre [i] = f[i].getName();	
					tam [i] = f[i].length();
					path [i] = f[i].getAbsolutePath();
					System.out.println(nombre[i] + " " + path[i] + " " + tam[i]);
				}
				
				/*se ocupan data streams porque mandaremos diferentes tipos de dato*/

				Socket cl = new Socket(host,pto);
				DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
				dos.writeInt(f.length);
				dos.flush();
				
				for (int i = 0; i < f.length ; i++) {
					/*Asumiendo que ya está conectado*/
					System.out.println("Conexion establecida.\n"+
								"Inicia transferencia de archivos: "+path[i]);

					DataInputStream dis = new DataInputStream(new FileInputStream(path[i]));
					int enviados = 0;
					dos.writeUTF(nombre[i]);
					dos.flush();
					dos.writeLong(tam[i]);
					dos.flush();

					byte[] b = new byte[1500];

					while(enviados < tam[i]){
						n = dis.read(b);
						enviados += n;
						dos.write(b,0,n);
						dos.flush();
						procentaje = (int)((enviados*100)/tam[i]);
						System.out.print("\r Transmitiendo el "+procentaje+"% del archivo.");
					}
					System.out.println("Archivo transmitido");
					dis.close();
				}
					dos.close();
					cl.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}