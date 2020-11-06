//**************************************************************
// Project: OnyxRidge Construction Management
// File: MainContentActivity.java
//
// Written by: Raymond O'Neill
//
// Written on: 11/4/2020
//
// Purpose: Contains the main functionality of the app, from the
//          main tab, to the browse photos and reports tabs, and
//          the settings/help tab
// ------------------------------------------------
// UPDATES
// ------------------------------------------------
// - 11/5/2020
// - R.O.
// - DETAILS:
//      - Added code to remove backstack to -1 (empty) if
//        there is any backstack present.
//      - Began a way to ensure signout occurs when back button
//        is pressed when the user is on the home page.
//**************************************************************
package com.icecrown.onyxridgecm.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.icecrown.onyxridgecm.R;
import com.icecrown.onyxridgecm.fragments.BrowsePhotosFragment;
import com.icecrown.onyxridgecm.fragments.BrowseReportsFragment;
import com.icecrown.onyxridgecm.fragments.MainMenuFragment;
import com.icecrown.onyxridgecm.fragments.SettingsAndHelpFragment;
import com.icecrown.onyxridgecm.interfaces.IActiveFragmentChanged;

public class MainContentActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private FragmentManager manager = getSupportFragmentManager();

    private BrowseReportsFragment browseReportsFragment = new BrowseReportsFragment();
    private BrowsePhotosFragment browsePhotosFragment = new BrowsePhotosFragment();
    private SettingsAndHelpFragment settingsHelpFragment = new SettingsAndHelpFragment();
    private MainMenuFragment mainMenuFragment = new MainMenuFragment();
    private Fragment activeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_content);

        bottomNavigationView = findViewById(R.id.menu_nav_bar);

        manager.beginTransaction().add(R.id.main_content_holder, settingsHelpFragment).hide(settingsHelpFragment).commit();
        manager.beginTransaction().add(R.id.main_content_holder, browsePhotosFragment).hide(browsePhotosFragment).commit();
        manager.beginTransaction().add(R.id.main_content_holder, browseReportsFragment).hide(browseReportsFragment).commit();
        manager.beginTransaction().add(R.id.main_content_holder, mainMenuFragment).commit();
        activeFragment = mainMenuFragment;

        bottomNavigationView.setSelectedItemId(R.id.main_page);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if(manager.getBackStackEntryCount() != -1) {
                while(manager.getBackStackEntryCount() != -1) {
                    manager.popBackStack();
                }
            }
            switch (item.getItemId()) {
                case R.id.main_page:
                    manager.beginTransaction().hide(activeFragment).show(mainMenuFragment).commit();
                    activeFragment = mainMenuFragment;
                    break;
                case R.id.browse_reports_page:
                    manager.beginTransaction().hide(activeFragment).show(browseReportsFragment).commit();
                    activeFragment = browseReportsFragment;
                    break;
                case R.id.browse_photos_page:
                    manager.beginTransaction().hide(activeFragment).show(browsePhotosFragment).commit();
                    activeFragment = browsePhotosFragment;
                    break;
                case R.id.settings_and_help_page:
                    manager.beginTransaction().hide(activeFragment).show(settingsHelpFragment).commit();
                    activeFragment = settingsHelpFragment;
                    break;
            }
            return true;
        });
    }

    public static Intent GenerateIntent(Context c) {
        return new Intent(c, MainContentActivity.class);
    }

    @Override
    public void onBackPressed() {
        // TODO: WHEN BACKSTACK COUNT == -1
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.confirm_sign_out_title)
                .setMessage(R.string.sign_out_confirmation_desc)
                .setPositiveButton(R.string.)
        super.onBackPressed();
    }
}
