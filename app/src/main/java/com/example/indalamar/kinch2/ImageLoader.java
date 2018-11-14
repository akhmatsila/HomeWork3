package com.example.indalamar.kinch2;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.os.Looper;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.Handler;
import android.util.Log;

public class ImageLoader extends IntentService {


    private Bitmap result;
    private OnLoad callback;
    private final Handler main = new Handler(Looper.getMainLooper());

    public ImageLoader() {
        super("ImageLoader");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new ImBinder(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String url = intent.getStringExtra("url");
        String pathDirectory = intent.getStringExtra("pathDirectory");
        loadImage(url, pathDirectory);
    }

    public static void load(Context context, String url, String pathDirectory) {
        Intent intent = new Intent(context, ImageLoader.class);
        intent.putExtra("pathDirectory", pathDirectory);
        intent.putExtra("url", url);
        context.startService(intent);
    }

    private void loadImage(String url, String pathDirectory) {
        Bitmap res = null;
        try {
            File file = new File(pathDirectory);
            if (file.exists()) {
                res = BitmapFactory.decodeFile(file.getAbsolutePath());
            } else {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                Log.d("TUT:", "loadImage: created dir");
                InputStream in = new java.net.URL(url).openStream();
                OutputStream out = new BufferedOutputStream(new FileOutputStream(pathDirectory));
                int i;
                while ((i = in.read()) != -1) {
                    out.write(i);
                }
                Log.d("TUT:", "loadImage: read pic");
                in.close();
                out.close();
                res = BitmapFactory.decodeFile(pathDirectory);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap finalRes = res;
        main.post(() -> deliver(finalRes));
    }

    private void deliver(Bitmap data) {
        if (callback != null) callback.onLoad(data);
        else result = data;
    }

    public static class ImBinder extends Binder {
        public ImBinder() {
            super();
        }

        private ImageLoader service;

        public ImBinder(ImageLoader loader) {
            service = loader;
        }

        public void setCallBack(final OnLoad callBack) {
            new Handler(Looper.getMainLooper()).post(() -> {
                service.callback = callBack;
                service.callback.onLoad(service.result);
            });
        }
    }

    public interface OnLoad {
        void onLoad(Bitmap data);
    }

    public boolean onUnbind(Intent intent) {
        callback = null;
        return super.onUnbind(intent);
    }

    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }


}
