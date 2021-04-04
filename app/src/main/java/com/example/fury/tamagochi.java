package com.example.fury;

public class tamagochi {
    private int hungriness = 0;
    private int happiness = 0;
    private int strength = 0;
    private int cleanliness = 0;

    public tamagochi(int hungriness, int happiness, int strength, int cleanliness) {
        this.hungriness = hungriness;
        this.happiness = happiness;
        this.strength = strength;
        this.cleanliness = cleanliness;
    }

    public void feed() {
        hungriness -=2;
        strength += 1;
        happiness+=2;
        if (hungriness<0) {
            hungriness = 0;
        }
        if (happiness > 10) {
            happiness = 10;
        }
        if (strength>10) {
            strength=10;
        }
    }
    public void walk () {
        happiness += 3;
        strength -= 2;
        cleanliness-=2;
        if (happiness > 10) {
            happiness = 10;
        }
        if (strength<0) {
            strength=0;
        }
        if (cleanliness<0) {
            cleanliness=0;
        }
    }
    public void clean() {
        cleanliness = 10;
        happiness-=2;
        if (happiness < 0) {
            happiness = 0;
        }
    }
    public void pet() {
        happiness+=1;
        if (happiness>10) {
            happiness = 10;
        }
    }
    public void sleep() {
        strength = 10;
    }

    public int getHungriness() {
        return hungriness;
    }

    public int getHappiness() {
        return happiness;
    }

    public int getStrength() {
        return strength;
    }

    public int getCleanliness() {
        return cleanliness;
    }

    public void passTime() {
        hungriness++;
        cleanliness--;
        if (strength==0) {
            strength = 10;
        } else strength --;
        if (hungriness>=7)
            happiness--;
        if (cleanliness < 3)
            happiness--;
        if (hungriness>10)
            hungriness = 10;
        if (cleanliness<0)
            cleanliness=0;
        if (happiness<0)
            happiness =0;
    }







}