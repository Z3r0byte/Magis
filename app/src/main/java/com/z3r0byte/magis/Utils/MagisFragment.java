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

package com.z3r0byte.magis.Utils;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;

import com.z3r0byte.magis.Adapters.GradesAdapter;
import com.z3r0byte.magis.Adapters.NewGradesAdapter;

import net.ilexiconn.magister.ParcelableMagister;
import net.ilexiconn.magister.container.Grade;

import tr.xip.errorview.ErrorView;

/**
 * Created by bas on 6-7-16.
 */
public class MagisFragment extends Fragment {
    public ParcelableMagister mMagister;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    public ListView listView;
    public ErrorView errorView;
    public NewGradesAdapter mNewGradesAdapter;
    public GradesAdapter mGradesAdapter;
    public Grade[] grades;
}
