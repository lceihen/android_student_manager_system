package com.example.project;
import android.app.Activity; 
import android.content.Intent; 
import android.graphics.Bitmap; 
import android.os.Bundle; 
import android.provider.MediaStore; 
import android.util.Log;

public class PhotographActivity extends Activity { 
    /** Called when the activity is first created. */ 
    private String logTag = "Exception"; 
    @Override 
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.photo); 

    }
}