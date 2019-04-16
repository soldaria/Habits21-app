package com.example.myapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import java.util.ArrayList;
public class ChartActivity extends AppCompatActivity {

    public static String HABIT_LIST = "HABIT_LIST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        ArrayList<Habit> habits = (ArrayList<Habit>) getIntent().getSerializableExtra(HABIT_LIST);
        if (habits.size() != 0) {
           DataPoint dataPoint[] = new DataPoint[habits.size()];

            for (int i = 0; i < habits.size(); i++) {
                dataPoint[i] = new DataPoint(i, habits.get(i).getGreen());
            }

            BarGraphSeries grapSeries = new BarGraphSeries<>(dataPoint);
            grapSeries.setDrawValuesOnTop(true);
            grapSeries.setValuesOnTopColor(getResources().getColor(R.color.colorPrimaryDark));
            grapSeries.setColor(getResources().getColor(R.color.colorPrimary));
            grapSeries.setSpacing(5);

            GraphView graphView = findViewById(R.id.graph);
            graphView.addSeries(grapSeries);
            graphView.setTitle(getString(R.string.progress));
            graphView.setPadding(10, 10, 10, 10);
            graphView.getViewport().setYAxisBoundsManual(true);
            graphView.getViewport().setMinY(0);
            graphView.getViewport().setXAxisBoundsManual(true);
            graphView.getViewport().setMaxX(habits.size());
            graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
                @Override
                public String formatLabel(double value, boolean isValueX) {
                    if (isValueX) {
                        return super.formatLabel(value, isValueX);
                    } else {
                        return super.formatLabel(value, isValueX) +" "+ getString(R.string.days);
                    }
                }
            });

        }
        Button btnBack = (Button) findViewById(R.id.btnBack);
        View.OnClickListener oclBtnBack = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        };
        btnBack.setOnClickListener(oclBtnBack);

    }
}
