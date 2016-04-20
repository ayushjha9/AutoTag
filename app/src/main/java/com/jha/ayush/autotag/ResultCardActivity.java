package com.jha.ayush.autotag;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by nick on 4/20/2016.
 */
public class ResultCardActivity extends Activity{
    TextView hashTags;
    ImageView bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.item);
        hashTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cm = (ClipboardManager)getBaseContext().getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(hashTags.getText());
                Toast.makeText(getBaseContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });
        bitmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cm = (ClipboardManager)getBaseContext().getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(hashTags.getText());
                Toast.makeText(getBaseContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });


        hashTags = (TextView) findViewById(R.id.TagOutPut);
        hashTags.setMovementMethod(new ScrollingMovementMethod());
        bitmap = (ImageView) findViewById(R.id.imageView);


    }


}
