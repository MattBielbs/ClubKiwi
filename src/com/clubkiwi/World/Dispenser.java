package com.clubkiwi.World;

import com.clubkiwi.Character.Item;
import com.clubkiwi.ClubKiwi;
import com.clubkiwi.Helper;
import com.clubkiwiserver.Packet.PacketType;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Mathew on 10/2/2015.
 */
public class Dispenser extends WorldItem
{
    private ArrayList<Item> items;
    private Random random = new Random();

    public Dispenser(int id, int xpos, int ypos, boolean bVisible)
    {
        super(id, xpos, ypos, 300, 300, false, bVisible, ClubKiwi.resMgr.getImage("bush"));
        this.items = ClubKiwi.items;
    }

    public void activate()
    {
        ClubKiwi.invMgr.addItemToInventory(items.get(random.nextInt(items.size())));
        //tell server dispenser has been activated
        ClubKiwi.connMgr.SendData(PacketType.WorldItemRemove, this.getID());
    }

}
