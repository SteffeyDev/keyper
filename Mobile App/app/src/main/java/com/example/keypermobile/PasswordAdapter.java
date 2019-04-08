package com.example.keypermobile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/*
* RecyclerView.Adapter
* RecyclerView.ViewHolder
*/
public class PasswordAdapter extends RecyclerView.Adapter<PasswordAdapter.PasswordViewHolder> {

    private Context mCtx;
    private List<Password> passwordList;

    public PasswordAdapter(Context mCtx, List<Password> passwordList) {
        this.mCtx = mCtx;
        this.passwordList = passwordList;
    }

    @NonNull
    @Override
    public PasswordViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_layout, null);
        return new PasswordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PasswordViewHolder holder, int position) {
        Password password = passwordList.get(position);

        holder.textViewTitle.setText(password.getTitle());
        holder.textViewWebsite.setText(password.getWebsite());

        holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(password.getImage()));
    }

    @Override
    public int getItemCount() {
        return passwordList.size();
    }

    class PasswordViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textViewTitle, textViewWebsite;


        public PasswordViewHolder(View itemView){
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewWebsite = itemView.findViewById(R.id.textViewWebsite);

        }
    }
}
