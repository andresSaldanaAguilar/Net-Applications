/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dropbocs;

/**
 *
 * @author andressaldana
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;

public class VentanaDropbocs {

    int x,y;
    JFrame f;
    JPanel panel;
    JScrollPane scrollPane;
    ArrayList<Button> buttons;

    
    public VentanaDropbocs() {     
        buttons = new ArrayList<Button>();
        f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(700,400);

        //initializing checkbox container
        //important, this makes the rows look thinner than they're
        this.x = 5;
        this.y = 1;
        panel = new JPanel();
        panel.setLayout(new GridLayout(0, y));

        
        //making scrollable the checkbox container
        scrollPane = new JScrollPane(panel);
        panel.setAutoscrolls(true);
        scrollPane.setPreferredSize(new Dimension(650,300));
        //creating the rest container
        JLabel title = new JLabel("Dropbocs");
        title.setSize(200, 200);
    
        JButton download = new JButton("Download");
        download.addActionListener(al);
        JButton upload = new JButton("Upload");
        upload.addActionListener(al);
        JButton getBack = new JButton("<-");
        getBack.addActionListener(al);
        
        JPanel jp = new JPanel();
        
        jp.add(title);
        jp.add(download);
        jp.add(upload);
        jp.add(getBack);
           
        JPanel jPrincipal = new JPanel();
        jPrincipal.add(jp);
        jPrincipal.add(scrollPane);
        f.add(jPrincipal);
        f.setVisible(true);       
    }

    void AddFile(String title) {
        //the columns always has to be bigger than the expected to not lose format
        x++;
        panel.setLayout(new GridLayout(x, y));
        //adding as much buttons as requested
        Button btn = new Button(title);
        //button.addActionListener((ActionListener) this);
        buttons.add(btn);
        JCheckBox button= btn.getCheckBox();
        button.setPreferredSize(new Dimension(400, 20));
        panel.add(button);
        button.setBorderPainted(true);
        //adding action listener
        button.addActionListener(al);       
    }
    
    
    void Clear(){
        //cleaning checkboxes from container
        for (Button btn : buttons) {
            btn.getCheckBox().setVisible(false); // line you have in your code, now
            panel.remove(btn.getCheckBox());     // add this line
            panel.updateUI();  
        }
        //cleaning list
        buttons.clear();
        x=5;
    }
    
    ArrayList<Button> getFiles(){
        return this.buttons;
    }
    
    void uploadFiles(){
        JFileChooser jf = new JFileChooser();
        jf.requestFocus(); 
        jf.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jf.setMultiSelectionEnabled(true);
        int r = jf.showOpenDialog(null);

        if (r==JFileChooser.APPROVE_OPTION) {
            //WIP upload logic 
        }
    }
    
    void changeDirectory(){
       this.Clear();
       //WIP change directory logic
    } 
    
    
    
    //listens to the clicked and unclicked checkboxes
    ActionListener al = new ActionListener(){
        public void actionPerformed(ActionEvent ae){
            
            AbstractButton ab = (AbstractButton) ae.getSource();           
            boolean selected = ab.getModel().isSelected();
            //searching for the clicked checkbox
            for (Button btn : buttons) {
                if(btn.getTextCheckBox().equals(ab.getText())){
                    btn.setState(selected);
                    System.out.println(btn.getTextCheckBox() + " " + btn.getState());
                }
            }
            
            if(ab.getText().equals("Download")){
                System.out.println("action1");
                getFiles();
            }
            if(ab.getText().equals("Upload")){
                System.out.println("action3");
                uploadFiles();
            }    
            if(ab.getText().equals("<-")){
                System.out.println("action2");
                changeDirectory();
            }    
            
        }
    };
    
        
    public static void main(String args[]){
        VentanaDropbocs v = new VentanaDropbocs();
        
        v.AddFile("0");
        v.AddFile("ke ase");
        v.AddFile("No lo se");
        v.AddFile("hola");
        v.Clear();
        v.AddFile("hola");
        v.AddFile("ke ase");
        v.AddFile("No lo se");
    }

}
