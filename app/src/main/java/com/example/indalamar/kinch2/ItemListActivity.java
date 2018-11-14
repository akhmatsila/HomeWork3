package com.example.indalamar.kinch2;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.indalamar.kinch2.dummy.DummyContent;


public class ItemListActivity extends AppCompatActivity {


    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    RecyclerView recyclerView;

    private SimpleRecyclerViewAdapter adapter = new SimpleRecyclerViewAdapter(this);
    private Loader.MyBinder binder;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ItemListActivity.this.binder = (Loader.MyBinder) service;
            binder.setCallBack(p -> adapter.setElement(p));
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        recyclerView = (RecyclerView) findViewById(R.id.item_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        assert recyclerView != null;
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        Loader.load(this, "https://wallpaperscraft.ru/all/1920x1080");
        bindService(new Intent(this, Loader.class), serviceConnection, 0);
    }
    public void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }

}


