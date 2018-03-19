package v1.f1nd.com.f1nd_newfeatures;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private TextView mTextMessage;

    ArrayList<PieEntry> entries = new ArrayList<>();

    String arr[] = {"Bharath","Barb","Ladoo","Kanmani","Danu","Crocodile"};

    private PieChart pieChart;
    PieData pieData1 = null;
    PieDataSet dataset1 = null;

    FloatingActionButton close;

    int[] colors = { Color.rgb(189, 47, 71), Color.rgb(228, 101, 92), Color.rgb(241, 177, 79),
            Color.rgb(161, 204, 89), Color.rgb(33, 197, 163), Color.rgb(58, 158, 173), Color.rgb(92, 101, 100),Color.rgb(10, 92, 30)};

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
                case R.id.settings:
                    mTextMessage.setText("Settings");
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);


        CharSequence text = getIntent()
                .getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);

        Toast.makeText(getApplicationContext(),"" + text ,Toast.LENGTH_SHORT).show();

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        pieChart = (PieChart) findViewById(R.id.platinum);
        pieChart.setCenterText("Click word for meaning");

        for(int i=0;i<arr.length;i++){
            entries.add(new PieEntry(1, arr[i]));
        }


        dataset1 = new PieDataSet(entries, "Click word for meaning");
        pieData1 = new PieData(dataset1);
        pieChart.setData(pieData1);

        pieChart.animateY(100, Easing.EasingOption.EaseOutCirc);
        pieChart.setHoleRadius(40);
        pieChart.setDescription(null);
        pieChart.setCenterTextColor(R.color.colorPrimary);
        pieChart.setTransparentCircleRadius(50);
        Legend l = pieChart.getLegend();
        l.setEnabled(false);
        dataset1.setColors(colors);
        pieData1.setValueTextColor(Color.rgb(255,255,255));
        pieData1.setValueTextSize(16);
        dataset1.setDrawValues(false);

        close = (FloatingActionButton)findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myService = new Intent(MainActivity.this, bgService.class);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(myService);
                } else {
                    startService(myService);
                }
            }
        });

    }

}
