//**************************************************************
// Project: OnyxRidge Construction Management
//
// File: CreateMonthlyReportFragment.java
//
// Written by: Raymond O'Neill
//
// Written on: 11/18/2020
//
// Purpose: Used for creating reports of total hours based
//          on month of given year
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.icecrown.onyxridgecm.R;
import com.icecrown.onyxridgecm.utility.ReportFactory;
import com.icecrown.onyxridgecm.workseries.WorkDay;
import com.icecrown.onyxridgecm.workseries.WorkMonth;
import java.io.File;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class CreateMonthlyReportFragment extends Fragment {

    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private Spinner jobNameSpinner;
    private String jobNameValue = "";
    private TextInputEditText jobYearInputField;
    private TextInputEditText jobMonthInputField;
    private PDFView reportView;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_monthly_total_report, container, false);

        jobYearInputField = v.findViewById(R.id.job_year_input_field);
        jobMonthInputField = v.findViewById(R.id.job_month_input_field);
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
            String monthString = jobMonthInputField.getText().toString();
            String yearString = jobYearInputField.getText().toString();
            if(monthString.equals("") || yearString.equals("")) {
                if(monthString.equals("")) {
                    jobMonthInputField.setError(getString(R.string.no_month_entered));
                }
                if(yearString.equals("")) {
                    jobYearInputField.setError(getString(R.string.no_year_entered));
                }
            }
            else {
                GenerateMonthlyReportEnter(monthString, yearString);
            }
        });
        return v;
    }

    private void GenerateMonthlyReportEnter(String month, String year) {
        CollectionReference daysInMonth = FirebaseFirestore.getInstance().collection("hours/" + jobNameValue + "/years/" + year + "/months/" + month.toLowerCase() + "/days");

        daysInMonth.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                List<DocumentSnapshot> days = task.getResult().getDocuments();
                if(days.size() != 0) {
                    int chosenMonthOffset = WorkMonth.DetermineMonthOffset(month.toLowerCase());
                    WorkMonth workMonth = new WorkMonth(Integer.parseInt(year), chosenMonthOffset);
                    for(DocumentSnapshot snapshot : days) {
                        WorkDay day = new WorkDay(new GregorianCalendar(Integer.parseInt(year), chosenMonthOffset, snapshot.getLong("day_of_month").intValue()), snapshot.getDouble("hours_worked"));
                        workMonth.SetDayAtInstance(day, snapshot.getLong("day_of_month").intValue() - 1);
                    }
                    File f = ReportFactory.GenerateMonthlyReport(workMonth, getContext(), jobNameValue);
                    if(f == null) {
                        // TODO: ADD REPORT GENERATION FAILED SNACKBAR
                        Snackbar.make(jobNameSpinner, R.string.report_not_made_admin, Snackbar.LENGTH_SHORT).show();
                    }
                    else {
                        Log.d("EPOCH-3", "Report generated.");
                        reportView.fromFile(f).load();
                    }
                }
                else {
                    Snackbar.make(jobNameSpinner, R.string.no_documents_found_with_month_day, Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }
}
