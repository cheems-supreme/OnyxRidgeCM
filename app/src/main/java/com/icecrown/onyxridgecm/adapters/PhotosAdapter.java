//*******************************************************************
// Project: BCS430W - Senior Project
//
// Project name: OnyxRidge
//
// File: Adapter_Photos.java
//
// Written by: Raymond O'Neill
//
// Date written: 10/19/2020
//
// Date added: 11/10/2020
//
// Detail: Adapter for photos in the BrowsePhotosFragment.java. Will
//         be used in the RecyclerView instance
// ------------------------------------------------
// UPDATES
// ------------------------------------------------
// - 11/12/2020
// - R.O.
// - DETAILS:
//      - Changed comment header layout
//      - Changed the `name` field to be `last-name, first-name`
//      - Made inner class `PhotoHolder` `private` variables `final`
// ------------------------------------------------
// - 11/20/2020
// - R.O.
// - DETAILS:
//      - Reformatted comment header
// ------------------------------------------------
// - 12/1/2020
// - R.O.
// - DETAILS:
//      - Added method to return current `List` of `Photo`s.
//*******************************************************************
package com.icecrown.onyxridgecm.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.icecrown.onyxridgecm.R;
import com.icecrown.onyxridgecm.utility.Photo;

import java.util.List;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotoHolder>{
    private List<Photo> photos;
    private PhotosAdapter.PhotoAdapterListener listener;



    public class PhotoHolder extends RecyclerView.ViewHolder {
        private final ImageView photoThumbnail;
        private final TextView uploadedBy;
        private final TextView dateOfContent;

        public PhotoHolder(View v) {
            super(v);
            photoThumbnail = v.findViewById(R.id.photo_image_view);
            uploadedBy = v.findViewById(R.id.photo_taken_by_text_view);
            dateOfContent = v.findViewById(R.id.photo_date_taken_text_view);
            v.setOnClickListener(v1 -> {
                if (listener != null)
                {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION)
                    {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }

    public PhotosAdapter(List<Photo> photos)
    {
        this.photos = photos;
    }

    @Override
    public PhotosAdapter.PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_photo_instance, parent, false);
        return new PhotosAdapter.PhotoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PhotosAdapter.PhotoHolder holder, final int position)
    {
        holder.photoThumbnail.setImageBitmap(photos.get(position).getThumbnail());
        holder.uploadedBy.setText(photos.get(position).getTakenByLastNameFirst());
        holder.dateOfContent.setText(photos.get(position).getDateAsString());

    }


    @Override
    public int getItemCount() {
        return photos.size();
    }

    public void setPhotos(List<Photo> photos)
    {
        this.photos = photos;
        notifyDataSetChanged();
    }
    public List<Photo> getPhotos() {
        return photos;
    }

    public interface PhotoAdapterListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(PhotosAdapter.PhotoAdapterListener listener)
    {
        this.listener = listener;
    }
}
