package com.example.myapplication;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener {

    private static int SPLASH_TIME_OUT = 4000;
    ArrayList<CategoriesReference> topicsItems;
    ProgressBar progressDialog;
    Snackbar snackbar;
    private NetworkStateReceiver networkStateReceiver;
    boolean isConnected=false,flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
         progressDialog=findViewById(R.id.progress_circular);
         topicsItems=new ArrayList<>();

        networkStateReceiver =new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                if(flag){
                    startActivity(i);
                    finish();
                }else{
                    flag = true;
                    progressDialog.setVisibility(View.VISIBLE);
                }
            }
        }, SPLASH_TIME_OUT);


    }


    private void connect() {
        Call<List<CategoriesReference>> call=RetrofitClient.getService().getCategoriesReference();
        call.enqueue(new Callback<List<CategoriesReference>>() {
            @Override
            public void onResponse(Call<List<CategoriesReference>> call, Response<List<CategoriesReference>> response) {
                if(response.body()==null)
                    return;
                topicsItems.addAll(response.body());
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                DataTransfer.putcategoriesReferenceArrayList().addAll(topicsItems);
                if(flag){
                    startActivity(i);
                    finish();
                }else{
                    flag = true;
                }


            }

            @Override
            public void onFailure(Call<List<CategoriesReference>> call, Throwable t) {
                snackbar = Snackbar
                        .make(progressDialog, "Connection Fail Restart App", Snackbar.LENGTH_INDEFINITE);

                snackbar.show();
                isConnected=false;

            }
        });
    }
    @Override
    public void networkAvailable() {
       // Toast.makeText(this, " Connection", Toast.LENGTH_LONG).show();

        if(!isConnected){
            DataTransfer.putcategoriesReferenceArrayList().clear();
            isConnected=true;
            connect();
        }

        if(snackbar!=null){
            progressDialog.setVisibility(View.VISIBLE);
            snackbar.dismiss();
        }


    }

    @Override
    public void networkUnavailable() {
        //Toast.makeText(this, " Connection no", Toast.LENGTH_LONG).show();
        progressDialog.setVisibility(View.GONE);
         snackbar = Snackbar
                .make(progressDialog, "No Connection", Snackbar.LENGTH_INDEFINITE);

        snackbar.show();
//        Snackbar.make( view"Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show();

    }

    public void onDestroy() {
        super.onDestroy();
        networkStateReceiver.removeListener(this);
        this.unregisterReceiver(networkStateReceiver);
    }
}
