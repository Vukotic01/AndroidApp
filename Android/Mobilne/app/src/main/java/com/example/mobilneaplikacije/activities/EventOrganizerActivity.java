package com.example.mobilneaplikacije.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mobilneaplikacije.R;
import com.example.mobilneaplikacije.fragments.ProductDetailsFragment;
import com.example.mobilneaplikacije.fragments.RateEventsFragment;
import com.example.mobilneaplikacije.fragments.ReservationsForEventOrganizer;
import com.example.mobilneaplikacije.fragments.ServiceDetailsFragment;
import com.google.android.material.navigation.NavigationView;

public class EventOrganizerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_organizer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //BudgetManager budgetManager = new BudgetManager();
        //budgetManager.createSampleBudget();

        // Load the initial fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProductDetailsFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_products); // Ensure you have this menu item
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;

        switch (item.getItemId()) {
            case R.id.nav_products:
                selectedFragment = new ProductDetailsFragment();
                break;
            case R.id.nav_services:
                // Handle services fragment
                selectedFragment = new ServiceDetailsFragment(); // Create this fragment
                break;
            case R.id.nav_packages:
                // Handle packages fragment
                //selectedFragment = new PackagesFragment(); // Create this fragment
                break;
            case R.id.nav_budgets:
                // Handle budgets fragment
                //selectedFragment = new BudgetsFragment(); // Create this fragment
                break;
            case R.id.nav_events:
                // Handle events fragment
                //selectedFragment = new EventsFragment(); // Create this fragment
                break;

            case R.id.nav_eventsRate:
                //dodaj fragment za listu za rate
                selectedFragment=new RateEventsFragment();
                break;

            case R.id.nav_reservations:
                // Handle reservations fragment
                selectedFragment = new ReservationsForEventOrganizer();
                break;
            case R.id.nav_logout:
                logout();
                return true;
        }

        if (selectedFragment != null) {
            replaceFragment(selectedFragment);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void logout() {
        // Clear the token from SharedPreferences
        SharedPreferences.Editor editor = getSharedPreferences("MyAppPrefs", MODE_PRIVATE).edit();
        editor.remove("auth_token");
        editor.apply();

        // Redirect to Login Activity
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
