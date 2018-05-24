package dropbocs;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;
import javax.swing.*;

public class VentanaDropbocs {

    int x, y;
    JFrame f;
    JPanel panel;
    GridBagConstraints gbc;
    GridBagLayout gbl;
    JScrollPane scrollPane;
    List<Button> buttons;
    TreeNode<String> baseNode;
    TreeNode<String> actualNode;
    Stack<String> nodeStack;
    JButton download, upload, getBack;
    ClienteDropbocs cliente;

    public VentanaDropbocs(TreeNode<String> baseN, ClienteDropbocs cliente){
        this.cliente = cliente;
        f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(700,400);
        
        x = 5;
        y = 2;
        panel = new JPanel();
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.weighty = 0;
        gbl = new GridBagLayout();
        panel.setLayout(gbl);
        //panel.setLayout(new GridLayout(0, y));
        
        //making scrollable the checkbox container
        scrollPane = new JScrollPane(panel);
        panel.setAutoscrolls(true);
        scrollPane.setPreferredSize(new Dimension(650,300));
        //creating the rest container
        JLabel title = new JLabel("Dropbocs");
        title.setSize(200, 200);
    
        download = new JButton("Download");
        download.addActionListener(al);
        upload = new JButton("Upload");
        upload.addActionListener(al);
        getBack = new JButton("<-");
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
        
        
        baseNode = actualNode = baseN;
        nodeStack = new Stack<>();
        
        buttons = new LinkedList<>();
        fillButtons(baseNode);
        f.setTitle(File.separator + getRelPath());
    }

    void fillButtons(TreeNode<String> node) {
        for (TreeNode<String> child : node.getChildren())
            AddFile(child.getData());
    }

    void AddFile(String title) {
        x++;
        //panel.setLayout(new GridLayout(x, y));
        Button btn = new Button(title);
        buttons.add(btn);
        JCheckBox box = btn.getCheckBox();
        //box.setSize(20, 20);
        box.addActionListener(al);
        //box.setPreferredSize(new Dimension(20, 20));
        gbc.weightx = 0;
        gbc.gridx = 0;
        gbl.setConstraints(box, gbc);
        box.setBorderPainted(true);
        panel.add(box);
        JButton bot = btn.getButton();
        //bot.setSize(20, 20);
        //bot.setPreferredSize(new Dimension(20, 20));
        bot.addActionListener(al);
        //panel.add(bot, gbc);
        gbc.weightx = 1;
        gbc.gridx = 1;
        gbl.setConstraints(bot, gbc);
        bot.setBorderPainted(true);
        bot.setHorizontalAlignment(SwingConstants.LEFT);
        if(actualNode.hasChild(bot.getText()) && !actualNode.getChild(bot.getText()).hasChildren())
            bot.setEnabled(false);
        panel.add(bot);
    }
    
    
    void Clear(){
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).getCheckBox().setVisible(false);
            panel.remove(buttons.get(i).getCheckBox());
            panel.remove(buttons.get(i).getButton());
            panel.updateUI();
        }
        //cleaning list
        buttons.clear();
        x=5;
    }
    
    void getFiles(){
        List<String> filePaths = new ArrayList<>();
        String relPath = getRelPath();
        for(int i = 0; i < buttons.size(); i++)
            if(buttons.get(i).getCheckBox().isSelected())
                filePaths.add(relPath + buttons.get(i).getButton().getText());
        if(filePaths.size() > 0){
            JFileChooser jf = new JFileChooser();
            jf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            jf.setAcceptAllFileFilterUsed(false);
            jf.requestFocus();

            if (jf.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) 
                cliente.requestFiles(jf.getSelectedFile(), filePaths, getRelPath());
        }
        else
            JOptionPane.showMessageDialog(panel, "Selecciona archivos para descargar.");
    }
    
    void uploadFiles(){
        JFileChooser jf = new JFileChooser();
        jf.setMultiSelectionEnabled(true);
        jf.setAcceptAllFileFilterUsed(false);
        jf.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        jf.requestFocus();
        int r = jf.showOpenDialog(null);

        if (r==JFileChooser.APPROVE_OPTION && jf.getSelectedFiles().length > 0){
            cliente.sendFiles(jf.getSelectedFiles(), getRelPath());
        }
    }
    
    void moveForward(String dir){
        this.Clear();
        actualNode = actualNode.getChild(dir);
        nodeStack.add(dir);
        fillButtons(actualNode);
        f.setTitle(File.separator + getRelPath());
    }
    void moveBackward(){
        if(actualNode != baseNode){
            this.Clear();
            actualNode = actualNode.getParent();
            nodeStack.pop();
            fillButtons(actualNode);
            f.setTitle(File.separator + getRelPath());
        }
        else
            JOptionPane.showMessageDialog(panel, "Estás en la carpeta raíz");
    }
    
    String getRelPath(){
        String newPath = "";
        for(String level : nodeStack)
            if(!level.equals(baseNode))
                newPath += level + File.separator;
        return newPath;
    }
    
    
    ActionListener al = new ActionListener(){
        public void actionPerformed(ActionEvent ae){
            
            if(ae.getSource().equals(getBack))
                moveBackward();
            else if(ae.getSource().equals(download))
                getFiles();
            else if(ae.getSource().equals(upload))
                uploadFiles();
            else{
                for (int i = 0; i < buttons.size(); i++) {
                    if(ae.getSource().equals(buttons.get(i).getButton())){
                        //if(actualNode.hasChild(buttons.get(i).getButton().getText()) && !actualNode.getChild(buttons.get(i).getButton().getText()).hasChildren())
                        //    buttons.get(i).getCheckBox().setSelected(!buttons.get(i).getCheckBox().isSelected());
                        //else    
                        moveForward(buttons.get(i).getButton().getText());
                    }
                }
            }
            
            
            /*
            if(ab.getText().equals("Upload")){
                System.out.println("action3");
                uploadFiles();
            }    
            if(ab.getText().equals("<-")){
                System.out.println("action2");
                changeDirectory();
            }    */
        }
    };
}
