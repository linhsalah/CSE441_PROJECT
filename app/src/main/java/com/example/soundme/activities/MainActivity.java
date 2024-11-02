package com.example.soundme.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.soundme.R;
import com.example.soundme.adapters.ViewPagerAdapter;
import com.example.soundme.fragments.FragmentDiscover;
import com.example.soundme.fragments.FragmentLibrary;
import com.example.soundme.fragments.FragmentMiniPlayer;
import com.example.soundme.fragments.FragmentSearch;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    //This is our viewPager
    private ViewPager viewPager;
    public static final int BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT = 1;

    //Fragments
    FragmentDiscover fragmentDiscover;
    FragmentSearch fragmentSearch;
    FragmentLibrary fragmentLibrary;

    MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkNotificationPermission();
        addControl();

//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.fragment_discover:
//                        viewPager.setCurrentItem(0);
//                        break;
//                    case R.id.fragment_search:
//                        viewPager.setCurrentItem(1);
//                        break;
//                    case R.id.fragment_library:
//                        viewPager.setCurrentItem(2);
//                        break;
//                }
//                return false;
//            }
//        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }

                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                menuItem = bottomNavigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setupViewPager(viewPager);


    }

    private void addControl() {
        viewPager = findViewById(R.id.viewpager);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        addMiniPlayer();
    }

    private void addMiniPlayer() {
        FragmentMiniPlayer fragmentMiniPlayer = new FragmentMiniPlayer();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.miniPlayer_frame, fragmentMiniPlayer);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void setupViewPager(ViewPager viewPager) {
            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        fragmentDiscover = new FragmentDiscover();
        fragmentSearch = new FragmentSearch();
        fragmentLibrary = new FragmentLibrary();

        adapter.addFragment(fragmentDiscover);
        adapter.addFragment(fragmentSearch);
        adapter.addFragment(fragmentLibrary);
        viewPager.setAdapter(adapter);
    }
    private void checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }
    }
}