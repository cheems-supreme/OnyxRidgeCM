//**************************************************************
// Project: OnyxRidge Construction Management
//
// File: BrowsePhotosFragment.java
//
// Written by: Raymond O'Neill
//
// Written on: 11/4/2020
//
// Purpose: Used for logic regarding viewing and downloading
//          photos
// ------------------------------------------------
// UPDATES
// ------------------------------------------------
// - 11/12/2020
// - R.O.
// - DETAILS:
//      - Changed the List of Photos to a final variable
//      - Changed format of onCreateView(...) (put recView def
//        lower in the method)
// ------------------------------------------------
// - 11/20/2020
// - R.O.
// - DETAILS:
//      - Refactored `LoadPhotoList(...)` to
//        `loadPhotoList(...)`
//      - Reformatted spacings
//      - Implemented the use of `LocalDate` and removed `Date`
//        instances.
//      - Removed unused imports
//**************************************************************
package com.icecrown.onyxridgecm.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.icecrown.onyxridgecm.R;
import com.icecrown.onyxridgecm.adapters.PhotosAdapter;
import com.icecrown.onyxridgecm.interfaces.IProjectSelectedCallback;
import com.icecrown.onyxridgecm.utility.Photo;
import com.icecrown.onyxridgecm.utility.PhotoFactory;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BrowsePhotosFragment extends Fragment {

    private RecyclerView recView;
    private String projectNameString;
    private MaterialTextView projectNameTextView;
    private final BrowsePhotosFragment singleton = this;
    private PhotosAdapter photosAdapter;
    private final List<Photo> photoList = new ArrayList<>();
    private FragmentManager manager;
    private LocalDate dateOfPhoto;



    private final IProjectSelectedCallback callback = new IProjectSelectedCallback() {
        @Override
        public void onProjectSelected(String projectName) {
            projectNameString = projectName;
            projectNameTextView.setText(projectName);
            loadPhotoList();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        manager = getActivity().getSupportFragmentManager();

        View v = inflater.inflate(R.layout.fragment_browse_content, container, false);

        projectNameTextView = v.findViewById(R.id.project_name);

        projectNameTextView.setOnClickListener(v1 -> {
            SelectProjectFragment fragment = new SelectProjectFragment();
            fragment.setCallback(callback);
            manager.beginTransaction().hide(singleton).add(R.id.main_content_holder, fragment).addToBackStack(null).commit();
        });

        photosAdapter = new PhotosAdapter(photoList);
        photosAdapter.setOnItemClickListener(position -> {
            final File f = PhotoFactory.generateImageFile(getContext());

            if(f != null) {
                photoList.get(position).getRef().getFile(f).addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        ViewPhotoFragment fragment = new ViewPhotoFragment();
                        fragment.setImageFile(f);
                        manager.beginTransaction().hide(singleton).add(R.id.main_content_holder, fragment).addToBackStack(null).commit();
                    } else {
                        Snackbar.make(recView, R.string.photo_cannot_be_loaded, Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });

        recView = v.findViewById(R.id.content_browse_rec_view);

        recView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recView.setItemAnimator(new DefaultItemAnimator());
        recView.addItemDecoration(new DividerItemDecoration(recView.getContext(), DividerItemDecoration.VERTICAL));

        recView.setAdapter(photosAdapter);

        return v;
    }


    private void loadPhotoList() {
        if(!photoList.isEmpty()) {
            photoList.clear();
        }

        FirebaseStorage.getInstance().getReference(projectNameString + "/photos/").listAll().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                final List<StorageReference> reference = task.getResult().getItems();
                if(reference.size() != 0) {
                    new Thread(() -> {
                        for(final StorageReference photo : reference) {
                            photo.getMetadata().addOnCompleteListener(task1 -> {
                                if(task.isSuccessful()) {
                                    final StorageMetadata meta = task1.getResult();
                                    try {
                                        dateOfPhoto = LocalDate.parse(meta.getCustomMetadata("date_uploaded"), DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                                    } catch (Exception e) {
                                        Log.d("EPOCH-3", "Date exception for 'date_uploaded' occurred.");
                                        e.printStackTrace();
                                    }

                                    File f = null;
                                    try {
                                        f = File.createTempFile("temp", ".jpg");
                                    } catch (IOException ioe) {
                                        Log.d("EPOCH-3", "createTempFile(...) IOException encountered.");
                                        ioe.printStackTrace();
                                    }
                                    catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    final File fileTemp = f;

                                    photo.getFile(fileTemp).addOnCompleteListener(task2 -> {
                                        Photo adder = new Photo(fileTemp.getName(), Uri.fromFile(fileTemp), photo, dateOfPhoto, meta.getCustomMetadata("taken_by_last"), meta.getCustomMetadata("taken_by_first"));
                                        adder.generateBitmapForUri(getActivity(), getActivity().getContentResolver());
                                        photoList.add(adder);

                                        if(photoList.size() == reference.size()) {
                                            photosAdapter.setPhotos(photoList);

                                        }
                                    });
                                }
                            });
                        }
                    }).start();

                }
                else {
                    Snackbar.make(recView, R.string.no_photos_to_show, Snackbar.LENGTH_SHORT).show();
                }
            } else {
                Snackbar.make(recView, R.string.no_photos_admin, Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
