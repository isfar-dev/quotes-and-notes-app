package com.isfar.quotesnotes.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.isfar.quotesnotes.fragments.HomeFragment;
import com.isfar.quotesnotes.fragments.MoreFragment;
import com.isfar.quotesnotes.R;
import com.isfar.quotesnotes.fragments.SavedFragment;
import com.isfar.quotesnotes.monetization.Admob;
import com.isfar.quotesnotes.notesection.NotesFragment;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    MaterialToolbar toolbar;
    NavigationView navigationView;
    BottomNavigationView bottomNavigation;
    private int currentItemId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, v.getPaddingBottom());
            return insets;
        });

        //====================================================================
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        //====================================================================


        // Show UI first, then request GDPR consent
//        new Handler(Looper.getMainLooper()).postDelayed(() -> {
//            // ad sdk initialization...
//            initMobileAds.requestConsentForm(MainActivity.this);
//        }, 500);


        //load Interstitial Ad....
        Admob.loadInterstitialAd(MainActivity.this);


        // Set up the Navigation Drawer
        toggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);


        // Set default fragment ===================
        currentItemId = R.id.bottom_nav_home;
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, new HomeFragment())
                    .commit();
        }


        //======= Toolbar OnClick ===================================
        toolbar.setOnMenuItemClickListener(item -> {

            if (item.getItemId()==R.id.settings) {
                bottomNavigation.setSelectedItemId(R.id.bottom_nav_more);
            } else if (item.getItemId()==R.id.share) {
                Intent share_intent = new Intent();
                share_intent.setAction(Intent.ACTION_SEND);
                share_intent.putExtra(Intent.EXTRA_TEXT, "Check out this amazing app:\n" +
                        "https://play.google.com/store/apps/details?id="
                        + MainActivity.this.getPackageName());
                share_intent.setType("text/plain");
                startActivity(Intent.createChooser(share_intent, "share app via"));
            }

            return false;

        });
        //======== toolbar onClick end...============



        //========== SET UP BOTTOMNAVIGATION ONCLICK ===========================
        bottomNavigation.setOnItemSelectedListener(navListener);


        //========== SET UP NAVIGATION ONCLICK ================================
        navigationView.setNavigationItemSelectedListener(sideNavListener);


        //======== Back Press Handling.....===============
        getOnBackPressedDispatcher().addCallback(MainActivity.this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                if (drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (bottomNavigation.getSelectedItemId()!=R.id.bottom_nav_home) {
                    bottomNavigation.setSelectedItemId(R.id.bottom_nav_home);
                } else {
                    finish();
                }

            }
        });



    }//onCreate End Here.....


//========== BottomNavigationView OnClick Method ===========================================
    private final NavigationBarView.OnItemSelectedListener navListener = item -> {

                    int itemId = item.getItemId();

                    if (itemId == currentItemId) return true;
                    currentItemId = itemId;


                    Fragment selectedFragment = null;

                    if (itemId == R.id.bottom_nav_home) {
                        selectedFragment = new HomeFragment();
                        toolbar.setTitle("Home");
                    } else if (itemId == R.id.bottom_nav_saved) {
                        Admob.showInterstitialAdinThreeClick(MainActivity.this);
                        selectedFragment = new SavedFragment();
                        toolbar.setTitle("Saved");
                    } else if (itemId == R.id.bottom_nav_notes) {
                        Admob.showInterstitialAdinThreeClick(MainActivity.this);
                        selectedFragment = new NotesFragment();
                        toolbar.setTitle("Notes");
                    } else if (itemId == R.id.bottom_nav_more) {
                        selectedFragment = new MoreFragment();
                        toolbar.setTitle("Settings");
                    }

                    if (selectedFragment != null) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frameLayout, selectedFragment)
                                .commit();
                    }

                    return true;

            };
//========== BottomNavigationView OnClick Method End ===========================================


//========== NavigationView OnClick Method ===============================================
    private final NavigationView.OnNavigationItemSelectedListener sideNavListener = item -> {

                    Intent intent;

                    if (item.getItemId()==R.id.nav_categories) {

                        drawerLayout.closeDrawer(GravityCompat.START);
                        intent = new Intent(MainActivity.this, CategoryListActivity.class);
                        startActivity(intent);

                    } else if (item.getItemId()==R.id.nav_quotes) {

                        drawerLayout.closeDrawer(GravityCompat.START);
                        intent = new Intent(MainActivity.this, AllQuotesActivity.class);
                        startActivity(intent);

                    } else if (item.getItemId()==R.id.nav_settings) {

                        drawerLayout.closeDrawer(GravityCompat.START);
                        bottomNavigation.setSelectedItemId(R.id.bottom_nav_more);

                    } else if (item.getItemId()==R.id.nav_share) {

                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent share_intent = new Intent();
                        share_intent.setAction(Intent.ACTION_SEND);
                        share_intent.putExtra(Intent.EXTRA_TEXT, "Check out this amazing app:\n" +
                                "https://play.google.com/store/apps/details?id="
                                + MainActivity.this.getPackageName());
                        share_intent.setType("text/plain");
                        startActivity(Intent.createChooser(share_intent, "share app via"));

                    } else if (item.getItemId()==R.id.nav_rating) {

                        drawerLayout.closeDrawer(GravityCompat.START);
                        final String appPackageName = MainActivity.this.getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }

                    } else if (item.getItemId()==R.id.nav_contact) {

                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                        emailIntent.setData(Uri.parse("mailto:mohammadisfar17@gmail.com"));

                        try {
                            startActivity(Intent.createChooser(emailIntent, "Send Email"));
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(MainActivity.this, "No email app found to handle this action", Toast.LENGTH_SHORT).show();
                        }

                    } else if (item.getItemId()==R.id.nav_about) {

                        drawerLayout.closeDrawer(GravityCompat.START);
                        intent = new Intent(MainActivity.this, AboutActivity.class);
                        startActivity(intent);

                    } else if (item.getItemId()==R.id.nav_policy) {

                        drawerLayout.closeDrawer(GravityCompat.START);

                        String url = "https://sites.google.com/view/quotesnotesprivacy";
                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);

                    }

                    return false;

            };
//========== NavigationView OnClick Method End =============================================



}//Main Class End Here....