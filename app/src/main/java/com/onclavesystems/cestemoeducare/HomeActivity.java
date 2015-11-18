package com.onclavesystems.cestemoeducare;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class HomeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //setFonts();
    }

    public void setFonts() {
        TextView annaLogo = (TextView) findViewById(R.id.annaLogo);
        TextView cestemoSoftware = (TextView) findViewById(R.id.cestemoSoftware);
        TextView developed = (TextView) findViewById(R.id.developed);
        TextView onclaveLogo = (TextView) findViewById(R.id.onclaveLogo);
        TextView venture = (TextView) findViewById(R.id.venture);
        TextView onclave = (TextView) findViewById(R.id.onclave);

        Typeface type01 = Typeface.createFromAsset(getAssets(), "fonts/plymouth.ttf");
        Typeface type02 = Typeface.createFromAsset(getAssets(), "fonts/rounded_elegance.ttf");

        annaLogo.setTypeface(type01);
        cestemoSoftware.setTypeface(type02);
        developed.setTypeface(type02);
        onclaveLogo.setTypeface(type01);
        venture.setTypeface(type02);
        onclave.setTypeface(type02);
    }
}
