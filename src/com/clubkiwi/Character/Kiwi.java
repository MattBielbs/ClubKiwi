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
        Right
    }
    //Attributes
    private int ID;
    private String name;
    private double health, money, hunger;

    //GUI things
    private int x, y, w, h;
    private Image kiwiimage;
    private ArrayList<MoveState> MoveStates = new ArrayList<>();
    private double rotate = 0;
    private boolean rotateup = true;
    private int currentroom;

    public Kiwi(String name, double health, double money, double hunger)
    {
        this.name = name;
        this.health = health;
        this.money = money;
        this.hunger = hunger;

        //GUI things
        this.x = 0;
        this.y = 0;
        this.w = 100;
        this.h = 145;

        kiwiimage = ClubKiwi.resMgr.getImage("kiwi");
        swaproom(ClubKiwi.gui.main);

        setSize(w, h);
        setLayout(null);
        setOpaque(false);
        setVisible(true);
    }

    public void updateKiwi(String name, double health, double money, double hunger)
    {
        this.name = name;
        this.health = health;
        this.money = money;
        this.hunger = hunger;
        repaint();
    }

    public void swaproom(Room room)
    {
        //Swap the kiwi to the new room
        ClubKiwi.gui.rooms.get(currentroom).remove(this);
        room.add(this, 1);
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

    public double getHunger()
    {
        return hunger;
    }

    public int getID()
    {
        return ID;
    }

    public int getW()
    {
        return w;
    }

    public int getH()
    {
        return h;
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
    //endregion

    //region Setters
    public void setHealth(double health)
    {
        if (health < 0)
            health = 0;

        if(health > 100)
            health = 100;

        this.health = health;
    }

    public void setMoney(double money)
    {
        if (money < 0)
            money = 0;

        if (money > 100000)
            money = 100000;

        this.money = money;
    }

    public void setHunger(double hunger)
    {
        if (hunger < 0)
            hunger = 0;

        if(hunger > 100)
            hunger = 100;

        this.hunger = hunger;
    }

    public void setID(int ID)
    {
        this.ID = ID;
    }

    public void setMovestate(MoveState movestate)
    {
        this.MoveStates.add(movestate);
    }
    //endregion

    public void giveItem(Item item)
    {
        //applies the effect to the kiwi
        this.setHealth(this.getHealth() + item.getEffect().getOrDefault("Health", 0.0));
        this.setHunger(this.getHunger() + item.getEffect().getOrDefault("Hunger", 0.0));
        this.setMoney(this.getMoney() + item.getEffect().getOrDefault("Money", 0.0));
        //Tell the server
        updateServer();
    }

    public void updateServer()
    {
        ClubKiwi.connMgr.SendData(PacketType.KiwiUpdate_C, getHealth(), getMoney(), getHunger());
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
                    this.y -= 5;

                if (hasMoveState(MoveState.Down) && y < ClubKiwi.gui.getCurrentRoom().getSizeY() - h)
                    this.y += 5;

                if (hasMoveState(MoveState.Left) && x > 0)
                    this.x -= 5;

                if (hasMoveState(MoveState.Right) && x < ClubKiwi.gui.getCurrentRoom().getSizeX() - w)
                    this.x += 5;

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


                //Handle room switching
                if(ClubKiwi.gui.getCurrentRoom() == ClubKiwi.gui.main)
                {
                    if(this.x >= ClubKiwi.gui.main.getSizeX() - 100)
                        ClubKiwi.gui.SwitchToRoom(ClubKiwi.gui.room2);
                }
                else
                {
                    if (this.x == 0 )
                        ClubKiwi.gui.SwitchToRoom(ClubKiwi.gui.main);
                }


                // repaint();
            }
            catch (Exception ex)
            {

            }
        }
    }
}
