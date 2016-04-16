//package com.jha.ayush.autotag;
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.util.Log;
//import android.widget.ArrayAdapter;
//import android.widget.Spinner;
//import android.view.*;
//import android.widget.TextView;
//
//import com.clarifai.api.ClarifaiClient;
//import com.clarifai.api.RecognitionRequest;
//import com.clarifai.api.RecognitionResult;
//import com.clarifai.api.Tag;
//import com.clarifai.api.exception.ClarifaiException;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.StringTokenizer;
//
///**
// * Created by nick on 4/16/2016.
// */
////TODO replace with Android Priority Queue
//public class ImageTags extends AsyncTask<Bitmap, Void, ArrayList<String>> {
//
//    private static final String TAG = ImageTags.class.getSimpleName();
//    private Context context;
//    private Spinner spinDisplay;
//    //private View rootView;
//
//
//    String APP_ID = "nUkuc0I4q608b_9P0swUOgipJGsqjuoLj2ndk_NA";
//    String APP_SECRET = "adURL-SDWVhtEo5wtwA6Oci0yRhOEtevOGQsKWX4";
//    ClarifaiClient clarifai = new ClarifaiClient(APP_ID, APP_SECRET);
//    ArrayList<String> clarifaiResults = new ArrayList<>();
//
////    public ImageTags(Context context){
////        this.context = context;
////        //this.rootView = rootView;
////    }
////
//    public ImageTags(ArrayList<String> imageResults){
////        this.spinDisplay = spinDisplay;
////        this.context = context;
//        clarifaiResults = imageResults;
//    }
//
//
//    @Override
//    protected ArrayList<String> doInBackground(Bitmap... images) {
//        System.out.println("Doin some background work, youknowwatimsayin");
//        System.out.println("DEEZ NUTS OBAMACARE 420 QUICKSCOPE BLAZEIT ..ERR");
//
//        for(Bitmap image: images) {
//            RecognitionResult results = recognizeBitmap(image);
//            for (Tag tag : results.getTags()) {
//                clarifaiResults.add(tag.toString());
//                System.out.println(tag.toString());
////                if (tag.getName().equals("portrait")){
////                    System.out.println("Selfie"+ ": " + tag.getProbability());
////                }
////                else {
////                    System.out.println(tag.getName() + ": " + tag.getProbability());
////                }
//            }
//        }
//        return clarifaiResults;
//    }
//
//    @Override
//    protected void onPostExecute(ArrayList<String> strings) {
//        super.onPostExecute(strings);
//       // context.updateSpinner(strings);
//
//    }
//
//    private RecognitionResult recognizeBitmap(Bitmap bitmap) {
//        try {
//            // Scale down the image. This step is optional. However, sending large images over the
//            // network is slow and  does not significantly improve recognition performance.
//            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 320,
//                    320 * bitmap.getHeight() / bitmap.getWidth(), true);
//
//            // Compress the image as a JPEG.
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            scaled.compress(Bitmap.CompressFormat.JPEG, 90, out);
//            byte[] jpeg = out.toByteArray();
//
//            // Send the JPEG to Clarifai and return the result.
//            return clarifai.recognize(new RecognitionRequest(jpeg)).get(0);
//        } catch (ClarifaiException e) {
//            Log.e(TAG, "Clarifai error", e);
//            return null;
//        }
//    }
//
//}
