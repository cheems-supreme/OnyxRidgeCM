//**************************************************************
// Project: OnyxRidge Construction Management
//
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
// ------------------------------------------------
// - 11/6/2020
// - R.O.
// - DETAILS:
//      - Changed a loop that pops back stack to just pop back
//        once if stack isn't empty (stack is only instantiated
//        at certain points).
//      - Commented out onBackPressed(...) method to allow code
//        compile.
//      - Changed switch statement that handles the changing of
//        fragments to an if/else if statement block (view IDs
//        are not final in Gradle 5.0, and switch statements need
//        final values for cases)
//**************************************************************
package com.icecrown.onyxridgecm.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.icecrown.onyxridgecm.R;
import com.icecrown.onyxridgecm.fragments.BrowsePhotosFragment;
import com.icecrown.onyxridgecm.fragments.BrowseReportsFragment;
import com.icecrown.onyxridgecm.fragments.MainMenuFragment;
import com.icecrown.onyxridgecm.fragments.HelpAndFaqFragment;

public class MainContentActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private final FragmentManager manager = getSupportFragmentManager();
    private final BrowseReportsFragment browseReportsFragment = new BrowseReportsFragment();
    private final BrowsePhotosFragment browsePhotosFragment = new BrowsePhotosFragment();
    private final HelpAndFaqFragment settingsHelpFragment = new HelpAndFaqFragment();
    private final MainMenuFragment mainMenuFragment = new MainMenuFragment();
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
                manager.popBackStack();
            }

            int itemId = item.getItemId();
            if(itemId == R.id.main_page) {
                manager.beginTransaction().hide(activeFragment).show(mainMenuFragment).commit();
                activeFragment = mainMenuFragment;
            }
            else if(itemId == R.id.browse_reports_page) {
                manager.beginTransaction().hide(activeFragment).show(browseReportsFragment).commit();
                activeFragment = browseReportsFragment;
            }
            else if(itemId == R.id.browse_photos_page) {
                manager.beginTransaction().hide(activeFragment).show(browsePhotosFragment).commit();
                activeFragment = browsePhotosFragment;
            }
            else if(itemId == R.id.settings_and_help_page) {
                manager.beginTransaction().hide(activeFragment).show(settingsHelpFragment).commit();
                activeFragment = settingsHelpFragment;
            }
            return true;
        });
    }

    public static Intent GenerateIntent(Context c) {
        return new Intent(c, MainContentActivity.class);
    }

//    @Override
//    public void onBackPressed() {
//        // TODO: WHEN BACKSTACK COUNT == -1
//        new MaterialAlertDialogBuilder(this)
//                .setTitle(R.string.confirm_sign_out_title)
//                .setMessage(R.string.sign_out_confirmation_desc)
//                .setPositiveButton(R.string.)
//        super.onBackPressed();
//    }
}
