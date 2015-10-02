package com.clubkiwi.World;

import com.clubkiwi.Character.Kiwi;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Mathew on 10/2/2015.
 */
public abstract class WorldItem extends JPanel
{
    protected int ID, x, y, w, h;
    protected boolean bCollide, bVisible;
    protected Image image;

    public WorldItem(int ID, int x, int y, int w, int h, boolean bCollide, boolean bVisible, Image image)
    {
        this.ID = ID;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.bCollide = bCollide;
        this.bVisible = bVisible;
        this.image = image;

        setLayout(null);
        setOpaque(false);
        setVisible(true);
        setLocation(x, y);
        setSize(w, h);
    }

    public int getID()
    {
        return ID;
    }

    @Override
    public int getX()
    {
        return x;
    }

    @Override
    public int getY()
    {
        return y;
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
            g2d.drawImage(image, x, y, null);
    }

    //Check for collision
    public boolean isColliding(Kiwi k)
    {
        return ((k.getX() + k.getW() > x && k.getX() < x + w) && (k.getY() + k.getH() > y && k.getY() < y + h));
    }

}
