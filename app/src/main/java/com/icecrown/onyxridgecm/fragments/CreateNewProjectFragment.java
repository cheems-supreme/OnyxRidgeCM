//**************************************************************
// Project: OnyxRidge Construction Management
//
// File: CreateNewProjectFragment.java
//
// Written by: Raymond O'Neill
//
// Written on: 11/24/2020
//
// Purpose: Used for logic regarding creation of new projects.
//          Does this by inserting an empty text file used as
//          an 'anchor' to establish permanence inside Storage
//**************************************************************
package com.icecrown.onyxridgecm.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.icecrown.onyxridgecm.R;
import com.icecrown.onyxridgecm.utility.ReportFactory;

import java.io.File;
import java.io.IOException;

public class CreateNewProjectFragment extends Fragment {

    private TextInputEditText projectNameEditText;
    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_new_project, container, false);
        projectNameEditText = v.findViewById(R.id.new_job_name_edit_text);

        MaterialButton createNewProjectButton = v.findViewById(R.id.create_new_project_button);
        createNewProjectButton.setOnClickListener(l -> {
            String newProjectName = projectNameEditText.getText().toString();
            if(newProjectName.equals("")) {
                projectNameEditText.setError(getString(R.string.no_new_project_name_entered));
            }
            else {
                addProjectToStorage(newProjectName);
            }
        });
        MaterialButton cancelAddNewProjectButton = v.findViewById(R.id.cancel_project_add_button);
        cancelAddNewProjectButton.setOnClickListener(l -> getActivity().getSupportFragmentManager().popBackStack());

        return v;
    }

    private void addProjectToStorage(String newProjectName) {
        try {
            File f = ReportFactory.generateStorageAnchorFile(getContext());
            storage.getReference(newProjectName + "/anchor.txt").putFile(Uri.fromFile(f)).addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    Snackbar.make(projectNameEditText, R.string.project_creation_success, Snackbar.LENGTH_SHORT).show();
                    getFragmentManager().popBackStack();
                }
                else {
                    Snackbar.make(projectNameEditText, R.string.project_not_made_admin, Snackbar.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            Log.d("EPOCH-3", "Exception encountered while creating or retrieving anchor file");
            e.printStackTrace();
        }

    }
}
