package com.icecrown.onyxridgecm.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.icecrown.onyxridgecm.R;

import java.io.File;

public class ViewPhotoFragment extends Fragment {
    private File imageFile;
    private ImageView imagePreview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.view_photo, container, false);

        imagePreview = v.findViewById(R.id.display_image_preview);
        v.getViewTreeObserver().addOnGlobalLayoutListener(() -> imagePreview.setImageURI(Uri.fromFile(imageFile)));

        return v;
    }

    public void setImageFile(File image) {
        imageFile = image;
    }
}
