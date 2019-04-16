package com.example.myapp;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;
import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Habit> habits = new ArrayList<>();
    SharedPreferencesHelper mSharedPrefHelper;
    ArrayAdapter<Habit> adapter;
    protected void showInputDialog(final Habit exhabit)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        final View newView = inflater.inflate(R.layout.prompt, null);
        final EditText habitName = (EditText)newView.findViewById(R.id.input_text);
        if (exhabit != null) {
            habitName.setText(exhabit.getName());
        }
        builder.setCancelable(false)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                if (exhabit  == null ) {
                                    Habit habit = new Habit(habitName.getText().toString());
                                    habits.add(habit);
                                } else
                                    exhabit.setName(habitName.getText().toString());
                                adapter.notifyDataSetChanged();
                                dialog.cancel();
                            }
                        })
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }});
        builder.setView(newView);
        AlertDialog alert = builder.create();
        alert.show();
    }
    protected void showInputDialog()
    {
        showInputDialog(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button addBtn = (Button) findViewById(R.id.addButton);
        Button chartBtn = (Button) findViewById(R.id.chartButton);
        mSharedPrefHelper = new SharedPreferencesHelper(getApplicationContext());
        //create array list with habits
        habits = mSharedPrefHelper.getHabits();
        final ListView listView = (ListView)findViewById(R.id.lView);

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, habits);
        listView.setAdapter(adapter);

        //add new habit
        View.OnClickListener oclBtnAdd = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog();
               }
        };
        addBtn.setOnClickListener(oclBtnAdd);

        //show chart
        View.OnClickListener oclBtnChart = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ChartActivity.class);
                intent.putExtra(ChartActivity.HABIT_LIST,(Serializable)habits);
                startActivity(intent);
            }
        };
        chartBtn.setOnClickListener(oclBtnChart);

        //click on a habit
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,long id) {
                Intent intent = new Intent(getApplicationContext(),HabitActivity.class);
                intent.putExtra(HabitActivity.CURRENT_HABIT,adapter.getItem(position));
                intent.putExtra(HabitActivity.LISTVIEW_NUM,position);
                startActivityForResult(intent,1);
            }
        });
        //long click on a habit
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                String editItem= getString(R.string.edit);
                String deleteItem = getString(R.string.delete);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                final Habit selectedItem = (Habit) parent.getItemAtPosition(position);
                builder.setCancelable(true)
                        .setTitle(R.string.choose)
                        .setPositiveButton(editItem,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        showInputDialog(selectedItem);
                                        dialog.cancel();
                                    }
                                })
                        .setNegativeButton(deleteItem,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                        adapter.remove(selectedItem);
                                        adapter.notifyDataSetChanged();

                                        Toast.makeText(getApplicationContext(),
                                                getString(R.string.habit)+" "+selectedItem +" "+ getString(R.string.remove),
                                                Toast.LENGTH_SHORT).show();
                                        dialog.cancel();
                                    }});
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }
        });

        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        //TODO RatingBar
        /*RatingBar.OnRatingBarChangeListener ocl = new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                try {
                    Uri uri = Uri.parse("market://details?id=" + getPackageName());
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(goToMarket);
                } catch (
                        ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id="
                                    + getPackageName())));
                }
            }
        };
        ratingBar.setOnRatingBarChangeListener(ocl);*/
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSharedPrefHelper.saveHabits(habits);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        Habit newHabit = (Habit) data.getSerializableExtra(HabitActivity.CURRENT_HABIT);
        int indexValue = data.getIntExtra(HabitActivity.LISTVIEW_NUM,0);
        habits.set(indexValue,newHabit);
        adapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_info:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                final View newView = inflater.inflate(R.layout.info_layout, null);
                builder.setCancelable(false)
                        .setPositiveButton(R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                builder.setView(newView);
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
