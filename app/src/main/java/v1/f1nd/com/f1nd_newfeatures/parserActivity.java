package v1.f1nd.com.f1nd_newfeatures;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

public class parserActivity extends Activity {

    ArrayList<PieEntry> entries = new ArrayList<>();

    String arr[] = null;

    private PieChart pieChart;
    PieData pieData1 = null;
    PieDataSet dataset1 = null;
    String copiedSentence;
    static int sentenceLength = 0;
    static int pieCounter = 1;
    static int remaingWords = 0;

    static Resources res;
    SharedPreferences prefs = null;

    FloatingActionButton closePie;
    FloatingActionButton prev,next;

    int[] colors = { Color.rgb(189, 47, 71), Color.rgb(228, 101, 92), Color.rgb(241, 177, 79),
            Color.rgb(161, 204, 89), Color.rgb(33, 197, 163), Color.rgb(58, 158, 173), Color.rgb(92, 101, 100),Color.rgb(10, 92, 30)};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_parser);

        WindowManager.LayoutParams wlp = getWindow().getAttributes();
        wlp.dimAmount = 0;
        wlp.flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS |
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        wlp.height = 1300;
        wlp.width = 900;
        getWindow().setAttributes(wlp);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(0,0,0,0)));

        prev = (FloatingActionButton)findViewById(R.id.back);
        next = (FloatingActionButton)findViewById(R.id.next);

        res = getApplicationContext().getResources();
        prefs = getApplicationContext().getSharedPreferences("f1nd.initial.bharath.newUI", Context.MODE_PRIVATE);

        copiedSentence = prefs.getString("meaningSearchWord","ROFL");

        sentenceLength = copiedSentence.split(" ").length;

        arr = copiedSentence.split(" ");

        if(sentenceLength > 8){
            Log.d("F1nd_parserActivity", "VISIBLEEEE" + sentenceLength);
            next.setVisibility(View.VISIBLE);
            prev.setVisibility(View.VISIBLE);
        }else{
            next.setVisibility(View.INVISIBLE);
            prev.setVisibility(View.INVISIBLE);
        }

        pieChart = (PieChart) findViewById(R.id.platinum);
        pieChart.setCenterText("Click word for meaning");

        remaingWords = sentenceLength % 8;
        counter(0);
        setPieListeners();

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

    }



    public void counter(int tCounter){
        for(int i = tCounter ; (i < tCounter + 8 && i < sentenceLength);  i++){
            entries.add(new PieEntry(1, arr[i]));
            pieCounter = i;
        }
        pieCounter += 1;
        Log.d("F1nd_Parser", "" + pieCounter);
    }


    public void animatePieChart(){
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
    }



    public void setPieListeners(){


        closePie = (FloatingActionButton)findViewById(R.id.close);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entries.clear();
                int tCounter = pieCounter;
                if(tCounter == sentenceLength){
                    tCounter = 0;
                }
                counter(tCounter);
                dataset1 = new PieDataSet(entries, "Click word for meaning");
                pieData1 = new PieData(dataset1);
                pieChart.setData(pieData1);
                animatePieChart();
            }
        });


        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entries.clear();
                int tCounter = pieCounter;
                //The first pie check...
                if(tCounter == 8 && (tCounter + 8 >= sentenceLength)){
                    //Do as such...
                }else if(tCounter == 8 && (tCounter + 8 < sentenceLength)){
                    tCounter = sentenceLength - (sentenceLength % 8);
                }else if(tCounter == sentenceLength){
                    if(tCounter % 8 != 0){
                        tCounter = tCounter - (tCounter % 8) - 8;
                    }else{
                        tCounter = 0;
                    }
                }else{
                    tCounter -= 16;
                }
                counter(tCounter);
                dataset1 = new PieDataSet(entries, "Click word for meaning");
                pieData1 = new PieData(dataset1);
                pieChart.setData(pieData1);
                animatePieChart();
            }
        });

        closePie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                String pieWord = null;
                Log.d("F1nd_Parser", "pieWORD " + pieCounter + " " + (int)h.getX() + " " + (pieCounter - 8 + (int)h.getX()) );
                int minusFactor = 8;
                if(pieCounter == sentenceLength && sentenceLength % 8 != 0){
                    minusFactor = sentenceLength % 8;
                }
                if(pieCounter > 8){
                    pieWord = arr[pieCounter - minusFactor + (int)h.getX()];
                }else{
                    pieWord = arr[(int)h.getX()];
                }
                Log.d("F1nd_Parser", "pieWORD " + pieWord);
                prefs.edit().putString("meaningSearchWord", "" + pieWord).commit();
                Intent popupMeaning = new Intent(parserActivity.this, v1.f1nd.com.f1nd_newfeatures.popupMeaning.class);
                startActivity(popupMeaning);
                finish();

            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

}
