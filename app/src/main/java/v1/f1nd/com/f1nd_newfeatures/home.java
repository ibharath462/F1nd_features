package v1.f1nd.com.f1nd_newfeatures;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link home.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class home extends Fragment {
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
    EditText searchText;
    JSONArray searchArray;
    ListView search_listView;
    TextView wodWord,wodWordType;
    static Resources res;
    SharedPreferences prefs = null;

    private OnFragmentInteractionListener mListener;

    public home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment home.
     */
    // TODO: Rename and change types and number of parameters
    public static home newInstance(String param1, String param2) {
        home fragment = new home();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        return view;
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
        search_listView = (ListView)getView().findViewById(R.id.search_listView);
        searchText = (EditText) getView().findViewById(R.id.searchText);
        wodWord = (TextView)getView().findViewById(R.id.WODword);
        wodWordType = (TextView)getView().findViewById(R.id.WODwordtype);
        searchArray = new JSONArray();
        searchArray = dbHandler.getHistory();
        Log.d("F1nd_MainActivity","Adapter count.." + searchArray.length() + "\n");
        words = new ArrayList<>();

        res = getResources();
        prefs = getContext().getSharedPreferences("f1nd.initial.bharath.newUI", Context.MODE_PRIVATE);
        if(prefs.getBoolean("firstrun", true)){
            wodWord.setText("Brb");
            wodWordType.setText("is");
        }else{
            wodWord.setText("" + prefs.getString("wodWord","Brb").toString());
            wodWordType.setText("" + prefs.getString("wodWordType","is").toString());
        }

        for(int i=0; i<searchArray.length();i++){
            try {
                JSONObject tWord = searchArray.getJSONObject(i);
                Log.d("F1nd_MainActivity","parsed " + tWord.toString() + "\n");
                boolean isLiked = false;
                if(tWord.has("fid")){
                    isLiked = true;
                }
                words.add(new Word(tWord.getLong("id"),tWord.getString("word"),tWord.getString("wordtype"),tWord.getString("meaning"),isLiked));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String searchTerm = searchText.getText().toString();
                searchAndAssign(searchTerm);
            }
        });

        adapter = new wordAdapter(getContext(), 0, words);
        search_listView.setAdapter(adapter);
    }

    public void searchAndAssign(String searchTerm){
        searchArray = dbHandler.searchWord(searchTerm);
        words = new ArrayList<>();

        for(int i=0; i<searchArray.length();i++){
            try {
                JSONObject tWord = searchArray.getJSONObject(i);
                boolean isLiked = false;
                if(tWord.has("fid")){
                    isLiked = true;
                }
                Log.d("F1nd_MainActivity","parsed " + tWord.toString() + " " + isLiked + "\n");
                words.add(new Word(tWord.getLong("id"),tWord.getString("word"),tWord.getString("wordtype"),tWord.getString("meaning"),isLiked));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapter = new wordAdapter(getContext(), 0, words);
        adapter.notifyDataSetChanged();
        search_listView.setAdapter(adapter);
    }
}
