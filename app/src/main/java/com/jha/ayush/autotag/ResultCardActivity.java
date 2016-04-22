package com.jha.ayush.autotag;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
}
