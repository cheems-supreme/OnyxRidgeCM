//**************************************************************
// Project: OnyxRidge Construction Management
//
// File: ViewPdfReportFragment.java
//
// Written by: Raymond O'Neill
//
// Written on: 11/9/2020
//
// Purpose: Used to view reports and other PDFs gathered from the
//          BrowseReportsFragment RecView
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
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.material.snackbar.Snackbar;
import com.icecrown.onyxridgecm.R;

import java.io.File;

public class ViewPdfReportFragment extends Fragment {

    private Uri chosenPdfUri = null;
    private File chosenPdfFile = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.view_report_file, container, false);


        PDFView reportView = v.findViewById(R.id.view_report_pdf_view);
        Log.d("EPOCH-3", "reportView made");

        reportView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {

            if(chosenPdfFile != null) {
                reportView.fromFile(chosenPdfFile).load();
            }
            else if (chosenPdfUri != null) {
                reportView.fromUri(chosenPdfUri).load();
            }
            else {
                Snackbar.make(reportView, R.string.pdf_viewing_report_not_loaded_admin, Snackbar.LENGTH_SHORT).show();
            }
        });


        return v;
    }

    public void setChosenPdf(Uri pdf) {
        chosenPdfUri = pdf;
    }
    public void setChosenPdf(File f) {
        chosenPdfFile = f;
    }
}
