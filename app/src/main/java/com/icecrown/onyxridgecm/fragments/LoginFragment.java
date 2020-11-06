//**************************************************************
// Project: OnyxRidge Construction Management
//
// File: LoginFragment.java
//
// Written by: Raymond O'Neill
//
// Written on: 10/23/2020
//
// Purpose: Used for login logic and redirection to main content
//          activity.
// ------------------------------------------------
// UPDATES
// ------------------------------------------------
// - 11/4/2020
// - R.O.
// - DETAILS:
//      - Added EditText and button variables.
//      - Placed this class in a new package:
//        onyxridgecm.fragments
// ------------------------------------------------
// - 11/5/2020
// - R.O.
// - DETAILS:
//      - Changed anonymous methods and classes to lambda expressions
// ------------------------------------------------
// - 11/6/2020
// - R.O.
// - DETAILS:
//      - Changed anonymous methods/classes to lambdas
//      - Made certain variables final
//      - Removed unused imports
//**************************************************************
package com.icecrown.onyxridgecm.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.icecrown.onyxridgecm.R;
import com.icecrown.onyxridgecm.activities.MainContentActivity;

public class LoginFragment extends Fragment {

    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;
    private MaterialButton signInButton;
    private MaterialTextView alertsMTextView;

    private SharedPreferences prefs;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth auth = FirebaseAuth.getInstance();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        emailEditText = view.findViewById(R.id.username_email_edit_text);
        passwordEditText = view.findViewById(R.id.password_edit_text);
        alertsMTextView = view.findViewById(R.id.alerts_text_view);


        signInButton = view.findViewById(R.id.sign_in_button);

        prefs = getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);


        signInButton.setOnClickListener(v -> {
            // Ensures button is not clicked more than once during sign in
            signInButton.setClickable(false);

            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            boolean isEmailBlank = email.isEmpty();
            boolean isPasswordBlank = password.isEmpty();

            if(isEmailBlank || isPasswordBlank) {
                if(isEmailBlank) {
                    emailEditText.setError(getString(R.string.email_not_entered));
                }
                if(isPasswordBlank) {
                    passwordEditText.setError(getString(R.string.password_not_entered));
                }
                signInButton.setClickable(true);
            }
            else {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        alertsMTextView.setText(R.string.login_succeeded);

                        db.collection("users").whereEqualTo("email", FirebaseAuth.getInstance().getCurrentUser().getEmail()).get()
                                .addOnCompleteListener(task1 -> {
                                    if(task1.isSuccessful()) {
                                        DocumentSnapshot snap = task1.getResult().getDocuments().get(0);
                                        if(snap.exists()) {
                                            SharedPreferences.Editor editor = prefs.edit();
                                            editor.putString("first_name", snap.getString("first_name"));
                                            editor.putString("last_name", snap.getString("last_name"));
                                            editor.putString("email", snap.getString("email"));
                                            // TODO: POTENTIALLY IMPLEMENT SECURITY FEATURES
                                            editor.apply();

                                            startActivity(MainContentActivity.GenerateIntent(getContext()));
                                            signInButton.setClickable(true);
                                        }
                                    }
                                    else {
                                        alertsMTextView.setText(R.string.login_failed_admin);
                                        signInButton.setClickable(true);
                                    }
                                });
                    } else {
                        alertsMTextView.setText(R.string.login_failed_creds);
                        passwordEditText.setText("");
                        signInButton.setClickable(true);
                    }
                });
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        alertsMTextView.setText("");
        passwordEditText.setText("");
        super.onResume();
    }
}
