package net.blocky.launch;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class OptionsButton extends JButton
{

    private static final long serialVersionUID = 7024721992250465353L;

    public OptionsButton(String s)
    {
        super(s);
        addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                OptionsDialog dial = new OptionsDialog(BlockyLauncher.frame);
                dial.setVisible(true);
            }
        });
    }
    
    public void paintComponent(Graphics g)
    {
        g.setColor(Color.white);
        g.drawRect(0, 0, getWidth()-1, getHeight()-1);
        g.drawString(this.getText(), getWidth()/2-g.getFontMetrics().charsWidth(getText().toCharArray(), 0, getText().length())/2, getHeight()/2+g.getFontMetrics().getHeight()/2-2);
    }
}
