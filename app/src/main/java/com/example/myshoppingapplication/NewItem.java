package com.example.myshoppingapplication;

import android.graphics.Bitmap;

public class NewItem {
    private String name;
    private int count;
    private double cost;
    private int state; //true yra kad item yra paimtas, false kad item yra nepaimtas
    private int id;
    private int cartID;

    private Bitmap photoe;

    public NewItem(){
        this.state = 0;
    }

    public NewItem(String name, int count, double cost){
        this.name = name;
        this.count = count;
        this.cost = cost;
        this.state = 0; //false reiskia kad item nera pazymetas, tai as renku kur state yra true kad item yra paimta
    }
    public NewItem(String name, int count, double cost, int cartID,int state, Bitmap photoe){
        this.name = name;
        this.count = count;
        this.cost = cost;
        this.state = state;
        this.cartID = cartID;
        this.photoe = photoe;
    }

    public String getName(){
        return name;
    }
    public int getCartID(){
        return this.cartID;
    }
    public void setName(String name){
        this.name = name;
    }
    public int getCount(){
        return count;
    }
    public void setID (int d){
       this.id = d;
    }
    public Bitmap getImage() {
        return photoe;
    }
    public void setImage(Bitmap photoe) {
        this.photoe = photoe;
    }
    public void setCount(int count){
        this.count = count;
    }
    public double getCost(){
        return cost;
    }
    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getState() { return state;}
    public void setState(int st){
        this.state = st;
    }
    public int getID(){
        return this.id;
    }
}

