//**************************************************************
// Project: OnyxRidge Construction Management
//
// File: CreateNewReportFragment.java
//
// Written by: Raymond O'Neill
//
// Written on: 11/5/2020
//
// Purpose: Used for logic behind creating reports
// ------------------------------------------------
// UPDATES
// ------------------------------------------------
// - 11/6/2020
// - R.O.
// - DETAILS:
//      - Removed unused variable, singleton
//      - Added variable to reduce calls to FirebaseStorage
//        .getInstance()
//      - Added check to see if backstack is -1 (empty) upon
//        file upload completion. If it isn't (backstack exists),
//        then pop it; else, do nothing.
//      - Simplified if/else statement
//      - Removed unused imports
// ------------------------------------------------
// - 11/8/2020
// - R.O.
// - DETAILS:
//      - Added code to close 'document' and 'writer' instances
//        at end of writing to file
// ------------------------------------------------
// - 11/12/2020
// - R.O.
// - DETAILS:
//      - Changed SharedPreferences fields from incorrect fields
//        to correct field names
//      - Truncated StorageReference directory location into
//        one line
// ------------------------------------------------
// - 11/20/2020
// - R.O.
// - DETAILS:
//      - Reformatted comment header
//      - Altered method names to lower camel case
//      - Reformatted `import` list
// ------------------------------------------------
// - 11/24/2020
// - R.O.
// - DETAILS:
//      - Added code to insert an entry into Firestore for
//        information regarding an accident occuring on-site
//        for any given day through the report generation page.
//      - Commented two variables to define what they represent
//        better
//      - Fixed report generation to stop headers printing twice.
// ------------------------------------------------
// - 11/27/2020
// - R.O.
// - DETAILS:
//      - Added method to solidify month and year documents inside
//        Firebase if they don't already exist.
//      - Called method in report upload block
// ------------------------------------------------
// - 12/14/2020
// - R.O.
// - DETAILS:
//      - Added code to perform a regular expression check on
//        the input entered from 'Hours Worked'
//**************************************************************
package com.icecrown.onyxridgecm.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import com.icecrown.onyxridgecm.R;
import com.icecrown.onyxridgecm.utility.ReportFactory;
import com.icecrown.onyxridgecm.workseries.WorkMonth;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CreateNewReportFragment extends Fragment {

    private Spinner jobNameSpinner;
    private DatePicker dateChooser;
    private TextInputEditText workersOnSiteEditText;
    private TextInputEditText hoursWorkedEditText;
    private TextInputEditText dailyLogEditText;
    private Spinner weatherSpinner;
    private MaterialCheckBox accidentHappenedCheckBox;
    private TextInputEditText accidentDetails;
    private MaterialButton submitReportButton;
    private String jobNameValue = "";
    private String weatherValue = "";
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final FirebaseFirestore store = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_new_report, container, false);

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

        dateChooser = v.findViewById(R.id.date_chooser);
        dateChooser.setMaxDate(Calendar.getInstance().getTimeInMillis());

        workersOnSiteEditText = v.findViewById(R.id.workers_on_site_edit_text);
        hoursWorkedEditText = v.findViewById(R.id.hours_worked_edit_text);
        dailyLogEditText = v.findViewById(R.id.daily_log_edit_text);

        weatherSpinner = v.findViewById(R.id.weather_spinner);
        ArrayAdapter<CharSequence> weathers = ArrayAdapter.createFromResource(getContext(), R.array.weather_values, android.R.layout.simple_spinner_item);
        weathers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weatherSpinner.setAdapter(weathers);
        weatherSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                weatherValue = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        accidentHappenedCheckBox = v.findViewById(R.id.accident_happened_check_box);
        accidentHappenedCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            accidentDetails.setActivated(isChecked);
        });
        accidentDetails = v.findViewById(R.id.accident_happened_description);
        accidentDetails.setActivated(false);

        submitReportButton = v.findViewById(R.id.generate_report_submit_button);
        submitReportButton.setOnClickListener(v1 -> {
            if(!areAnyFieldsBlank()) {
                new Thread(() -> {
                    File f = ReportFactory.generateFile(getContext(), getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE));

                    SharedPreferences prefs = getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);

                    int workersOnSite = Integer.parseInt(workersOnSiteEditText.getText().toString());
                    float hoursPerWorker = Float.parseFloat(hoursWorkedEditText.getText().toString());

                    Document document = ReportFactory.initializeDocumentAndHeader(f, getContext(), jobNameValue);
                    ReportFactory.addDateContent(getContext(), document, String.format(Locale.getDefault(), "%d/%d/%d", (dateChooser.getMonth() + 1), dateChooser.getDayOfMonth(), dateChooser.getYear()));
                    ReportFactory.addCreatedByContent(getContext(), document, String.format(Locale.getDefault(), "%s %s", prefs.getString("first_name", "null"), prefs.getString("last_name", "null")));
                    ReportFactory.addWorkersHoursAndTotal(getContext(), document, workersOnSite, hoursPerWorker);
                    ReportFactory.addDailyLogContent(getContext(), document, dailyLogEditText.getText().toString());
                    ReportFactory.addLineSeparator(document);
                    ReportFactory.addWeatherContent(getContext(), document, weatherValue);
                    ReportFactory.addAccidentOccurredContent(getContext(), document, accidentHappenedCheckBox.isChecked(), accidentDetails.getText().toString());
                    // TODO:
                    ReportFactory.uploadPhotosToDoc(getContext(), document, new File[0]);
                    //ReportFactory.UploadPhotosToDoc(getContext(), document, imagesChosen);
                    try {

                        PdfWriter writer = document.getPdfDocument().getWriter();
                        ReportFactory.closeDocument(document);
                        ReportFactory.closeWriter(writer);
                    } catch(IOException ioe) {
                        Log.d("EPOCH-3", "IOException encountered on PdfWriter.close() in file CreateNewReportFragment.java");
                    }

                    insertAnchorForYearAndMonth();
                    insertTotalHoursIntoDB(workersOnSite * hoursPerWorker);
                    uploadFileToStorageAndClose(Uri.fromFile(f), f.getName());
                    if(accidentHappenedCheckBox.isChecked()) {
                        insertAccidentReportIntoDB(accidentDetails.getText().toString());
                    }
                }).start();
            }
            else {
                Log.d("EPOCH-3", "File not uploaded or generated.");
            }

        });

        return v;
    }

    private boolean areAnyFieldsBlank() {
        boolean doesAFieldHaveAnError = false;

        if(jobNameValue.equals("") || jobNameValue.equals(getString(R.string.job_name_default_value))) {
            Snackbar.make(jobNameSpinner, R.string.job_name_not_chosen, Snackbar.LENGTH_SHORT).show();
            doesAFieldHaveAnError = true;
        }
        if(workersOnSiteEditText.getText().toString().isEmpty()) {
            workersOnSiteEditText.setError(getString(R.string.workers_on_site_not_entered));
            doesAFieldHaveAnError = true;
        }
        if(hoursWorkedEditText.getText().toString().isEmpty()) {
            hoursWorkedEditText.setError(getString(R.string.hours_worked_not_entered));
            doesAFieldHaveAnError = true;
        }
        String hoursWorkedString = hoursWorkedEditText.getText().toString();
        if((!hoursWorkedString.matches("\\d{1,2}.\\d{1,2}")) && (hoursWorkedString.length() < 3 || hoursWorkedString.length() > 5)) {
            hoursWorkedEditText.setError(getString(R.string.hours_worked_not_formatted_properly));
            doesAFieldHaveAnError = true;
        }
        if(dailyLogEditText.getText().toString().isEmpty()) {
            dailyLogEditText.setError(getString(R.string.daily_log_not_entered));
            doesAFieldHaveAnError = true;
        }
        if(weatherValue.equals("") || weatherValue.equals(getString(R.string.weather_default_value))) {
            Snackbar.make(weatherSpinner, R.string.weather_not_chosen, Snackbar.LENGTH_SHORT).show();
            doesAFieldHaveAnError = true;
        }
        if(accidentHappenedCheckBox.isChecked() && accidentDetails.getText().toString().isEmpty()) {
            accidentDetails.setError(getString(R.string.accident_happened_not_entered));
            doesAFieldHaveAnError = true;
        }
        return doesAFieldHaveAnError;
    }

    private void insertTotalHoursIntoDB(final double totalHours) {
        // Updates total hours for project
        final CollectionReference hoursRef = store.collection("hours");
        // Inserts actual info into DB
        final CollectionReference monthHoursRef = store.collection("hours/" + jobNameValue + "/years/" + dateChooser.getYear() + "/months/" + WorkMonth.determineMonthName(dateChooser.getMonth()).toLowerCase() + "/days");
        final DocumentReference docRef = monthHoursRef.document(String.valueOf(dateChooser.getDayOfMonth()));

        docRef.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                DocumentSnapshot doc = task.getResult();
                if(doc.exists()) {
                    Log.d("EPOCH-2", "Document exists");
                }
                else {
                    Map<String, Object> hours = new HashMap<>();
                    hours.put("day_of_month", dateChooser.getDayOfMonth());
                    hours.put("hours_worked", totalHours);
                    docRef.set(hours);
                }
            }
            else {
                Log.d("EPOCH-2", "Document gathering failed.");
            }
        });
        final DocumentReference totalHoursRef = hoursRef.document(jobNameValue);
        totalHoursRef.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                DocumentSnapshot doc = task.getResult();
                Map<String, Object> newHours = new HashMap<>();
                Double totalHoursPrev;

                if(task.getResult().exists()) {
                    Log.d("EPOCH-2", "Document exists");
                    totalHoursPrev = doc.getDouble("total_hours");
                    newHours.put("total_hours", totalHoursPrev + totalHours);
                    totalHoursRef.update(newHours);
                } else {
                    Log.d("EPOCH-2", "Document doesn't exist");
                    totalHoursPrev = totalHours;
                    newHours.put("total_hours", totalHoursPrev);
                    totalHoursRef.set(newHours);
                }

            }
        });
    }

    private void uploadFileToStorageAndClose(Uri chosenFile, final String fileName)
    {
        SharedPreferences prefs = getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);

        final StorageReference insertionInstance = storage.getReference().child(jobNameValue + "/documents/" + fileName);

        Calendar cal = Calendar.getInstance();

        final String todaysDate = (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR); // Calendar.MONTH is offset by 1 (?) (no explanation why)

        insertionInstance.putFile(chosenFile).addOnCompleteListener(task -> {
            StorageMetadata metadata = new StorageMetadata.Builder().setContentType("application/pdf")
                    .setCustomMetadata("first_name", prefs.getString("first_name", "Unavail."))
                    .setCustomMetadata("last_name", prefs.getString("last_name", "Unavail."))
                    .setCustomMetadata("date_created", todaysDate)
                    .setCustomMetadata("date_of_content", (dateChooser.getMonth() + 1) + "/" + dateChooser.getDayOfMonth() + "/" + dateChooser.getYear())
                    .setCustomMetadata("document_name", fileName)
                    .setCustomMetadata("accident_happened", String.valueOf(accidentHappenedCheckBox.isChecked())).build();
            insertionInstance.updateMetadata(metadata).addOnCompleteListener(task1 -> {
                if(task1.isSuccessful()) {
                    Snackbar.make(submitReportButton, R.string.report_upload_success, Snackbar.LENGTH_SHORT).show();
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    if(manager.getBackStackEntryCount() != -1) {
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                }
                else {
                    Snackbar.make(submitReportButton, R.string.upload_report_failed, Snackbar.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void insertAccidentReportIntoDB(String accidentReportLog) {
        String dayOfAccident = (dateChooser.getMonth() + 1) + "/" + dateChooser.getDayOfMonth() + "/" + dateChooser.getYear();
        String dayOfAccidentDir = (dateChooser.getMonth() + 1) + "_" + dateChooser.getDayOfMonth() + "_" + dateChooser.getYear();
        CollectionReference accidentDirectory = store.collection("accidents/" + jobNameValue + "/logs");
        DocumentReference accidentInsertionInstance = accidentDirectory.document(dayOfAccidentDir);
        Map<String, Object> accidentInsert = new HashMap<>();
        accidentInsert.put("accident_log", accidentReportLog);
        accidentInsert.put("weather_conditions", weatherValue);
        accidentInsert.put("date_of_accident", dayOfAccident);

        accidentInsertionInstance.set(accidentInsert).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Log.d("EPOCH-3", "Accident entry placed successfully");
            } else {
                Log.d("EPOCH-3", "Accident entry placement failed. Cause: ");
                task.getException().printStackTrace();
                Snackbar.make(jobNameSpinner, R.string.accident_report_not_entered_admin, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void insertAnchorForYearAndMonth() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference yearRef = db.collection("hours/" + jobNameValue + "/years").document(String.valueOf(dateChooser.getYear()));
        DocumentReference monthRef = db.collection("hours/" + jobNameValue + "/years/" + dateChooser.getYear() + "/months").document(WorkMonth.determineMonthName(dateChooser.getMonth()).toLowerCase());

        yearRef.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                if(!task.getResult().exists()) {
                    Map<String, String> input = new HashMap<>();
                    input.put("anchor", "anchor");
                    yearRef.set(input);
                }
                else {
                    Log.d("EPOCH-3", "Year Document already exists.");
                }
            }
            else {
                Snackbar.make(jobNameSpinner, R.string.report_not_made_admin, Snackbar.LENGTH_SHORT).show();
            }
        });

        monthRef.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                if(!task.getResult().exists()) {
                    Map<String, String> input = new HashMap<>();
                    input.put("anchor", "anchor");
                    monthRef.set(input);
                }
                else {
                    Log.d("EPOCH-3", "Month Document already exists.");
                }
            }
            else {
                Snackbar.make(jobNameSpinner, R.string.report_not_made_admin, Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
