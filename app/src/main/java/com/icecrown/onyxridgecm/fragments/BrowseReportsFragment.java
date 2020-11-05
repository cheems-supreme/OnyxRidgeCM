//**************************************************************
// Project: OnyxRidge Construction Management
//
// File: BrowseReportsFragment.java
//
// Written by: Raymond O'Neill
//
// Written on: 11/4/2020
//
// Purpose: Used for logic regarding viewing and downloading
//          reports
//**************************************************************
package com.icecrown.onyxridgecm.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.icecrown.onyxridgecm.R;
import com.icecrown.onyxridgecm.adapters.DocumentAdapter;
import com.icecrown.onyxridgecm.utility.Document;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BrowseReportsFragment extends Fragment {

    private MaterialTextView projectNameTextView;

    private String projectNameString = "";

    private List<Document> documentList = new ArrayList<>();

    private DocumentAdapter documentAdapter;

    private final SelectProjectFragment.ProjectSelectedCallback callback = new SelectProjectFragment.ProjectSelectedCallback() {
        @Override
        public void onProjectSelected(String projectName) {
            projectNameString = projectName;
            projectNameTextView.setText(projectName);
            LoadDocumentsList();
        }
    };

    private final BrowseReportsFragment singleton = this;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_browse_content, container, false);

        projectNameTextView = v.findViewById(R.id.project_name);

        projectNameTextView.setOnClickListener(v1 -> {
            SelectProjectFragment fragment = new SelectProjectFragment();
            fragment.setCallback(callback);

            FragmentManager manager = getActivity().getSupportFragmentManager();
            manager.beginTransaction().hide(singleton).add(R.id.main_content_holder, fragment).addToBackStack(null).commit();

        });
        return v;
    }

    private void LoadDocumentsList() {
        if(!documentList.isEmpty()) {
            documentList.clear();
        }

        FirebaseStorage.getInstance().getReference(projectNameString + "/documents/").listAll().addOnCompleteListener(new OnCompleteListener<ListResult>() {
            @Override
            public void onComplete(@NonNull Task<ListResult> task) {
                if(task.isSuccessful()) {
                    final List<StorageReference> refList = task.getResult().getItems();

                    new Thread(() -> {
                        if(task.getResult().getItems().size() != 0) {
                            for(final StorageReference doc : refList) {
                                doc.getMetadata().addOnCompleteListener(task1 -> {
                                    if(task1.isSuccessful()) {
                                        StorageMetadata data = task1.getResult();

                                        Date dateUploaded;
                                        Date dateOfContent;
                                        try {
                                            dateUploaded = DateFormat.getDateInstance(DateFormat.SHORT).parse(data.getCustomMetadata("date_created"));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            dateUploaded = new Date();
                                        }

                                        try {
                                            dateOfContent = DateFormat.getDateInstance(DateFormat.SHORT).parse(data.getCustomMetadata("date_of_content"));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            dateOfContent = new Date();
                                        }


                                        documentList.add(new Document(doc, data.getCustomMetadata("document_name"), dateUploaded, dateOfContent, data.getCustomMetadata("first_name"), data.getCustomMetadata("last_name"), Boolean.parseBoolean(data.getCustomMetadata("accident_happened"))));

                                        if(documentList.size() == refList.size()) {
                                            documentAdapter.setDocs(documentList);
                                        }
                                    }
                                });
                            }
                        } else {
                            Snackbar.make(projectNameTextView, R.string.no_documents_found, Snackbar.LENGTH_SHORT).show();
                        }
                    }).start();
                }
            }
        });
    }
}
