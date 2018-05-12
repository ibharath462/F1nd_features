package v1.f1nd.com.f1nd_newfeatures;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import ru.dimorinny.showcasecard.ShowCaseView;
import ru.dimorinny.showcasecard.position.ViewPosition;
import ru.dimorinny.showcasecard.radius.Radius;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link settings.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link settings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class settings extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button startService;
    boolean isServiceRuning = false;
    EditText timeInterval;


    static Resources res;
    SharedPreferences prefs = null;

    Switch eCopy,eLaW;

    private OnFragmentInteractionListener mListener;

    public settings() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment settings.
     */
    // TODO: Rename and change types and number of parameters
    public static settings newInstance(String param1, String param2) {
        settings fragment = new settings();
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
        return inflater.inflate(R.layout.fragment_settings, container, false);
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

        res = getResources();
        prefs = getContext().getSharedPreferences("f1nd.initial.bharath.newUI", Context.MODE_PRIVATE);

        if(prefs.getBoolean("settingsFirstRun", true)){

            new ShowCaseView.Builder(getContext())
                    .withTypedPosition(new ViewPosition(getActivity().findViewById(R.id.startService)))
                    .withTypedRadius(new Radius(186F))
                    .withContent("Once you have choose F1nd services and set time interval, press here.")
                    .build()
                    .show(this);
            prefs.edit().putBoolean("settingsFirstRun", false).commit();

        }


        startService = (Button)getView().findViewById(R.id.startService);

        //saveSettings = (Button)getView().findViewById(R.id.saveSettings);

        timeInterval = (EditText)getView().findViewById(R.id.wodInterval);

        eCopy = (Switch)getView().findViewById(R.id.eCopy);

        eLaW = (Switch)getView().findViewById(R.id.eLaW);


        isServiceRuning = isMyServiceRunning(bgService.class) || isMyServiceRunning(clipboardService.class);


        eCopy.setChecked(prefs.getBoolean("isCopyListener",false));
        eLaW.setChecked(prefs.getBoolean("isLaWEnabled",false));

        Long wodInterval = Long.parseLong(prefs.getString("wodInterval", "5"));


        Log.d("F1nd_Settings ","Inside settings activity");

        timeInterval.setText("" + wodInterval);

        if(isServiceRuning){
            startService.setBackgroundColor(Color.RED);
            startService.setText("Stop Service");
        }else{
            startService.setBackgroundColor(Color.GREEN);
            startService.setText("Start Service");
        }

//        saveSettings.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("F1nd_Settings ","Settings saved");
//                Toast.makeText(getContext(),"Settings saved :-)",Toast.LENGTH_SHORT).show();
//                prefs.edit().putString("wodInterval", "" + timeInterval.getText().toString()).commit();
//            }
//        });

        eCopy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                prefs.edit().putBoolean("isCopyListener", b).commit();
            }
        });

        eLaW.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                prefs.edit().putBoolean("isLaWEnabled", b).commit();
            }
        });

        startService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent service = new Intent(getActivity(),bgService.class);
                Intent clipBoardService = new Intent(getActivity(),clipboardService.class);

                if(!isServiceRuning){
                    if(null != timeInterval.getText().toString() && !timeInterval.getText().toString().equals("")){
                        prefs.edit().putString("wodInterval", "" + timeInterval.getText().toString()).commit();
                    }else{
                        prefs.edit().putString("wodInterval", "" + 15).commit();
                    }
                    boolean finishFlag = true;
                    boolean noneFlag = true;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        if(prefs.getBoolean("isLaWEnabled",false)){
                            getContext().startForegroundService(service);
                            noneFlag = false;
                        }
                        if(prefs.getBoolean("isCopyListener",false)){
                            getContext().startForegroundService(clipBoardService);
                            finishFlag = false;
                            noneFlag = false;
                        }

                    } else {
                        if(prefs.getBoolean("isLaWEnabled",false)){
                            getActivity().startService(new Intent(getActivity(),bgService.class));
                            noneFlag = false;
                        }
                        if(prefs.getBoolean("isCopyListener",false)){
                            getActivity().startService(new Intent(getActivity(),clipboardService.class));
                            finishFlag = false;
                            noneFlag = false;
                        }
                    }
                    if(!noneFlag){
                        startService.setText("Stop Service");
                        startService.setBackgroundColor(Color.RED);
                        isServiceRuning = true;
                        Log.d("F1nd_Settings ","Started service");
                    }else{
                        Toast.makeText(getContext(),"Enable any one service...",Toast.LENGTH_SHORT).show();
                    }
                    if(!finishFlag){
                        getActivity().finish();
                    }
                }else{
                    getContext().stopService(service);

                    if(isMyServiceRunning(clipboardService.class)){
                        getContext().stopService(clipBoardService);
                    }
                    getContext().stopService(clipBoardService);

                    wodReceiver stopWOD = new wodReceiver();
                    stopWOD.cancelAlarm(getContext());
                    NotificationManager mNotificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.cancelAll();
                    startService.setText("Start Service");
                    isServiceRuning = false;
                    startService.setBackgroundColor(Color.GREEN);
                    Log.d("F1nd_Settings ","Service stopped");
                }

            }
        });
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
