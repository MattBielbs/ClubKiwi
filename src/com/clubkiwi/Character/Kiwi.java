package com.clubkiwi.Character;

import com.clubkiwi.ClubKiwi;
import com.clubkiwi.GUI;
import com.clubkiwi.Helper;
import com.clubkiwiserver.Packet.PacketType;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
/**
 * Kiwi character class.
 */
public class Kiwi extends JPanel implements Runnable
{
    //Attributes
    private int ID;
    private String name;
    private double health, money;

    //Additional stats
    private double strength, speed, flight, swag;

    //Needs
    private double hunger, mood, energy;

    //Client things (not synced to server)
    private boolean sleeping;

    //GUI things
    private int x, y;
    private BufferedImage kiwiimage;
    private MoveState movestate;
    private double rotate;
    private boolean rotateup;

    public Kiwi(String name, double health, double money, double strength, double speed, double flight, double swag, double hunger, double mood, double energy)
    {
        this.name = name;
        this.health = health;
        this.money = money;
        this.strength = strength;
        this.speed = 100;
        this.flight = flight;
        this.swag = swag;
        this.hunger = hunger;
        this.mood = mood;
        this.energy = energy;

        //Client defaults
        this.sleeping = true;
        this.movestate = MoveState.None;
        this.rotate = 0;
        this.rotateup = true;

        //GUI things
        this.x = 20;
        this.y = 20;

        try
        {
            kiwiimage = ImageIO.read(ClubKiwi.cldr.getResource("kiwi.png"));
        }
        catch(Exception ex)
        {
            //System.out.println("Could not load kiwi image");
        }

        setSize(100,70);
        setOpaque(false);
        setVisible(true);
    }

    public void updateKiwi(String name, double health, double money, double strength, double speed, double flight, double swag, double hunger, double mood, double energy)
    {
        this.name = name;
        this.health = health;
        this.money = money;
        this.strength = strength;
        //this.speed = speed;
        this.flight = flight;
        this.swag = swag;
        this.hunger = hunger;
        this.mood = mood;
        this.energy = energy;
        repaint();
    }

    public void updateKiwiPos(int x, int y)
    {
        this.x = x;
        this.y = y;
        repaint();
    }

    //region Getters
    public String getName()
    {
        return name;
    }

    public double getHealth()
    {
        return health;
    }

    public double getMoney()
    {
        return money;
    }

    public double getSwag()
    {
        return swag;
    }

    public double getStrength()
    {
        return strength;
    }

    public double getSpeed()
    {
        return speed;
    }

    public double getFlight()
    {
        return flight;
    }

    public double getHunger()
    {
        return hunger;
    }

    public double getMood()
    {
        return mood;
    }

    public double getEnergy()
    {
        return energy;
    }

    public boolean isSleeping()
    {
        return sleeping;
    }

    public int getID()
    {
        return ID;
    }

    //endregion

    //region Setters
    public void setHealth(double health)
    {
        if (health < 0)
            health = 0;

        this.health = health;
    }

    public void setMoney(double money)
    {
        if (money < 0)
            money = 0;

        if (money < 100000)
            money = 100000;

        this.money = money;
    }

    public void setSwag(double swag)
    {
        this.swag = swag;
    }

    public void setStrength(double strength)
    {
        if (strength < 0)
            strength = 0;

        if(strength > 100)
            strength = 100;

        this.strength = strength;
    }

    public void setSpeed(double speed)
    {
        if (speed < 0)
            speed = 0;

        if(speed > 100)
            speed = 100;

        this.speed = speed;
    }

    public void setFlight(double flight)
    {
        if (flight < 0)
            flight = 0;

        if(flight > 100)
            flight = 100;

        this.flight = flight;
    }

    public void setHunger(double hunger)
    {
        if (hunger < 0)
            hunger = 0;

        if(hunger > 100)
            hunger = 100;

        this.hunger = hunger;
    }

    public void setMood(double mood)
    {
        if (mood < 0)
            mood = 0;

        if(mood > 100)
            mood = 100;
        this.mood = mood;
    }

    public void setEnergy(double energy)
    {
        if (energy < 0)
            energy = 0;

        if(energy > 100)
            energy = 100;

        this.energy = energy;
    }

    @Override
    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    @Override
    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public void giveItem(Item item)
    {
        //applies the effect to the kiwi
        this.setHunger(this.getHunger() + item.getEffect().getOrDefault("Hunger", 0.0));
        this.setMood(this.getMood() + item.getEffect().getOrDefault(("Mood"), 0.0));
        this.setEnergy(this.getEnergy() + item.getEffect().getOrDefault("Energy", 0.0));
        this.setSpeed(this.getSpeed() + item.getEffect().getOrDefault("Speed", 0.0));
        this.setStrength(this.getStrength() + item.getEffect().getOrDefault("Strength", 0.0));
    }

    public void setSleeping(boolean sleeping)
    {
        this.sleeping = sleeping;
    }

    public void setID(int ID)
    {
        this.ID = ID;
    }

    public void setMovestate(MoveState movestate)
    {
        this.movestate = movestate;
    }

    //endregion


    public void sendpos()
    {
        //update the rotation
        if(this.rotateup)
        {
            if(this.rotate == 0.08)
                this.rotateup = false;

            rotate+=0.01;
        }
        else
        {
            if(this.rotate == -0.08)
                this.rotateup = true;

            rotate -=0.01;
        }
        //send update for server
        ClubKiwi.conn.SendData(PacketType.KiwiPos_C, x, y);
    }
    //String things

    @Override
    public String toString()
    {
        return "Kiwi{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", x=" + x +
                ", y=" + y +
                "} " + super.toString();
    }

    //For drawing the kiwi
    @Override
    protected void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D)g;
        super.paintComponent(g);
        setLocation(this.x, this.y);

        //Name
        g2d.setColor(Color.BLACK);
        g2d.drawString(name, 0, 8);

        //Kiwi
        AffineTransform transform = new AffineTransform();
        transform.translate(getWidth() / 2, getHeight() / 2);
        transform.rotate(rotate);
        transform.translate(-kiwiimage.getWidth() / 2, -kiwiimage.getHeight() / 2);
        g2d.drawImage(Helper.makeColorTransparent(kiwiimage, Color.WHITE), transform, null);

        //Health bar
        drawBar(g2d, getHealth(), Color.green, 0, 60, 100, 2);

        //Hunger bar
        drawBar(g2d, getHunger(), Color.orange, 0, 63, 100, 2);
    }

    private void drawBar(Graphics2D g2d, double stat, Color color, int x, int y, int w, int h)
    {
        g2d.setColor(Color.BLACK);
        g2d.fillRect(x, y, w, h);
        g2d.setColor(color);
        g2d.fillRect(x, y, (int) stat, h);
    }

    @Override
    public void run()
    {
        while(ClubKiwi.running)
        {
            try
            {
                Thread.sleep(20);
                if (movestate == MoveState.Up && y > 0)
                {
                    this.y -= (speed / 20);
                    sendpos();
                }
                if (movestate == MoveState.Down && y < 420)
                {
                    this.y += (speed / 20);
                    sendpos();
                }

                if (movestate == MoveState.Left && x > 0)
                {
                    this.x -= (speed / 20);
                    sendpos();
                }

                if (movestate == MoveState.Right && x < 700)
                {
                    this.x += (speed / 20);
                    sendpos();
                }
                repaint();
            }
            catch (Exception ex)
            {

            }
        }
    }
}
