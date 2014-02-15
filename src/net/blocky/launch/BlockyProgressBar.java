package net.blocky.launch;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JProgressBar;

public class BlockyProgressBar extends JProgressBar
{

    private static final long serialVersionUID = 5399214801102489550L;
    private BufferedImage progressImg;

    public BlockyProgressBar()
    {
        try
        {
            progressImg = ImageIO.read(BlockyProgressBar.class.getResource("/assets/progress.png"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public void paintComponent(Graphics g)
    {
        g.setColor(new Color(0x404040));
        g.drawRect(0, 0, getWidth()-1, getHeight()-1);
        int min = this.getMinimum();
        int val = this.getValue()-min;
        int max = this.getMaximum()-min;
        g.setColor(new Color(0x008800));
        int x1 = (int) ((float)getWidth() * ((float)val/(float)max));
        g.drawLine(1+x1, 1, 1+x1, getHeight()-2);
        g.setColor(new Color(0x00FF00));
        g.fillRect(1, 1, 1+(x1-1), getHeight()-2);
        g.setColor(new Color(0x808080));
        g.fillRect(1+(x1+1), 1, getWidth()-(1+(x1+1))-1, getHeight()-2);
        if(progressImg != null)
        {
            int w = (int)(((float)(getHeight()-2)/(float)progressImg.getHeight()) * (float)progressImg.getWidth());
            g.drawImage(progressImg, x1-w/2, 1, w, getHeight()-1, null);
        }
    }
}
