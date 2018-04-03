package v1.f1nd.com.f1nd_newfeatures;

import android.util.Log;

/**
 * Created by bharath on 30/3/18.
 */

public class Word {

    private String word,wordType,meaning;
    private long id;
    boolean liked = false;
    boolean deletable = false;
    int type;

    public Word(long id,String word,String wordType,String meaning,boolean isLiked,boolean deletable,int type){

        this.id=id;
        this.word=word;
        this.wordType=wordType;
        this.meaning=meaning;
        this.liked = isLiked;
        this.deletable = deletable;
        this.type = type;
        Log.d("F1nd_Word","WORD " + id + " " + word + " " + wordType + " " + meaning + " " + isLiked + " " + deletable + " " + type + "\n");
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
    public boolean getDeletable(){
        return deletable;
    }
    public int getType(){
        return type;
    }


}
