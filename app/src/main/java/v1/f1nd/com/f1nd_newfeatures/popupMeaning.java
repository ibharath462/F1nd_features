package v1.f1nd.com.f1nd_newfeatures;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.*;

public class popupMeaning extends Activity {

    static Resources res;
    SharedPreferences prefs = null;
    AutoCompleteTextView word;
    ListView meaning_listView;
    String sword;
    ArrayAdapter<meaningBean> adapter=null;
    ArrayAdapter<String> suggestionsAdapter=null;
    databaseHandler dbHandler;
    ArrayList<meaningBean> meaningAL;
    JSONArray searchArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_meaning);

        WindowManager.LayoutParams wlp = getWindow().getAttributes();
        wlp.flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS |
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;


        wlp.height = (height) / 2;
        wlp.width = (3 * width) / 4 ;



        getWindow().setAttributes(wlp);
        getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.rounded_corners));

        res = getApplicationContext().getResources();
        prefs = getApplicationContext().getSharedPreferences("f1nd.initial.bharath.newUI", Context.MODE_PRIVATE);

        //Getting the process text...
        if(getIntent().getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT) != null){
            sword = getIntent().getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT).toString().trim();
        }else{
            sword = prefs.getString("meaningSearchWord","ROFL");
        }
        prefs.edit().putBoolean("toastExample", true).commit();

        dbHandler = new databaseHandler(getApplicationContext());

        Long id = dbHandler.getIdForWord(sword);
        dbHandler.addHistory(id);

        //Toast.makeText(getApplicationContext(),"" + sword + " " + id,Toast.LENGTH_SHORT).show();

        //Setting the word & meaning...
        word = (AutoCompleteTextView) findViewById(R.id.word);
        meaning_listView = (ListView)findViewById(R.id.meaning_listView);

        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        word.setEnabled(true);
        word.setThreshold(1);
        word.setTextIsSelectable(true);
        word.setText(sword);

        word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imm.showSoftInput(word, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        word.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                runOnBG bgThread = new runOnBG();

                bgThread.execute(charSequence.toString());



            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(!sword.equals(word)){
                    sword = word.getText().toString();
                    //setContent();
                }

            }
        });


        setContent();



    }


    public void setContent(){


        JSONArray resM = dbHandler.getMeaning(sword);
        meaningAL = new ArrayList<>();
        Long historyId = 1L;

        if(resM.length() == 0){
            Toast.makeText(getApplicationContext(),  "No matches found for the word, please try changing the tense / word",Toast.LENGTH_SHORT).show();
        }

        try {
            for(int i=0; i<resM.length(); i++){
                JSONObject tWord = resM.getJSONObject(i);
                String meaningString = tWord.getString("meaning");
                meaningString = meaningString.replaceAll("^ +| +$|( )+", " ");
                meaningString = meaningString.replace("\n", "").replace("\r", "");
                boolean isLiked = false;
                if(tWord.has("fid")){
                    isLiked = true;
                }
                historyId = tWord.getLong("id");
                meaningAL.add(new meaningBean(tWord.getLong("id"),tWord.getString("word"),tWord.getString("wordtype"),meaningString,isLiked));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("F1nd_Popup_Meaning" , "" + meaningAL.size());
        dbHandler.addHistory(historyId);
        adapter = new meaningAdapter(getApplicationContext(), 0, meaningAL);
        meaning_listView.setAdapter(adapter);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (MotionEvent.ACTION_OUTSIDE == event.getAction()) {
            prefs.edit().putBoolean("toastExample", false).commit();
            finish();
            return true;
        }

        // Delegate everything else to Activity.
        return super.onTouchEvent(event);
    }

    public  class runOnBG extends AsyncTask<String,String,String>{


        @Override
        protected String doInBackground(String... params) {

            searchArray = dbHandler.searchWord(params[0]);

            final String[] suggestions;

            List<String> tList = new ArrayList<>();

            if(searchArray.length() > 0){

                for(int c=0; c<searchArray.length();c++){

                    try {
                        JSONObject tWord = searchArray.getJSONObject(c);
                        tList.add(tWord.getString("word"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                suggestions = tList.toArray(new String[0]);

                if(suggestionsAdapter == null){
                    suggestionsAdapter = new ArrayAdapter<String>(popupMeaning.this, android.R.layout.simple_dropdown_item_1line, suggestions);
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            word.setAdapter(suggestionsAdapter);
                            word.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    setContent();
                                }
                            });

                        }
                    });

                }else{

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            if(suggestionsAdapter != null && !suggestionsAdapter.isEmpty()){
                                suggestionsAdapter.clear();
                            }

                            for(String t : suggestions){
                                Log.d("F1nd_Popup_Meaning" , "new search " + t);
                                suggestionsAdapter.add(t);
                            }
                            word.setAdapter(suggestionsAdapter);
                            suggestionsAdapter.notifyDataSetChanged();

                        }
                    });
                }


            }


            return null;
        }

    }
}
