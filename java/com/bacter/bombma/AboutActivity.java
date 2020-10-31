package com.bacter.bombma;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity
{
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_about);
        animateIcon();
        animateWarning();
    }
    public void animateIcon()
    {
        Animation shake = AnimationUtils.loadAnimation(mContext,R.anim.shake);
        ImageView logoBomb = findViewById(R.id.logobomb);
        logoBomb.setImageResource(R.drawable.bombbam);
        logoBomb.startAnimation(shake);
    }
    public void animateWarning()
    {
        Animation blink = AnimationUtils.loadAnimation(mContext, R.anim.blink);
        TextView textWarn = findViewById(R.id.textWarn);
        textWarn.startAnimation(blink);
    }
}