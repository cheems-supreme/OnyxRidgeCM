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
// ------------------------------------------------
// - 12/3/2020
// - R.O.
// - DETAILS:
//      - Added an assignment to the yearsAddedCount to keep
//        report generation inclusive of all years (see changelog
//        for more details)
//      - Added code to handle what happens if there are no reports,
//        and by extension entries in the hours log, for the selected
//        project.
//      - Move the countOfAdditions.getAndIncrement() call to an else
//        block underneath conditional
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

public class CreateProjectHoursTotalFragment extends Fragment {

    private PDFView reportView;
    private Spinner jobNameSpinner;
    private String jobNameValue = "";
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private WorkYear[] years;

    private int yearsAddedCount = 0;
    private int yearsTakenFromDBCount = 0;

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
                Snackbar.make(jobNameSpinner, R.string.no_project_chosen, Snackbar.LENGTH_SHORT).show();
            }
            else {
                generateProjectTotals();
            }
        });
        return v;
    }

    private void generateProjectTotals() {
        CollectionReference projectYearsDir = db.collection("hours/" + jobNameValue + "/years/");
        projectYearsDir.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("EPOCH-3", jobNameValue + " document (year) size: " + task.getResult().getDocuments().size());
                if(!(task.getResult().getDocuments().size() == 0)) {
                    years = new WorkYear[task.getResult().size()];
                    yearsAddedCount = 0;
                    yearsTakenFromDBCount = task.getResult().size();
                    Log.d("EPOCH-3", String.valueOf(yearsTakenFromDBCount));

                    for (int i = 0; i < task.getResult().size(); i++) {
                        final int year = Integer.parseInt(task.getResult().getDocuments().get(i).getId());
                        years[i] = new WorkYear(year);
                        loadMonthsIntoYear(years[i]);
                    }
                }
                else {
                        Snackbar.make(jobNameSpinner, R.string.no_reports_found_for_project_overall_total, Snackbar.LENGTH_SHORT).show();
                }
            } else {
                Log.d("EPOCH-3", "Gathering failed");
                Snackbar.make(jobNameSpinner, R.string.report_not_made_admin, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMonthsIntoYear(WorkYear year) {
        CollectionReference[] yearColl = new CollectionReference[12];

        // Get around 'final' and inner class issue
        AtomicInteger countOfAdditions = new AtomicInteger();

            for(int i = 0; i < 12; i++) {
            yearColl[i] = db.collection("hours/" + jobNameValue + "/years/" + year.getYear() + "/months/" + WorkMonth.determineMonthName(i).toLowerCase() + "/days");
            final int monthOffset = i;

            yearColl[i].get().addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    List<DocumentSnapshot> days = task.getResult().getDocuments();
                    Log.d("EPOCH-3", "directory: " + yearColl[monthOffset].getPath());
                    Log.d("EPOCH-3", "days size: " + days.size());
                    if(days.size() != 0) {
                        WorkMonth workMonth = new WorkMonth(year.getYear(), monthOffset);
                        for(DocumentSnapshot day : days) {
                            WorkDay workDay = new WorkDay(new GregorianCalendar(year.getYear(), monthOffset, day.getLong("day_of_month").intValue()), day.getDouble("hours_worked"));
                            workMonth.setDayAtInstance(workDay, day.getLong("day_of_month").intValue() - 1);
                        }
                        year.setMonthIndex(monthOffset, workMonth);
                    }
                    else {
                        Log.d("EPOCH-3", "MonthOffset: " + monthOffset + " has zero days in it.");
                    }

                    if(countOfAdditions.get() == 11) {
                        yearsAddedCount++;
                        determineYearGatheringCompleted();
                    }
                    else {
                        countOfAdditions.getAndIncrement();
                    }
                }
                else {
                    Log.d("EPOCH-3", "No entry found");
                }
            });
        }
    }

    private void determineYearGatheringCompleted() {
        if(yearsAddedCount == yearsTakenFromDBCount) {
            File report = ReportFactory.generateProjectTotalsReport(years, getContext(), jobNameValue);
            reportView.fromFile(report).load();
        }
    }
}
