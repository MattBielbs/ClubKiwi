package com.clubkiwi;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Mathew on 9/28/2015.
 */
public class Room extends JPanel
{
    private String name;
    private int sizeX, sizeY, startX, staryY;
    private BufferedImage bg;

    public Room(String name, int sizeX, int sizeY, int startX, int startY, String bg)
    {
        this.name = name;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.startX = startX;
        this.staryY = startY;
        setLayout(null);
        setSize(new Dimension(800, 500));
        setLocation(0,0);
        try
        {
            this.bg = ImageIO.read(ClubKiwi.cldr.getResource(bg));
        }
        catch(Exception ex)
        {
            //System.out.println("Could not load kiwi image");
        }
    }

    public int getStartX()
    {
        return startX;
    }

    public int getStaryY()
    {
        return staryY;
    }

    public String getName()
    {
        return name;
    }

    public int getSizeX()
    {
        return sizeX;
    }

    public int getSizeY()
    {
        return sizeY;
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
       //  setLocation(-ClubKiwi.gui.getCamX(), -ClubKiwi.gui.getCamY());
        g.drawImage(bg, -ClubKiwi.gui.getCamX(), -ClubKiwi.gui.getCamY(), null);
      //  g.drawImage(bg, 0,0, null);
    }
}
