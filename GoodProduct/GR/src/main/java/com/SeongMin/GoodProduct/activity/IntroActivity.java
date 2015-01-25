package com.SeongMin.GoodProduct.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class IntroActivity extends Activity {
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ImageView IntroImage = (ImageView)findViewById(R.id.intro);

        IntroImage.setOnClickListener(new ImageView.OnClickListener(){
            @Override
            public void onClick(View v) {
                handler = new Handler( );
                handler.postDelayed(( new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent( IntroActivity.this, MainActivity.class);

                        startActivity(intent);

                        finish( );


                    }
                }), 2000);
            }
        });

        Log.i("my_tag", "app started");

    }
    @Override
    public void onBackPressed( ){
        super.onBackPressed();

    }
    @Override
    public void finish(){

        super.finish();

    }

}
