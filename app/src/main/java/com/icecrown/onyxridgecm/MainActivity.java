//**************************************************************
// Project: OnyxRidge Construction Management
// File: MainActivity.java

// Written by: Raymond O'Neill
// Written on: 10/23/2020
//
// Purpose: Starting point of the app; used to hold the login
//          fragment
//**************************************************************
package com.icecrown.onyxridgecm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

// TODO: PLACE THIS FRAGMENT ON THE BACKSTACK
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().add(R.id.main_activity_fragment_holder, new LoginFragment()).commit();
    }
}