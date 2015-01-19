package com.st.seongmin.standardtech;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        handler = new Handler( );
        handler.postDelayed(( new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent( MainActivity.this, MaterialDrawActivity.class);
                Log.i("Why","0");
                startActivity(intent);
                Log.i("Why","0");
                finish( );


            }
        }), 2000);
        Log.i("my_tag", "app started");

    }
    @Override
    public void onBackPressed( ){
        super.onBackPressed();

    }
    @Override
    public void finish(){
        this.overridePendingTransition(0, R.anim.exitanim);
        super.finish();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
