package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Notice extends AppCompatActivity {

    ArrayList<String > arrayList;
    ListView listView;
    ArrayAdapter adapter;
    ArrayList<String > link;
    ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        progressBar=new ProgressDialog(this);
        arrayList=new ArrayList<>();
        link=new ArrayList<>();
        listView=findViewById(R.id.list);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = link.get(position);
                if(url==null||url.length()<=0)
                    return;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        progressBar.setMessage("Loding");
        progressBar.show();
        progressBar.setCancelable(false);
        connect();
    }

    private void connect() {
        Call<List<NoticePOJO>> call =RetrofitClient.getService().getNotice();
        call.enqueue(new Callback<List<NoticePOJO>>() {
            @Override
            public void onResponse(Call<List<NoticePOJO>> call, Response<List<NoticePOJO>> response) {
             progressBar.dismiss();
                if(response.body()==null)
                    return;
                for(int i=0;i<response.body().size();i++){
                    arrayList.add(response.body().get(i).getNewsTitle());
                    link.add(response.body().get(i).getNewsLink());
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<List<NoticePOJO>> call, Throwable t) {

            }
        });
    }
}
