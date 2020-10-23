//**************************************************************
// Project: OnyxRidge Construction Management
// File: LoginFragment.java

// Written by: Raymond O'Neill
// Written on: 10/23/2020
//
// Purpose: Used for login logic and redirection to main content
//          activity.
//**************************************************************
package com.icecrown.onyxridgecm;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

public class LoginFragment extends Fragment {
    private String emailAddress;
    private String password;
//    private SharedPreferences prefs = getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);



        return view;
    }
}
