package v1.f1nd.com.f1nd_newfeatures;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.graphics.Color.*;

public class popupMeaning extends Activity {

    static Resources res;
    SharedPreferences prefs = null;
    ArrayAdapter<meaningBean> adapter=null;
    databaseHandler dbHandler;
    ArrayList<meaningBean> meaningAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_meaning);

        WindowManager.LayoutParams wlp = getWindow().getAttributes();
        wlp.dimAmount = 0;
        wlp.flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS |
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        wlp.height = 1300;
        wlp.width = 900;
        getWindow().setAttributes(wlp);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(255,148,0,211)));

        //Getting the process text...

        String sword = getIntent().getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT).toString().trim();
        Toast.makeText(getApplicationContext(),"" + sword,Toast.LENGTH_SHORT).show();
        res = getApplicationContext().getResources();
        prefs = getApplicationContext().getSharedPreferences("f1nd.initial.bharath.newUI", Context.MODE_PRIVATE);

        dbHandler = new databaseHandler(getApplicationContext());

//        Long id = Long.parseLong(prefs.getString("id","1").toString());
//        dbHandler.addHistory(id);

        //Setting the word & meaning...
        TextView word = (TextView)findViewById(R.id.word);
        ListView meaning_listView = (ListView)findViewById(R.id.meaning_listView);

        JSONArray resM = dbHandler.getMeaning(sword);
        meaningAL = new ArrayList<>();

        try {
            word.setText("" + resM.getJSONObject(0).getString("word"));
            for(int i=0; i<resM.length(); i++){
                JSONObject tWord = resM.getJSONObject(i);
                String meaningString = tWord.getString("meaning");
                meaningString = meaningString.replaceAll("^ +| +$|( )+", " ");
                meaningString = meaningString.replace("\n", "").replace("\r", "");
                boolean isLiked = false;
                if(tWord.has("fid")){
                    isLiked = true;
                }

                meaningAL.add(new meaningBean(tWord.getLong("id"),tWord.getString("word"),tWord.getString("wordtype"),meaningString,isLiked));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("F1nd_Meaning adapter" , "" + meaningAL.size());

        adapter = new meaningAdapter(getApplicationContext(), 0, meaningAL);
        meaning_listView.setAdapter(adapter);




    }
}
