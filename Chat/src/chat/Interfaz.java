/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import static chat.Messenger.getRooms;
import static chat.Messenger.setRoom;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 *
 * @author andressaldana
 */
public class Interfaz extends javax.swing.JFrame implements Runnable{

    /**
     * Creates new form Interfaz
     */
    Cliente cl,cl1;
    
    //enviador de mensajes
    Messenger msgr;
    //recibidor de mensajes
    Reciever msgreciever;
    //recibidor de imagenes
    RecieverImg imgreciever;    
    //Puerto
    int pto;
    //lista de puertos
    LinkedList<Sala> ar;
    
    
    public void run(){ 
            //todo este despapaye es para iniciar el un joption pane
    JFrame frame = new JFrame();     
    Image image = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
    //cuestion
    int resp=JOptionPane.showConfirmDialog(null,"Nueva sala de chat?");
    if (JOptionPane.OK_OPTION == resp){
            //nombre chat
            String name = (String)JOptionPane.showInputDialog(frame,"Ingresa Nombre","Conexion",
                    JOptionPane.PLAIN_MESSAGE,new ImageIcon(image),null,"");
            //contraseña chat
            String password = (String)JOptionPane.showInputDialog(frame,"Ingresa Password","Conexion",
                    JOptionPane.PLAIN_MESSAGE,new ImageIcon(image),null,"");
        try {
            setRoom(name+" "+password);
        } catch (IOException ex) {
            Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
        }

    }      
        
    //traemos lista de salas
        try {
            ar = getRooms();
        } catch (IOException ex) {
            Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
        }

    
    Object[] possibilities = new Object[ar.size()];
    for(int i = 0; i< ar.size(); i++){
        possibilities[i] = ar.get(i).getNombre();
    }      
    
        //recibimos el puerto y password de la sala
        String roomName = (String)JOptionPane.showInputDialog(frame,"Selecciona Sala","Conexion",
                        JOptionPane.PLAIN_MESSAGE,new ImageIcon(image),possibilities,"");
        
        String [] infoSala = getRoomInfo(roomName);
        
        //validando contrasenia
        String roomPass = (String)JOptionPane.showInputDialog(frame,"Contraseña","Conexion",
                        JOptionPane.PLAIN_MESSAGE,new ImageIcon(image),null,"");
        
        //si la contraseña es la indicada..
        if(roomPass.equals(infoSala[1])){
            pto = Integer.parseInt(infoSala[0]);       
            initComponents();
            //deshabilitamos en lo que se conecta otras acciones
            jEditorPane1.setEditable(false);
            jTextField1.setEnabled(false);
            jButton1.setEnabled(false);
            jButton3.setEnabled(false);
            //modo html
            jEditorPane1.setContentType("text/html");
        }
        else{
            System.out.println("wrong pass");
        }
    }
    
    /**
     * Este metodo conecta la interfaz con la direccion y puerto ip
     */
    public void Connect() throws UnknownHostException{
        //creando cliente al puerto
        cl = new Cliente(pto);          
        //inicializamos el mensajero
        MulticastSocket m = cl.joinGeneralChat(jTextField2.getText());
        //este es universal para imagenes y mensajes, es el cliente principal
        msgr = new Messenger(m, InetAddress.getByName("228.1.1.1") , cl.pto); 
        //inicializamos el recibidor que este escuchando por mensajes para pintar en el jEditorPane
        msgreciever = new Reciever(m, InetAddress.getByName("228.1.1.1") , cl.pto ,jEditorPane1,cl.id);
        msgreciever.start();       
        //para las imagenes, lo mismo, un puerto adelante
        cl1 = new Cliente(pto+1);
        MulticastSocket m1 = cl1.joinGeneralChat(jTextField2.getText());
        imgreciever = new RecieverImg(m1, InetAddress.getByName("228.1.1.1") , cl1.pto ,jEditorPane1,cl1.id);
        imgreciever.start();        
    }

    String[] getRoomInfo(String nombre){
        String arr [] = new String[2];
        for(int i = 0; i < ar.size(); i++){
            if(ar.get(i).getNombre().equals(nombre)){                
                arr[0] = ""+ar.get(i).getPuerto();
                arr[1] = ar.get(i).getContrasenia();
                break;
            }
        }
        return arr;
    }
        
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The contenEt of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        jButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jTextField2 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jScrollPane2.setViewportView(jEditorPane1);

        jButton1.setBackground(new java.awt.Color(86, 115, 129));
        jButton1.setText("Enviar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(133, 162, 183));

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Connect");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(255, 255, 255));
        jButton4.setText("New Chat");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton4)
                .addGap(16, 16, 16))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2)
                    .addComponent(jButton4))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jButton3.setBackground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Image");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 13, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        //
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        //enviamos el mensaje escrito
        msgr.sendMsg(jTextField1.getText()+"\t"+cl.id);                
    }//GEN-LAST:event_jButton1ActionPerformed

    //boton de conexion
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try{
            //envia id para el cliente y conecta
            Connect();
            //habilita las opciones de mensajeria
            jTextField2.setEnabled(false);
            jTextField1.setEnabled(true);
            jButton1.setEnabled(true);
            jButton2.setEnabled(false);
            jButton3.setEnabled(true);
        } catch (UnknownHostException ex) {
            System.out.println("Host invalido");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        File file = new File("/Users/andressaldana/Documents/Github/Net-Applications/Chat/img");
        JFileChooser jf = new JFileChooser(file);
        jf.requestFocus();
        int r = jf.showOpenDialog(null);

        if (r==JFileChooser.APPROVE_OPTION) {
            File f = jf.getSelectedFile();
            String path = f.getAbsolutePath();
            try {
                //sending image
                msgr.sendImg(path);
            } catch (IOException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        CreateClient();
    }//GEN-LAST:event_jButton4ActionPerformed
    
    
   
    /**
     * @param args the command line arguments
     */
    /*public static void main(String args[]) {
        System.setProperty("java.net.preferIPv4Stack", "true");
      
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        */
    
        /* Create and display the form *//*
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Interfaz().setVisible(true);                  
                    
                } catch (UnknownHostException ex) {
                    Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }*/
    
    public void CreateClient(){
        Interfaz i1 = new Interfaz();
        i1.setVisible(true);
        Thread t1 = new Thread(i1);
        t1.start();        
    }
    
    public static void main(String args[]) throws UnknownHostException {
        System.setProperty("java.net.preferIPv4Stack", "true");
        Interfaz i = new Interfaz();
        i.setVisible(true);
        Thread t = new Thread(i);
        t.start();

    }
        

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables

    /*@Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }*/
}
