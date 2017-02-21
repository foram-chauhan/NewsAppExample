package com.example.dell.newsappexample;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by DELL on 09-02-2017.
 */

public class NewsLoader extends AsyncTaskLoader<List<News>> {
    /** Tag for log messages */
    private static final String LOG_TAG = NewsLoader.class.getName();

    /** Query URL */
    private String mUrl;

    public NewsLoader(Context context,String url) {
        super(context);
        mUrl = url;

    }
    @Override
    protected void onStartLoading() {
        forceLoad();
        Log.v("Loader State","Loader starts loading");
        super.onStartLoading();
    }

    @Override
    public List<News> loadInBackground() {
        if(mUrl == null){
            return null;
        }

        //perform network request, parse the response andextract a list of books.
        List<News> news = QueryUtils.fetchNewsData(mUrl);
        return news;
    }


}
