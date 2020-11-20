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
// ------------------------------------------------
// UPDATES
// ------------------------------------------------
// - 11/6/2020
// - R.O.
// - DETAILS:
//      - Made a method a lambda
// ------------------------------------------------
// - 11/8/2020
// - R.O.
// - DETAILS:
//      - Added RecyclerView instance to this and corresponding
//        `browse_content` XML file
// ------------------------------------------------
// - 11/9/2020
// - R.O.
// - DETAILS:
//      - Added code to handle user clicking on a report entry
//        of the RecyclerView
// ------------------------------------------------
// - 11/20/2020
// - R.O.
// - DETAILS:
//      - Refactored `LoadDocumentList(...)` to
//        `loadDocumentList(...)`
//      - Reformatted comment headers
//      - Made `documentList` `final`
//      - Reformatted class-level variable list
//**************************************************************
package com.icecrown.onyxridgecm.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
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
import com.icecrown.onyxridgecm.adapters.DocumentAdapter;
import com.icecrown.onyxridgecm.interfaces.IProjectSelectedCallback;
import com.icecrown.onyxridgecm.utility.Document;
import com.icecrown.onyxridgecm.utility.ReportFactory;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BrowseReportsFragment extends Fragment {

    private MaterialTextView projectNameTextView;
    private String projectNameString = "";
    private final List<Document> documentList = new ArrayList<>();
    private DocumentAdapter documentAdapter;
    private final BrowseReportsFragment singleton = this;

    private final IProjectSelectedCallback callback = new IProjectSelectedCallback() {
        @Override
        public void onProjectSelected(String projectName) {
            projectNameString = projectName;
            projectNameTextView.setText(projectName);
            loadDocumentsList();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_browse_content, container, false);

        projectNameTextView = v.findViewById(R.id.project_name);
        RecyclerView recView = v.findViewById(R.id.content_browse_rec_view);

        recView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recView.setItemAnimator(new DefaultItemAnimator());
        recView.addItemDecoration(new DividerItemDecoration(recView.getContext(), DividerItemDecoration.VERTICAL));

        projectNameTextView.setOnClickListener(v1 -> {
            SelectProjectFragment fragment = new SelectProjectFragment();
            fragment.setCallback(callback);
            getActivity().getSupportFragmentManager().beginTransaction().hide(singleton).add(R.id.main_content_holder, fragment).addToBackStack(null).commit();
        });


        documentAdapter = new DocumentAdapter(documentList);
        documentAdapter.setOnItemClickListener(position -> {
            final File chosenFile = ReportFactory.generateFile(getContext(), getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE));
            documentAdapter.getDocuments().get(position).getRef().getFile(chosenFile).addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    Log.d("EPOCH-3", "documentAdapter onClick successful");

                    ViewPdfReportFragment pdfFragment = new ViewPdfReportFragment();
                    pdfFragment.setChosenPdf(chosenFile);
                    getActivity().getSupportFragmentManager().beginTransaction().hide(singleton).add(R.id.main_content_holder, pdfFragment).addToBackStack(null).commit();
                } else {
                    Snackbar.make(recView, R.string.pdf_viewing_report_not_loaded_admin, Snackbar.LENGTH_SHORT).show();
                }
            });
        });

        recView.setAdapter(documentAdapter);

        return v;
    }

    private void loadDocumentsList() {
        if(!documentList.isEmpty()) {
            documentList.clear();
        }

        FirebaseStorage.getInstance().getReference(projectNameString + "/documents/").listAll().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                final List<StorageReference> refList = task.getResult().getItems();

                new Thread(() -> {
                    if(refList.size() != 0) {
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
        });
    }
}
