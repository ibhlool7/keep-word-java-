package com.iman.keepword;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iman.keepword.dialog.TimeoutDialog;
import com.iman.keepword.model.WordModel;
import com.iman.keepword.rest.RestApi;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddWordActivity extends AppCompatActivity {

    private ImageView back;
    private EditText word, definition, examples, nativeLanguage;
    private RelativeLayout save;
    private final Activity activity =this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);
        back = findViewById(R.id.back);
        word = findViewById(R.id.word);
        definition = findViewById(R.id.definition);
        examples = findViewById(R.id.examples);
        nativeLanguage = findViewById(R.id.nativeLanguage);
        save = findViewById(R.id.save);

        init();
    }

    private void init() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendWord();
            }
        });
    }

    private void sendWord() {
        SharedPreferences sharedPreferences = getSharedPreferences(Setting.SHARENAME, MODE_PRIVATE);
        Gson gson = new GsonBuilder().setLenient().create();

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(Setting.TIMEOUTTIME, TimeUnit.SECONDS)
                .readTimeout(Setting.TIMEOUTTIME, TimeUnit.SECONDS)
                .writeTimeout(Setting.TIMEOUTTIME, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Setting.BASEURL).
                addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
        RestApi restApi = retrofit.create(RestApi.class);
        WordModel wordModel = new WordModel(word.getText().toString(),
                sharedPreferences.getString("username", ""),
                definition.getText().toString(),
                examples.getText().toString(),
                nativeLanguage.getText().toString());

        Call<WordModel> wordModelCall = restApi.addWord(sharedPreferences.getString("username", ""),
                sharedPreferences.getString("password", ""),
                wordModel);

        wordModelCall.enqueue(new Callback<WordModel>() {
            @Override
            public void onResponse(Call<WordModel> call, Response<WordModel> response) {
                Headers headers = response.headers();
                String message = headers.get("message");
                String status = headers.get("status");
                if (!status.equals("1")){
                    new TimeoutDialog(activity, TimeoutDialog.ERROR,message);
                }

            }

            @Override
            public void onFailure(Call<WordModel> call, Throwable t) {
                String s ="";
            }
        });
    }

}
