package com.bacter.bombma;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity
{
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_splash);

        final ImageView splashImg = findViewById(R.id.splashImg);
        Animation shake = AnimationUtils.loadAnimation(mContext,R.anim.shake);
        Animation blink = AnimationUtils.loadAnimation(mContext,R.anim.blink);

        splashImg.startAnimation(blink);

        final Intent intent = new Intent(this,MainActivity.class);
        Thread timer = new Thread()
        {
            public void run()
            {
                try
                {
                    sleep(3000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    splashImg.startAnimation(shake);
                    startActivity(intent);
                    finish();
                }
            }
        };
        timer.start();
    }
}