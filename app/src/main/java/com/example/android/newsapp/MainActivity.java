package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.support.design.widget.BaseTransientBottomBar.LENGTH_INDEFINITE;

/**
 * Created by Hp on 8/30/2018.
 */


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>>, SharedPreferences.OnSharedPreferenceChangeListener {

    private String News_Request_Url = "https://content.guardianapis.com";
    ///search?section=world&page-size=20&show-fields=headline,byline,shortUrl,publication,standfirst,thumbnail&order-by=newest&api-key=06f23686-8cfc-4620-a506-e6cc5ba78ebf";
    private CoordinatorLayout coordinatorLayout;
    private NewsAdapter mNewsAdapter;
    private TextView noInternet;
    private ProgressBar progressBar;
    private LoaderManager loaderManager;
    private static final int LOADER_ID = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.snackbar);
        loaderManager = getLoaderManager();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        noInternet = (TextView) findViewById(R.id.no_internet);
        noInternet.setText(R.string.no_internet_connection);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        // And register to be notified of preference changes
        // So we know when the user has adjusted the query settings
        prefs.registerOnSharedPreferenceChangeListener(this);

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            //Snackbar.make(coordinatorLayout, getString(R.string.NoInternet), Snackbar.LENGTH_SHORT).show();
            Log.i("connection", "connection created");
            performSearch();
            getLoaderManager().initLoader(LOADER_ID, null, this);
        } else {
            noInternet.setVisibility(View.VISIBLE);
            Snackbar.make(coordinatorLayout, R.string.no_internet_connection, LENGTH_INDEFINITE).setAction("SETTINGS", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
                    startActivity(intent);
                }
            }).setActionTextColor(ContextCompat.getColor(this, R.color.white)).show();
        }

        ListView newsList = (ListView) findViewById(R.id.list_item);
        mNewsAdapter = new NewsAdapter(this, new ArrayList<News>());
        newsList.setAdapter(mNewsAdapter);

        newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                News currnetNews = mNewsAdapter.getItem(position);

                Uri NewsUri = Uri.parse(currnetNews.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, NewsUri);
                startActivity(websiteIntent);
            }
        });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        if (key.equals(getString(R.string.settings_min_magnitude_key)) ||
                key.equals(getString(R.string.List_preference_key))) {
            mNewsAdapter.clear();
            View loadingIndicator = findViewById(R.id.progressBar);
            loadingIndicator.setVisibility(View.VISIBLE);
            getLoaderManager().restartLoader(LOADER_ID, null, this);
        }
    }

    public void performSearch() {
        progressBar.setVisibility(View.VISIBLE);
        loaderManager.initLoader(LOADER_ID, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, Main2Activity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String newsChoice = sharedPrefs.getString(getString(R.string.List_preference_key), getString(R.string.settings_min_magnitude_default));
        String news_Size = sharedPrefs.getString(getString(R.string.settings_min_magnitude_key), getString(R.string.settings_min_magnitude_default));

        Uri mainUri = Uri.parse(News_Request_Url);
        Uri.Builder uriBuild = mainUri.buildUpon();
        uriBuild.appendPath(newsChoice);
        uriBuild.appendQueryParameter("format", "json");
        uriBuild.appendQueryParameter("page-size", news_Size);
        uriBuild.appendQueryParameter("show-fields", "all");
        uriBuild.appendQueryParameter("api-key", "06f23686-8cfc-4620-a506-e6cc5ba78ebf");

        return new NewsLoader(this, uriBuild.toString());
    }


    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        mNewsAdapter.clear();

        if (news != null && !news.isEmpty()) {
            mNewsAdapter.addAll(news);
            mNewsAdapter.notifyDataSetChanged();
        }
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        mNewsAdapter.clear();

    }

}
