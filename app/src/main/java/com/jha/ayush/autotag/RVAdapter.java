package com.jha.ayush.autotag;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

/**
 * Created by nick on 4/20/2016.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ResultViewHolder>{
    List<ResultCard> resultCardList;
    static Context contx;

    RVAdapter(List<ResultCard> resultCardList, Context contx){
        this.resultCardList = resultCardList;
        this.contx = contx;
    }

    @Override
    public RVAdapter.ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent,false);
        ResultViewHolder rvh = new ResultViewHolder(v);
        return rvh;
    }

    @Override
    public void onBindViewHolder(RVAdapter.ResultViewHolder holder, int position) {
        holder.hashTags.setText(resultCardList.get(position).hashTags);
        holder.image.setImageBitmap(resultCardList.get(position).bitmap);
        holder.uri = resultCardList.get(position).uri;
    }

    @Override
    public int getItemCount() {
        return resultCardList.size();
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ResultViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView hashTags;
        ImageView image;
        AppCompatImageButton copyButton, shareButton;
        Uri uri;

        ResultViewHolder(final View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.card_view);
            hashTags = (TextView)itemView.findViewById(R.id.TagOutPut);
            image = (ImageView)itemView.findViewById(R.id.imageView);
            copyButton = (AppCompatImageButton) itemView.findViewById(R.id.copyButton);
            shareButton = (AppCompatImageButton) itemView.findViewById(R.id.shareButton);
            copyButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager) itemView.getContext().getSystemService(itemView.getContext().CLIPBOARD_SERVICE);
                    clipboard.setText(hashTags.getText()+ " #AutoTag");
                    Toast.makeText(itemView.getContext().getApplicationContext(), "Copied to clipboard", Toast.LENGTH_LONG).show();
                }
            });
            shareButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager) itemView.getContext().getSystemService(itemView.getContext().CLIPBOARD_SERVICE);
                    clipboard.setText(hashTags.getText()+ " #AutoTag");
                    Toast.makeText(itemView.getContext().getApplicationContext(), "Copied to clipboard", Toast.LENGTH_LONG).show();
                    String type = "image/*";
                    Share(type, uri);
                }
            });
        }

        public void Share(String type, Uri uri){
            // Create the new Intent using the 'Send' action.
            Intent share = new Intent(Intent.ACTION_SEND);

            // Set the MIME type
            share.setType(type);

            // Create the URI from the media
//            File media = new File(mediaPath);
//            Uri uri = Uri.fromFile(media);

            // Add the URI to the Intent.
            share.putExtra(Intent.EXTRA_STREAM, uri);
            share.putExtra(Intent.EXTRA_TEXT,hashTags.getText()+" #AutoTag");
            share.putExtra(Intent.EXTRA_TITLE,hashTags.getText()+" #AutoTag");
            System.out.println(uri);


                // Broadcast the Intent.
            contx.startActivity(share);
            contx.startActivity(Intent.createChooser(share, "Share to"));
            //Toast.makeText(itemView.getContext().getApplicationContext(), "Could not share", Toast.LENGTH_LONG).show();
        }

    }

}

