//**************************************************************
// Project: OnyxRidge Construction Management
//
// File: CreateAccidentReportFragment.java
//
// Written by: Raymond O'Neill
//
// Written on: 11/24/2020
//
// Purpose: Used for logic regarding creation and viewing of
//          accident reports
// ------------------------------------------------
// UPDATES
// ------------------------------------------------
// - 12/3/2020
// - R.O.
// - DETAILS:
//      - Added code to handle what happens when no accident
//        logs exist for a given project.
//**************************************************************
package com.icecrown.onyxridgecm.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.icecrown.onyxridgecm.R;
import com.icecrown.onyxridgecm.utility.ReportFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CreateAccidentReportFragment extends Fragment {
    private Spinner jobNameSpinner;
    private String jobNameValue;
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private PDFView reportView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_report_of_accidents, container, false);

        reportView = v.findViewById(R.id.accident_report_viewer_pdf_view);
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

        MaterialButton generateReportButton = v.findViewById(R.id.create_report_button);
        generateReportButton.setOnClickListener(l -> {
            if(jobNameValue.equals("") || jobNameValue.equals(getString(R.string.job_name_default_value))) {
                Snackbar.make(jobNameSpinner, R.string.no_new_project_name_entered, Snackbar.LENGTH_SHORT).show();
            }
            else {
                generateAccidentReportEntry();
            }
        });


        return v;
    }

    private void generateAccidentReportEntry() {
        DocumentReference accidentLogsExist = db.document("accidents/" + jobNameValue);
        accidentLogsExist.get().addOnCompleteListener(taskDoc -> {
            if(taskDoc.getResult().exists()) {
                CollectionReference accidentLogCollection = db.collection("accidents/" + jobNameValue + "/logs");
                accidentLogCollection.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // TODO: SORT BY DATE (OLDEST TO NEWEST)
                        List<DocumentSnapshot> accidentEntries = task.getResult().getDocuments();
                        File f = ReportFactory.generateAccidentReport(accidentEntries, getContext(), jobNameValue);
                        reportView.fromFile(f).load();
                    } else {
                        Snackbar.make(jobNameSpinner, R.string.accident_report_no_accidents, Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                Snackbar.make(jobNameSpinner, R.string.accident_report_no_accidents, Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
