package com.st.seongmin.standardtech;

import android.os.AsyncTask;
import android.util.Log;

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
 * Created by SeongMin on 2015-01-14.
 */
public class GetGRJson {
    ArrayList<String> arGeneralCopy = new ArrayList<String>();
    public void GetData( ArrayList<String> arGeneral){

       new JsonLoadingTask().execute();
    }
    public String getJsonText() {

        StringBuffer sb = new StringBuffer();

        try {

            String URLString = "http://api.ibtk.kr/grStandard_api/f78ac854ed7e35a84b634ec617e5ea98?model_query_pageable.pageSize=50&model_query_fields="
                    + URLEncoder.encode("{'standardname':1,'_id':0}", "UTF-8");
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
                arGeneralCopy.add(insideObject.getString("standardname"));

            } // for
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("size2",arGeneralCopy.size()+" ");
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

        }
    } // JsonLoadingTask*/

}


