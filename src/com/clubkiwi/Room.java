package com.clubkiwi;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Mathew on 9/28/2015.
 */
public class Room extends JLayeredPane
{
    private int ID;
    private String name;
    private int sizeX, sizeY, startX, staryY;
    private BufferedImage bg;

    public Room(int ID, String name, int sizeX, int sizeY, int startX, int startY, BufferedImage bg)
    {
        this.ID = ID;
        this.name = name;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.startX = startX;
        this.staryY = startY;
        this.bg = bg;
        setLayout(null);
        setSize(new Dimension(800, 500));
        setLocation(0, 0);
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

    public int getID()
    {
        return ID;
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        try //this exception is not thrown in debug mode and does not effect the program at all.
        {
            if (ClubKiwi.gui.isIngame())
                g.translate(-ClubKiwi.gui.getCamX(), -ClubKiwi.gui.getCamY());

            g.drawImage(bg, 0, 0, null);
        }
        catch(Exception ex)
        {

        }
    }
}
