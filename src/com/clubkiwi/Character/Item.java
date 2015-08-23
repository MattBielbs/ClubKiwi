package com.clubkiwi.Character;

import java.util.HashMap;

/**
 * Item class (food and toys for now)
 */
public class Item
{
    private int index;
    private double dPrice;
    private String sName, sDescription;
    private ItemType type;
    private HashMap<String, Double> effect;

    public Item(int index, String sName, String sDescription, double dPrice, ItemType type, HashMap<String, Double> effect)
    {
        this.index = index;
        this.dPrice = dPrice;
        this.sName = sName;
        this.sDescription = sDescription;
        this.type = type;
        this.effect = effect;
    }

    public int getIndex()
    {
        return index;
    }

    public double getdPrice()
    {
        return dPrice;
    }

    public String getsName()
    {
        return sName;
    }

    public String getsDescription()
    {
        return sDescription;
    }

    public ItemType getType()
    {
        return type;
    }

    public HashMap<String, Double> getEffect()
    {
        return effect;
    }

    @Override
    public String toString()
    {
        return "Item{" +
                "index=" + index +
                ", dPrice=" + dPrice +
                ", sName='" + sName + '\'' +
                ", sDescription='" + sDescription + '\'' +
                ", type=" + type +
                '}';
    }
}
