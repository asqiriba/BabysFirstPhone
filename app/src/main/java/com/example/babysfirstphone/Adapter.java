package com.example.babysfirstphone;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter{

    List<String> type;
    List<String> images;
    List<String> info;
    Context context;
    LayoutInflater inflater;

    public Adapter(Context ctx, List<String> type, List<String> images, List<String> info){
        this.type = type;
        this.images = images;
        this.info = info;
        this.inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getItemViewType(int position) {
        if(type.get(position).equals("phone")){
            return 0;
        }else if(type.get(position).equals("video")){
            return 1;
        }else{
            return 2;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;

        if (viewType == 0){
            view = layoutInflater.inflate(R.layout.phone_grid_layout, parent, false);
            return new ViewHolderPhone(view);
        }
        if (viewType == 1){
            view = layoutInflater.inflate(R.layout.video_grid_layout, parent, false);
            return new ViewHolderVideo(view);
        }
        view = layoutInflater.inflate(R.layout.app_grid_layout, parent, false);
        return new ViewHolderApp(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(type.get(position).equals("phone")){
            ViewHolderPhone viewHolderPhone = (ViewHolderPhone) holder;
//            viewHolderPhone.title.setText(type.get(position));
            viewHolderPhone.gridIcon.setImageBitmap(BitmapFactory.decodeFile(images.get(position)));
            viewHolderPhone.mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), Router.class);
                    intent.putExtra("type", type.get(position));
                    intent.putExtra("info", info.get(position));
                    v.getContext().startActivity(intent);
                }
            });
        } else if (type.get(position).equals("video")){
            ViewHolderVideo viewHolderVideo = (ViewHolderVideo) holder;
//            viewHolderVideo.title.setText(type.get(position));
            viewHolderVideo.gridIcon.setImageBitmap(BitmapFactory.decodeFile(images.get(position)));
            viewHolderVideo.mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), Router.class);
                    intent.putExtra("type", type.get(position));
                    intent.putExtra("info", info.get(position));
                    v.getContext().startActivity(intent);
                }
            });
        }else{
            ViewHolderApp viewHolderApp = (ViewHolderApp) holder;
//            viewHolderApp.title.setText(type.get(position));
            viewHolderApp.gridIcon.setImageBitmap(BitmapFactory.decodeFile(images.get(position)));
            viewHolderApp.mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), Router.class);
                    intent.putExtra("type", type.get(position));
                    intent.putExtra("info", info.get(position));
                    v.getContext().startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return type.size();
    }

    class ViewHolderPhone extends RecyclerView.ViewHolder{

        TextView title;
        ImageView gridIcon;
        ConstraintLayout mainLayout;

        public ViewHolderPhone(@NonNull View itemView) {
            super(itemView);
//            title = itemView.findViewById(R.id.textViewPhone);
            gridIcon = itemView.findViewById(R.id.imageViewPhone);
            mainLayout = itemView.findViewById(R.id.phone_grid_layout);
        }
    }

    class ViewHolderVideo extends RecyclerView.ViewHolder{

        TextView title;
        ImageView gridIcon;
        ConstraintLayout mainLayout;

        public ViewHolderVideo(@NonNull View itemView) {
            super(itemView);
//            title = itemView.findViewById(R.id.textViewVideo);
            gridIcon = itemView.findViewById(R.id.imageViewVideo);
            mainLayout = itemView.findViewById(R.id.video_grid_layout);
        }
    }

    class ViewHolderApp extends RecyclerView.ViewHolder{

        TextView title;
        ImageView gridIcon;
        ConstraintLayout mainLayout;

        public ViewHolderApp(@NonNull View itemView) {
            super(itemView);
//            title = itemView.findViewById(R.id.textViewApp);
            gridIcon = itemView.findViewById(R.id.imageViewApp);
            mainLayout = itemView.findViewById(R.id.app_grid_layout);
        }
    }
}

//public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
//
//    List<String> type;
//    List<Integer> images;
//    List<String> info;
//    Context context;
//    LayoutInflater inflater;
//
//    public Adapter(Context ctx, List<String> type, List<Integer> images, List<String> info){
//        this.type = type;
//        this.images = images;
//        this.info = info;
//        this.inflater = LayoutInflater.from(ctx);
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = inflater.inflate(R.layout.custom_grid_layout, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.title.setText(type.get(position));
//        holder.gridIcon.setImageResource(images.get(position));
//
//        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(), Router.class);
//                intent.putExtra("type", type.get(position));
//                intent.putExtra("info", info.get(position));
//                v.getContext().startActivity(intent);
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return type.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder{
//        TextView title;
//        ImageView gridIcon;
//        ConstraintLayout mainLayout;
//
//        public ViewHolder(@NonNull View itemView){
//            super(itemView);
//            title = itemView.findViewById(R.id.textView);
//            gridIcon = itemView.findViewById(R.id.imageView);
//            mainLayout = itemView.findViewById(R.id.custom_grid_layout);
//
//        }
//
//    }
//
//}
