package com.example.keypermobile.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.keypermobile.R;
import com.example.keypermobile.models.Site;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/*
* RecyclerView.Adapter
* RecyclerView.ViewHolder
*/
public class PasswordAdapter extends RecyclerView.Adapter<PasswordAdapter.PasswordViewHolder> implements Filterable {

    private Context mCtx;
    private List<Site> passwordList;
    private List<Site> passwordListFull;
    private OnPasswordClickListener onPasswordClickListener;

    public PasswordAdapter(Context mCtx, List<Site> passwordList, OnPasswordClickListener onPasswordClickListener) {
        this.mCtx = mCtx;
        this.passwordList = new ArrayList<>(passwordList);
        passwordListFull = passwordList;
        this.onPasswordClickListener = onPasswordClickListener;
    }

    @NonNull
    @Override
    public PasswordViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_layout, null);
        return new PasswordViewHolder(view, onPasswordClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final PasswordViewHolder holder, int position) {
        final Site password = passwordList.get(position);

        if (password.getTitle() != null && password.getTitle().length() > 0 && password.getUrl() != null && password.getUrl().length() > 0)
            holder.textViewTitle.setText(password.getTitle() + " (" + password.getUrl().substring(0, Math.min(password.getUrl().length() - 1, 10)) + ")");
        else if (password.getTitle() != null && password.getTitle().length() > 0)
            holder.textViewTitle.setText(password.getTitle());
        else
            holder.textViewTitle.setText(password.getUrl());
        holder.textViewUsername.setText(password.getUsername() != null && password.getUsername().length() > 0 ? password.getUsername() : password.getEmail());

        // Get a handler that can be used to post to the main thread
        final Handler mainHandler = new Handler(mCtx.getMainLooper());

        holder.copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) mCtx.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Password", password.getPassword());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(mCtx, "Password Copied", Toast.LENGTH_SHORT).show();
            }
        });

        holder.openSiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = password.getUrl();
                if (!url.startsWith("http"))
                    url = "https://" + url;
                Uri webPage = Uri.parse(url);
                Intent webIntent = new Intent(Intent.ACTION_VIEW,webPage);

                mCtx.startActivity(webIntent);
            }
        });

        // Get favicon and set
        new Thread(new Runnable(){
            public void run() {
                String url = password.getUrl();
                if (!url.startsWith("http"))
                    url = "https://" + url;
                final Bitmap favicon = fetchFavicon(Uri.parse(url));
                if (favicon != null) {
                    Runnable myRunnable = new Runnable() {
                        @Override
                        public void run() {
                            holder.imageView.setImageBitmap(favicon);
                        }
                    };
                    mainHandler.post(myRunnable);
                }
            }
        }).start();
    }

    @Override
    public int getItemCount() { return passwordList.size(); }

    @Override
    public Filter getFilter() { return passwordFilter; }

    private Filter passwordFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Site> filteredList = new ArrayList<>();

            // for when you haven't typed anything in the searchbar it shows the full list
            if (constraint == null || constraint.length() == 0)
            {
                filteredList.addAll(passwordListFull);
            } else {
                //keeps searched for string lowercases and trims them
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(Site password : passwordListFull) {
                    //Searches through all the titles of every password or Searches through all the websites of every password
                    if(password.getTitle().toLowerCase().contains(filterPattern)
                            || password.getUrl().toLowerCase().contains(filterPattern)
                            || password.getUsername().toLowerCase().contains(filterPattern)
                            || password.getEmail().toLowerCase().contains(filterPattern)) {
                        filteredList.add(password);
                    } else {
                        for (String tag : password.getTags()) {
                            if (tag.toLowerCase().contains(filterPattern)) {
                                filteredList.add(password);
                                break;
                            }
                        }
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            passwordList.clear();
            passwordList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    class PasswordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView textViewTitle, textViewUsername;
        ImageButton copyButton;
        Button openSiteButton;
        OnPasswordClickListener onPasswordClickListener;


        public PasswordViewHolder(View itemView, OnPasswordClickListener onPasswordClickListener){
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewUsername = itemView.findViewById(R.id.textViewUsername);
            copyButton = itemView.findViewById(R.id.imageButtonCopy);
            openSiteButton = itemView.findViewById(R.id.openSite);
            this.onPasswordClickListener = onPasswordClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onPasswordClickListener.onPasswordClick(getAdapterPosition());
        }
    }

    public interface OnPasswordClickListener
    {
        void onPasswordClick(int position);
    }

    private Bitmap fetchFavicon(Uri uri) {
        final Uri iconUri = uri.buildUpon().path("favicon.ico").build();
        Log.i("Keyper", "Fetching favicon from: " + iconUri);

        InputStream is = null;
        BufferedInputStream bis = null;
        try
        {
            URLConnection conn = new URL(iconUri.toString()).openConnection();
            conn.connect();
            is = conn.getInputStream();
            bis = new BufferedInputStream(is, 8192);
            return BitmapFactory.decodeStream(bis);
        } catch (IOException e) {
            Log.w("Keyper", "Failed to fetch favicon from " + iconUri, e);
            return null;
        }
    }
}
