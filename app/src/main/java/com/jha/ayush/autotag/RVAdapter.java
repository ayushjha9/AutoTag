package com.jha.ayush.autotag;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

/**
 * Created by nick on 4/20/2016.
 */
public class RVAdapter
        extends RecyclerView.Adapter<RVAdapter.ResultViewHolder> {
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
        //holder.position = position;
        holder.hashTags.setText(resultCardList.get(position).hashTags);
        holder.image.setImageBitmap(resultCardList.get(position).bitmap);
        holder.uri = resultCardList.get(position).uri;
        resultCardList.get(position).spinner = holder.spinner;
    }

    public void removeCard(int position){
        resultCardList.remove(position);
        notifyItemRemoved(position);
    }

    public ResultCard getItem(int position){
        return resultCardList.get(position);
    }

    @Override
    public int getItemCount() {
        return resultCardList.size();
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ResultViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener {
        CardView cv;
        TextView hashTags;
        ImageView image;
        AppCompatImageButton copyButton, shareButton, overflowButton;
        Uri uri;
        ProgressBar spinner;
        int position;

        ResultViewHolder(final View itemView) {
            super(itemView);
            itemView.setOnCreateContextMenuListener(this);
            cv = (CardView)itemView.findViewById(R.id.card_view);
            hashTags = (TextView)itemView.findViewById(R.id.TagOutPut);
            image = (ImageView)itemView.findViewById(R.id.imageView);
            copyButton = (AppCompatImageButton) itemView.findViewById(R.id.copyButton);
            shareButton = (AppCompatImageButton) itemView.findViewById(R.id.shareButton);
            overflowButton = (AppCompatImageButton) itemView.findViewById(R.id.overflowButton);
            spinner = (ProgressBar) itemView.findViewById(R.id.progressBar);
            overflowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PopupMenu popupMenu = new PopupMenu(contx, v);
                    final Menu menu = popupMenu.getMenu();
                    final int itemPosition = getAdapterPosition();
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getGroupId()){
                                case 0: FirstActivityScreen.adapter.removeCard(itemPosition);
                                    break;
                                case 1: AlertDialog.Builder alert = new AlertDialog.Builder(contx);
                                    final EditText edittext = new EditText(contx);
                                    edittext.setText(hashTags.getText());
                                    alert.setTitle("Edit Tags:");

                                    alert.setView(edittext);

                                    alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            //What ever you want to do with the value
                                            //Editable YouEditTextValue = edittext.getText();
                                            //OR
                                            hashTags.setText(edittext.getText().toString());

                                        }
                                    });

                                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            // what ever you want to do with No option.
                                        }
                                    });

                                    alert.show();
                            }
                            return true;
                        }
                    });

                    menu.add(0, v.getLabelFor(), 0, "Delete Card");
                    menu.add(1, v.getLabelFor(), 1, "Edit Tags");

                    popupMenu.show();
                }
            });
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

        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(0, v.getLabelFor(), 0, "Delete Card");
            menu.add(1, v.getLabelFor(), 1, "Edit Tags");
        }

    }

}

