package com.onclavesystems.cestemoeducare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

public class EmailDialogActivity extends AppCompatActivity {

    private String registrationno = "", name = "", query = "", to="abhro.mukherjee093@gmail.com", subject = "Students query";
    private EditText et_query;
    private boolean instanceSaved = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_email_dialog);
        if(savedInstanceState != null) {
            query = savedInstanceState.getString("sv_query");
            instanceSaved = savedInstanceState.getBoolean("InstanceSaved");
        }


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
        et_query = (EditText) findViewById(R.id.input_querymail);
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
        registrationno = appPrefs.getString("registrationno", "");
        name = appPrefs.getString("name","");
        TextView tRegistrationno = (TextView) findViewById(R.id.textRegistrationno);
        tRegistrationno.setText(registrationno);

        TextView tName = (TextView) findViewById(R.id.textName);
        tName.setText(name);
    }
    
    public void onSubmitQuery(View view){

        if(validateQuery()) {
            if(isInternetOn()) {
                query = "Registration No.:" + registrationno + "\nName:" +name + "\nQuery:"+ et_query.getText().toString();
                sendByIntent();
            }else {
                Snackbar.make(view, "Error connecting to net", Snackbar.LENGTH_SHORT).show();
            }
        } else {
            Snackbar.make(view, "Query field cannot be empty", Snackbar.LENGTH_LONG).show();
        }
    }

    public void sendByIntent(){
        Intent email = new Intent(Intent.ACTION_SEND);
        // prompts email clients only
        email.setType("message/rfc822");
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
        email.putExtra(Intent.EXTRA_SUBJECT, subject);
        email.putExtra(Intent.EXTRA_TEXT, query);
        try {
            // the user can choose the email client
            startActivity(Intent.createChooser(email, "Choose an email client from..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Snackbar.make(findViewById(R.id.linearLayout1), "No email client installed", Snackbar.LENGTH_LONG).show();
        }
        finally {
            et_query.setText("");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("sv_query", et_query.getText().toString());
        savedInstanceState.putBoolean("InstanceSaved", true);
    }
    
    private boolean validateQuery() {
        EditText query = (EditText) findViewById(R.id.input_querymail);
        if (TextUtils.isEmpty((query).getText().toString().trim())) {
            return false;
        } else {
            return true;
        }
    }
    
    private boolean isInternetOn() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;

        }
    }

}
