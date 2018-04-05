package v1.f1nd.com.f1nd_newfeatures;

import android.util.Log;

/**
 * Created by bharath on 6/4/18.
 */

public class meaningBean {

    private String word,wordType,meaning;
    private long id;
    boolean liked = false;


    public meaningBean(long id,String word,String wordType,String meaning,boolean isLiked){

        this.id=id;
        this.word=word;
        this.wordType=wordType;
        this.meaning=meaning;
        this.liked = isLiked;
        Log.d("F1nd_Word","menaingBean " + id + " " + word + " " + wordType + " " + meaning + " " + isLiked + "\n");
    }
    public String getWord(){
        return word;
    }
    public  String getWordType(){
        return wordType;
    }
    public String getMeaning(){
        return meaning;
    }
    public long getId(){
        return id;
    }
    public  boolean getLiked(){
        return liked;
    }
}
