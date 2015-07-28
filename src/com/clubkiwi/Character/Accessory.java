package com.clubkiwi.Character;

import java.util.HashMap;

/**
 * Accessory class (bling and such)
 */
public class Accessory
{
    private double dPrice;
    private String sName, sDescription;
    private HashMap<String, Double> statReqs, statBoosts;

    public Accessory(double dPrice, String sName, String sDescription, HashMap<String, Double> sReqs, HashMap<String, Double> sBoosts)
    {
        this.dPrice = dPrice;
        this.sName = sName;
        this.sDescription = sDescription;
        this.statReqs = sReqs;
        this.statBoosts = sBoosts;
    }
}
