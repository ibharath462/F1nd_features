package v1.f1nd.com.f1nd_newfeatures;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link favorites.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link favorites#newInstance} factory method to
 * create an instance of this fragment.
 */
public class favorites extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    databaseHandler dbHandler;
    ArrayAdapter<Word> adapter=null;
    ArrayList<Word> words;
    ListView favorite_listView;

    View swipeView = null;

    private OnFragmentInteractionListener mListener;

    public favorites() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment favorites.
     */
    // TODO: Rename and change types and number of parameters
    public static favorites newInstance(String param1, String param2) {
        favorites fragment = new favorites();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        swipeView = inflater.inflate(R.layout.word_view,container,false);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




        dbHandler = new databaseHandler(getContext());
        favorite_listView = (ListView)getView().findViewById(R.id.favorite_listView);
        JSONArray favoriteArray = new JSONArray();
        favoriteArray = dbHandler.getFavorites();
        Log.d("F1nd_MainActivity","Adapter count.." + favoriteArray.length() + "\n");
        words = new ArrayList<>();

        for(int i=0; i<favoriteArray.length();i++){
            try {
                JSONObject tWord = favoriteArray.getJSONObject(i);
                Log.d("F1nd_FAV_Fragment ","parsed " + tWord.toString() + "\n");
                words.add(new Word(tWord.getLong("id"),tWord.getString("word"),tWord.getString("wordtype"),tWord.getString("meaning"),true,false,2));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        adapter = new wordAdapter(getContext(), 0, words);
        favorite_listView.setAdapter(adapter);






    }

    @Override
    public void onResume() {
        super.onResume();
        //mBlurEngine.onResume(getRetainInstance());
    }




}
