package com.example.myapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;



public class SharedPreferencesHelper {
    public static final String SHARED_PREF_NAME = "SHARED_PREF_NAME";
    public static final String HABITS_KEY = "HABITS_KEY";
    public static final Type HABITS_TYPE = new TypeToken<List<Habit>>() {
    }.getType();


    private SharedPreferences mSharedPreferences;
    private Gson mGson = new Gson();

    public SharedPreferencesHelper(Context context) {
        mSharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public ArrayList<Habit> getHabits() {
        ArrayList<Habit> habits = mGson.fromJson(mSharedPreferences.getString(HABITS_KEY, ""), HABITS_TYPE);
        return habits == null ? new ArrayList<Habit>() : habits;
    }

    public void saveHabits(ArrayList<Habit> habits) {
        mSharedPreferences.edit().putString(HABITS_KEY, mGson.toJson(habits, HABITS_TYPE)).apply();
    }
}
