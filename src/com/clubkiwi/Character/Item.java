package com.clubkiwi.Character;

import java.util.HashMap;

/**
 * Item class (food items)
 */
public class Item
{
    //for future expansion
    public enum ItemType
    {
        Food
    }

    private final int index;
    private final double dPrice;
    private final String sName;
    private final String sDescription;
    private final ItemType type;
    private final HashMap<String, Double> effect;

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
    public boolean equals(Object obj)
    {
        return (this.index == ((Item)obj).index);
    }

    @Override
    public String toString()
    {
        return "(" + index + ") " + type + ": "  + sName + " " + sDescription;
    }
}
