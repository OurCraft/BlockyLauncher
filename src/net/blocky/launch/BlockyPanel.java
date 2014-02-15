package net.blocky.launch;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class BlockyPanel extends JPanel
{

    private static final long serialVersionUID = -6935075862647403636L;
    private BufferedImage img;

    public BlockyPanel()
    {
        super();
        try
        {
            img = ImageIO.read(BlockyPanel.class.getResource("/assets/background.png"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public void paintComponent(Graphics g)
    {
        if(img != null)
        {
            g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
        }
    }
}
