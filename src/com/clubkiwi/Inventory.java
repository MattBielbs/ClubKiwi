package com.clubkiwi;

import com.clubkiwi.Character.Item;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    public Inventory(ClubKiwi ck)
    {
        setLayout(null);
        setVisible(false);
        setSize(300, 300);
        this.ck = ck;
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
        if(selecteditem <= items.size())
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
        g.setColor(Color.gray);
        g.fillRect(0, 0, 300, 300);
        g.setColor(Color.WHITE);
        g.drawRect(0, 0, 299, 299);
        g.setColor(Color.BLACK);
        g.drawString("Inventory |  ck: " + ck.getLocalKiwi().getMoney(), 10, 20);

        int count = 1;
        for(InvItem i : items)
        {
            int drawy = 40 + count * 15;
            if(selecteditem == count)
            {
                g.drawRect(20, drawy - 15, 260, 15);
            }
            g.drawString(i.getItem().getsName() + " x " + i.getCount(), 20, drawy);
            count ++;
        }
    }

    public Item getSelectedItem()
    {
        return items.get(selecteditem - 1).getItem();
    }
}
