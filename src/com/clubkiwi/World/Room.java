package com.clubkiwi.World;

import com.clubkiwi.Character.PoofEffect;
import com.clubkiwi.ClubKiwi;
import com.clubkiwiserver.Packet.Packet;
import com.sun.istack.internal.Nullable;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by Mathew on 9/28/2015.
 */
public class Room extends JLayeredPane
{
    private int ID;
    private String name;
    private int sizeX, sizeY, startX, staryY;
    private Image bg;
    private ArrayList<WorldItem> worldItems = new ArrayList<>();

    public Room(int ID, String name, int sizeX, int sizeY, int startX, int startY, Image bg)
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

    public ArrayList<WorldItem> getWorldItems()
    {
        return worldItems;
    }

    public void addWorldItem(Packet p)
    {
        WorldItem item = new Dispenser((int)p.getData(0), (int)p.getData(1),(int)p.getData(2),(boolean)p.getData(3));

        if(!worldItems.contains(item))
        {
            worldItems.add(item);
            add(item, 100);
        }
    }

    public void removeWorldItem(Packet p)
    {
        WorldItem item = getWorldItemById((int)p.getData(0));
        if(item != null)
        {
            worldItems.remove(item);
            remove(item);
            add(new PoofEffect(item.getXpos(), item.getYpos()));
        }
    }

    public void updateWorldItem(Packet p)
    {
        WorldItem item = new Dispenser((int)p.getData(0), (int)p.getData(1),(int)p.getData(2),(boolean)p.getData(3));
        WorldItem temp = getWorldItemById(item.getID());
        if(temp != null)
        {
            temp = item;
        }
    }

    @Nullable
    public WorldItem getCollidingItem()
    {
        for(WorldItem item : worldItems)
        {
            if(item.isColliding(ClubKiwi.getLocalKiwi()) && item.isVisible())
                return item;
        }

        return null;
    }

    @Nullable
    private WorldItem getWorldItemById(int id)
    {
        for(WorldItem item : worldItems)
        {
            if(item.getID() == id)
                return item;
        }

        return null;
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
