package com.example.adittrivedi.startrader;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by adittrivedi on 09/02/2015.
 */
public class NewsFragment extends ListFragment{

    private SearchView sView;
    private String stockQuote;
    private ArrayList<YahooFeedsData> yList = new ArrayList<YahooFeedsData>();
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View newsView = inflater.inflate(R.layout.news_fragment, container, false);
        sView = (SearchView) newsView.findViewById(R.id.searchViewNews);
        listView = (ListView) newsView.findViewById(android.R.id.list);
        return newsView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                stockQuote = query;
                //Log.v("Stock Quote:", stockQuote);
                String url = "http://feeds.finance.yahoo.com/rss/2.0/headline?s=" +
                        stockQuote.trim().toUpperCase() + "&region=US&lang=en-US";
                DownloadYahooDataFeeds yahooData = new DownloadYahooDataFeeds();
                yahooData.execute(url);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private class DownloadYahooDataFeeds extends AsyncTask<String, Void, ArrayList<YahooFeedsData>>{
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setIndeterminate(true);
            progressDialog.setTitle("Getting " + stockQuote + " news, please wait!");
            progressDialog.show();
        }

        @Override
        protected ArrayList<YahooFeedsData> doInBackground(String... url) {
            try {
                yList.clear();
                Document news = Jsoup.connect(url[0]).
                        userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 " +
                                "(KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2").get();
                if(news != null){
                    for(Element item : news.select("item")){
                        YahooFeedsData yahooFeedsData = new YahooFeedsData(
                          item.select("title").first().text(),
                                item.select("link").first().text(),
                                item.select("pubDate").first().text());
                        yList.add(yahooFeedsData);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return yList;
        }

        @Override
        protected void onPostExecute(ArrayList<YahooFeedsData> yahooFeedsDatas) {
            super.onPostExecute(yahooFeedsDatas);
            if(progressDialog!=null && progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }
    }


}
