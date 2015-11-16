package com.onclavesystems.cestemoeducare;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

public class EmailDialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_dialog);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        fillDetails();
        this.setFinishOnTouchOutside(false);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        View view = getWindow().getDecorView();
        WindowManager.LayoutParams lp = (WindowManager.LayoutParams) view.getLayoutParams();
        lp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
        lp.x = 0;
        lp.y = 0;
        lp.width = (int)(0.95 * width);
        lp.height = (int)(0.90 * height);
        getWindowManager().updateViewLayout(view, lp);
    }

    public void fillDetails(){
        SharedPreferences appPrefs = getSharedPreferences("com.onclavesystems.cestemoeducare_preferences", Context.MODE_PRIVATE);
        String registrationno = appPrefs.getString("registrationno", "");
        String name = appPrefs.getString("name","");
        EditText etRegistrationno = (EditText) findViewById(R.id.editTextRegistrationno);
        etRegistrationno.setText(registrationno);

        EditText etName = (EditText) findViewById(R.id.editTextName);
        etName.setText(name);
    }

}
