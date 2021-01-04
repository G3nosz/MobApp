package com.example.myshoppingapplication;

public class Profile {
    private double Spent;
    private double Saved;
    private int Carts;
    private int Items;

    public void NewProfile(){
        this.Spent = 0;
        this.Saved = 0;
        this.Carts = 0;
        this.Items = 0;
    }
    public void NewProfile(double Spent, double Saved, int Carts, int Items){
        this.Spent = Spent;
        this.Saved = Saved;
        this.Carts = Carts;
        this.Items = Items;
    }
    public double getSaved() {
        return Saved;
    }

    public double getSpent() {
        return Spent;
    }

    public int getCarts() {
        return Carts;
    }

    public int getItems() {
        return Items;
    }

    public void setCarts(int carts) {
        Carts = carts;
    }

    public void setItems(int items) {
        Items = items;
    }

    public void setSaved(double saved) {
        Saved = saved;
    }

    public void setSpent(double spent) {
        Spent = spent;
    }
}
