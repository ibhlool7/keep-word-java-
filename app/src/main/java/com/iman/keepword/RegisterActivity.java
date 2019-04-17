package com.iman.keepword;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iman.keepword.model.User;
import com.iman.keepword.rest.SignUp;

import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity implements Callback<User> {

    private TextView wayToLogin;
    private TextView wayToSignUp;

    private ConstraintLayout loginPage;
    private ConstraintLayout signUpPage;

    private EditText username1,password1,repassword1;
    private EditText username2,password2;
    private Button loginButton,signUpButton;

    private RelativeLayout loading;



    private SharedPreferences sharedPreferences = null;
    private SharedPreferences.Editor editor = null;

    private final Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        wayToLogin = findViewById(R.id.wayToLogin);
        wayToSignUp = findViewById(R.id.wayToSignUp);
        loginPage = findViewById(R.id.loginPage);
        signUpPage = findViewById(R.id.signUpPage);
        loading = findViewById(R.id.loading);

        username1 = findViewById(R.id.username);
        password1 = findViewById(R.id.password);
        repassword1 = findViewById(R.id.repass);
        signUpButton = findViewById(R.id.signUpButton);

        username2= findViewById(R.id.username2);
        password2 = findViewById(R.id.password2);
        loginButton = findViewById(R.id.loginButton);

        design();
        initialize();

    }

    private void initialize() {

        sharedPreferences = getSharedPreferences(Setting.SHARENAME,MODE_PRIVATE);
        editor = sharedPreferences.edit();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = true;
                if (username1.getText().toString().equals("")){
                    toaster("please complete Email Field !!!");
                    check = false;
                }
                if (password1.getText().toString().equals("") && check){
                    toaster("please complete password Field !!!");
                    check = false;
                }
                if (repassword1.getText().toString().equals("") && check){
                    toaster("please complete rePassword Field !!!");
                    check = false;
                }
                if (!repassword1.getText().toString().equals(password1.getText().toString()) && check){
                    toaster("passwords aren't match !!!");
                    check = false;
                }
                if (check){
                    signUpRequest();
                    loading.setVisibility(View.VISIBLE);
                }
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = true;
                if (username2.getText().toString().equals("")){
                    toaster("please complete username field");
                    check = false;
                }
                if (password2.getText().toString().equals("") & check){
                    toaster("please complete password field");
                    check = false;
                }
                if (check){
                    loginRequest();
                }
            }
        });
    }

    private void loginRequest() {
        Gson gson = new GsonBuilder().setLenient().create();
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Setting.BASEURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        SignUp login = retrofit.create(SignUp.class);
        Call<User> userCall = login.login(new User(username2.getText().toString(),password2.getText().toString()));
        userCall.enqueue(this);
    }

    private void signUpRequest() {
        Gson gson = new GsonBuilder().setLenient().create();
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Setting.BASEURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
        SignUp signUp = retrofit.create(SignUp.class);
        Call<User> ret = signUp.signUp(new User(username1.getText().toString(),password1.getText().toString()));
        ret.enqueue(this);
    }

    private void toaster(String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private void design() {
        loading.setVisibility(View.GONE);
        String text1 = "do you have an account? <font color='#FF5722'> login</font>";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            wayToLogin.setText(Html.fromHtml(text1,  Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE);
        } else {
            wayToLogin.setText(Html.fromHtml(text1), TextView.BufferType.SPANNABLE);
        }

        String text = "do you want create an account? <font color='#FF5722'> Sign Up</font>";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            wayToSignUp.setText(Html.fromHtml(text,  Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE);
        } else {
            wayToSignUp.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
        }

        wayToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginPage.setVisibility(View.VISIBLE);
                signUpPage.setVisibility(View.GONE);
            }
        });
        wayToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginPage.setVisibility(View.GONE);
                signUpPage.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onResponse(Call<User> call, Response<User> response) {
        Headers headers = response.headers();
        String message = headers.get("message");
        String status = headers.get("status");
        if (status.equals("1")){
            editor.putString("username",response.body().getEmail());
            editor.putString("password",response.body().getPassword());
            editor.apply();
            nextStep();
        }else{
            toaster(message);
            loading.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFailure(Call<User> call, Throwable t) {
        toaster(t.getMessage());
        loading.setVisibility(View.GONE);
    }

    private void nextStep(){
        CountDownTimer countDownTimer = new CountDownTimer(2000,2000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(activity,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }
}
