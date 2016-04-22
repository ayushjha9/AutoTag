package com.jha.ayush.autotag;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.support.v7.widget.AppCompatImageButton;

import java.util.ArrayList;

/**
 * Created by nick on 4/20/2016.
 */
public class ResultCard extends Activity{
    String hashTags;
    Bitmap bitmap;
    AppCompatImageButton copyButton;

    ResultCard(ArrayList<String> hashTags, Bitmap bitmap){
        ArrayList<String> temp = new ArrayList<>(hashTags);
        StringBuilder sb = new StringBuilder();
        for(String item : temp) {
            sb.append(item);
        }
        this.hashTags = sb.toString();
        this.bitmap = ThumbnailUtils.extractThumbnail(bitmap,FirstActivityScreen.card_height,FirstActivityScreen.card_height);
//        this.setContentView(R.layout.item);
//        copyButton = (AppCompatImageButton) findViewById(R.id.copyButton);
    }

}
