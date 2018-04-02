package v1.f1nd.com.f1nd_newfeatures;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.PixelFormat;
import android.media.Image;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
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
                Toast.makeText(getContext(),"Long pressed " + word.getText().toString() + " " + w.getLiked(),Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Light_Dialog);
                } else {
                    builder = new AlertDialog.Builder(getContext());
                }

                builder.setTitle("" + word.getText().toString())
                        .setMessage("" + w.getMeaning())
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();
                return false;
            }

        });


        return view;
    }



}
