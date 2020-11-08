//**************************************************************
// Project: OnyxRidge Construction Management
//
// File: SelectProjectFragment.java
//
// Written by: Raymond O'Neill
//
// Written on: 11/4/2020
//
// Purpose: Used for selecting projects from storage to browse
//          reports and photos in
// ------------------------------------------------
// UPDATES
// ------------------------------------------------
// - 11/6/2020
// - R.O.
// - DETAILS:
//      - Removed unused variable singleton
//**************************************************************
package com.icecrown.onyxridgecm.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.icecrown.onyxridgecm.R;
import com.icecrown.onyxridgecm.adapters.ProjectAdapter;
import com.icecrown.onyxridgecm.interfaces.IProjectSelectedCallback;

import java.util.List;

public class SelectProjectFragment extends Fragment {

    private List<StorageReference> projectList;
    private RecyclerView recView;
    private ProjectAdapter projectAdapter;
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private IProjectSelectedCallback callback;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_select_directory, container, false);

        recView = v.findViewById(R.id.project_rec_view);

        return v;
    }

    @Override
    public void onStart() {
        storage.getReference().listAll().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                    projectList = task.getResult().getPrefixes();
                    if(projectList.size() != 0) {
                        projectAdapter = new ProjectAdapter(projectList);
                        projectAdapter.setOnItemClickListener(position -> {
                            callback.onProjectSelected(projectList.get(position).getName());
                            getActivity().getSupportFragmentManager().popBackStack();
                        });
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                        recView.setLayoutManager(layoutManager);
                        recView.setItemAnimator(new DefaultItemAnimator());
                        recView.setAdapter(projectAdapter);
                        recView.addItemDecoration(new DividerItemDecoration(recView.getContext(), DividerItemDecoration.VERTICAL));
                    }
                    else {
                        Snackbar.make(recView, R.string.projects_not_found, Snackbar.LENGTH_SHORT).show();
                    }
            }
            else {
                Snackbar.make(recView, R.string.projects_not_loaded, Snackbar.LENGTH_LONG).show();
            }
        });
        super.onStart();
    }

    public void setCallback(IProjectSelectedCallback callback) {
        this.callback = callback;
    }
}
