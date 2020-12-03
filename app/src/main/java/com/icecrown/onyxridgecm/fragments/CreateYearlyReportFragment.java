//**************************************************************
// Project: OnyxRidge Construction Management
//
// File: CreateYearlyReportFragment.java
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
// - 11/20/2020
// - R.O.
// - DETAILS:
//      - Changed method name to lower camel case
//      - Reformatted `import` list
// ------------------------------------------------
// - 11/24/2020
// - R.O.
// - DETAILS:
//      - Added code to handle what happens when there is no
//        project selected.
// ------------------------------------------------
// - 12/3/2020
// - R.O.
// - DETAILS:
//      - Added code to handle what happens when the projects
//        hours log doesn't contain any information for the
//        entered year.
//      - Removed unneeded calls to Log.d(...).
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.icecrown.onyxridgecm.R;
import com.icecrown.onyxridgecm.utility.ReportFactory;
import com.icecrown.onyxridgecm.workseries.WorkDay;
import com.icecrown.onyxridgecm.workseries.WorkMonth;
import com.icecrown.onyxridgecm.workseries.WorkYear;

import java.io.File;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CreateYearlyReportFragment extends Fragment {

    private PDFView reportView;
    private TextInputEditText yearInput;
    private Spinner jobNameSpinner;
    private String jobNameValue = "";
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_yearly_report, container, false);

        yearInput = v.findViewById(R.id.job_year_input_field);
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
            } else {
                Snackbar.make(jobNameSpinner, R.string.projects_not_loaded, Snackbar.LENGTH_SHORT).show();
            }
        });

        reportView = v.findViewById(R.id.total_report_viewer_pdf_view);

        MaterialButton createMonthlyReportButton = v.findViewById(R.id.create_report_button);
        createMonthlyReportButton.setOnClickListener(l -> {
            String yearString = yearInput.getText().toString();
            if(jobNameValue.equals("") || jobNameValue.equals(getString(R.string.job_name_default_value))) {
                Snackbar.make(jobNameSpinner, R.string.no_project_chosen, Snackbar.LENGTH_SHORT).show();
            }
            else {
                if (yearString.equals("")) {
                    yearInput.setError(getString(R.string.no_year_entered));
                } else {
                    generateYearlyReportEnter(yearString);
                }
            }
        });

        return v;
    }


    private void generateYearlyReportEnter(final String yearString) {
        CollectionReference[] yearColl = new CollectionReference[12];
        final int yearActual = Integer.parseInt(yearString);
        WorkYear year = new WorkYear(yearActual);

        // Get around 'final' and inner class issue
        AtomicInteger countOfAdditions = new AtomicInteger();

        DocumentReference yearExists = db.document("hours/" + jobNameValue + "/years/" + yearActual);
        yearExists.get().addOnCompleteListener(taskDoc -> {
            if(taskDoc.getResult().exists()) {
                for (int i = 0; i < 12; i++) {
                    yearColl[i] = db.collection("hours/" + jobNameValue + "/years/" + yearActual + "/months/" + WorkMonth.determineMonthName(i).toLowerCase() + "/days");
                    final int monthOffset = i;

                    yearColl[i].get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> days = task.getResult().getDocuments();
                            if (days.size() != 0) {
                                WorkMonth workMonth = new WorkMonth(yearActual, monthOffset);
                                for (DocumentSnapshot day : days) {
                                    WorkDay workDay = new WorkDay(new GregorianCalendar(yearActual, monthOffset, day.getLong("day_of_month").intValue()), day.getDouble("hours_worked"));
                                    workMonth.setDayAtInstance(workDay, day.getLong("day_of_month").intValue() - 1);
                                }
                                year.setMonthIndex(monthOffset, workMonth);
                            } else {
                                Log.d("EPOCH-3", "MonthOffset: " + monthOffset + " has zero days in it.");
                            }

                            countOfAdditions.getAndIncrement();

                            if (countOfAdditions.get() == 11) {
                                File yearlyReportFile = ReportFactory.generateYearlyReport(year, getContext(), jobNameValue);
                                Log.d("EPOCH-3", "Report generated.");
                                reportView.fromFile(yearlyReportFile).load();
                            }
                        } else {
                            Log.d("EPOCH-3", "No entry found");
                        }
                    });
                }
            }
            else {
                Snackbar.make(jobNameSpinner, R.string.no_year_found_for_project, Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
