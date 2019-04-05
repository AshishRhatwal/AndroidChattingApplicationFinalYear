package mra.com.androidchattingapplicationfinalyear;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;

public class BeforeSignup extends AppCompatActivity {

    Button sign;
    ProgressBar progressBar;
    public int Splash=3200;
    AnimatedCircleLoadingView animatedCircleLoadingView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_signup);
        progressBar=(ProgressBar)findViewById(R.id.splashprogress);

        animatedCircleLoadingView=(AnimatedCircleLoadingView)findViewById(R.id.c);

        animatedCircleLoadingView.startDeterminate();
        animatedCircleLoadingView.setPercent(10);

        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent h=new Intent(BeforeSignup.this,Signup_page.class);
                startActivity(h);
                finish();

            }
        },Splash);


    }
}
