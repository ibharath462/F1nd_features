package v1.f1nd.com.f1nd_newfeatures;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link meaning.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link meaning#newInstance} factory method to
 * create an instance of this fragment.
 */
public class meaning extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    static Resources res;
    SharedPreferences prefs = null;
    databaseHandler dbHandler;

    private OnFragmentInteractionListener mListener;

    public meaning() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment meaning.
     */
    // TODO: Rename and change types and number of parameters
    public static meaning newInstance(String param1, String param2) {
        meaning fragment = new meaning();
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
        return inflater.inflate(R.layout.fragment_meaning, container, false);
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

        res = getContext().getResources();
        prefs = getContext().getSharedPreferences("f1nd.initial.bharath.newUI", Context.MODE_PRIVATE);
        dbHandler = new databaseHandler(getContext());

        Long id = Long.parseLong(prefs.getString("id","1").toString());
        dbHandler.addHistory(id);

        //Setting the word & meaning...
        TextView word = (TextView)getView().findViewById(R.id.word);
        TextView meaning = (TextView)getView().findViewById(R.id.meaning);
        TextView wordtype = (TextView)getView().findViewById(R.id.wordType);

        JSONArray resM = dbHandler.getMeaning(id);

        try {
            word.setText("" + resM.getJSONObject(0).getString("word"));
            wordtype.setText("" + resM.getJSONObject(0).getString("wordtype"));
            for(int i=0; i<resM.length(); i++){
                String meaningString = resM.getJSONObject(i).getString("meaning");
                meaningString = meaningString.replaceAll("^ +| +$|( )+", " ");
                meaningString = meaningString.replace("\n", "").replace("\r", "");
                meaning.append(meaningString);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("F1nd_meaning","Added to history  " + String.valueOf(id) + "\n");
    }
}
