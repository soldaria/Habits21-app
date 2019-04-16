package com.example.myapp;

import java.io.Serializable;
import java.util.ArrayList;


public class Habit implements Serializable {
    private String name;
    private int daysCount;
    public ArrayList<String> greenList;
    public static final int CONST_DAYS_COUNT = 21;
    private String goldDay;

    public Habit(String name) {
        this.name = name;
        this.daysCount = CONST_DAYS_COUNT;
        this.greenList = new ArrayList<String>();
        this.goldDay = "";
    }

    public String toString()
    {
        return this.name;
    }
    public String getName() {
        return name;
    }

    public int getDaysCount(){
        return daysCount;
    }

    public void setDaysCount(int daysCount) {
        this.daysCount = daysCount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGreen() {
        return greenList == null ? 0 : greenList.size();
    }

    public String getGoldDay() {
        return goldDay;
    }

    public void setGoldDay(String goldDay) {
        this.goldDay = goldDay;
    }


}
