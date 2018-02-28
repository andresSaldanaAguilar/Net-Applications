/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dropbocs;

import javax.swing.JCheckBox;

/**
 *
 * @author andressaldana
 */
public class Button {
    JCheckBox button;
    boolean state;
    
    public Button(String title){
        button = new JCheckBox(title);
        state = false;    
    }
    
    boolean getState(){
        return this.state;
    }
    JCheckBox getCheckBox(){
        return this.button;
    }
    void setState(boolean state){
       this.state = state;
    }
    String getTextCheckBox(){
        return button.getText();
    }
}
