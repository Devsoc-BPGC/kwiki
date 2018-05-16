package com.example.visiondemo;

import android.app.Application;

import com.google.firebase.FirebaseApp;

/**
 * @author Rushikesh Jogdand.
 */
public class VisionDemo extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
