package v1.f1nd.com.f1nd_newfeatures;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bharath on 19/3/18.
 */

public class dbHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "dict.db";
    public static final String WORD = "word";
    public static final String MEANING = "meaning";
    public static final String TYPE = "wordtype";
    private SQLiteDatabase database;

    private  String DB_PATH = "";

    private  String DB_NAME = "dict";

    private final Context myContext;


    public dbHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.myContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onCreate(sqLiteDatabase);
    }

    public JSONArray getMeaning(String word) throws JSONException {
        JSONArray rArr = new JSONArray();
        JSONObject rObj = new JSONObject();
        String meaning = "";
        String type = "";
        Log.d("F1nd : dbHandler","Inside getMeaning dbHandler");
        DB_PATH = myContext.getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath();
        String myPath = DB_PATH + DB_NAME;
        Log.d("F1nd : dbHandler", "" + myPath);
        database = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        String query = "SELECT * FROM dict where UPPER(word)='" + word + "'";
        Cursor cursor = database.rawQuery(query,null);
        Log.i("F1nd : dbHandler", "" + cursor.toString());
        if(cursor.getCount() != 0){
            Log.d("F1nd : Count","" + cursor.getCount() + ", Col count : " + cursor.getColumnCount());
            while(cursor.moveToNext()){
                meaning = cursor.getString(cursor.getColumnIndex(MEANING));
                type = cursor.getString(cursor.getColumnIndex(TYPE));
                rObj = new JSONObject();
                rObj.put(MEANING,meaning);
                rObj.put(TYPE,type);
                rArr.put(rObj);
            }
            cursor.close();
        }
        Log.d("F1nd : dbHandler","" + rArr);
        database.close();
        return null;
    }

}
