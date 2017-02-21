package com.example.dell.newsappexample;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>>{
    public static final String LOG_TAG = MainActivity.class.getName();

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;

    /*String query global variable*/
    private String query;

    /*ListView global variable*/
    private ListView newsListView;

    /*BookAdapter global variable*/
    private NewsAdapter newsAdapter;

    /*LoadingIndicator global variable*/
    private View loadingIndicator;


    /**
     * URL for Book data from the Google dataset
     */
    private String NEWS_REQUEST_URL;
    /**
     * Constant value for the book loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int NEWS_LOADER_ID = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        //The website doesn't provide the latest data
        //as per indian time it's 04:01 am 09/02/2017
        //It doesn't show data for this date yet so kindly use 08/02/2017 as the most recent data date.
        NEWS_REQUEST_URL= "http://content.guardianapis.com/search?q=debate&tag=politics/politics&from-date="+date //instead of date use "2017-02-08"
                +"&api-key=test";
        // Find a reference to the {@link ListView} in the layout
        newsListView = (ListView) findViewById(R.id.listView);

        // Hide loading indicator because the data has been loaded
        loadingIndicator = findViewById(R.id.loading_indicator);

        // Create a new {@link ArrayAdapter} of books
        newsAdapter = new NewsAdapter(
                this, new ArrayList<News>());
        newsListView.setAdapter(newsAdapter);

        Log.v("url",NEWS_REQUEST_URL);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
            //Check if there's a loader running
            if(getLoaderManager().getLoader(NEWS_LOADER_ID).isStarted()){
                //restart if it's running for the following requests
                getLoaderManager().restartLoader(NEWS_LOADER_ID,null,this);
            }
        }else{
            //Otherwise,display error
            //First hide loading indicatorso error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);

        }
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        newsAdapter.clear();
        //bookListView.invalidateViews();
        newsListView.setAdapter(newsAdapter);
        //((BookAdapter)bookListView.getAdapter()).notifyDataSetChanged();
        //bookListView.setEmptyView(mEmptyStateTextView);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected earthquake.
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                News currentNews = newsAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri earthquakeUri = Uri.parse(currentNews.getWebUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        loadingIndicator.setVisibility(View.VISIBLE);
        // Create a new loader for the given URL
        Log.v("Loader State","on Create Loader");
        return new NewsLoader(this, NEWS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        loadingIndicator.setVisibility(View.GONE);
        // Clear the adapter of previous book data
        newsAdapter.clear();
        // If there is a valid list of {@link Book}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (news != null && !news.isEmpty()) {
            mEmptyStateTextView.setVisibility(View.INVISIBLE);
            newsAdapter.addAll(news);
            newsAdapter.notifyDataSetChanged();
        }

        // Set empty state text to display "No books found."
        mEmptyStateTextView.setText(R.string.no_news);
        Log.v("Loader State","on Load Finished");
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        // Loader reset, so we can clear out our existing data.
        newsAdapter.clear();
        Log.v("Loader State","on Loader Reset");
    }
}
