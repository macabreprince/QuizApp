package com.example.myapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener {

    int id;
    int correctans;
    ArrayList<Message> questionsItem;
    Snackbar snackbar;
    CardView option1,option2,option3,option4;
    LinearLayout ooption1,ooption2,ooption3,ooption4;
    ImageButton next,back;
    TextView textViewOption1,textViewOption2,textViewOption3,textViewOption4,textViewQuestion;
    int currentIndex=0;
    ProgressDialog progressDialog;
    String title;
    int language;
    //boolean flag=true;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home||item.getItemId()==R.id.gohome)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        getSupportActionBar().setTitle("Questions");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent intent = getIntent();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        findIdOfViews();
        String idd = intent.getStringExtra("id");
        title=intent.getStringExtra("title");
        language = intent.getIntExtra("language",2);
        getSupportActionBar().setTitle(title);
        if(idd!=null&&idd.length()!=0)
            id = Integer.parseInt(idd);
        else
            id = 1;
        questionsItem = new ArrayList<>();
        fetchData();
    }

    private void findIdOfViews() {
        ooption1 = findViewById(R.id.ooption1);
        ooption2 = findViewById(R.id.ooption2);
        ooption3 = findViewById(R.id.ooption3);
        ooption4 = findViewById(R.id.ooption4);
        option1=findViewById(R.id.option1);
        option2=findViewById(R.id.option2);
        option3=findViewById(R.id.option3);
        option4=findViewById(R.id.option4);
        textViewOption1 = findViewById(R.id.textOption1);
        textViewOption2 = findViewById(R.id.textOption2);
        textViewOption3 = findViewById(R.id.textOption3);
        textViewOption4 = findViewById(R.id.textOption4);
        textViewQuestion = findViewById(R.id.textQuestion);
        back=findViewById(R.id.BackButton);
        next=findViewById(R.id.Next);
        ooption1.setClickable(true);
        ooption2.setClickable(true);
        ooption3.setClickable(true);
        ooption4.setClickable(true);
        ooption2.setOnClickListener(this);
        ooption1.setOnClickListener(this);
        ooption3.setOnClickListener(this);
        ooption4.setOnClickListener(this);
    }

    private void fetchData() {
        Call<QuestionsReference> resultCall;
        if(id==-1){
            if(language==2)
                resultCall = RetrofitClient.getService().getEnglishMiscellaneous();
            else
                resultCall = RetrofitClient.getService().getHindiMiscellaneous();
        }else{
            if(language==2)
                resultCall = RetrofitClient.getService().getEnglishQuestionsReference(id);
            else
                resultCall = RetrofitClient.getService().getHindiQuestionsReference(id);
        }

        resultCall.enqueue(new Callback<QuestionsReference>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(Call<QuestionsReference> call, Response<QuestionsReference> response) {
                if(response.body()==null)
                    displayNoData();
                QuestionsReference questionsReference = response.body();
                questionsItem.addAll(questionsReference.getMessage());
                progressDialog.dismiss();
                displayData();
            }

            @Override
            public void onFailure(Call<QuestionsReference> call, Throwable t) {
                Toast.makeText(QuestionActivity.this,"Network Error",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void displayNoData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Questions in this category");
        builder.setCancelable(false);
        builder.setPositiveButton("BACK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onBackPressed();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void displayData() {
        if(questionsItem.size()==0||questionsItem==null){
            snackbar = Snackbar
                    .make(option1, "No Questions in this category",Snackbar.LENGTH_INDEFINITE);
            snackbar.show();
            displayNoData();
            return;
        }
        setTextView();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPressed();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View v) {
                nextPressed();
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void nextPressed() {

        if(currentIndex+1>=questionsItem.size()) {
            Toast.makeText(this, "On Last Question", Toast.LENGTH_SHORT).show();
            return;
        }
        currentIndex++;
        ooption1.setBackgroundColor(getResources().getColor(R.color.backgroundOption2));
        ooption2.setBackgroundColor(getResources().getColor(R.color.backgroundOption2));
        ooption3.setBackgroundColor(getResources().getColor(R.color.backgroundOption2));
        ooption4.setBackgroundColor(getResources().getColor(R.color.backgroundOption2));
        ooption1.setClickable(true);
        ooption2.setClickable(true);
        ooption3.setClickable(true);
        ooption4.setClickable(true);
        setTextView();

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setTextView() {

        Message message = questionsItem.get(currentIndex);
        textViewOption1.setText(message.getAns1());
        textViewOption2.setText(message.getAns2());
        textViewOption3.setText(message.getAns3());
        textViewOption4.setText(message.getAns4());
        textViewQuestion.setText(message.getQuestion());
        textViewQuestion.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textViewOption1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textViewOption2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textViewOption3.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textViewOption4.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        if(questionsItem.get(currentIndex).getCorrectAnswer()==1)
            correctans=ooption1.getId();
        else if(questionsItem.get(currentIndex).getCorrectAnswer()==2)
            correctans=ooption2.getId();
        else if(questionsItem.get(currentIndex).getCorrectAnswer()==3)
            correctans=ooption3.getId();
        else if(questionsItem.get(currentIndex).getCorrectAnswer()==4)
            correctans=ooption4.getId();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void backPressed() {

        if(currentIndex-1<0) {
            Toast.makeText(this, "On First Question", Toast.LENGTH_SHORT).show();
            return;
        }
        currentIndex--;
        ooption1.setBackgroundColor(getResources().getColor(R.color.backgroundOption2));
        ooption2.setBackgroundColor(getResources().getColor(R.color.backgroundOption2));
        ooption3.setBackgroundColor(getResources().getColor(R.color.backgroundOption2));
        ooption4.setBackgroundColor(getResources().getColor(R.color.backgroundOption2));

        ooption1.setClickable(true);
        ooption2.setClickable(true);
        ooption3.setClickable(true);
        ooption4.setClickable(true);
        setTextView();
    }

    @Override
    public void onClick(View v) {
        /* */
        if(questionsItem.size()==0||questionsItem==null)
            return;

        int x = v.getId();
        if(x!=correctans) {
            MediaPlayer mPlayer = MediaPlayer.create(this, R.raw.no2);
            mPlayer.start();
            v.setBackgroundColor(getResources().getColor(R.color.wrongOption));
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Wrong Answer");
            builder.setCancelable(true);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }else{
            MediaPlayer mPlayer = MediaPlayer.create(this, R.raw.no1);
            mPlayer.start();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Correct Answer");
            builder.setCancelable(true);
            builder.setPositiveButton("NEXT", new DialogInterface.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    nextPressed();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        View v1=findViewById(correctans);
        v1.setBackgroundColor(getResources().getColor(R.color.correctoption));
        ooption1.setClickable(false);
        ooption2.setClickable(false);
        ooption3.setClickable(false);
        ooption4.setClickable(false);
    }
}