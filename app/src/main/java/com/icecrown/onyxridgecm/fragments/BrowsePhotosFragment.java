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
//**************************************************************
package com.icecrown.onyxridgecm.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.icecrown.onyxridgecm.R;
import com.icecrown.onyxridgecm.adapters.PhotosAdapter;
import com.icecrown.onyxridgecm.interfaces.IProjectSelectedCallback;
import com.icecrown.onyxridgecm.utility.Photo;
import com.icecrown.onyxridgecm.utility.PhotoFactory;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BrowsePhotosFragment extends Fragment {

    private RecyclerView recView;
    private String projectNameString;
    private MaterialTextView projectNameTextView;
    private final BrowsePhotosFragment singleton = this;
    private PhotosAdapter photosAdapter;
    private List<Photo> photoList = new ArrayList<>();
    private FragmentManager manager;
    private Date dateOfPhoto;



    private final IProjectSelectedCallback callback = new IProjectSelectedCallback() {
        @Override
        public void onProjectSelected(String projectName) {
            projectNameString = projectName;
            projectNameTextView.setText(projectName);
            LoadPhotoList();
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        manager = getActivity().getSupportFragmentManager();

        View v = inflater.inflate(R.layout.fragment_browse_content, container, false);

        projectNameTextView = v.findViewById(R.id.project_name);
        recView = v.findViewById(R.id.content_browse_rec_view);

        recView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recView.setItemAnimator(new DefaultItemAnimator());
        recView.addItemDecoration(new DividerItemDecoration(recView.getContext(), DividerItemDecoration.VERTICAL));

        projectNameTextView.setOnClickListener(v1 -> {
            SelectProjectFragment fragment = new SelectProjectFragment();
            fragment.setCallback(callback);
            manager.beginTransaction().hide(singleton).add(R.id.main_content_holder, fragment).addToBackStack(null).commit();
        });

        photosAdapter = new PhotosAdapter(photoList);
        photosAdapter.setOnItemClickListener(position -> {
            final File f = PhotoFactory.GenerateImageFile();

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

        return v;
    }


    private void LoadPhotoList() {
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
                                        dateOfPhoto = DateFormat.getDateInstance(DateFormat.SHORT).parse(meta.getCustomMetadata("date_uploaded"));
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
                                        Photo adder = new Photo(fileTemp.getName(), Uri.fromFile(fileTemp), photo, dateOfPhoto, meta.getCustomMetadata("taken_by"));
                                        adder.GenerateBitmapForUri(getActivity(), getActivity().getContentResolver());
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
            }
        });
    }

}
