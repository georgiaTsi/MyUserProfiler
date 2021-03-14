package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

public class MainActivity extends AppCompatActivity {
    ProgressDialog dialog;

    String gender, age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PermissionChecker.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.INTERNET}, 1);
        } else {
            StartMakingPredictions();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] == 0) {
            StartMakingPredictions();
        }
    }

    private void StartMakingPredictions() {
        ShowLoadWaiting();

        /*Weka weka = new Weka(this);
        weka.StartCrawler();

        String gender = weka.PredictGender();
        String age = weka.PredictAge();*/

        Weka weka = new Weka(this);

        new Thread() {
            Activity activ;

            public void run() {
                try {
                    weka.StartCrawler();
                    gender = weka.PredictGender();
                    age = weka.PredictAge();
                    // do the background process or any work that takes time to see progress dialog
                } catch (Exception e) {
                    Log.e("tag", e.getMessage());
                }
                // dismiss the progress dialog
                dialog.dismiss();
                UpdateUI(gender, age);
            }
        }.start();

        //UpdateUI(gender, age);
    }

    private void ShowLoadWaiting() {
        dialog = new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        if (!this.isFinishing()) {
            if (!dialog.isShowing())
                dialog.show();
        }

        dialog.setMessage("Please wait");
    }

    private void UpdateUI(String gender, String age) {
        try {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView genderTextview = (TextView) findViewById(R.id.textview_main_gender);
                    TextView ageTextview = (TextView) findViewById(R.id.textview_main_age);

                    genderTextview.setText(gender);
                    ageTextview.setText(age);
                }
            });
        } catch (Exception ex) {
            Exception test = ex;
        }
    }
}