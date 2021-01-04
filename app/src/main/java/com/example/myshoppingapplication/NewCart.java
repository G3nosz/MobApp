package com.example.myshoppingapplication;

import android.provider.ContactsContract;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class NewCart{ //tai cia bus laikomas item masyvas su ivairia informacija
    List<NewItem> items = new ArrayList<>();
    private String name;
    private int itemCount;
    private double itemCost;
    private double limit;
    private int Year;
    private int Month;
    private int Day;
    private int Hour;
    private int Minute;
    private String collorCode;
    private int id;
    private int state;

    public NewCart(){
        this.name = "Change name";
        this.itemCount = 0;
        this.itemCost = 0;
        this.state = 0;
    }
    public NewCart(String name){
        this.name = name;
        this.itemCost = 0;
        this.itemCount = 0;
        this.state = 0;
    }
    public NewCart(int id, String name, int count, double cost, int state){
        this.name = name;
        this.itemCost = cost;
        this.itemCount = count;
        this.state = state;
        this.id = id;
    }

    @Override
    public String toString(){
        return "CartModel {" +
                "id = " + this.id +
                ", name = " + this.name + '\''+
                ", count = " + this.itemCount +
                ", cost = " + this.itemCost +
                "}";
    }
    private void cartAddItem(NewItem item){
        items.add(item);
        itemCount++;

        //iskarto tuomet reikia kviesti metoda pakeisti itemCost
    }
    private void removeItem(int identity)
    {
        for (NewItem item : items) {
            if(item.getID() == identity){
                this.itemCount--;
                this.itemCost -= item.getCost();
            }
        }
    }
    private NewItem getItem(int id){
        for ( NewItem item : items ) {
            if (item.getID() == id){
                return item;
            }
        }
        return null;
    }
    public double getLimit(){

        return this.limit;
    }

    public int getYear(){
        return this.Year;
    }
    public int getMonth(){
        return this.Month;
    }
    public int getDay(){
        return this.Day;
    }
    public void setYear(int y){
        this.Year = y;
    }
    public void setMonth(int m){
        this.Month = m;
    }
    public void setDay(int d){
        this.Day = d;
    }

    public void setHour(int h){
        this.Hour = h;
    }
    public void setMinute(int m){
        this.Minute = m;
    }
    public int getHour(){
        return this.Hour;
    }
    public int getMinute(){
        return this.Minute;
    }

    public void setLimit(double lim){
        this.limit = lim;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setID(int d){
        this.id = d;
    }

    //sitas turetu automatiskai apsiskaicuoti pagal tai kiek yra item ir kainas
    public void setCount(int count){
        this.itemCount = count;
    }
    public void setPrice(double price){
        this.itemCost = price;
    }
    public String getName()
    {
        return this.name;
    }
    public int getCount(){
        return this.itemCount;
    }
    public double getCost(){
        return this.itemCost;
    }
    public int getState(){
        return this.state;
    }
    public void setState(int st) {
        this.state = st;
    }
    public int getID(){return this.id;}
}
