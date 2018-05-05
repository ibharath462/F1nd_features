package v1.f1nd.com.f1nd_newfeatures;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.like.LikeButton;
import com.like.OnLikeListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by bharath on 6/4/18.
 */

public class meaningAdapter extends ArrayAdapter{

    private Context context;
    private List<meaningBean> menaingDetails;
    databaseHandler dbHandler;
    ImageButton shareButton;

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

        final Date now = new Date();
        final CardView cardView = (CardView)view.findViewById(R.id.shareCardView);
        shareButton = (ImageButton)view.findViewById(R.id.share);



        final EditText meaning=(EditText)view.findViewById(R.id.meaning);
        TextView wordType = (TextView)view.findViewById(R.id.wordtype);
        final TextView tips = (TextView)view.findViewById(R.id.tips);
        final LikeButton favorite= (LikeButton)view.findViewById(R.id.favorite);


        meaning.setText(m.getMeaning());
        wordType.setText(m.getWordType());
        favorite.setLiked(m.getLiked());

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/*");
                share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                share.putExtra(Intent.EXTRA_TEXT, "F1nd the word for this meaning only on F1nd app ;-)");
                cardView.setDrawingCacheEnabled(true);
                tips.setVisibility(View.VISIBLE);
                Bitmap bitmap = Bitmap.createBitmap(cardView.getDrawingCache());
                shareButton.setVisibility(View.VISIBLE);
                tips.setVisibility(View.INVISIBLE);
                cardView.setDrawingCacheEnabled(false);

                String mPath = Environment.getExternalStorageDirectory().getAbsolutePath().toString() + "/"  + "barbieee1.jpeg";
                File imageFile = new File(mPath);

                FileOutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(imageFile);
                    int quality = 100;
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
                    outputStream.flush();
                    outputStream.close();
                    Toast.makeText(getContext(),"Shared to " + mPath,Toast.LENGTH_SHORT).show();
                    share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(imageFile));
                    getContext().startActivity(share);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        favorite.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                dbHandler.addFavorite(m.getId());

            }

            @Override
            public void unLiked(LikeButton likeButton) {
                dbHandler.removeFavorite(m.getId());
            }
        });

        return view;

    }
}
