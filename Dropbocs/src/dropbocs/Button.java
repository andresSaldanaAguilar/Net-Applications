package dropbocs;

import javax.swing.JButton;
import javax.swing.JCheckBox;

public class Button {
    JCheckBox button;
    JButton bot;
    
    public Button(String title){
        button = new JCheckBox();
        bot = new JButton(title);
    }
    JCheckBox getCheckBox(){
        return button;
    }
    JButton getButton(){
        return bot;
    }
    String getText(){
        return bot.getText();
    }
}
