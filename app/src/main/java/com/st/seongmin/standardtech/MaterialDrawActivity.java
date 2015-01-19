package com.st.seongmin.standardtech;

import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by SeongMin on 2015-01-15.
 */
public class MaterialDrawActivity extends ActionBarActivity {

    Toolbar toolbar;
    DrawerLayout dlDrawer;
    ActionBarDrawerToggle dtToggle;

    private static ArrayAdapter<String> Adapter;
    private static ArrayAdapter<String> Adapter2;
    private ListView mDrawerList;
    private String[] mPlanetTitles;
    static ArrayList<String> standardName = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_material_draw);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        dlDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        setSupportActionBar(toolbar);

        dtToggle = new ActionBarDrawerToggle(this, dlDrawer, R.string.app_name, R.string.app_name);
        dlDrawer.setDrawerListener(dtToggle);

        /********************/
        mPlanetTitles = getResources().getStringArray(R.array.planets_array);

        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        Adapter = new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mPlanetTitles);

        mDrawerList.setAdapter(Adapter);


        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

// Sync the toggle state after onRestoreInstanceState has occurred.
        dtToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        dtToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (dtToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    /********************************************************************************************************/
/* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
            String mes;
            mes= "selected Item = " + mPlanetTitles[position];
            Toast.makeText(MaterialDrawActivity.this, mes, Toast.LENGTH_LONG).show();
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
        dlDrawer.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        //mTitle = title;
        //getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */



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
                    standardName.add(insideObject.getString("standardname"));

                } // for
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.i("size2", standardName.size() + " ");
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
                    R.layout.drawer_list_item, standardName));

            AnimationSet set = new AnimationSet( true );
            Animation rtl = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f
            );

            rtl.setDuration(500);
            set.addAnimation(rtl);

            Animation alpha = new AlphaAnimation(0.0f, 1.0f);
            alpha.setDuration(1000);
            set.addAnimation(alpha);

            LayoutAnimationController controller = new LayoutAnimationController(set, 0.5f);
            rootView.setLayoutAnimation(controller);

            new JsonLoadingTask().execute();

            getActivity().setTitle(planet);
            return rootView;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            standardName.clear();
        }
    }

}