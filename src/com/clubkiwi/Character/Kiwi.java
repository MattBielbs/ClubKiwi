package com.clubkiwi.Character;

import com.clubkiwi.ClubKiwi;
import com.clubkiwi.Helper;
import com.clubkiwi.World.Room;
import com.clubkiwiserver.Packet.PacketType;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Kiwi character class.
 */
public class Kiwi extends JPanel implements Runnable
{
    public enum MoveState
    {
        None,
        Up,
        Down,
        Left,
        Right,
        Fly
    }
    //Attributes
    private int ID;
    private String name;
    private double health, money;

    //Additional stats
    private double strength, speed, flight, swag;

    //Needs
    private double hunger, mood, energy;

    //Client things (not synced to server)
    private boolean sleeping = true;

    //GUI things
    private int x, y, w, h;
    private Image kiwiimage;
    private ArrayList<MoveState> MoveStates = new ArrayList<>();
    private double rotate = 0;
    private boolean rotateup = true;
    private int currentroom;

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

        //GUI things
        this.x = 0;
        this.y = 0;
        this.w = 100;
        this.h = 145;

        try
        {
            //can fail on the server so it dosnt matter (fix later)
            kiwiimage = ClubKiwi.resMgr.getImage("kiwi");
            swaproom(ClubKiwi.gui.main);
        }
        catch(Exception ex)
        {
        }

        setSize(w,h);
        setLayout(null);
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

    public void swaproom(Room room)
    {
        //Swap the kiwi to the new room
        ClubKiwi.gui.rooms.get(currentroom).remove(this);
        room.add(this);
        currentroom = room.getID();

        //Move player to start pos
        this.x = room.getStartX();
        this.y = room.getStaryY();

        //Send your pos to server.
        if(this == ClubKiwi.getLocalKiwi())
            sendpos();
    }

    public void updateKiwiPos(int x, int y, int currentroom)
    {
        this.x = x;
        this.y = y;
        if(this.currentroom != currentroom)
            swaproom(ClubKiwi.gui.rooms.get(currentroom));

        rotate();
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

    public int getCurrentroom()
    {
        return currentroom;
    }

    public int getW()
    {
        return w;
    }

    public int getH()
    {
        return h;
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

    public void setCurrentroom(int currentroom)
    {
        this.currentroom = currentroom;
    }

    //endregion

    public void giveItem(Item item)
    {
        //applies the effect to the kiwi
        this.setHealth(this.getHealth() + item.getEffect().getOrDefault("Health", 0.0));
        this.setHunger(this.getHunger() + item.getEffect().getOrDefault("Hunger", 0.0));
        this.setMood(this.getMood() + item.getEffect().getOrDefault(("Mood"), 0.0));
        this.setEnergy(this.getEnergy() + item.getEffect().getOrDefault("Energy", 0.0));
        this.setSpeed(this.getSpeed() + item.getEffect().getOrDefault("Speed", 0.0));
        this.setStrength(this.getStrength() + item.getEffect().getOrDefault("Strength", 0.0));

        //Tell the server
        updateServer();
    }

    public void updateServer()
    {
        ClubKiwi.connMgr.SendData(PacketType.KiwiUpdate_C, getHealth(), getMoney(), getStrength(), getSpeed(), getFlight(), getSwag(), getHunger(), getMood(), getEnergy());
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
        this.MoveStates.add(movestate);
    }

    public void removeMovestate(MoveState movestate)
    {
        this.MoveStates.remove(movestate);
    }

    public void clearMoveStates()
    {
        this.MoveStates.clear();
    }

    public boolean hasMoveState(MoveState movestate)
    {
        return this.MoveStates.contains(movestate);
    }




    private void sendpos()
    {
        rotate();
        //send update for server
        ClubKiwi.connMgr.SendData(PacketType.KiwiPos_C, x, y, currentroom);
    }

    private void rotate()
    {
        //update the rotation
        if(this.rotateup)
        {
            if(this.rotate >= 0.08)
                this.rotateup = false;

            rotate+=0.02;
        }
        else
        {
            if(this.rotate <= -0.08)
                this.rotateup = true;

            rotate -=0.02;
        }
    }

    public void removeKiwi()
    {
        ClubKiwi.gui.rooms.get(currentroom).remove(this);
        ClubKiwi.gui.rooms.get(currentroom).add(new PoofEffect(this.x, this.y));
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
                ", currentroom=" + currentroom +
                "} ";
    }

    //For drawing the kiwi
    @Override
    protected void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g);
        int drawx = this.x - ClubKiwi.gui.getCamX();
        int drawy = this.y - ClubKiwi.gui.getCamY();
        setLocation(drawx, drawy);

        //Kiwi
        AffineTransform transform = new AffineTransform();
        transform.translate(getWidth() / 2, getHeight() / 2);
        transform.rotate(rotate);
        transform.translate(-kiwiimage.getWidth(null) / 2, -kiwiimage.getHeight(null) / 2);
        g2d.drawImage(kiwiimage, transform, null);
        //Shadow
        Font f = new Font("Arial", Font.BOLD, 15);
        g.setFont(f);
        FontMetrics fm = g2d.getFontMetrics();
        int ypos = fm.stringWidth(name);
        g2d.setColor(Color.BLACK);
        g2d.drawString(name, 49 - (ypos / 2), 14);
        g2d.drawString(name, 51 - (ypos / 2), 16);
        g2d.drawString(name, 49 - (ypos / 2), 16);
        g2d.drawString(name, 51 - (ypos / 2), 14);

        //Name
        f = new Font("Arial", Font.BOLD, 14);
        g.setFont(f);
        fm = g2d.getFontMetrics();
        ypos = fm.stringWidth(name);
        g2d.setColor(Color.WHITE);
        g2d.drawString(name, 50 - (ypos / 2), 15);

        //Health bar
        drawBar(g2d, getHealth(), Color.green, 0, 140, 100, 2);

        //Hunger bar
        drawBar(g2d, getHunger(), Color.orange, 0, 143, 100, 2);
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

                //Move player
                if (hasMoveState(MoveState.Up) && y > 0)
                    this.y -= (speed / 20);

                if (hasMoveState(MoveState.Down) && y < ClubKiwi.gui.getCurrentRoom().getSizeY() - h)
                    this.y += (speed / 20);

                if (hasMoveState(MoveState.Left) && x > 0)
                    this.x -= (speed / 20);

                if (hasMoveState(MoveState.Right) && x < ClubKiwi.gui.getCurrentRoom().getSizeX() - w)
                    this.x += (speed / 20);

                if (MoveStates.size() > 0)
                    sendpos();


                //Move camera
                int camx = this.x - 400 + (w / 2);
                int camy = this.y - 250 + (h / 2);

                if (camx < 0)
                    camx = 0;

                if (camy < 0)
                    camy = 0;

                if (camx > ClubKiwi.gui.getCurrentRoom().getSizeX() - 800)
                    camx = ClubKiwi.gui.getCurrentRoom().getSizeX() - 800;

                if (camy > ClubKiwi.gui.getCurrentRoom().getSizeY() - 500)
                    camy = ClubKiwi.gui.getCurrentRoom().getSizeY() - 500;

                ClubKiwi.gui.setCamX(camx);
                ClubKiwi.gui.setCamY(camy);

                ClubKiwi.gui.getCurrentRoom().revalidate();
                ClubKiwi.gui.getCurrentRoom().repaint();


                // repaint();
            }
            catch (Exception ex)
            {

            }
        }
    }
}
