package com.example.indalamar.kinch2;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.IBinder;
import android.renderscript.ScriptGroup;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ImageViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.indalamar.kinch2.dummy.DummyContent;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;

public class ItemDetailFragment extends Fragment {



    private String mItem;
    private ImageLoader.ImBinder binder;
    private ImageView rootView ;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ItemDetailFragment.this.binder = (ImageLoader.ImBinder) service;
            binder.setCallBack(p -> setBitmap(p));
        }

        public void setBitmap(Bitmap data) {
            rootView.setImageBitmap(data);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mItem = getArguments().getString("image");
    }

    @Override
    public ImageView onCreateView(LayoutInflater inflater, ViewGroup container,
                                  Bundle savedInstanceState) {
        String pathDirectory = getContext().getCacheDir().getAbsolutePath()+"/"+mItem;
        rootView = (ImageView) inflater.inflate(R.layout.item_detail, container, false);
        ImageLoader.load(rootView.getContext(), mItem,pathDirectory);
        getContext().bindService(new Intent(getContext(), ImageLoader.class), serviceConnection, 0);
        return rootView;
    }
    @Override
    public void onDestroy() {
        getContext().unbindService(serviceConnection);
        super.onDestroy();
    }

}
