package com.company.photoalbumproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder> {

    private List<Photo> photos = new ArrayList<>();
    private onImageClickListener listener;

    public void setListener(onImageClickListener listener) {
        this.listener = listener;
    }

    public interface onImageClickListener {
        void onImageClick(Photo photo);
    }

    @NonNull
    @Override
    public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.photo_item, parent, false);

        return new PhotoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoHolder holder, int position) {
        Photo currentPhoto = photos.get(position);
        byte[] array = currentPhoto.getPhotoItem();
        Bitmap image = BitmapFactory.decodeByteArray(array, 0, array.length);

        holder.showImage.setImageBitmap(image);
        holder.imageTitle.setText(currentPhoto.getTitle());
        holder.imageDescription.setText(currentPhoto.getDescription());

    }

    public Photo getPosition(int position) {
        return photos.get(position);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
        notifyDataSetChanged();
    }

    class PhotoHolder extends RecyclerView.ViewHolder{
        ImageView showImage;
        TextView imageTitle;
        TextView imageDescription;

        public PhotoHolder(@NonNull View itemView) {
            super(itemView);
            showImage = itemView.findViewById(R.id.showImage);
            imageTitle = itemView.findViewById(R.id.imageTitle);
            imageDescription = itemView.findViewById(R.id.imageDescription);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onImageClick(photos.get(position));
                    }
                }
            });
        }
    }

}
