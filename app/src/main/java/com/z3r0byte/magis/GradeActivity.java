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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.google.gson.Gson;
import com.z3r0byte.magis.Fragments.MainGradesFragment;
import com.z3r0byte.magis.Fragments.NewGradesFragment;
import com.z3r0byte.magis.GUI.NavigationDrawer;
import com.z3r0byte.magis.Utils.MagisActivity;

import net.ilexiconn.magister.container.Profile;
import net.ilexiconn.magister.container.School;
import net.ilexiconn.magister.container.User;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

public class GradeActivity extends MagisActivity implements MaterialTabListener {
    private static final String TAG = "GradeActivity";


    Profile mProfile;
    User mUser;
    School mSchool;

    Toolbar mToolbar;
    NavigationDrawer navigationDrawer;

    MaterialTabHost tabHost;
    ViewPager viewPager;
    PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);

        mToolbar = (Toolbar) findViewById(R.id.Toolbar);
        mToolbar.setTitle(R.string.title_grades);
        setSupportActionBar(mToolbar);

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
                return new NewGradesFragment();
            } else {
                return new MainGradesFragment();
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
