package com.SeongMin.GoodProduct.activity;

import com.SeongMin.GoodProduct.activity.util.SystemUiHider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import static android.app.PendingIntent.getActivity;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
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
