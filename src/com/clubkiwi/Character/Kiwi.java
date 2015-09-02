package com.clubkiwi.Character;

import com.clubkiwi.Drawable;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.texture.Texture;

import java.util.Random;

/**
 * Kiwi character class.
 */
public class Kiwi extends Drawable
{
    //Attributes
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
    private Texture tex;

    public Kiwi(String name, double health, double money, double strength, double speed, double flight, double swag, double hunger, double mood, double energy)
    {
        super(0, 0);
        this.name = name;
        this.health = health;
        this.money = money;
        this.strength = strength;
        this.speed = speed;
        this.flight = flight;
        this.swag = swag;
        this.hunger = hunger;
        this.mood = mood;
        this.energy = energy;

        //Client defaults
        this.sleeping = true;
    }

    public void updateKiwi(String name, double health, double money, double strength, double speed, double flight, double swag, double hunger, double mood, double energy, int x, int y)
    {
        this.x = x;
        this.y = y;
        this.name = name;
        this.health = health;
        this.money = money;
        this.strength = strength;
        this.speed = speed;
        this.flight = flight;
        this.swag = swag;
        this.hunger = hunger;
        this.mood = mood;
        this.energy = energy;
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

    public Texture getTex()
    {
        return tex;
    }

    public void setTex(Texture tex)
    {
        this.tex = tex;
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
    //endregion

    //String things
    @Override
    public String toString()
    {
        return getKiwiGraphic() + "\n" + name + ": " +
                "\nhealth=" + getPercent(getHealth(), 5) +
                // "\nmoney=" + money +
                // ", strength=" + strength +
                // ", speed=" + speed +
                //  ", flight=" + flight +
                //  ", swag=" + swag +
                "\nhunger=" + getPercent(getHunger(), 5) +
                "\nmood  =" + getPercent(getMood(), 5) +
                "\nenergy=" + getPercent(getEnergy(), 5);
    }

    //Makes a nice [===] percent bar used in printing.
    private String getPercent(double value, int scale)
    {
        String temp = "[";
        for (int i = 0; i < 100; i += scale)
        {
            if (i < value)
                temp += "=";
            else
                temp += " ";
        }

        temp += "] " + value + "%";

        return temp;
    }

    private String getKiwiGraphic()
    {
        //Random int for animation.
        Random rnd = new Random();
        int rndint = rnd.nextInt(2);

        if(health <= 0)
        {
            //Tombstone for dead kiwi.
            return "       _____\n" +
                    "     //  +  \\\n" +
                    "    ||  RIP  |\n" +
                    "    ||       |\n" +
                    "    ||       |      " + getName() + "\n" +
                    "   \\||/\\/\\//\\|/";
        }

        if (sleeping)
        {
            //Sleeping has closed eye and zzz.
            return "   _ __ zzz\n" +
                    " /  (->-\n" +
                    " \\__/\n" +
                    "  L\\_";
        }

        if (mood > 40)
        {
            //Left-Right facing animation style if happy.
            if (rndint > 0)
            {
                return "   __ _\n" +
                        "  /  ('>-\n" +
                        "  \\__/\n" +
                        "   L\\_";
            }
            else
            {
                return "   _ __\n" +
                        " -<')  \\\n" +
                        "    \\__/\n" +
                        "    _/I";
            }
        }

        return "   _ __\n" +
                " -<x.x)  \\\n" +
                "    \\__/\n" +
                "    _/I";
    }

    public void doDraw(GL2 gl, TextRenderer render)
    {
        gl.glBindTexture(gl.GL_TEXTURE_2D, tex.getTextureObject());
        gl.glDrawArrays(gl.GL_QUADS, 0, 4);
    }
}
