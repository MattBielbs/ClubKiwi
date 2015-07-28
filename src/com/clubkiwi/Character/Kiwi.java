package com.clubkiwi.Character;

/**
 * Kiwi character class.
 */
public class Kiwi
{
    //Identifier
    private int index;
    //Attributes
    private String name;
    private double health, money;

    //Additional stats
    private double strength, speed, flight, swag;

    //Needs
    private double hunger, social, energy;

    public Kiwi(int index, String name, double health, double money, double strength, double speed, double flight, double swag, double hunger, double social, double energy)
    {
        this.index = index;
        this.name = name;
        this.health = health;
        this.money = money;
        this.strength = strength;
        this.speed = speed;
        this.flight = flight;
        this.swag = swag;
        this.hunger = hunger;
        this.social = social;
        this.energy = energy;
    }

    //region Getters
    public String getName() {
        return name;
    }

    public double getHealth() {
        return health;
    }

    public double getMoney() {
        return money;
    }

    public double getSwag() {
        return swag;
    }

    public double getStrength() {
        return strength;
    }

    public double getSpeed() {
        return speed;
    }

    public double getFlight() {
        return flight;
    }

    public double getHunger() {
        return hunger;
    }

    public double getSocial() {
        return social;
    }

    public double getEnergy() {
        return energy;
    }
    //endregion

    //region Setters
    public void setHealth(double health) {
        if(health < 0)
            throw new IllegalArgumentException("Health cannot be set smaller than 0");

        this.health = health;
    }

    public void setMoney(double money) {
        if(money < 0)
            throw new IllegalArgumentException("Money cannot be set smaller than 0");

        if(money < 100000)
            throw new IllegalArgumentException("Money cannot be set larger than 100000");

        this.money = money;
    }

    public void setSwag(double swag) {
        this.swag = swag;
    }

    public void setStrength(double strength) {
        if(strength < 0)
            throw new IllegalArgumentException("Strength cannot be set smaller than 0");
        this.strength = strength;
    }

    public void setSpeed(double speed) {
        if(speed < 0)
            throw new IllegalArgumentException("Speed cannot be set smaller than 0");
        this.speed = speed;
    }

    public void setFlight(double flight) {
        if(flight < 0)
            throw new IllegalArgumentException("Flight cannot be set smaller than 0");
        this.flight = flight;
    }

    public void setHunger(double hunger) {
        if(hunger < 0)
            throw new IllegalArgumentException("Hunger cannot be set smaller than 0");
        this.hunger = hunger;
    }

    public void setSocial(double social) {
        if(social < 0)
            throw new IllegalArgumentException("Social cannot be set smaller than 0");
        this.social = social;
    }

    public void setEnergy(double energy) {
        if(energy < 0)
            throw new IllegalArgumentException("Energy cannot be set smaller than 0");
        this.energy = energy;
    }
    //endregion


    @Override
    public String toString()
    {
        return "Kiwi{" +
                "index=" + index +
                ", name='" + name + '\'' +
                ", health=" + health +
                ", money=" + money +
                ", strength=" + strength +
                ", speed=" + speed +
                ", flight=" + flight +
                ", swag=" + swag +
                ", hunger=" + hunger +
                ", social=" + social +
                ", energy=" + energy +
                '}';
    }
}
