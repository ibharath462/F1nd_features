package v1.f1nd.com.f1nd_newfeatures;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.media.Image;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by bharath on 30/3/18.
 */

public class wordAdapter extends ArrayAdapter{

    private Context context;
    private List<Word> wordDetails;
    databaseHandler dbHandler;

    static Resources res;
    SharedPreferences prefs = null;



    public wordAdapter(Context context, int resource,ArrayList<Word> objects) {
        super(context, resource,objects);
        this.context = context;
        this.wordDetails = objects;
    }

    public View getView(final int position, View convertView, final ViewGroup parent){

        final Word w = wordDetails.get(position);

        //get the inflater and inflate the XML layout for each item
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.word_view, null);

        final TextView word=(TextView)view.findViewById(R.id.word);
        TextView wordType=(TextView)view.findViewById(R.id.wordtype);
        LinearLayout top_wrapper = (LinearLayout)view.findViewById(R.id.topLayout);

        LikeButton favorite= (LikeButton)view.findViewById(R.id.favorite);

        favorite.setLiked(w.getLiked());

        dbHandler = new databaseHandler(getContext());



        favorite.setEnabled(true);

        favorite.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                dbHandler.addFavorite(w.getId());
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                dbHandler.removeFavorite(w.getId());
                //notifyDataSetChanged();
            }
        });


        word.setText(w.getWord());
        wordType.setText(w.getWordType());

        top_wrapper.setOnLongClickListener(new View.OnLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onLongClick(View vie) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Light_Dialog);
                } else {
                    builder = new AlertDialog.Builder(getContext());
                }

                String meaning = w.getMeaning();
                meaning = meaning.trim();
                meaning = meaning.replaceAll("\\d","");
                meaning = meaning.replaceAll("[^a-zA-Z]"," ");
                meaning = meaning.replaceAll("^ +| +$|( )+", " ");
                meaning = meaning.replace("\n", "").replace("\r", "");

                builder.setTitle("" + word.getText().toString())
                        .setMessage("" + meaning)
                        .show();
                return false;
            }

        });




        top_wrapper.setOnTouchListener(new View.OnTouchListener() {

            private GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    if(w.getDeletable()){
                        if(w.getType() == 0){
                            dbHandler.removeHistory(w.getId());
                            Toast.makeText(getContext(),"Removed " + w.getWord() + " from history :p",Toast.LENGTH_SHORT).show();
                        }else if(w.getType() == 3){
                            dbHandler.removeWOD(w.getId());
                            Toast.makeText(getContext(),"Removed " + w.getWord() + " from WOD :-(",Toast.LENGTH_SHORT).show();
                        }

                        notifyDataSetChanged();
                    }else{
                        Toast.makeText(getContext(),"Delete is not enabled here :-)",Toast.LENGTH_SHORT).show();
                    }
                    return super.onDoubleTap(e);
                }

                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {

                    res = context.getResources();
                    prefs = context.getSharedPreferences("f1nd.initial.bharath.newUI", Context.MODE_PRIVATE);
                    prefs.edit().putString("id", "" + w.getId()).commit();
                    prefs.edit().putString("meaningSearchWord", "" + w.getWord()).commit();

                    android.support.v4.app.FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    meaning fragment = new meaning();
                    fragmentTransaction.replace(R.id.content, fragment);
                    fragmentTransaction.commit();
                    return super.onSingleTapConfirmed(e);
                }
            });

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gestureDetector.onTouchEvent(motionEvent);
                return false;
            }
        });


        return view;
    }



}
