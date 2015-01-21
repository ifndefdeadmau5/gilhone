package com.tekinarslan.material.sample;

import android.support.v4.app.ListFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
 * Created by SeongMin on 2015-01-20.
 */
public class GRListFragment extends ListFragment {
    private static final String ARG_POSITION = "position";

    public static final String ARG_PLANET_NUMBER = "planet_number";
    private ArrayList<String> standardName = new ArrayList<String>();
    private static ArrayAdapter<String> Adapter;
    private static ArrayAdapter<String> Adapter2;
    private int GRNumber;
    public static GRListFragment newInstance(int position) {
        GRListFragment f = new GRListFragment();
        f.GRNumber = position;
        return f;
    }
    public GRListFragment() {

    }

    public void onActivityCreated(Bundle savedInstanceState ) {
        super.onActivityCreated(savedInstanceState);
    }
    public String getJsonText() {

        StringBuffer sb = new StringBuffer();

        try {
            //GRNumber = 1;//(getArguments().getInt(ARG_PLANET_NUMBER)+1);
            String GRNumberString = GRNumber<10?"0"+GRNumber:String.valueOf(GRNumber);
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
        return sb.toString();
    } // getJsonText

    public String getStringFromUrl(String url)
            throws UnsupportedEncodingException {

        BufferedReader br = new BufferedReader(new InputStreamReader(
                getInputStreamFromUrl(url), "UTF-8"));

        StringBuffer sb = new StringBuffer();

        try {
            Log.i("my_tag","getStringFromUrl");
            String line = null;
            //System.out.println(line);

            while ((line = br.readLine()) != null) {
                sb.append(line);
                //Log.i("my_tag",line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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
            ;
            //Log.i("my_tag",result);
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
        int i = 1;//getArguments().getInt(ARG_PLANET_NUMBER);
        String planet = getResources().getStringArray(R.array.planets_array)[i];

        rootView.setAdapter(Adapter2 = new ArrayAdapter<String>(getActivity(),
                R.layout.drawer_list_item, standardName));

        new JsonLoadingTask().execute();

        getActivity().setTitle(planet);
        Log.i("mytag",GRListFragment.this.toString()+"뷰가생겼어염");
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("mytag", GRListFragment.this.toString()+"뷰가지워졌어염");
        standardName.clear();//Adapter2.notifyDataSetChanged();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.i("mytag",GRListFragment.this.toString()+"쥬금");

    }
}

