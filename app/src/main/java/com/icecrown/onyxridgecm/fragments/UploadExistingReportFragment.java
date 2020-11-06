package com.icecrown.onyxridgecm.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.icecrown.onyxridgecm.R;

import java.util.ArrayList;
import java.util.List;

public class UploadExistingReportFragment extends Fragment {
    private String jobNameValue = "";
    private final int PICK_FILE_TO_UPLOAD = 0;
    private Uri chosenFile;
    private PDFView pdfView;
    private MaterialTextView workingLoadingTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_upload_existing_files, container, false);

        pdfView = v.findViewById(R.id.file_preview_viewer);
        workingLoadingTextView = v.findViewById(R.id.working_and_loading_text_view);

        MaterialButton pickFileButton = v.findViewById(R.id.select_file_from_explorer);

        pickFileButton.setOnClickListener(l -> {
            OpenFileFromDirectory();
        });

        final Spinner jobNameSpinner = v.findViewById(R.id.job_name_spinner);
        FirebaseStorage.getInstance().getReference().listAll().addOnCompleteListener(new OnCompleteListener<ListResult>() {
            @Override
            public void onComplete(@NonNull Task<ListResult> task) {
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
            }
        });


        return v;
    }

    private void OpenFileFromDirectory() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("application/pdf");
        i.putExtra(DocumentsContract.EXTRA_INITIAL_URI, chosenFile);
        startActivityForResult(i, PICK_FILE_TO_UPLOAD);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_FILE_TO_UPLOAD && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                chosenFile = data.getData();
                workingLoadingTextView.setText(R.string.loading_in_progress);
                pdfView.fromUri(chosenFile);
                workingLoadingTextView.setText("");
            }
        }
    }
}
