package com.clubkiwi;

import com.clubkiwi.Character.Item;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Mathew on 9/29/2015.
 */
public class Inventory extends JPanel
{
    //stuffed this here cause its more of a struct than a class.
    class InvItem
    {
        private int count;
        private Item item;

        public InvItem(int count, Item item)
        {
            this.count = count;
            this.item = item;
        }

        public void remove()
        {
            if(count > 0)
                count--;
        }

        public void add()
        {
            count++;
        }

        public int getCount()
        {
            return count;
        }

        public Item getItem()
        {
            return item;
        }
    }

    private ClubKiwi ck;
    private List<InvItem> items = new ArrayList<>();
    private int selecteditem = 1;

    private BufferedImage foodimage;

    public Inventory(ClubKiwi ck)
    {
        setLayout(null);
        setVisible(false);
        setSize(300, 300);
        this.ck = ck;

        try
        {
            this.foodimage = ImageIO.read(ClubKiwi.cldr.getResource("apple.png"));
        }
        catch(Exception ex)
        {

        }
    }

    public void addItemToInventory(Item item)
    {
        boolean exist = false;
        for(InvItem i : items)
        {
            if(i.getItem().equals(item))
            {
                i.add();
                exist = true;
            }
        }

        if(!exist)
            items.add(new InvItem(1, item));
    }

    public void removeItemFromInventory(Item item)
    {
        for(InvItem i : items)
        {
            if(i.getItem().equals(item))
            {
                i.remove();

                if(i.getCount() == 0)
                {
                    items.remove(i);
                    break;
                }
            }
        }

        if(selecteditem > items.size() && items.size() != 0)
            selecteditem = items.size();
    }

    public void useItem()
    {
        if(getSelectedItem() != null)
        {
            ck.getLocalKiwi().giveItem(getSelectedItem());
            removeItemFromInventory(getSelectedItem());
        }
    }

    public void nextItem()
    {
        if(selecteditem == items.size())
            selecteditem = 1;
        else
            selecteditem++;
    }

    public void prevItem()
    {
        if(selecteditem == 1)
            selecteditem = items.size();
        else
            selecteditem--;
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        setLocation(ClubKiwi.gui.getCamX() + 250, ClubKiwi.gui.getCamY() + 100);
        g.setColor(Color.lightGray);
        g.fillRect(0, 0, 300, 300);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, 299, 299);
      //  g.setColor(Color.BLACK);
        g.drawString("Inventory |  ck: " + ck.getLocalKiwi().getMoney(), 10, 20);

        g.drawRect(20, 200, 260, 80);
        int count = 1;
        for(InvItem i : items)
        {
            int drawy = 40 + count * 15;

            //Draw the item
            g.drawString(i.getItem().getsName(), 50, drawy);
            g.drawString("x " + i.getCount(), 260, drawy);

            //Small item image 15x15
            if(i.getItem().getType() == Item.ItemType.Food)
                g.drawImage(Helper.makeColorTransparent(foodimage, Color.WHITE), 25, drawy - 13, null);

            //Is the current item the selected one?
            if(selecteditem == count)
            {
                //The selected rectangle
                g.drawRect(20, drawy - 13, 270, 15);

                //Selected item info
                g.drawString(i.getItem().getsDescription(), 25, 220);
                int count2 = 0;
                for(Map.Entry<String, Double> entry : i.getItem().getEffect().entrySet())
                {
                    g.drawString(entry.getKey() + ": " + entry.getValue(), 25, 235 + count2 * 15);
                    count2++;
                }
            }

            //Next loop
            count ++;
        }
    }

    public Item getSelectedItem()
    {
        try
        {
            return items.get(selecteditem - 1).getItem();
        }
        catch(Exception ex)
        {
            return null;
        }
    }
}
