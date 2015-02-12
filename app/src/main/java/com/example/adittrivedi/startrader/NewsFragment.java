package com.example.adittrivedi.startrader;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by adittrivedi on 09/02/2015.
 */
public class NewsFragment extends ListFragment {

    private SearchView sView;
    private String stockQuote;
    private ArrayList<YahooFeedsData> yList = new ArrayList<YahooFeedsData>();
    private ListView listView;
    private YahooNewsAdapter yahooNewsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View newsView = inflater.inflate(R.layout.news_fragment, container, false);
        Log.wtf("unique Debug:", "yahooData.execute(url);");

        sView = (SearchView) newsView.findViewById(R.id.searchViewNews);
        listView = (ListView) newsView.findViewById(android.R.id.list);
        yahooNewsAdapter = new YahooNewsAdapter(new ArrayList<YahooFeedsData>(), getActivity());
        listView.setAdapter(yahooNewsAdapter);

        sView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                stockQuote = query;
                //Log.v("Stock Quote:", stockQuote);
                String url = "http://feeds.finance.yahoo.com/rss/2.0/headline?s=" +
                        stockQuote.trim().toUpperCase() + "&region=US&lang=en-US";
                DownloadYahooDataFeeds yahooData = new DownloadYahooDataFeeds();
                yahooData.execute(url);
                Log.wtf("unique Debug:", "yahooData.execute(url);");
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        return newsView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // usually used for some init of variables
    }

//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        sView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                stockQuote = query;
//                //Log.v("Stock Quote:", stockQuote);
//                String url = "http://feeds.finance.yahoo.com/rss/2.0/headline?s=" +
//                        stockQuote.trim().toUpperCase() + "&region=US&lang=en-US";
//                DownloadYahooDataFeeds yahooData = new DownloadYahooDataFeeds();
//                yahooData.execute(url);
//                Log.wtf("unique Debug:", "yahooData.execute(url);");
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
//    }

    private class DownloadYahooDataFeeds extends AsyncTask<String, Void, ArrayList<YahooFeedsData>> {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.wtf("unique Debug:", "onPreExecute");
            progressDialog.setIndeterminate(true);
            progressDialog.setTitle("Getting " + stockQuote + " news, please wait!");
            progressDialog.show();
        }

        @Override
        protected ArrayList<YahooFeedsData> doInBackground(String... url) {
            Log.wtf("unique Debug:", "doInBackground");
            try {
                Document news = Jsoup.connect(url[0]).
                        userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 " +
                                "(KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2").get();
                if (news != null) {
                    for (Element item : news.select("item")) {
                        //doesn't retrieve links
                        Log.wtf("Element item", "item= "+item.className());
                        Log.wtf("Element item", "item= "+item.text());
                        String title = item.select("title").text();
                        String link = item.select("link").text();
                        String pubDate = item.select("pubDate").text();
                        YahooFeedsData yahooFeedsData = new YahooFeedsData(title, link, pubDate);
//                        YahooFeedsData yahooFeedsData = new YahooFeedsData(
//                                item.select("title").first().text(),
//                                item.select("link").first().text(),
//                                item.select("pubDate").first().text());
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
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            Log.wtf("unique Debug:", String.valueOf(yahooFeedsDatas.size()));
            yahooNewsAdapter.setYahooFeedsDatas(yahooFeedsDatas);
            yahooNewsAdapter.notifyDataSetChanged();
//            yList.clear();
        }
    }
}
