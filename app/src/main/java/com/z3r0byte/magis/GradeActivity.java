/*
 * Copyright 2016 Bas van den Boom 'Z3r0byte'
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.z3r0byte.magis;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.z3r0byte.magis.Adapters.StudyAdapter;
import com.z3r0byte.magis.Fragments.MainGradesFragment;
import com.z3r0byte.magis.Fragments.NewGradesFragment;
import com.z3r0byte.magis.GUI.NavigationDrawer;
import com.z3r0byte.magis.Utils.DateUtils;
import com.z3r0byte.magis.Utils.MagisActivity;

import net.ilexiconn.magister.ParcelableMagister;
import net.ilexiconn.magister.container.Profile;
import net.ilexiconn.magister.container.School;
import net.ilexiconn.magister.container.Study;
import net.ilexiconn.magister.container.User;
import net.ilexiconn.magister.handler.StudyHandler;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Collections;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

public class GradeActivity extends MagisActivity implements MaterialTabListener {
    private static final String TAG = "GradeActivity";


    Profile mProfile;
    User mUser;
    School mSchool;
    ParcelableMagister mMagister;

    Toolbar mToolbar;
    NavigationDrawer navigationDrawer;

    MaterialTabHost tabHost;
    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    Spinner spinner;
    StudyAdapter studyAdapter;
    Study[] studies = new Study[1];
    public static Study selectedStudy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mMagister = extras.getParcelable("Magister");
        } else {
            Log.e(TAG, "onCreate: No valid Magister!", new InvalidParameterException());
            Toast.makeText(this, R.string.err_unknown, Toast.LENGTH_SHORT).show();
            finish();
        }

        mToolbar = (Toolbar) findViewById(R.id.Toolbar);
        mToolbar.setTitle(R.string.title_grades);
        setSupportActionBar(mToolbar);


        studies[0] = new Study();
        studies[0].description = "Laden...";
        studyAdapter = new StudyAdapter(this, studies);
        spinner = (Spinner) findViewById(R.id.studyPicker);
        spinner.getBackground().setColorFilter(getResources().getColor(R.color.md_white_1000), PorterDuff.Mode.SRC_ATOP);
        spinner.setAdapter(studyAdapter);

        mProfile = new Gson().fromJson(getSharedPreferences("data", MODE_PRIVATE).getString("Profile", null), Profile.class);
        mUser = new Gson().fromJson(getSharedPreferences("data", MODE_PRIVATE).getString("User", null), User.class);
        mSchool = new Gson().fromJson(getSharedPreferences("data", MODE_PRIVATE).getString("School", null), School.class);

        navigationDrawer = new NavigationDrawer(this, mToolbar, mProfile, mUser, "Cijfers");
        navigationDrawer.SetupNavigationDrawer();


        //Setting up Tabs
        tabHost = (MaterialTabHost) findViewById(R.id.materialTabHost);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                tabHost.setSelectedNavigationItem(position);

            }
        });

        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            tabHost.addTab(
                    tabHost.newTab()
                            .setText(pagerAdapter.getPageTitle(i))
                            .setTabListener(this)
            );

        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String date = DateUtils.formatDate(DateUtils.getToday(), "yyyy-MM-dd");
                    StudyHandler studyHandler = new StudyHandler(mMagister);
                    studies = studyHandler.getStudies(true, date);
                    Collections.reverse(Arrays.asList(studies)); //reversing so the newest Study moves to top
                    studyAdapter = new StudyAdapter(getApplicationContext(), studies);
                    spinner.setAdapter(studyAdapter);
                    Log.d(TAG, "onCreate: Amount of studies: " + studies.length);
                    for (Study study : studies) {
                        Log.d(TAG, "onCreate: Study: " + study.description);
                    }
                } catch (IOException e) {
                    Log.e(TAG, "onCreate: Geen verbinding");
                }
            }
        }).start();

    }


    @Override
    public void onTabSelected(MaterialTab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab tab) {

    }

    @Override
    public void onTabUnselected(MaterialTab tab) {

    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        public Fragment getItem(int num) {
            if (num == 0) {
                NewGradesFragment newGradesFragment = NewGradesFragment.newInstance(mMagister);
                return newGradesFragment;
            } else {
                MainGradesFragment mainGradesFragment = MainGradesFragment.newInstance(mMagister);
                return mainGradesFragment;
            }

        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "Nieuwste Cijfers";
            } else {
                return "Cijfers";
            }
        }

    }

}
