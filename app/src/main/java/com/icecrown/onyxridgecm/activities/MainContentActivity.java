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
import com.icecrown.onyxridgecm.R;
import com.icecrown.onyxridgecm.fragments.BrowsePhotosFragment;
import com.icecrown.onyxridgecm.fragments.BrowseReportsFragment;
import com.icecrown.onyxridgecm.fragments.MainMenuFragment;
import com.icecrown.onyxridgecm.fragments.SettingsAndHelpFragment;

public class MainContentActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private BrowseReportsFragment broseReports = new BrowseReportsFragment();

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

        // TODO: EITHER INSTANTIATE AND INFLATE FRAGMENTS FIRST,
        //       FOR QUICK ADDING, OR

        manager.beginTransaction().add(R.id.main_content_holder, settingsHelpFragment).hide(settingsHelpFragment).commit();
        manager.beginTransaction().add(R.id.main_content_holder, browsePhotosFragment).hide(browsePhotosFragment).commit();
        manager.beginTransaction().add(R.id.main_content_holder, browseReportsFragment).hide(browseReportsFragment).commit();
        manager.beginTransaction().add(R.id.main_content_holder, mainMenuFragment).commit();
        activeFragment = mainMenuFragment;

        bottomNavigationView.setSelectedItemId(R.id.main_page);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
            }
        });
    }


    public static Intent GenerateIntent(Context c) {
        return new Intent(c, MainContentActivity.class);
    }

    public interface SetActiveFragmentCallback {
        void setActiveFragment(Fragment newActive);
    }
}
