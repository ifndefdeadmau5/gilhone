package com.SeongMin.GoodProduct.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.SeongMin.GoodProduct.global.db;

import java.util.ArrayList;

public class GRDetail extends ActionBarActivity {
    TextView textView;
    Cursor mCursor;
    private db dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grdetail);

        Intent intent = getIntent();
        String code = intent.getStringExtra("standardcode");
        String name = intent.getStringExtra("standardname");
        String area = intent.getStringExtra("areacode");
        dbAdapter = new db(this);
        dbAdapter.open();
        mCursor = dbAdapter.fetchNote(code);


        textView = (TextView) findViewById(R.id.grtext);
        textView.append(area);
        textView.append("\n");
        textView.append(code);
        textView.append("\n");
        textView.append(name);
        textView.append("\n");
        textView.append("취득 업체\n");
        while (mCursor.moveToNext()) {
            int i = 0;
            ArrayList<String> comp = new ArrayList<String>();
            comp.add(mCursor.getString(0));
            textView.append(comp.get(i) + "\n");
            i++;
        }
        textView.append("현재 database 파일이 생성되어있" + ((dbAdapter.isEmpty()) ? "지않습니다" : "습니다"));
        dbAdapter.close();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_grdetail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
