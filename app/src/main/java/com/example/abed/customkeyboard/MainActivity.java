package com.example.abed.customkeyboard;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Activity demonstrates some GUI functionalities from the Android support library.
 * <p>
 * Created by Andreas Schrade on 14.12.2015.
 */
public class MainActivity extends AppCompatActivity {

    @Bind(R.id.sample_text_username)
    TextView txt_username;

    @Bind(R.id.sample_text_password)
    TextView txt_password;

    @Bind(R.id.sample_text_appname)
    TextView txt_appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.fab)
    public void onFabClicked(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("PREF_NAME", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        List<Credentials> credentialsList;
        String jsonPreferences = sharedPreferences.getString("KEY", null);

        Gson gson = new Gson();
        if (jsonPreferences != null) {
            Type type = new TypeToken<List<Credentials>>() {
            }.getType();
            credentialsList = gson.fromJson(jsonPreferences, type);
        } else {
            credentialsList = new ArrayList<>();
        }
        credentialsList.add(new Credentials(txt_appName.getText().toString(), txt_username.getText().toString(), txt_password.getText().toString()));
        String json = gson.toJson(credentialsList);
        editor.putString("KEY", json);
        editor.commit();
        Snackbar.make(view, "Saved!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }


}
