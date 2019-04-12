package com.iman.keepword;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iman.keepword.dialog.TimeoutDialog;
import com.iman.keepword.model.User;
import com.iman.keepword.rest.SignUp;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class MainActivity extends AppCompatActivity implements Callback<User> {

    private Button signup;
    private EditText email, password;

    private Retrofit retrofit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signup = findViewById(R.id.signup);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });

    }
    public void start(){
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
        SignUp signUps = retrofit.create(SignUp.class);
        User user = new User(email.getText().toString(),password.getText().toString());
        Call<User> call = signUps.getUser(user);
        call.enqueue(this);
    }

    private void sendSignUpData() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8080")
                .build();
        SignUp signUp = retrofit.create(SignUp.class);
        User user = new User(email.getText().toString(),password.getText().toString());
//        Call<List<User>> userBacked=  signUp.getUser(user);
        String s = "";

    }



    @Override
    public void onResponse(Call<User> call, Response<User> response) {
        String s="";
    }

    @Override
    public void onFailure(Call<User> call, Throwable t) {
        new TimeoutDialog(this,TimeoutDialog.ERROR,t.getMessage());
    }
}
