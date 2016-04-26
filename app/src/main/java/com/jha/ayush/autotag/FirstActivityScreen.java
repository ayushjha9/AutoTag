package com.jha.ayush.autotag;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;

import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.clarifai.api.ClarifaiClient;
import com.clarifai.api.RecognitionRequest;
import com.clarifai.api.RecognitionResult;
import com.clarifai.api.Tag;
import com.clarifai.api.exception.ClarifaiException;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FirstActivityScreen extends AppCompatActivity {

    private static final boolean AUTO_HIDE = true;
    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private int PICK_IMAGE_REQUEST = 1;
    private View mContentView;
    static int card_height = 0;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
//            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
//                    | View.SYSTEM_UI_FLAG_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private Context mContext;
    private List<ResultCard> resultCardList = new ArrayList<>();
    RVAdapter adapter = new RVAdapter(resultCardList, this);
    RecyclerView rv;


    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */

    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    private static final String TAG = ImageTags.class.getSimpleName();


    /* Whether or not the system UI should be auto-hidden after
         * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
         */
    public void openGallery(View view) {
        if(!isOnline()){
            Toast.makeText(getApplicationContext(), "No Internet Conection", Toast.LENGTH_LONG).show();
            return;
        }
        //open gallery in response to clicking button
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            new ImageTags().execute(uri);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        card_height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,150, getResources().getDisplayMetrics());

        setContentView(R.layout.recyclerview_activity);
        //set up a "list" for the cars to be added
        rv = (RecyclerView)findViewById(R.id.rv);
        //sets the recycle view to a list view
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);

        //setContentView(R.layout.activity_first_activity_screen);

        mContext = getApplicationContext();

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark)));

        mVisible = true;
//        mControlsView = findViewById(R.id.fullscreen_content_controls);
//        mContentView = findViewById(R.id.fullscreen_content_controls);

        //mRelativeLayout = (RelativeLayout) findViewById(R.id.rv);

        // Set up the user interaction to manually show or hide the system UI.
//        mContentView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                toggle();
//            }
//        });



        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.fab).setOnTouchListener(mDelayHideTouchListener);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        menu.add(0, v.getId(), 0, "Copy");

        //cast the received View to TextView so that you can get its text
        TextView yourTextView = (TextView) v;

        //place your TextView's text in the clipboard
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboard.setText(yourTextView.getText());
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.hide();
//        }
//        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
//        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "FirstActivityScreen Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.jha.ayush.autotag/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "FirstActivityScreen Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.jha.ayush.autotag/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    public boolean isOnline() {

        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }

    private class ImageTags extends AsyncTask<Uri, Void, ArrayList<String>> {


        private Context context;
        private Spinner spinDisplay;
        Bitmap bm;
        Uri uri;
        //private View rootView;


        String APP_ID = "nUkuc0I4q608b_9P0swUOgipJGsqjuoLj2ndk_NA";
        String APP_SECRET = "adURL-SDWVhtEo5wtwA6Oci0yRhOEtevOGQsKWX4";
        ClarifaiClient clarifai = new ClarifaiClient(APP_ID, APP_SECRET);
        ArrayList<String> clarifaiResults = new ArrayList<>();

        @Override
        protected ArrayList<String> doInBackground(Uri... uris) {

            for(Uri uri: uris) {
                try {
                    bm = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    RecognitionResult results = recognizeBitmap(bm);
                    int i = 0;
                    for (Tag tag : results.getTags()) {
                        clarifaiResults.add("#"+toCamelCase(tag.getName())+ " ");
                        System.out.println(tag.toString());
                    }
                    this.uri = uri;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return clarifaiResults;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);
            ResultCard card = new ResultCard(strings, bm, uri);
            resultCardList.add(card);
            rv.setAdapter(adapter);
        }

        private RecognitionResult recognizeBitmap(Bitmap bitmap) {
            try {
                // Scale down the image. This step is optional. However, sending large images over the
                // network is slow and  does not significantly improve recognition performance.
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 320,
                        320 * bitmap.getHeight() / bitmap.getWidth(), true);

                // Compress the image as a JPEG.
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                scaled.compress(Bitmap.CompressFormat.JPEG, 90, out);
                byte[] jpeg = out.toByteArray();

                // Send the JPEG to Clarifai and return the result.
                return clarifai.recognize(new RecognitionRequest(jpeg)).get(0);
            } catch (ClarifaiException e) {
                Log.e(TAG, "Clarifai error", e);
                return null;
            }
        }

        private String toCamelCase(String normalCase){
            normalCase = normalCase.toLowerCase();
            char cArr[] = normalCase.toCharArray();
            cArr[0] -= 32;
            for(int i=0; i<cArr.length; i++){
                if(cArr[i] == ' ' && i+1<cArr.length){
                    cArr[i+1] -= 32;
                }
            }

            String camelCase = new String(cArr);
            camelCase = camelCase.replaceAll(" ", "");

            return camelCase;
        }
    }




}
