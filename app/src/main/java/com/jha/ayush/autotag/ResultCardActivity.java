package com.jha.ayush.autotag;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.AppCompatImageButton;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

/**
 * Created by nick on 4/20/2016.
 */
public class ResultCardActivity extends Activity{
    TextView hashTags;
    ImageView bitmap;
    public AppCompatImageButton copyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.item);

        hashTags = (TextView) findViewById(R.id.TagOutPut);
        hashTags.setMovementMethod(new ScrollingMovementMethod());
        bitmap = (ImageView) findViewById(R.id.imageView);
        copyButton = (AppCompatImageButton) findViewById(R.id.copyButton);
        String type = "image/*";
        String filename = "/myPhoto.jpg";
        String mediaPath = Environment.getExternalStorageDirectory() + filename;

        createInstagramIntent(type, mediaPath);
        System.out.println("instagramIntent");


        //copyButton.setClickable(true);
        copyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clipboard.setText(hashTags.getText());
                Toast.makeText(getApplicationContext(), "Copied to clipboard", Toast.LENGTH_LONG).show();
                System.out.println("copied to Clipboard");
            }
        });
    }


    private void createInstagramIntent(String type, String mediaPath){

        // Create the new Intent using the 'Send' action.
        Intent share = new Intent(Intent.ACTION_SEND);

        // Set the MIME type
        share.setType(type);

        // Create the URI from the media
        File media = new File(mediaPath);
        Uri uri = Uri.fromFile(media);

        // Add the URI to the Intent.
        share.putExtra(Intent.EXTRA_STREAM, uri);

        // Broadcast the Intent.
        startActivity(Intent.createChooser(share, "Share to"));
    }
}
