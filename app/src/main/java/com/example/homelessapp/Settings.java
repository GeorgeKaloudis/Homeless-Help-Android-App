package com.example.homelessapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class Settings extends AppCompatActivity {

    TextView editProfile,changePassword,logout,changeLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        editProfile = findViewById(R.id.editProfile);
        changePassword = findViewById(R.id.changePassword);
        changeLanguage = findViewById(R.id.changeLanguage);
        logout = findViewById(R.id.logout);



        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this,EditProfile.class);
                startActivity(intent);
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this,ChangePassword.class);
                startActivity(intent);
            }
        });

        //Create Alert dialog box for change laguage
        changeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] listItems ={"Ελληνικά","English"};
                AlertDialog.Builder mbuilder = new AlertDialog.Builder(Settings.this);
                mbuilder.setTitle("Choose Language...");
                mbuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //Change language to greek
                        if(i == 0){
                            String languageToLoad  = "el";
                            Locale locale = new Locale(languageToLoad);
                            Locale.setDefault(locale);
                            Configuration config = new Configuration();
                            config.locale = locale;
                            getBaseContext().getResources().updateConfiguration(config,
                                    getBaseContext().getResources().getDisplayMetrics());

                            finish();
                            startActivity(getIntent());
                        }
                        //Change language to english
                        if(i==1){
                            String languageToLoad  = "en"; // your language
                            Locale locale = new Locale(languageToLoad);
                            Locale.setDefault(locale);
                            Configuration config = new Configuration();
                            config.locale = locale;
                            getBaseContext().getResources().updateConfiguration(config,
                                    getBaseContext().getResources().getDisplayMetrics());

                            finish();
                            startActivity(getIntent());
                        }
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog mdialog = mbuilder.create();
                mdialog.show();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();
                SharedPreferences pref = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
                pref.edit().remove("user_key").commit();
                Intent intent = new Intent(Settings.this, MainActivity.class);
                finishAffinity();
                startActivity(intent);
            }
        });
    }
}