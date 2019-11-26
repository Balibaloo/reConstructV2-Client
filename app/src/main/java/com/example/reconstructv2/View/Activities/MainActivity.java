package com.example.reconstructv2.View.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.reconstructv2.R;
import com.example.reconstructv2.View.Fragments.AccountViewFragment;
import com.example.reconstructv2.View.Fragments.HomeFragment;
import com.example.reconstructv2.View.Fragments.SingleListingFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity
        implements HomeFragment.OnFragmentInteractionListener,
                    SingleListingFragment.OnFragmentInteractionListener,
                    AccountViewFragment.OnFragmentInteractionListener,
                    NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        init();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // handles drawer menu items being selected
        switch (menuItem.getItemId()) {

            case (R.id.nav_to_home):{
                // clear backstack on navigation to home
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.main_nav_graph,true).build();
                Navigation.findNavController(this,R.id.nav_host_fragment)
                        .navigate(R.id.homeFragment2,null, navOptions);
                break;
            }

            case (R.id.nav_to_account):{
                Navigation.findNavController(this,R.id.nav_host_fragment).navigate(R.id.accountViewFragment);
                break;
            }

        }

        menuItem.setChecked(true);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void init(){
        // initialises navController and drawerLayour with navController
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this,navController,drawerLayout);
        NavigationUI.setupWithNavController(navigationView,navController);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private boolean isValidDestination(int Destination){
      return Destination != Navigation.findNavController(this,R.id.nav_host_fragment)
              .getCurrentDestination().getId();
    };

    @Override
    public boolean onSupportNavigateUp() {
        //
        return NavigationUI.navigateUp(Navigation.findNavController(this,R.id.nav_host_fragment),drawerLayout);
    }


    @Override
    public void onHomeFragmentInteraction(Uri uri) {
        // Do stuff
    }

    @Override
    public void onSingleListingFragmentInteraction(Uri uri) {
        // Do different stuff
    }

    @Override
    public void onAccountViewFragmentInteraction(Uri uri) {

    }
}