package v1.f1nd.com.f1nd_newfeatures;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bharath on 29/3/18.
 */

public class databaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "dict.db";
    public static final String WORD = "word";
    public static final String MEANING = "meaning";
    public static final String WORDTYPE = "wordtype";
    public static final String ID = "id";
    public static final String FID = "fId";

    private SQLiteDatabase database;

    private String DB_PATH = "";

    private String DB_NAME = "dict";

    private final Context myContext;


    public databaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.database = database;
        this.myContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onCreate(sqLiteDatabase);
    }

    public JSONArray getFavorites() {
        JSONArray resultArray = new JSONArray();
        Log.d("F1nd_DB: ", "Inside getFavorites");
        DB_PATH = myContext.getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath();
        String myPath = DB_PATH + DB_NAME;
        Log.d("F1nd_DB: ", "path " + myPath);
        try{

            database = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
            String selectQuery = "select dict.id,dict.word,dict.wordtype,dict.meaning from dict left join favorites_mapping on favorites_mapping.id = dict.id where favorites_mapping.id = dict.id ;";
            Cursor cursor = database.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    JSONObject tWord = new JSONObject();
                    try {
                        tWord.put("word", cursor.getString(cursor.getColumnIndex(WORD)));
                        tWord.put("meaning", cursor.getString(cursor.getColumnIndex(MEANING)));
                        tWord.put("wordtype", cursor.getString(cursor.getColumnIndex(WORDTYPE)));
                        tWord.put("id", cursor.getString(cursor.getColumnIndex(ID)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    resultArray.put(tWord);
                } while (cursor.moveToNext());
            }
            database.close();
            Log.d("F1nd_DB: ", "result array of getFavorites " + resultArray.toString());

        }catch (Exception e){
            Log.e("F1nd_Exception","" + e);
        }
        return resultArray;
    }

    public JSONArray searchWord(String q) {
        JSONArray resultArray = new JSONArray();
        List<String> addedWords = new ArrayList<>();
        Log.d("F1nd_DB: ", "Inside searchTerm " + q);
        DB_PATH = myContext.getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath();
        String myPath = DB_PATH + DB_NAME;
        Log.d("F1nd_DB: ", "path " + myPath);
        database = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        String selectQuery = "SELECT  dict.id,dict.word,dict.meaning,dict.wordtype,favorites_mapping.fId from dict left join favorites_mapping on favorites_mapping.id = dict.id WHERE UPPER(" + WORD + ") LIKE '" + q.toUpperCase() + "%' limit 20;";
        try{

            Cursor cursor = database.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    JSONObject tWord = new JSONObject();
                    try {
                        if(!addedWords.contains(cursor.getString(cursor.getColumnIndex(WORD)))){
                            tWord.put("word", cursor.getString(cursor.getColumnIndex(WORD)));
                            tWord.put("meaning", cursor.getString(cursor.getColumnIndex(MEANING)));
                            tWord.put("wordtype", cursor.getString(cursor.getColumnIndex(WORDTYPE)));
                            tWord.put("id", cursor.getString(cursor.getColumnIndex(ID)));
                            if (cursor.getString(cursor.getColumnIndex(FID)) != null) {
                                tWord.put("fid", cursor.getString(cursor.getColumnIndex(FID)));
                            }
                            addedWords.add(cursor.getString(cursor.getColumnIndex(WORD)));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    resultArray.put(tWord);
                } while (cursor.moveToNext());
            }
            database.close();
            Log.d("F1nd_DB: ", "result array of search word " + resultArray.toString());

        }catch (Exception e){
            Log.e("F1nd_Exception","" + e);
        }
        return resultArray;
    }

    public void addFavorite(Long id) {
        DB_PATH = myContext.getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath();
        String myPath = DB_PATH + DB_NAME;
        database = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        ContentValues cv = new ContentValues();
        cv.put("id", id);
        database.insert("favorites_mapping", null, cv);
        Log.d("F1nd_DB:", "Favorites added succesfull " + id);
        database.close();
    }


    public void removeFavorite(Long id) {
        DB_PATH = myContext.getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath();
        String myPath = DB_PATH + DB_NAME;
        database = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        database.delete("favorites_mapping", "id=" + id, null);
        database.close();
    }

    public void addHistory(Long id) {
        DB_PATH = myContext.getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath();
        String myPath = DB_PATH + DB_NAME;
        database = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

        //Check if there is already an entry..
        String selectQuery = "select * from history_mapping where id=" + id;
        try{

            Cursor cursor = database.rawQuery(selectQuery, null);
            if (!cursor.moveToFirst()) {
                ContentValues cv = new ContentValues();
                cv.put("id", id);
                cv.put("time", System.currentTimeMillis());
                database.insert("history_mapping", null, cv);
                Log.d("F1nd_DB:", "History added succesfull " + id);
            }

        }catch (Exception e){
            Log.e("F1nd_Exception","" + e);
        }
        database.close();
    }

    public JSONArray getHistory() {
        DB_PATH = myContext.getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath();
        String myPath = DB_PATH + DB_NAME;
        database = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        JSONArray resultArray = new JSONArray();
        String selectQuery = "select dict.id,dict.word,dict.wordtype,dict.meaning,favorites_mapping.fId from dict left join history_mapping on history_mapping.id = dict.id left join favorites_mapping on favorites_mapping.id = dict.id where history_mapping.id = dict.id order by history_mapping.time desc;";
        try{
            Cursor cursor = database.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    JSONObject tWord = new JSONObject();
                    try {
                        tWord.put("word", cursor.getString(cursor.getColumnIndex(WORD)));
                        tWord.put("meaning", cursor.getString(cursor.getColumnIndex(MEANING)));
                        tWord.put("wordtype", cursor.getString(cursor.getColumnIndex(WORDTYPE)));
                        tWord.put("id", cursor.getString(cursor.getColumnIndex(ID)));
                        if (cursor.getString(cursor.getColumnIndex(FID)) != null) {
                            tWord.put("fid", cursor.getString(cursor.getColumnIndex(FID)));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    resultArray.put(tWord);
                } while (cursor.moveToNext());
            }
            database.close();
            Log.d("F1nd_DB: ", "result array of history" + resultArray.toString());
        }catch (Exception e){
            Log.e("F1nd_Exception","" + e);
        }
        return resultArray;
    }

    public void removeHistory(Long id) {
        DB_PATH = myContext.getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath();
        String myPath = DB_PATH + DB_NAME;
        database = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        database.delete("history_mapping", "id=" + id, null);
        database.close();
    }

    public void removeWOD(Long id) {
        DB_PATH = myContext.getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath();
        String myPath = DB_PATH + DB_NAME;
        database = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        database.delete("wod_mapping", "id=" + id, null);
        database.close();
    }

    public JSONObject getWordOfTheDay() {
        JSONObject wod = new JSONObject();
        String wodId = "";
        DB_PATH = myContext.getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath();
        String myPath = DB_PATH + DB_NAME;
        database = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        String selectQuery = "SELECT * FROM dict ORDER BY RANDOM() LIMIT 1;";
        try{

            Cursor cursor = database.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    try {
                        wod.put("word", cursor.getString(cursor.getColumnIndex(WORD)));
                        wod.put("meaning", cursor.getString(cursor.getColumnIndex(MEANING)));
                        wod.put("wordtype", cursor.getString(cursor.getColumnIndex(WORDTYPE)));
                        wodId = cursor.getString(cursor.getColumnIndex(ID));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } while (cursor.moveToNext());
            }

            //Adding details in the WOD table...
            ContentValues cv = new ContentValues();
            cv.put("id", wodId);
            cv.put("time", System.currentTimeMillis());
            database.insert("wod_mapping", null, cv);
            Log.d("F1nd_DB:", "WOD added succesfull " + wodId);
            database.close();

        }catch (Exception e){
            Log.e("F1nd_Exception","" + e);
        }

        return wod;
    }

    public JSONArray getWordsOfDays() {
        JSONArray resultArray = new JSONArray();
        Log.d("F1nd_DB: ", "Inside getWordsOfDays");
        DB_PATH = myContext.getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath();
        String myPath = DB_PATH + DB_NAME;
        Log.d("F1nd_DB: ", "path " + myPath);
        database = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        String selectQuery = "select dict.id,dict.word,dict.wordtype,dict.meaning,favorites_mapping.fId from dict left join wod_mapping on wod_mapping.id = dict.id left join favorites_mapping on favorites_mapping.id = dict.id where wod_mapping.id = dict.id order by wod_mapping.time desc;";
        try{
            Cursor cursor = database.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    JSONObject tWord = new JSONObject();
                    try {
                        tWord.put("word", cursor.getString(cursor.getColumnIndex(WORD)));
                        tWord.put("meaning", cursor.getString(cursor.getColumnIndex(MEANING)));
                        tWord.put("wordtype", cursor.getString(cursor.getColumnIndex(WORDTYPE)));
                        tWord.put("id", cursor.getString(cursor.getColumnIndex(ID)));
                        if (cursor.getString(cursor.getColumnIndex(FID)) != null) {
                            tWord.put("fid", cursor.getString(cursor.getColumnIndex(FID)));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    resultArray.put(tWord);
                } while (cursor.moveToNext());
            }
            database.close();
        }catch (Exception e){
            Log.e("F1nd_Exception","" + e);
        }
        Log.d("F1nd_DB: ", "result array of getWordsOfDays " + resultArray.toString());
        return resultArray;
    }

    public JSONArray getMeaning(String meaningSearchWord) {
        JSONArray resultArray = new JSONArray();
        Log.d("F1nd_DB: ", "Inside getMeaning " + meaningSearchWord);
        DB_PATH = myContext.getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath();
        String myPath = DB_PATH + DB_NAME;
        Log.d("F1nd_DB: ", "path " + myPath);
        database = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        String selectQuery = "SELECT  dict.id,dict.word,dict.meaning,dict.wordtype,favorites_mapping.fId from dict left join favorites_mapping on favorites_mapping.id = dict.id WHERE UPPER(" + WORD + ") = '" + meaningSearchWord.toUpperCase() + "' limit 10;";
        try {
            Cursor cursor = database.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    JSONObject tWord = new JSONObject();
                    try {
                        tWord.put("word", cursor.getString(cursor.getColumnIndex(WORD)));
                        tWord.put("id", cursor.getString(cursor.getColumnIndex(ID)));
                        tWord.put("meaning", cursor.getString(cursor.getColumnIndex(MEANING)));
                        tWord.put("wordtype", cursor.getString(cursor.getColumnIndex(WORDTYPE)));
                        if (cursor.getString(cursor.getColumnIndex(FID)) != null) {
                            tWord.put("fid", cursor.getString(cursor.getColumnIndex(FID)));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    resultArray.put(tWord);
                } while (cursor.moveToNext());
            }
            database.close();
            Log.d("F1nd_DB: ", "result array of get menaning of  word " + resultArray.toString());
        }catch (Exception e){
            Log.e("F1nd_Exception","" + e);
        }
        return resultArray;

    }

    public long getIdForWord(String word){
        long id = 462;
        Log.d("F1nd_DB: ", "Inside  getIdForWord" + word);
        DB_PATH = myContext.getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath();
        String myPath = DB_PATH + DB_NAME;
        Log.d("F1nd_DB: ", "path " + myPath);
        database = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        String selectQuery = "SELECT  dict.id,dict.word from dict WHERE UPPER(" + WORD + ") LIKE '" + word.toUpperCase() + "%' limit 1;";
        try{
            Cursor cursor = database.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)));
            }
            database.close();
            Log.d("F1nd_DB: ", "result array of search word " + id);
        }catch (Exception e){
            Log.e("F1nd_Exception","" + e);
        }
        return id;
    }

    public String getExample(String word){
        String example = "";
        Log.d("F1nd_DB: ", "Inside  getIdForWord" + word);
        DB_PATH = myContext.getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath();
        String myPath = DB_PATH + DB_NAME;
        Log.d("F1nd_DB: ", "path " + myPath);
        database = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        String selectQuery = "SELECT dict.usage from dict WHERE UPPER(" + WORD + ") LIKE '" + word.toUpperCase() + "';";
        try{
            Cursor cursor = database.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    if(!cursor.getString(cursor.getColumnIndex("usage")).equals("")){
                        example += cursor.getString(cursor.getColumnIndex("usage")) + "\n\n";
                    }
                }while (cursor.moveToNext());
            }
            database.close();
            Log.d("F1nd_DB: ", "Example....  " + example);
        }catch (Exception e){
            Log.e("F1nd_Exception","" + e);
        }
        return example;
    }
}
