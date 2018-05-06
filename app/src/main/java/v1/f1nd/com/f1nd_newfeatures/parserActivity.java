package v1.f1nd.com.f1nd_newfeatures;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
    Random rand = new Random();

    List<int[]> colorArray = new ArrayList<>();
    int[] greenWheel,violetWheel,indigoWheel,blueWheel,yellowWheel,orangeWheel,redWheel;

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

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        //initializing color array....
        greenWheel = new int[]{Color.parseColor("#2e7d32"),Color.parseColor("#388e3c"),Color.parseColor("#43a047"),Color.parseColor("#4caf50"),
                            Color.parseColor("#66bb6a"),Color.parseColor("#81c784"),Color.parseColor("#a5d6a7"),Color.parseColor("#43a047")};
        violetWheel = new int[]{Color.parseColor("#6a1b9a"),Color.parseColor("#7b1fa2"),Color.parseColor("#8e24aa"),Color.parseColor("#9c27b0"),
                Color.parseColor("#ab47bc"),Color.parseColor("#ba68c8"),Color.parseColor("#ce93d8"),Color.parseColor("#e1bee7")};
        indigoWheel = new int[]{Color.parseColor("#283593"),Color.parseColor("#303F9F"),Color.parseColor("#3949AB"),Color.parseColor("#3F51B5"),
                Color.parseColor("#5C6BC0"),Color.parseColor("#7986CB"),Color.parseColor("#9FA8DA"),Color.parseColor("#C5CAE9")};
        blueWheel = new int[]{Color.parseColor("#0d47a1"),Color.parseColor("#2962ff"),Color.parseColor("#1565c0"),Color.parseColor("#2979ff"),
                Color.parseColor("#1976d2"),Color.parseColor("#448aff"),Color.parseColor("#1e88e5"),Color.parseColor("#2196f3")};
        yellowWheel = new int[]{Color.parseColor("#263238"),Color.parseColor("#37474f"),Color.parseColor("#455a64"),Color.parseColor("#546e7a"),
                Color.parseColor("#607d8b"),Color.parseColor("#90a4ae"),Color.parseColor("#b0bec5"),Color.parseColor("#cfd8dc")};
        orangeWheel = new int[]{Color.parseColor("#ff6d00"),Color.parseColor("#ef6c00"),Color.parseColor("#ff9100"),Color.parseColor("#ffab40"),
                Color.parseColor("#fb8c00"),Color.parseColor("#ff9800"),Color.parseColor("#ffa726"),Color.parseColor("#ffb74d")};
        redWheel = new int[]{Color.parseColor("#b71c1c"),Color.parseColor("#d50000"),Color.parseColor("#c62828"),Color.parseColor("#d32f2f"),
                Color.parseColor("#e53935"),Color.parseColor("#f44336"),Color.parseColor("#ff5252"),Color.parseColor("#ef5350")};

        colorArray.add(violetWheel);
        colorArray.add(indigoWheel);
        colorArray.add(blueWheel);
        colorArray.add(greenWheel);
        colorArray.add(yellowWheel);
        colorArray.add(orangeWheel);
        colorArray.add(redWheel);


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
        pieChart.setCenterText("F1nd");

        remaingWords = sentenceLength % 8;
        counter(0);
        setPieListeners();

        dataset1 = new PieDataSet(entries, "F1nd");
        dataset1.setDrawValues(false);
        pieData1 = new PieData(dataset1);
        pieData1.setValueTextColor(Color.rgb(0,0,0));
        pieData1.setValueTextSize(32);
        pieChart.setData(pieData1);

        pieChart.animateY(100, Easing.EasingOption.EaseOutCirc);
        pieChart.setHoleRadius(40);
        pieChart.setDescription(null);
        pieChart.setCenterTextColor(R.color.colorPrimary);
        pieChart.setTransparentCircleRadius(50);
        Legend l = pieChart.getLegend();
        l.setEnabled(false);
        colors = colorArray.get(rand.nextInt(7));
        Log.d("F1nd_Parser", "pieColors " + colors);
        int isReverse = rand.nextInt(2);
        if(isReverse == 1){
            Collections.reverse(Arrays.asList(colors));
            Log.d("F1nd_Parser", "pieColors " + colors);
        }
        dataset1.setColors(colors);

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
        colors = colorArray.get(rand.nextInt(7));
        Log.d("F1nd_Parser", "pieColors " + colors);
        int isReverse = rand.nextInt(2);
        if(isReverse == 1){
            Collections.reverse(Arrays.asList(colors));
            Log.d("F1nd_Parser", "pieColors " + colors);
        }
        dataset1.setColors(colors);
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
                dataset1 = new PieDataSet(entries, "F1nd");
                pieData1 = new PieData(dataset1);
                pieChart.setData(pieData1);
                animatePieChart();
            }
        });

        closePie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/*");
                share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                share.putExtra(Intent.EXTRA_TEXT, "" + copiedSentence + ".\n\nI parsed this sentence and found meaning for a word!");
                LinearLayout parserLL = (LinearLayout)findViewById(R.id.parserLL);
                closePie.setVisibility(View.INVISIBLE);
                parserLL.setDrawingCacheEnabled(true);
                Bitmap bitmap = Bitmap.createBitmap(parserLL.getDrawingCache());
                closePie.setVisibility(View.VISIBLE);
                parserLL.setDrawingCacheEnabled(false);

                String mPath = Environment.getExternalStorageDirectory().getAbsolutePath().toString() + "/"  + "barbieee1.jpeg";
                File imageFile = new File(mPath);

                FileOutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(imageFile);
                    int quality = 100;
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
                    outputStream.flush();
                    outputStream.close();
                    Toast.makeText(getApplicationContext(),"Shared to " + mPath,Toast.LENGTH_SHORT).show();
                    share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(imageFile));
                    getApplicationContext().startActivity(share);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
