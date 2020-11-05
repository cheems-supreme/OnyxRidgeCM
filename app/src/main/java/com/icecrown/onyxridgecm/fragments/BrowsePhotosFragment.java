//**************************************************************
// Project: OnyxRidge Construction Management
//
// File: BrowsePhotosFragment.java
//
// Written by: Raymond O'Neill
//
// Written on: 11/4/2020
//
// Purpose: Used for logic regarding viewing and downloading
//          photos
//**************************************************************
package com.icecrown.onyxridgecm.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.icecrown.onyxridgecm.R;

public class BrowsePhotosFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_browse_content, container, false);


        return v;
    }
}
