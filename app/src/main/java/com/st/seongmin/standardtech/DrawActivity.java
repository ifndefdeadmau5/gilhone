/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.st.seongmin.standardtech;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.ActionBarDrawerToggle;
//import android.support.v4.app.ListFragment;
import android.support.v4.view.GravityCompat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This example illustrates a common usage of the DrawerLayout widget
 * in the Android support library.
 * <p/>
 * <p>When a navigation (left) drawer is present, the host activity should detect presses of
 * the action bar's Up affordance as a signal to open and close the navigation drawer. The
 * ActionBarDrawerToggle facilitates this behavior.
 * Items within the drawer should fall into one of two categories:</p>
 * <p/>
 * <ul>
 * <li><strong>View switches</strong>. A view switch follows the same basic policies as
 * list or tab navigation in that a view switch does not create navigation history.
 * This pattern should only be used at the root activity of a task, leaving some form
 * of Up navigation active for activities further down the navigation hierarchy.</li>
 * <li><strong>Selective Up</strong>. The drawer allows the user to choose an alternate
 * parent for Up navigation. This allows a user to jump across an app's navigation
 * hierarchy at will. The application should treat this as it treats Up navigation from
 * a different task, replacing the current task stack using TaskStackBuilder or similar.
 * This is the only form of navigation drawer that should be used outside of the root
 * activity of a task.</li>
 * </ul>
 * <p/>
 * <p>Right side drawers should be used for actions, not navigation. This follows the pattern
 * established by the Action Bar that navigation should be to the left and actions to the right.
 * An action should be an operation performed on the current contents of the window,
 * for example enabling or disabling a data overlay on top of the current content.</p>
 */
public class DrawActivity extends Activity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    //Toolbar toolbar;

    private CharSequence mDrawerTitle; // ActionBar의 제목 변경
    private CharSequence mTitle; // ActionBar의 제목 변경하기 위한 변수
    private String[] mPlanetTitles; // 태양계 행성 이름들
    private static ArrayAdapter<String> Adapter;
    private static ArrayAdapter<String> Adapter2;
    static ArrayList<String> arGeneral = new ArrayList<String>();
    static ArrayList<String> standardCode = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mTitle = mDrawerTitle = getTitle();
        // strings.xml 의 데이터
        mPlanetTitles = getResources().getStringArray(R.array.planets_array);


        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        // 서랍레이아웃 그림자 정의
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        // ListView 데이터 정의


        /* 어댑터 초기화
        *어댑터 초기화
        * 어댑터 초기화
         */
        mDrawerList.setAdapter(Adapter = new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mPlanetTitles));

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        // ActionBar의 홈버튼을 Navagation Drawer 토글기능으로 사용..
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // The action bar home/up action should open or close the drawer.
         // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
        case R.id.action_websearch:
            // create intent to perform web search for this planet
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
            // catch event that there's no activity to handle intent
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
            }
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
            String mes;
            mes= "selected Item = " + mPlanetTitles[position];
            Toast.makeText(DrawActivity.this, mes, Toast.LENGTH_LONG).show();
        }
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
        ListFragment fragment = new GRList();

        // Fragmant 에 추가적인 정보 저장
        Bundle args = new Bundle();
        args.putInt(GRList.ARG_PLANET_NUMBER, position);
        fragment.setArguments(args);

        // FragmentManager 가 Fragment가 바뀔 때 마다 교체해준다
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        fragmentManager.beginTransaction().commitAllowingStateLoss();


        // update selected item and title, then close the drawer
        // 아이템이 계속 클릭된 상태로 유지
        mDrawerList.setItemChecked(position, true);
        // 액션바 제목 변경
        setTitle(mPlanetTitles[position]);
        // DrawerLayout 닫기기
       mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Fragment that appears in the "content_frame", shows a planet
     */
    public static class GRList extends ListFragment {
        public static final String ARG_PLANET_NUMBER = "planet_number";

        public GRList() {
            // Empty constructor required for fragment subclasses
        }

        public void onActivityCreated(Bundle savedInstanceState ) {
            super.onActivityCreated(savedInstanceState);
                 /*
                  *
                  *
                  */
        }
        public String getJsonText() {

            StringBuffer sb = new StringBuffer();

            try {
                int GRNumber = (getArguments().getInt(ARG_PLANET_NUMBER)+1);
                String GRNumberString = GRNumber<10?"0"+GRNumber:String.valueOf(GRNumber);

                //String URLString = "http://api.ibtk.kr/grStandard_api/f78ac854ed7e35a84b634ec617e5ea98?model_query_pageable.pageSize=50&model_query_fields="
                //        + URLEncoder.encode("{'standardname':1,'_id':0}", "UTF-8");
                String URLString = "http://api.ibtk.kr/grStandard_api/f78ac854ed7e35a84b634ec617e5ea98?model_query_pageable.pageSize=222&model_query="
                        + URLEncoder.encode("{'areacode':{'$regex':'GR" + GRNumberString + "'}}");

                String line = getStringFromUrl( URLString );
                JSONObject object = new JSONObject(line);
                JSONArray Array = new JSONArray(object.getString("content"));

                for (int i = 0; i < Array.length(); i++) {

                    JSONObject insideObject = Array.getJSONObject(i);

                    sb.append("표준명 : ")
                            .append(insideObject.getString("standardname"))
                            .append("\n");
                    sb.append("\n");
                    sb.append("\n");
                    arGeneral.add(insideObject.getString("standardname"));

                } // for
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.i("size2",arGeneral.size()+" ");
            return sb.toString();
        } // getJsonText

        public String getStringFromUrl(String url)
                throws UnsupportedEncodingException {

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    getInputStreamFromUrl(url), "UTF-8"));

            StringBuffer sb = new StringBuffer();
            Log.i("my_tag","트라이 진입전");
            try {
                Log.i("my_tag","getStringFromUrl");
                String line = null;
                //System.out.println(line);

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                    Log.i("my_tag",line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.i("my_tag","트라이 후");
            return sb.toString();
        } // getStringFromUrl


        public static InputStream getInputStreamFromUrl(String url) {
            InputStream contentStream = null;
            try {

                Log.i("my_tag","getInputStreamFromUrl");
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(new HttpGet(url));
                contentStream = response.getEntity().getContent();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return contentStream;
        } // getInputStreamFromUrl

        /*
         * AsyncTask : UI 처리 및 Background 작업 등을 하나의 클래스에서 작업 할 수 있게 지원
         * onPreExecute : Bacground 작업 시작전에 UI 작업을 진행
         * doInBackground : Background 작업을 진행
         * onPostExecute : Bacground 작업 후에 UI 작업을 진행
         */
        private class JsonLoadingTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... strs) {
                return getJsonText();
            }

            @Override
            protected void onPostExecute(String result) {
                Log.i("my_tag","JsonLoadingTask");
                Log.i("my_tag",result);
                Adapter2.notifyDataSetChanged();
            }
        } // JsonLoadingTask*/
        /*
        *
        *
        *
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState)
        // 분야별코드에 따른 리스트 출력
        {
            ListView rootView = (ListView)inflater.inflate(R.layout.gr_detail, container, false);
            int i = getArguments().getInt(ARG_PLANET_NUMBER);
            String planet = getResources().getStringArray(R.array.planets_array)[i];

            rootView.setAdapter(Adapter2 = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_list_item_1, arGeneral));


            new JsonLoadingTask().execute();

            getActivity().setTitle(planet);
            return rootView;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            arGeneral.clear();
        }
    }
}