package com.clubkiwi.Character;

import com.clubkiwi.ClubKiwi;
import com.clubkiwi.Helper;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Mathew on 9/29/2015.
 */
public class PoofEffect extends JPanel implements Runnable
{
    private int x, y;
    private Image poof;

    public PoofEffect(int x, int y)
    {
        this.x = x;
        this.y = y;
        this.poof = ClubKiwi.resMgr.getImage("poof");

        setLocation(x, y);
        setSize(109, 129);
        setOpaque(false);
        setVisible(true);
        Thread temp = new Thread(this);
        temp.start();
    }

    @Override
    public void run()
    {
        //Sleep
        try
        {
            Thread.sleep(500);
        }
        catch(Exception ex)
        {

        }

        this.setVisible(false);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(poof, 0,0, null);
    }
}
