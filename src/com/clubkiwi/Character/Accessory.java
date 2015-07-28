package com.clubkiwi.Character;

import java.util.HashMap;

/**
 * Accessory class (bling and such)
 */
public class Accessory
{
    private int index;
    private double dPrice;
    private String sName, sDescription;
    private HashMap<String, Double> statReqs, statBoosts;

    public Accessory(int index, String sName, String sDescription, double dPrice, HashMap<String, Double> statReqs, HashMap<String, Double> statBoosts)
    {
        this.index = index;
        this.dPrice = dPrice;
        this.sName = sName;
        this.sDescription = sDescription;
        this.statReqs = statReqs;
        this.statBoosts = statBoosts;
    }

    @Override
    public String toString()
    {
        return "Accessory{" +
                "index=" + index +
                ", dPrice=" + dPrice +
                ", sName='" + sName + '\'' +
                ", sDescription='" + sDescription + '\'' +
                ", statReqs=" + statReqs +
                ", statBoosts=" + statBoosts +
                '}';
    }
}
