//**************************************************************
// Project: OnyxRidge Construction Management
//
// File: MainMenuFragment.java
//
// Written by: Raymond O'Neill
//
// Written on: 11/4/2020
//
// Purpose: Used for main menu logic, such as generating monthly
//          and yearly reports based on project.
// ------------------------------------------------
// UPDATES
// ------------------------------------------------
// - 11/6/2020
// - R.O.
// - DETAILS:
//      - Changed lambdas to expression lambdas
//      - Converted certain variables from class to local
// ------------------------------------------------
// - 11/10/2020
// - R.O.
// - DETAILS:
//      - Added code to launch fragment to upload existing photos
//**************************************************************
package com.icecrown.onyxridgecm.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.card.MaterialCardView;
import com.icecrown.onyxridgecm.R;

public class MainMenuFragment extends Fragment {
    private final MainMenuFragment singleton = this;
    private FragmentManager manager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_menu, container, false);

        manager = getActivity().getSupportFragmentManager();

        MaterialCardView createNewReportCardView = v.findViewById(R.id.create_new_report_card_view);
        createNewReportCardView.setOnClickListener(v1 -> manager.beginTransaction().hide(singleton).add(R.id.main_content_holder, new CreateNewReportFragment()).addToBackStack(null).commit());

        MaterialCardView uploadExistingReportCardView = v.findViewById(R.id.upload_existing_report_card_view);
        uploadExistingReportCardView.setOnClickListener(v1 -> manager.beginTransaction().hide(singleton).add(R.id.main_content_holder, new UploadExistingReportFragment()).addToBackStack(null).commit());

        MaterialCardView takeNewPhotoCardView = v.findViewById(R.id.take_new_photo_card_view);
        takeNewPhotoCardView.setOnClickListener(l -> manager.beginTransaction().hide(singleton).add(R.id.main_content_holder, new TakeNewPhotoFragment()).addToBackStack(null).commit());

        MaterialCardView uploadExistingPhotoCardView = v.findViewById(R.id.upload_existing_photo_card_view);
        uploadExistingPhotoCardView.setOnClickListener(l -> manager.beginTransaction().hide(singleton).add(R.id.main_content_holder, new UploadExistingPhotoFragment()).addToBackStack(null).commit());
        return v;
    }
}
