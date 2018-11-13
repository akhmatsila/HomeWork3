package com.example.indalamar.kinch2;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.renderscript.ScriptGroup;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

    public static final String ARG_ITEM_ID = "item_id";


    private DummyContent.DummyItem mItem;
    private Bitmap mImage;


    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.content);
            }
        }
    }

    @Override
    public ImageView onCreateView(LayoutInflater inflater, ViewGroup container,
                                  Bundle savedInstanceState) {
        ImageView rootView = (ImageView) inflater.inflate(R.layout.item_detail, container, false);
        ImageDownloader task = new ImageDownloader();
        try {
            return task.execute(rootView).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return rootView;
    }

    private class ImageDownloader extends AsyncTask<ImageView, Void, ImageView> {

        @Override
        protected ImageView doInBackground(ImageView... imageViews) {
            try {
                InputStream in = new java.net.URL(mItem.image).openStream();
                mImage = BitmapFactory.decodeStream(in);
                imageViews[0].setImageBitmap(mImage);
                return imageViews[0];
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
