package com.clubkiwi.World;

import com.clubkiwi.Character.Item;
import com.clubkiwi.ClubKiwi;
import com.clubkiwi.Helper;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by Mathew on 10/2/2015.
 */
public class Dispenser extends WorldItem
{
    private ArrayList<Item> items;

    public Dispenser(int id, int x, int y, boolean bVisible)
    {
        super(id, x, y, 300, 300, false, bVisible, ClubKiwi.resMgr.getImage("bush"));
        this.items = ClubKiwi.items;
    }


}
