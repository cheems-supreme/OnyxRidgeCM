//**************************************************************
// Project: OnyxRidge Construction Management
//
// File: MainActivity.java
//
// Written by: Raymond O'Neill
//
// Written on: 10/23/2020
//
// Purpose: Starting point of the app; used to hold the login
//          fragment
// ------------------------------------------------
// UPDATES
// ------------------------------------------------
// - 11/4/2020
// - R.O.
// - DETAILS:
//      - Placed this class in a new package:
//        onyxridgecm.activities
// ========================================================
// ========================================================
//  PROJECT NOTE:
//      THIS CODE COMES, PRIMARILY, FROM THE PREVIOUS INSTALLMENT
//      OF THIS PROJECT. THE SPEED OF WHICH THIS PROGRAM WAS WRITTEN
//      IS SOLELY DUE TO THE FACT THAT MOST OF THE CODE EXISTED
//      ALREADY; IT SIMPLY NEEDED TO BE ADDED AND REFACTORED TO
//      FIT THE PROJECT AND METHOD OF CONSTRUCTION (FITTING FRAGMENT
//      USE, FOR INSTANCE)
//**************************************************************
package com.icecrown.onyxridgecm.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.icecrown.onyxridgecm.fragments.LoginFragment;
import com.icecrown.onyxridgecm.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().add(R.id.main_activity_fragment_holder, new LoginFragment()).commit();
    }

}