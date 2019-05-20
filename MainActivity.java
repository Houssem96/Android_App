package com.example.ayed.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent i = getIntent();
        String r = i.getStringExtra(Welcome.USERNAME);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "For more details contact us on +21652286501 \nor visit our website www.WantIT.com", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


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



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent i = getIntent();
        String r = i.getStringExtra(Welcome.USERNAME);
        if (id == R.id.nav_profil) {

            Intent intentProfil = new Intent(this, profile.class);
            intentProfil.putExtra(Welcome.USERNAME,r);
            startActivity(intentProfil);


        } else if (id == R.id.nav_add_product) {
            Intent intentProfil = new Intent(this, product_form.class);
            intentProfil.putExtra(Welcome.USERNAME,r);
            startActivity(intentProfil);

        } else if (id == R.id.nav_logout) {
            Intent intentProfil = new Intent(this, Welcome.class);
            startActivity(intentProfil);
        }
        else if (id == R.id.nav_aboutUs) {
            Intent intentProfil = new Intent(this, aboutUs.class);
            intentProfil.putExtra(Welcome.USERNAME, r);
            startActivity(intentProfil);
        }
        else if (id == R.id.Mes_Produits) {
                Intent intentProfil = new Intent(this, MesProduits.class);
                intentProfil.putExtra(Welcome.USERNAME,r);
                startActivity(intentProfil);
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void vehicule(View view)
    {
        Intent i = getIntent();
        String r = i.getStringExtra(Welcome.USERNAME);
        Intent intent = new Intent(this,vehicule.class);
        intent.putExtra(Welcome.USERNAME,r);
        startActivity(intent);
    }
    public void multimedia(View view)
    {
        Intent i = getIntent();
        String r = i.getStringExtra(Welcome.USERNAME);
        Intent intent = new Intent(this,Multimedia.class);
        intent.putExtra(Welcome.USERNAME,r);
        startActivity(intent);
    }
    public void homeappliances(View view)
    {
        Intent i = getIntent();
        String r = i.getStringExtra(Welcome.USERNAME);
        Intent intent = new Intent(this,HomeAppliances.class);
        intent.putExtra(Welcome.USERNAME,r);
        startActivity(intent);
    }
    public void buildings(View view)
    {
        Intent i = getIntent();
        String r = i.getStringExtra(Welcome.USERNAME);
        Intent intent = new Intent(this,Buildings.class);
        intent.putExtra(Welcome.USERNAME,r);
        startActivity(intent);
    }
    public void leisures(View view)
    {
        Intent i = getIntent();
        String r = i.getStringExtra(Welcome.USERNAME);
        Intent intent = new Intent(this,Leisures.class);
        intent.putExtra(Welcome.USERNAME,r);
        startActivity(intent);
    }
    public void other(View view)
    {
        Intent i = getIntent();
        String r = i.getStringExtra(Welcome.USERNAME);
        Intent intent = new Intent(this,Other.class);
        intent.putExtra(Welcome.USERNAME,r);
        startActivity(intent);
    }

}
