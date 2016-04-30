package com.jha.ayush.autotag;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.support.v7.widget.AppCompatImageButton;
import android.widget.ProgressBar;

import java.util.ArrayList;

/**
 * Created by nick on 4/20/2016.
 */
public class ResultCard extends Activity{
    String hashTags;
    Bitmap bitmap;
    Uri uri;
    ProgressBar spinner;

    ResultCard(ArrayList<String> hashTags, Bitmap bitmap, Uri uri){
        ArrayList<String> temp = new ArrayList<>(hashTags);
        StringBuilder sb = new StringBuilder();
        for(String item : temp) {
            sb.append(item);
        }
        this.hashTags = sb.toString();
        this.bitmap = ThumbnailUtils.extractThumbnail(bitmap,FirstActivityScreen.card_height,FirstActivityScreen.card_height);
        this.uri = uri;
    }

    ResultCard(){

    }

    public void setConents(ArrayList<String> hashTags, Bitmap bitmap, Uri uri){
        ArrayList<String> temp = new ArrayList<>(hashTags);
        StringBuilder sb = new StringBuilder();
        for(String item : temp) {
            sb.append(item);
        }
        this.hashTags = sb.toString();
        this.bitmap = ThumbnailUtils.extractThumbnail(bitmap,FirstActivityScreen.card_height,FirstActivityScreen.card_height);
        this.uri = uri;
    }

}
