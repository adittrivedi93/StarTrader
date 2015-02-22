package com.example.adittrivedi.startrader;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.SearchView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;

/**
 * Created by adittrivedi on 09/02/2015.
 */
public class NewsFragment extends ListFragment {

    private SearchView sView;
    private String stockQuote, yLink, yTitle;
    private ArrayList<YahooFeedsData> yList = new ArrayList<YahooFeedsData>();
    private ArrayList<String> newsData = new ArrayList<String>();
    private ListView listView;
    private YahooNewsAdapter yahooNewsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View newsView = inflater.inflate(R.layout.news_fragment, container, false);

        sView = (SearchView) newsView.findViewById(R.id.searchViewNews);
        listView = (ListView) newsView.findViewById(android.R.id.list);
        yahooNewsAdapter = new YahooNewsAdapter(new ArrayList<YahooFeedsData>(), getActivity());
        listView.setAdapter(yahooNewsAdapter);

        sView.setQueryHint("Please enter a stock symbol for related news articles");
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
        return newsView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // usually used for some init of variables
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        yLink = yList.get(position).getLink();
        yTitle = yList.get(position).getTitle();
        DownloadNewsContent newsContent = new DownloadNewsContent();
        newsContent.execute(yLink);
    }

    private void loadNewsArticle() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Stock: "+stockQuote.toUpperCase());
        builder.setMessage("Would you like to view this article?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loadUrl();
                dialog.cancel();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void loadUrl() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle(yTitle);
        final WebView webView = new WebView(getActivity());
        webView.loadUrl(yLink);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.getSettings().setBuiltInZoomControls(true);
                webView.getSettings().setDisplayZoomControls(true);
                webView.loadUrl(url);
                return true;
            }
        });
        alert.setView(webView);
        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    private class DownloadYahooDataFeeds extends AsyncTask<String, Void, ArrayList<YahooFeedsData>> {
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
                Document news = Jsoup.connect(url[0]).
                        userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 " +
                                "(KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2").get();
                if (news != null) {
                    yList.clear();
                    for (Element item : news.select("item")) {
                        String title = item.select("title").text();
                        String link = item.select("item").select("link").first().nextSibling().toString();
                        String pubDate = item.select("pubDate").text();
                        YahooFeedsData yahooFeedsData = new YahooFeedsData(title, link, pubDate);
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
            yahooNewsAdapter.setYahooFeedsDatas(yahooFeedsDatas);
            yahooNewsAdapter.notifyDataSetChanged();
        }
    }

    private class DownloadNewsContent extends AsyncTask<String, Void, ArrayList<String>> {
        ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.MyTheme);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();
        }

        @Override
        protected ArrayList<String> doInBackground(String... url) {
            try {
                newsData.clear();
                Document doc = Jsoup.connect(url[0]).get();
                Elements paragraphs = doc.select("p");
                for(Element p : paragraphs){
                    newsData.add(p.text());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);
            progressDialog.dismiss();
            //onPostExecuteMethod
            Log.v("Size of news:", Integer.toString(newsData.size()));
            loadNewsArticle();
        }
    }
}
