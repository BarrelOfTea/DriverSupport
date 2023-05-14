package com.example.driversupport.presentation.ui.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;

import com.example.driversupport.R;

public class Activity extends AppCompatActivity {
    NavController navController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.v("aaa", "main activity is created from java class");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);

    }


    void setupNavigation(){
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.nav_graph);
        navController.setGraph(navGraph);
    }

}
