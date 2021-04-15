package com.example.babysfirstphone.contacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.babysfirstphone.R;
import com.example.babysfirstphone.controllers.Contacts;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    ArrayList<Contacts> moviesList;

    public RecyclerAdapter(ArrayList<Contacts> moviesList) {
        this.moviesList = moviesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.contact_row_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(moviesList.get(position).getName());
        holder.phoneView.setText(moviesList.get(position).getNumberFormat());
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView textView, phoneView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
            phoneView = itemView.findViewById(R.id.phoneView);

            itemView.setOnClickListener(this);

        }

        /*
            Function for debugging purposes. Prints out contact name.
         */
        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), moviesList.get(getAdapterPosition()).getName(), Toast.LENGTH_SHORT).show();
        }
    }
}