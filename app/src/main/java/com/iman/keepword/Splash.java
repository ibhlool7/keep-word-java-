package com.iman.keepword;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iman.keepword.model.Log;
import com.iman.keepword.rest.SignUp;

import java.security.Security;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Splash extends AppCompatActivity implements Callback<Log> {

    private ImageView logo;
    private ImageView top;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logo = findViewById(R.id.logo);
        top = findViewById(R.id.top);
        logo.setVisibility(View.INVISIBLE);
        animate();

    }

    private void animate() {

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fall);
        top.startAnimation(animation);
        CountDownTimer countDownTimer = new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                logo.setVisibility(View.VISIBLE);
                Drawable drawable = logo.getDrawable();
                if (drawable instanceof Animatable) {
                    ((Animatable) drawable).start();
                }
                sendRequest();
            }
        }.start();

    }

    private void sendRequest() {
        Gson gson = new GsonBuilder().setLenient().create();
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Setting.BASEURL).
                addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
        SignUp signUp = retrofit.create(SignUp.class);
        Call<Log> logCall = signUp.enter(new Log(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID)));
        logCall.enqueue(this);
    }

    @Override
    public void onResponse(Call<Log> call, Response<Log> response) {
        Headers headers = response.headers();
        String message = headers.get("message");
        String status = headers.get("status");
        Log body = response.body();
        if (status.equals("1")) {
            nextStep(true, "");
        } else {
            nextStep(false, message);
        }
    }

    @Override
    public void onFailure(Call<Log> call, Throwable t) {
        nextStep(false, t.getMessage());
    }

    private void nextStep(final boolean b, final String message) {
        CountDownTimer countDownTimer = new CountDownTimer(2000, 2000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (b) {
                    startActivity(new Intent(Splash.this, RegisterActivity.class));
                    finish();
                } else {
                    Toast.makeText(Splash.this, message, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }.start();

    }
}
