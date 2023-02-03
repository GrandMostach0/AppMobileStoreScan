package com.vic.scannerstoreoficial.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.makeramen.roundedimageview.RoundedImageView;
import com.vic.scannerstoreoficial.R;
import com.vic.scannerstoreoficial.interfaz.image;

import java.util.List;

public class imageAdapter extends  RecyclerView.Adapter<imageAdapter.imageViewHolder>{

    private List<image> imageList;
    private ViewPager2 viewPager2;

    public imageAdapter(List<image> imageList, ViewPager2 viewPager2){
        this.imageList = imageList;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public imageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slide_container, parent, false);
        return new imageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull imageViewHolder holder, int position) {
        holder.imageView.setImageResource(imageList.get(position).getImage());

        if(position == imageList.size()-2){
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    class imageViewHolder extends RecyclerView.ViewHolder{

        private RoundedImageView imageView;

        public imageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlider);
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            imageList.addAll(imageList);
            notifyDataSetChanged();
        }
    };

}
