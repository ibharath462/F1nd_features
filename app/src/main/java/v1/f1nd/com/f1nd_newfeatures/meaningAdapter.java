package v1.f1nd.com.f1nd_newfeatures;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bharath on 6/4/18.
 */

public class meaningAdapter extends ArrayAdapter{

    private Context context;
    private List<meaningBean> menaingDetails;
    databaseHandler dbHandler;

    public meaningAdapter(Context context, int resource,ArrayList<meaningBean> objects) {
        super(context, resource,objects);
        this.context = context;
        this.menaingDetails = objects;

    }

    public View getView(final int position, View convertView, final ViewGroup parent){

        final meaningBean m = menaingDetails.get(position);

        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.single_meaning_view, null);

        dbHandler = new databaseHandler(getContext());


        final EditText meaning=(EditText)view.findViewById(R.id.meaning);
        TextView wordType = (TextView)view.findViewById(R.id.wordtype);
        LikeButton favorite= (LikeButton)view.findViewById(R.id.favorite);


        meaning.setText(m.getMeaning());
        wordType.setText(m.getWordType());
        favorite.setLiked(m.getLiked());

        favorite.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                dbHandler.addFavorite(m.getId());
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                dbHandler.removeFavorite(m.getId());
                //notifyDataSetChanged();
            }
        });

        return view;

    }
}
