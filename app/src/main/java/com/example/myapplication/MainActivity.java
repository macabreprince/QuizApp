package com.example.myapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener {

    RecyclerView recyclerView;
    View view;
    Snackbar snackbar;
    boolean isConnected;
    ArrayList<CategoriesReference> topicsItems;
    TopicsAdapter adapter;
//    CardView cardViewEnglish,cardViewHindi;
    AlertDialog dialog;
    int language = 1;
    private NetworkStateReceiver networkStateReceiver;
    CardView miscellaneousCardView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Topics");
        TextView textView = findViewById(R.id.textView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Instructions");

                alert.setMessage("The Physical Education & Sports quizzes consists of 10000+ questions carefully designed to help you self-assess your comprehension of the information presented on the topics covered in the module. No data will be collected by this application or on the website regarding your responses or how many times you take the quiz.\n" +
                        "\n" +
                        "\n" +
                        "Each question in the quiz is of multiple-choice format. Read each question carefully, and click on the button next to your response that is based on the information covered on the topic in the module. Each correct or incorrect response will result in appropriate feedback immediately on the next screen.\n" +
                        "\n" +
                        "\n" +
                        "After responding to a question, click on the \"Next Question\" button at the bottom to go to the next question.");
                alert.setPositiveButton("OK",new Dialog.OnClickListener(){
                    public void onClick(DialogInterface dialog, int whichButton){

                    }
                });
                alert.show();
            }
        });
        SharedPreferences preferences = getSharedPreferences("progress", MODE_PRIVATE);
        int appUsedCount = preferences.getInt("appUsedCount",0);
        appUsedCount++;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("appUsedCount", appUsedCount);
        editor.apply();

        if (appUsedCount==5 || appUsedCount==50 || appUsedCount==100 || appUsedCount==200 || appUsedCount==250){
            AskForRating(appUsedCount);
        } else {
//            finish();
//            dialog.cancel();
        }
        view=findViewById(R.id.view);
        miscellaneousCardView = findViewById(R.id.miscellaneousCardView);
        miscellaneousCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                miscellaneousCardViewClicked();
            }
        });
        topicsItems = new ArrayList<>();
        topicsItems.addAll(DataTransfer.getCategoriesReferenceArrayList());
        recyclerView = findViewById(R.id.recylerview_topics);
        isConnected=false;
        networkStateReceiver =new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));

        setRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notice,menu);
//        MenuItem menuItem = menu.findItem(R.id.notice);
//        SubMenu subMenu = menuItem.getSubMenu();
//
//        for(int i=0;i<10;i++){
//            subMenu.add(R.id.gp,i,0,i+"ListListListListListList\nListListListListListList\nListListListListListList");
//        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i=item.getItemId();
      //  Toast.makeText(this,i+" ",Toast.LENGTH_LONG).show();
        Intent intent=new Intent(this,Notice.class);
        startActivity(intent);

        return super.onOptionsItemSelected(item);
    }

    private void miscellaneousCardViewClicked() {
        selectLanguageDialog(-1);
    }

    public void onDestroy() {
        super.onDestroy();
        networkStateReceiver.removeListener(this);
        this.unregisterReceiver(networkStateReceiver);
    }


    private void setRecyclerView() {
        OnViewClickInterface onViewClickInterface = new OnViewClickInterface() {
            @Override
            public void onViewClick(View view, int position) {
                openQuestionActivity(position);
            }
        };
        adapter = new TopicsAdapter(this,topicsItems,onViewClickInterface);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void openQuestionActivity(int position) {
        selectLanguageDialog(position);
    }

    private void selectLanguageDialog(final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_view_layout,null);
        builder.setView(dialogView);
        builder.setCancelable(true);
        CardView englishCardView = dialogView.findViewById(R.id.cancelCardView);
        CardView hindiCardView = dialogView.findViewById(R.id.submitCardView);
        englishCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                language = 2;
                openQuestionActivity2(position);
                dialog.dismiss();
            }
        });
        hindiCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                language = 3;
                openQuestionActivity2(position);
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    private void openQuestionActivity2(int position) {
        Intent intent = new Intent(this,QuestionActivity.class);
        if(position==-1){
            intent.putExtra("id","-1");
            intent.putExtra("language",language);
            startActivity(intent);
            return;
        }
        intent.putExtra("id",topicsItems.get(position).getCat_id());
        intent.putExtra("title",topicsItems.get(position).cat_name);
        intent.putExtra("language",language);
        startActivity(intent);
    }

    @Override
    public void networkAvailable() {
        //Toast.makeText(this, " Connection", Toast.LENGTH_LONG).show();

        if(!isConnected){
            isConnected=true;
            //connect();
        }

        if(snackbar!=null)
     snackbar.dismiss();

    }

    @Override
    public void networkUnavailable() {

        snackbar = Snackbar
                .make(view, "No Connection", Snackbar.LENGTH_INDEFINITE);

        snackbar.show();
//        Snackbar.make( view"Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show();

    }
    private void AskForRating(int _appUsedCount){

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Please Rate Us");

        alert.setMessage("Thanks for using the application. If you like Physical Education App please rate us! Your feedback is important for us!");
        alert.setPositiveButton("Rate it",new Dialog.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton){
                final Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());

                final Intent rateAppIntent = new Intent(Intent.ACTION_VIEW, uri);
                if (getPackageManager().queryIntentActivities(rateAppIntent, 0).size() > 0)
                {
                    startActivity(rateAppIntent);
                }
                else
                {
                    //errror vali case
                }
            }
        });
        alert.setNegativeButton("Not now", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish();
                dialog.cancel();
            }
        });
        alert.show();
    }
}
