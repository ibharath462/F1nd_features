package v1.f1nd.com.f1nd_newfeatures;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.shashank.sony.fancywalkthroughlib.FancyWalkthroughActivity;
import com.shashank.sony.fancywalkthroughlib.FancyWalkthroughCard;

import java.util.ArrayList;
import java.util.List;

public class onBoarding extends FancyWalkthroughActivity {

    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_on_boarding);
        prefs = getSharedPreferences("f1nd.initial.bharath.newUI", MODE_PRIVATE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;

        height = (height > 2000) ? 1024 : 768;

        Intent ti =getIntent();
        boolean isFromMA = ti.getBooleanExtra("isFromMA",false);

        if(prefs.getBoolean("firstrun", true) || isFromMA){

            FancyWalkthroughCard fancywalkthroughCard1 = new FancyWalkthroughCard("Introducing F1nd", "Completely offline popup dictionary, also acts as normal dictionary",R.drawable.popupedit);
            FancyWalkthroughCard fancywalkthroughCard2 = new FancyWalkthroughCard("With sentence parser", "Introducing first of its kind parser where a word can be extracted from tweets / feeds on copying. Cycle through the words with the controls",R.drawable.parser);
            FancyWalkthroughCard fancywalkthroughCard3 = new FancyWalkthroughCard("Select to F1nd", "If you are using Android Marshmallow or later versions , then select a word on any app then click on three dots and choose F1nd",R.drawable.process);
            FancyWalkthroughCard fancywalkthroughCard4 = new FancyWalkthroughCard("Copy to F1nd", "Enable copy to F1nd and start service, F1nd will run in bg & u can copy word/sentence for meaning",R.drawable.copy);
            FancyWalkthroughCard fancywalkthroughCard5 = new FancyWalkthroughCard("Learn a Word", "Enable LaW and start service, F1nd will give u new words as per defined interval. Dont worry if u have missed, u can catch it in LaW tab",R.drawable.law);
            FancyWalkthroughCard fancywalkthroughCard9 = new FancyWalkthroughCard("Long press", "Long press on a word for quick meaning",R.drawable.quickview);
            FancyWalkthroughCard fancywalkthroughCard10 = new FancyWalkthroughCard("Share", "Challenge friends by sharing the meaning with friends and asking them to F1nd the word !",R.drawable.sharesss);



            fancywalkthroughCard1.setBackgroundColor(R.color.white);
            fancywalkthroughCard1.setIconLayoutParams(2048,height,0,0,0,0);
            fancywalkthroughCard2.setBackgroundColor(R.color.white);
            fancywalkthroughCard2.setIconLayoutParams(2048,height,0,0,0,0);
            fancywalkthroughCard3.setBackgroundColor(R.color.white);
            fancywalkthroughCard3.setIconLayoutParams(2048,height,0,0,0,0);
            fancywalkthroughCard4.setBackgroundColor(R.color.white);
            fancywalkthroughCard4.setIconLayoutParams(2048,height,0,0,0,0);
            fancywalkthroughCard5.setBackgroundColor(R.color.white);
            fancywalkthroughCard5.setIconLayoutParams(2048,height,0,0,0,0);


            fancywalkthroughCard9.setBackgroundColor(R.color.white);
            fancywalkthroughCard9.setIconLayoutParams(2048,height,0,0,0,0);
            fancywalkthroughCard10.setBackgroundColor(R.color.white);
            fancywalkthroughCard10.setIconLayoutParams(2048,height,0,0,0,0);
            List<FancyWalkthroughCard> pages = new ArrayList<>();

            pages.add(fancywalkthroughCard1);
            pages.add(fancywalkthroughCard2);
            pages.add(fancywalkthroughCard3);
            pages.add(fancywalkthroughCard4);
            pages.add(fancywalkthroughCard5);
            pages.add(fancywalkthroughCard9);
            pages.add(fancywalkthroughCard10);


            for (FancyWalkthroughCard page : pages) {
                page.setTitleColor(R.color.black);
                fancywalkthroughCard4.setBackgroundColor(R.color.white);
                page.setDescriptionColor(R.color.black);
            }
            setFinishButtonTitle("Get Started");
            showNavigationControls(true);
            setColorBackground(R.color.colorPrimaryDark );
            //setImageBackground(R.drawable.dictionary);
            setInactiveIndicatorColor(R.color.grey_600);

            setActiveIndicatorColor(R.color.colorAccent);
            setOnboardPages(pages);

        }else{
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
            finish();
        }





    }

    @Override
    public void onFinishButtonPressed() {

        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
        finish();
    }




}

