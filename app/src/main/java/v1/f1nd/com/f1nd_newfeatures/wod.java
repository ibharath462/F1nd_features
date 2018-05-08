package v1.f1nd.com.f1nd_newfeatures;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link wod.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link wod#newInstance} factory method to
 * create an instance of this fragment.
 */
public class wod extends Fragment {
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
    ListView wod_listView;

    Fragment currentFragment = null;
    android.support.v4.app.FragmentTransaction ft;

    private OnFragmentInteractionListener mListener;

    public wod() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment wod.
     */
    // TODO: Rename and change types and number of parameters
    public static wod newInstance(String param1, String param2) {
        wod fragment = new wod();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wod, container, false);
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHandler = new databaseHandler(getContext());
        wod_listView = (ListView)getView().findViewById(R.id.wod_listView);
        JSONArray favoriteArray = new JSONArray();
        favoriteArray = dbHandler.getWordsOfDays();
        Log.d("F1nd_MainActivity","Adapter count.." + favoriteArray.length() + "\n");
        words = new ArrayList<>();

        if(favoriteArray.length() > 0){

            for(int i=0; i<favoriteArray.length();i++){
                try {
                    JSONObject tWord = favoriteArray.getJSONObject(i);
                    Log.d("F1nd_WOD_Fragment ","parsed " + tWord.toString() + "\n");
                    boolean isLiked = false;
                    if(tWord.has("fid")){
                        isLiked = true;
                    }
                    words.add(new Word(tWord.getLong("id"),tWord.getString("word"),tWord.getString("wordtype"),tWord.getString("meaning"),isLiked,true,3));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }else{
            Toast.makeText(getContext(),"No words in Learn A Word :-(",Toast.LENGTH_SHORT).show();
            words.add(new Word(242518,"Brb","iS","Be Right Back",false,false,0));
        }

        adapter = new wordAdapter(getContext(), 0, words);
        wod_listView.setAdapter(adapter);

    }
}
