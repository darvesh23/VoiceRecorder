package com.example.audiofiles;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.astuetz.PagerSlidingTabStrip;
import com.example.audiofiles.Adapters.MyTabAdapter;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity {
@BindView(R.id.tabs) PagerSlidingTabStrip tabs;
@BindView(R.id.pager) ViewPager viewPager;
@BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager.setAdapter(new MyTabAdapter(getSupportFragmentManager()));
        tabs.setViewPager(viewPager);

        setSupportActionBar(toolbar);

    }
}