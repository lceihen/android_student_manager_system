package com.example.project;
import android.app.Activity; 
import android.graphics.Bitmap; 
import android.os.Bundle; 
import android.util.Log; 
import android.widget.ImageView;

public class ShowImageActivity extends Activity {
    private String logTag = "exception";

    private ImageView view; 
    @Override 
    protected void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.photoshow);

        try { 
            view = (ImageView) findViewById(R.id.imageView1);   
            Bundle bundle = this.getIntent().getExtras(); 
            Bitmap b = bundle.getParcelable("image"); 
            view.setImageBitmap(b); 
            //setContentView(view); 
        } catch (Exception e) { 
            Log.v(logTag, e.getMessage()); 
            throw new RuntimeException(e); 
        } 
    } 
}