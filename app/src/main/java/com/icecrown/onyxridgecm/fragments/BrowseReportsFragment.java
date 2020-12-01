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
// ------------------------------------------------
// - 12/1/2020
// - R.O.
// - DETAILS:
//      - Added code to filter and sort the content inside
//        of the list.
//**************************************************************
package com.icecrown.onyxridgecm.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

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
import com.icecrown.onyxridgecm.enums.FilterType;
import com.icecrown.onyxridgecm.enums.SortType;
import com.icecrown.onyxridgecm.interfaces.IProjectSelectedCallback;
import com.icecrown.onyxridgecm.utility.Document;
import com.icecrown.onyxridgecm.utility.Photo;
import com.icecrown.onyxridgecm.utility.ReportFactory;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class BrowseReportsFragment extends Fragment {


    private MaterialTextView projectNameTextView;
    private String projectNameString = "";
    private final List<Document> documentList = new ArrayList<>();
    private DocumentAdapter documentAdapter;
    private final BrowseReportsFragment singleton = this;
    private MaterialTextView sortTV;
    private MaterialTextView filterTV;

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

        sortTV = v.findViewById(R.id.sort_text_view);
        sortTV.setOnClickListener(l -> {
            PopupMenu sortOptions = new PopupMenu(getActivity(), sortTV);
            sortOptions.getMenuInflater().inflate(R.menu.menu_sort_options, sortOptions.getMenu());
            sortOptions.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                if(itemId == R.id.no_sort) {
                    Log.d("EPOCH-3", "No sort entered.");
                    documentAdapter.setDocs(documentList);
                }
                else if(itemId == R.id.last_name_sort_asc) {
                    Log.d("EPOCH-3", "last name asc sort entered.");
                    sortDocumentsByOption(documentAdapter.getDocuments(), SortType.LAST_NAME_ASC);
                }
                else if(itemId == R.id.last_name_sort_desc) {
                    Log.d("EPOCH-3", "last name desc sort entered.");
                    sortDocumentsByOption(documentAdapter.getDocuments(), SortType.LAST_NAME_DESC);
                }
                else if(itemId == R.id.date_sort_asc) {
                    Log.d("EPOCH-3", "date asc sort entered.");
                    sortDocumentsByOption(documentAdapter.getDocuments(), SortType.DATE_ASC);
                }
                else if(itemId == R.id.date_sort_desc) {
                    Log.d("EPOCH-3", "date desc sort entered.");
                    sortDocumentsByOption(documentAdapter.getDocuments(), SortType.DATE_DESC);
                }
                documentAdapter.notifyDataSetChanged();
                return true;
            });
            sortOptions.show();
        });

        filterTV = v.findViewById(R.id.filter_text_view);
        filterTV.setOnClickListener(l -> {
            PopupMenu filterOptions = new PopupMenu(getActivity(), filterTV);
            filterOptions.getMenuInflater().inflate(R.menu.menu_filter_options, filterOptions.getMenu());
            filterOptions.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.no_filter) {
                    documentAdapter.setDocs(documentList);
                }
                else if (itemId == R.id.accident_filter) {
                    documentAdapter.setDocs(GetListOnFilterOnAdapter(documentList, FilterType.ACCIDENT_HAPPENED));
                }
                documentAdapter.notifyDataSetChanged();
                return true;
            });
            filterOptions.show();
        });

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

    private void sortDocumentsByOption(List<Document> docs, SortType type) {
        Comparator<Document> c;

        switch(type) {
            case LAST_NAME_ASC:
                c = (o1, o2) -> {
                    int docLastVal = o1.getAuthorByLast().compareTo(o2.getAuthorByLast());
                    return Integer.compare(docLastVal, 0);
                };
                break;
            case LAST_NAME_DESC:
                c = (o1, o2) -> {
                    int docLastVal = o1.getAuthorByLast().compareTo(o2.getAuthorByLast());
                    return Integer.compare(docLastVal, 0);
                };
                c = c.reversed();
                break;
            case DATE_ASC:
                c = (o1, o2) -> {
                    if(o1.getDateOfContent().before(o2.getDateOfContent())) {
                        return -1;
                    }
                    else if(o1.getDateOfContent().after(o2.getDateOfContent())) {
                        return 1;
                    }
                    else {
                        return 0;
                    }
                };
                break;
            case DATE_DESC:
                c = (o1, o2) -> {
                    if(o1.getDateOfContent().before(o2.getDateOfContent())) {
                        return -1;
                    }
                    else if(o1.getDateOfContent().after(o2.getDateOfContent())) {
                        return 1;
                    }
                    else {
                        return 0;
                    }
                };
                c = c.reversed();
                break;
            default:
                c = null;
                break;
        }
        docs.sort(c);
    }

    private List<Document> GetListOnFilterOnAdapter(List<Document> documents, FilterType type) {
        switch(type) {
            case NO_FILTER:
                return documentList;
            case ACCIDENT_HAPPENED:
                List<Document> accidentOccurredDocs = new ArrayList<>();

                for(Document d : documents) {
                    if(d.hasAccident()) {
                        accidentOccurredDocs.add(d);
                    }
                    else {
                        Log.d("EPOCH-3", d.getFilename() + " accident status: " + d.hasAccident());
                    }
                }
                return accidentOccurredDocs;
            default:
                return new ArrayList<>();
        }
    }
}
