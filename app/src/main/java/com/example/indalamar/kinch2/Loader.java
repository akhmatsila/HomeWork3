package com.example.indalamar.kinch2;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Pair;

import lombok.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Loader extends IntentService {
    private ArrayList<Pair<String, String>> returnValue = new ArrayList<>();
    private final Handler main = new Handler(Looper.getMainLooper());
    private OnLoad callback;


    public Loader() {
        super("Loader");
    }


    protected void onHandleIntent(Intent intent) {
        String source = intent.getStringExtra("source");
        loadAllDescription(source);
    }

    public static void load(Context context, String urlSource) {
        Intent intent = new Intent(context, Loader.class);
        intent.putExtra("source", urlSource);
        context.startService(intent);
    }

    public void onCreate() {
        super.onCreate();
    }

    public void loadAllDescription(String urlString) {

        ArrayList<Pair<String, String>> HY = new ArrayList<>();
        try {
            Document base = Jsoup.connect(urlString).get();
            Elements images = base.select("img[src~=(?i)\\.(png|jpe?g|gif)]");
            for (org.jsoup.nodes.Element image : images) {
                String src = image.attr("src").toString();
                String description = image.attr("alt").toString();
                HY.add(Pair.create(src, description));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        main.post(() -> deliver(HY));
    }

    public static class MyBinder extends Binder {
        public MyBinder() {
            super();
        }

        private Loader service;

        public MyBinder(Loader loader) {
            service = loader;
        }

        public void setCallBack(final OnLoad callBack) {
            new Handler(Looper.getMainLooper()).post(() -> {
                service.callback = callBack;
                service.callback.onLoad(service.returnValue);
            });
        }
    }

    public IBinder onBind(Intent intent) {
        return new MyBinder(this);
    }

    public boolean onUnbind(Intent intent) {
        callback = null;
        return super.onUnbind(intent);
    }

    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    public int onStartCommand(Intent intent, int flags, int stardId) {
        return super.onStartCommand(intent, flags, stardId);
    }

    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    private void deliver(ArrayList<Pair<String, String>> data) {
        if (callback != null) callback.onLoad(data);
        else returnValue = data;
    }

    public interface OnLoad {
        void onLoad(ArrayList<Pair<String, String>> data);
    }
}
