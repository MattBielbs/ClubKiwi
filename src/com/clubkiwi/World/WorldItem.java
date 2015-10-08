package com.clubkiwi.World;

import com.clubkiwi.Character.Kiwi;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Mathew on 10/2/2015.
 */
public abstract class WorldItem extends JPanel
{
    protected int ID, xpos, ypos, w, h;
    protected boolean bCollide, bVisible;
    protected Image image;

    public WorldItem(int ID, int xpos, int ypos, int w, int h, boolean bCollide, boolean bVisible, Image image)
    {
        this.ID = ID;
        this.xpos = xpos;
        this.ypos = ypos;
        this.w = w;
        this.h = h;
        this.bCollide = bCollide;
        this.bVisible = bVisible;
        this.image = image;

        setLayout(null);
        setOpaque(false);
        setVisible(true);
        setLocation(xpos, ypos);
        setSize(w, h);
    }

    public int getID()
    {
        return ID;
    }

    public int getXpos()
    {
        return xpos;
    }

    public int getYpos()
    {
        return ypos;
    }

    public boolean isbVisible()
    {
        return bVisible;
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if(isVisible())
            g2d.drawImage(image, 0, 0, null);
    }

    //Check for collision
    public boolean isColliding(Kiwi k)
    {
        return ((k.getX() + k.getW() > xpos && k.getX() < xpos + w) && (k.getY() + k.getH() > ypos && k.getY() < ypos + h));
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof WorldItem)
            return ((WorldItem)obj).getID() == this.getID();

        return false;
    }
}
