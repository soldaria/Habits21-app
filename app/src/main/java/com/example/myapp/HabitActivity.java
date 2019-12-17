package com.example.myapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HabitActivity extends AppCompatActivity {
    public static String CURRENT_HABIT = "CURRENT_HABIT";
    public static String LISTVIEW_NUM = "LISTVIEW_NUM";
    private final String HABIT_STATE_KEY = "HABIT_STATE_KEY";
    public static final int CONST_WEEK_DAYS_CNT = 7;
    Habit currHabit;

    protected void fillDays(TableLayout habit_layout, int start,int stop)
    {
        int titleRow = (start/2)+1;
        int btnName = (start*CONST_WEEK_DAYS_CNT)/2+1;
        for (int i=start;i<stop;i++) {
            if (i % 2 == 0)
            {
                TextView tv = new TextView(this);
                tv.setText(getString(R.string.week)+" "+titleRow);
                tv.setTextSize(15);
                habit_layout.addView(tv,i);
                titleRow++;
            }
            else
            {
                TableRow tableRow = new TableRow(getApplicationContext());
                tableRow.setLayoutParams(new TableRow.LayoutParams( TableRow.LayoutParams.FILL_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                for (int j = 0; j < CONST_WEEK_DAYS_CNT; j++) {
                    Button newButton = new Button(this);
                    newButton.setText(String.valueOf(btnName));
                    btnName++;
                    newButton.setBackground(getResources().getDrawable((R.drawable.round_btn)));
                    View.OnClickListener oclBtnH = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Button btn = (Button) v;
                            if (currHabit.greenList.contains(btn.getText().toString())) {
                                v.setBackground(getResources().getDrawable(R.drawable.round_btn));
                                currHabit.greenList.remove(btn.getText().toString());
                            } else {
                                currHabit.greenList.add(btn.getText().toString());
                                if (currHabit.getGreen() == Habit.CONST_DAYS_COUNT)
                                {
                                    v.setBackground(getResources().getDrawable((R.drawable.star_btn)));
                                    currHabit.setGoldDay(btn.getText().toString());
                                }
                                else{
                                    v.setBackground(getResources().getDrawable(R.drawable.round_btn_push));
                                }
                            }
                        }
                    };
                    newButton.setOnClickListener(oclBtnH);
                    if (currHabit.greenList.contains(newButton.getText().toString())) {
                        if (currHabit.getGoldDay().equalsIgnoreCase(newButton.getText().toString()))
                        {
                            newButton.setBackground(getResources().getDrawable(R.drawable.star_btn));
                        }
                        else
                        {
                            newButton.setBackground(getResources().getDrawable(R.drawable.round_btn_push));
                        }

                    }
                    tableRow.addView(newButton, j);
                    habit_layout.setColumnShrinkable(j,true);
                }
                habit_layout.addView(tableRow, i);
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit);
        TextView habitName = (TextView) findViewById(R.id.habitName);
        Button btnBack = (Button) findViewById(R.id.btnBack);
        Button btnAddWeek = (Button) findViewById(R.id.btnWeek);

        View.OnClickListener oclBtnBack = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        };
        btnBack.setOnClickListener(oclBtnBack);


        if (getIntent().hasExtra(CURRENT_HABIT)) {
            currHabit = (Habit) getIntent().getSerializableExtra(CURRENT_HABIT);
            habitName.setText(currHabit.getName());

            //create grid
            final TableLayout habit_layout = (TableLayout)findViewById(R.id.habitLayout);
            fillDays(habit_layout,0,(currHabit.getDaysCount()/7)*2);
            //add week button
            View.OnClickListener oclBtnWeek = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currHabit.setDaysCount(currHabit.getDaysCount()+7);
                    int stop = (currHabit.getDaysCount()/7)*2;
                    fillDays(habit_layout,stop-2,stop);
                }
            };
            btnAddWeek.setOnClickListener(oclBtnWeek);

        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + CURRENT_HABIT);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        intent.putExtra(CURRENT_HABIT,currHabit);
        intent.putExtra(LISTVIEW_NUM,getIntent().getIntExtra(LISTVIEW_NUM,0));
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(HABIT_STATE_KEY, currHabit);
    }
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currHabit = (Habit) savedInstanceState.getSerializable(HABIT_STATE_KEY);
        TableLayout parent = (TableLayout) findViewById(R.id.habitLayout);
        for (int i = parent.getChildCount() - 1; i >= 0; i--) {
            final View child = parent.getChildAt(i);
            if (child instanceof Button)
            {
                if (currHabit.greenList.contains(i+1)){
                    ((Button)child).setBackground(getResources().getDrawable(R.drawable.round_btn_push));
                }
            }
        }
        }
}
