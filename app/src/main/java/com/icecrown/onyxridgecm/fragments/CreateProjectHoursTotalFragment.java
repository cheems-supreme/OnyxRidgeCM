//**************************************************************
// Project: OnyxRidge Construction Management
//
// File: CreateProjectHoursTotalFragment.java
//
// Written by: Raymond O'Neill
//
// Written on: 11/20/2020
//
// Purpose: Used for logic behind creating a total hours report
//          for any given project.
// ------------------------------------------------
// UPDATES
// ------------------------------------------------
// - 11/23/2020
// - R.O.
// - DETAILS:
//      - Added class-level variables, code to load Spinner, and
//        entry to generating report total
//**************************************************************
package com.icecrown.onyxridgecm.fragments;

import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.icecrown.onyxridgecm.R;

import java.util.ArrayList;
import java.util.List;

public class CreateProjectHoursTotalFragment extends Fragment {

    private PDFView reportView;
    private Spinner jobNameSpinner;
    private String jobNameValue = "";
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_project_totals, container, false);

        reportView = v.findViewById(R.id.total_report_viewer_pdf_view);

        jobNameSpinner = v.findViewById(R.id.job_name_spinner);
        storage.getReference().listAll().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                List<String> jobNames = new ArrayList<>();

                jobNames.add(getString(R.string.job_name_default_value));

                for(StorageReference ref : task.getResult().getPrefixes()) {
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

        MaterialButton createMonthlyReportButton = v.findViewById(R.id.create_report_button);
        createMonthlyReportButton.setOnClickListener(l -> {
            if(jobNameValue.equals("") || jobNameValue.equals(getString(R.string.job_name_default_value))) {
                // TODO: ERROR MESSAGE FOR NO PROJECT SELECTED
            }
            else {
                generateProjectTotals();
            }
        });
        return v;
    }

    private void generateProjectTotals() {
        CollectionReference projectYearsDir = FirebaseFirestore.getInstance().collection("hours/" + jobNameValue + "/years/");
        projectYearsDir.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Log.d("EPOCH-3", "Number of results from projectYearsDir: " + task.getResult().size());
            }
            else {
                Log.d("EPOCH-3", "Gathering failed");
                Snackbar.make(jobNameSpinner, R.string.report_not_made_admin, Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
