package com.example.exameneventos.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.MenuItem;

import androidx.appcompat.app.AlertDialog;

import com.example.exameneventos.R;
import com.example.exameneventos.auth.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MenuManager {

    private Activity activity;
    private FirebaseAuth mAuth;

    public MenuManager(Activity activity) {
        this.activity = activity;
        this.mAuth = FirebaseAuth.getInstance();
    }

    public boolean handleItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            performLogout();
            return true;
        } else if (id == R.id.action_settings) {
            showViewModeDialog();
            return true;
        }
        return false;
    }

    private void performLogout() {
        mAuth.signOut();
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    private void showViewModeDialog() {
        String[] options = {"List View (Image + Text)", "Grid View (Image Only)"};
        boolean isGridMode = loadGridPreference();
        int checkedItem = isGridMode ? 1 : 0;

        new AlertDialog.Builder(activity)
                .setTitle("Select View Mode")
                .setSingleChoiceItems(options, checkedItem, (dialog, which) -> {
                    boolean newMode = (which == 1);
                    saveGridPreference(newMode);
                    dialog.dismiss();

                    activity.recreate();
                })
                .show();
    }

    private void saveGridPreference(boolean isGrid) {
        SharedPreferences prefs = activity.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        prefs.edit().putBoolean("grid_mode", isGrid).apply();
    }

    public boolean loadGridPreference() {
        SharedPreferences prefs = activity.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        return prefs.getBoolean("grid_mode", false);
    }
}