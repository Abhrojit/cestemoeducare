package com.onclavesystems.cestemoeducare;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ADITI extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private int menuID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aditi);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = getSharedPreferences("hiddenpref", Context.MODE_PRIVATE);
                boolean registered = sp.getBoolean("registered",false);

                if(registered) {
                    Intent intent = new Intent(ADITI.this, EmailDialogActivity.class);
                    startActivity(intent);


                }else{
                    Snackbar.make(view, getString(R.string.no_registered_account), Snackbar.LENGTH_LONG).show();
                    String TAG = "SETTINGS";
                    Fragment fragment = new SettingsFragment();
                    swapFragments(fragment,TAG);
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(savedInstanceState != null) {
            menuID = savedInstanceState.getInt("menuID");
        }

        checkMenuID(menuID);
    }


    public void checkMenuID(int id) {
        if(id == 0) {
            swapFragments(new HomeFragment(), "HOME");
        }
        else {
            onItemForFragmentSelected(menuID);
        }
    }

    public void swapFragments(Fragment fragment, String TAG) {
        Fragment savedFragment = null;
        if(fragment != null) {
            savedFragment = getFragmentManager().findFragmentByTag(TAG);
            FragmentTransaction ft = getFragmentManager().beginTransaction();

            if(savedFragment != null) {
                ft.replace(R.id.content_frame, savedFragment, TAG);
            }
            else {
                ft.replace(R.id.content_frame, fragment, TAG);
            }

            ft.addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        }
        else {
            Snackbar.make(findViewById(R.id.drawer_layout),"Fragment not initiated", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        CreateMenu(menu);
        return  true;
    }

    private void CreateMenu(Menu menu) {
        menu.add(0, 0, 0, "settings");
        menu.add(0, 1, 1, "About");
        menu.add(0, 2, 2, "exit");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return MenuChoice(item);
    }

    public boolean MenuChoice(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                String TAG = "SETTINGS";
                Fragment fragment = new SettingsFragment();
                swapFragments(fragment,TAG);
                return true;
            case 1:
                Intent intent = new Intent(getBaseContext(), HomeActivity.class);
                startActivity(intent);
                return true;

            case 2:
                new AlertDialog.Builder(this)
                        .setTitle("Really Exit?")
                        .setMessage("Are you sure you want to exit?")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                finish();
                            }
                        }).create().show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        menuID = item.getItemId();

        onItemForFragmentSelected(menuID);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onItemForFragmentSelected(int id) {
        Fragment fragment = null;
        String TAG = "HOME";

        if (id == R.id.nav_home) {
            // Handle the home action
            TAG = "HOME";
            fragment = new HomeFragment();
        } else if (id == R.id.nav_location) {
            //Handle the location action
            TAG = "LOCATION";
            fragment = new LocationFragment();
        } else if (id == R.id.nav_about) {
            //Handle the about action here
        } else if (id == R.id.nav_contact) {
            //Handle the contact action here
            TAG = "CONTACT";
            fragment = new ContactUsFragment();
        } else if (id == R.id.nav_email) {
            //Handle the email action here
            TAG = "EMAIL";
            fragment = new EmailFragment();
        } else if(id == R.id.nav_setting) {
            //Handle the setting action here
            TAG = "SETTINGS";
            fragment = new SettingsFragment();
        } else if(id == R.id.nav_website) {
            //Handle the website action here
            if(isInternetOn()) {
                TAG = "WEBSITE";
                fragment = new WebsiteFragment();
            }
        } else {
            TAG = "HOME";
            fragment = new HomeFragment();
        }

        swapFragments(fragment, TAG);
    }

    public void click_location(View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Contact this center via. phone call?");

        alertDialogBuilder.setPositiveButton("Call", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                try {

                    String uri = "tel:8017741809";
                    Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(uri));

                    if ( ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED ) {
                        startActivity(callIntent);
                    }else {
                        callIntent = new Intent((Intent.ACTION_DIAL), Uri.parse(uri));
                        startActivity(callIntent);
                    }
                }catch(Exception e) {
                    Snackbar.make(findViewById(R.id.content_frame), R.string.call_permission_error, Snackbar.LENGTH_LONG).show();
                }

            }
        });

        alertDialogBuilder.setNegativeButton("Deny",null);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("menuID", menuID);
    }

    public boolean isInternetOn() {
        ConnectivityManager cm = (ConnectivityManager)getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if(!isConnected) {
            Snackbar.make(findViewById(R.id.content_frame), R.string.no_internet_connectivity, Snackbar.LENGTH_INDEFINITE).show();
        }

        return isConnected;
    }

}
