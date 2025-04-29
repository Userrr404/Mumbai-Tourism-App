package com.example.tourismapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {

    ImageView logoImageIntro;
    Animation splashAnim;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        // label hide
        getSupportActionBar().hide();

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_intro);

        logoImageIntro = findViewById(R.id.logoImageIntro);
        splashAnim = AnimationUtils.loadAnimation(this,R.anim.splash_animation);
        /**
         * Also check user data is on or not
         * using isNetworkAvailable() func.
         * if available then redirect to HomeActivity after 4sec neither redirect to ErrorActivity after 4sec
         */
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isNetworkAvailable()){
                    SharedPreferences preferences = getSharedPreferences("login",MODE_PRIVATE);
                    boolean isLogin = preferences.getBoolean("isLoggedIn",false);
                    String userId = preferences.getString("user_id",null);

                    if(userId != null && isLogin){
                        Intent iHome = new Intent(IntroActivity.this, HomeActivity.class);
                        startActivity(iHome);
                    }else{
                        Intent iLogin = new Intent(IntroActivity.this, LoginActivity.class);
                        startActivity(iLogin);
                    }
                }else{
                    Intent iError = new Intent(IntroActivity.this,ErrorActivity.class);
                    startActivity(iError);
                }
                finish();
            }
        },4000);

        /**
         * Animated logo
         */

        logoImageIntro.startAnimation(splashAnim);
    }

    /**
     * Check user network is on or not
     */

    public boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager != null){
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected(); // return true;
        }
        return false;
    }
}