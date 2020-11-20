//**************************************************************
// Project: OnyxRidge Construction Management
//
// File: UploadExistingPhotoFragment.java
//
// Written by: Raymond O'Neill
//
// Written on: 11/12/2020
//
// Purpose: Used to take new photos and upload them to Storage
// ------------------------------------------------
// UPDATES
// ------------------------------------------------
// - 11/20/2020
// - R.O.
// - DETAILS:
//      - Reformatted `import` list
//      - Refactored method names to lower camel case
//**************************************************************
package com.icecrown.onyxridgecm.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import com.icecrown.onyxridgecm.R;
import com.icecrown.onyxridgecm.utility.PhotoFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TakeNewPhotoFragment extends Fragment {

    private final int TAKE_NEW_PICTURE = 1;
    private ImageView imageView;
    private Uri chosenFile;
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private Spinner jobNameSpinner;
    private String jobNameValue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_take_new_photo, container, false);

        jobNameSpinner = v.findViewById(R.id.job_name_spinner);
        storage.getReference().listAll().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> jobNames = new ArrayList<>();

                jobNames.add(getString(R.string.job_name_default_value));
                for (StorageReference ref : task.getResult().getPrefixes()) {
                    jobNames.add(ref.getName());
                }
                ArrayAdapter<String> jobNamesAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, jobNames);
                jobNamesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                jobNameSpinner.setAdapter(jobNamesAdapter);
                jobNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        jobNameValue = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
            else {
                Snackbar.make(jobNameSpinner, R.string.projects_not_loaded, Snackbar.LENGTH_SHORT).show();
            }
        });

        imageView = v.findViewById(R.id.image_view_for_file);

        MaterialButton takeNewPhotoButton = v.findViewById(R.id.take_new_button);
        takeNewPhotoButton.setOnClickListener(l -> takeNewPhotoWithCamera());

        MaterialButton uploadFileToStorageButton = v.findViewById(R.id.upload_file_to_storage_button);
        uploadFileToStorageButton.setOnClickListener(l -> {
            if(chosenFile == null) {
                Snackbar.make(jobNameSpinner, R.string.no_picture_taken_for_upload, Snackbar.LENGTH_SHORT).show();
            }
            else if (jobNameValue.equals(getString(R.string.job_name_default_value)) || jobNameValue.equals("")) {
                Snackbar.make(jobNameSpinner, R.string.no_project_chosen_upload, Snackbar.LENGTH_SHORT).show();
            }
            else {
                uploadFileToStorage(new File(chosenFile.getLastPathSegment()).getName());
            }
            });
        return v;
    }


    private void takeNewPhotoWithCamera() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File f = PhotoFactory.generateImageFile(getContext());

        if(f != null) {
            chosenFile = FileProvider.getUriForFile(getContext(), "com.icecrown.onyxridgecm.fileprovider", f);
            i.putExtra(MediaStore.EXTRA_OUTPUT, chosenFile);
            startActivityForResult(i, TAKE_NEW_PICTURE);
        } else {
            Log.d("EPOCH-3", "File generation and printing failed.");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == TAKE_NEW_PICTURE) {
            if(resultCode == Activity.RESULT_OK) {
                if (chosenFile != null) {
                    imageView.setImageURI(chosenFile);
                }
            }
            else {
                chosenFile = null;
            }
        }
    }

    private void uploadFileToStorage(final String filename) {
        final StorageReference insertionRef = storage.getReference().child(jobNameValue).child("photos/" + filename);

        insertionRef.putFile(chosenFile).addOnCompleteListener(task -> {
            Calendar c = Calendar.getInstance();
            SharedPreferences prefs = getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);

            StorageMetadata metadata = new StorageMetadata.Builder().setContentType("image")
                    .setCustomMetadata("taken_by_first", prefs.getString("first_name", "Unavail."))
                    .setCustomMetadata("taken_by_last", prefs.getString("last_name", "Unavail."))
                    .setCustomMetadata("date_uploaded", (1 + c.get(Calendar.MONTH)) + "/" + c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.YEAR))
                    .setCustomMetadata("photo_name", filename).build();
            insertionRef.updateMetadata(metadata).addOnCompleteListener(task1 -> {
                if(task1.isSuccessful()) {
                    Snackbar.make(jobNameSpinner, R.string.photo_upload_success, Snackbar.LENGTH_SHORT).show();
                    imageView.setImageDrawable(null);
                } else {
                    Snackbar.make(jobNameSpinner, R.string.upload_photo_failed, Snackbar.LENGTH_SHORT).show();
                }
            });
        });
    }
}
