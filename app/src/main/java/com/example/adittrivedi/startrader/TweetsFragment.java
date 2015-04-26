package com.example.adittrivedi.startrader;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by adittrivedi on 09/02/2015.
 */
public class TweetsFragment extends Fragment {

    private SearchView sView;
    private ListView lView;
    private String tweet;
    private Twitter twitter;
    private List<Status> tweetList = new ArrayList<Status>();
    private TwitterAdapter twitterAdapter;
    private HashMap<String, String> sentiMap = new HashMap<String, String>();
    private ArrayList<String> posSentTweet = new ArrayList<String>();
    private ArrayList<String> negSentTweet = new ArrayList<String>();
    private ArrayList<String> mixedSentTweet = new ArrayList<String>();
    private ArrayList<String> unclassSentTweet = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View tweetsView = inflater.inflate(R.layout.news_fragment, container, false);
        sView = (SearchView) tweetsView.findViewById(R.id.searchViewNews);
        lView = (ListView) tweetsView.findViewById(android.R.id.list);
        twitterAdapter = new TwitterAdapter(new ArrayList<Status>(), getActivity());
        lView.setAdapter(twitterAdapter);
        getSentiData();
        sView.setIconifiedByDefault(false);
        sView.setQueryHint("Please enter a stock symbol for related tweets");
        sView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                tweet = query;
                Toast.makeText(getActivity(), query, Toast.LENGTH_SHORT).show();
                getTweets(tweet);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return tweetsView;
    }

    private void getSentiData(){
        DownloadSentimentData downloadSentimentData = new DownloadSentimentData();
        downloadSentimentData.execute();
    }

    private void getTweets(String tweet) {
        DownloadTwitterData downloadTwitterData = new DownloadTwitterData();
        downloadTwitterData.execute(tweet);
    }

    private void verifyUser() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey(getResources().getString(R.string.consumer_key));
        cb.setOAuthConsumerSecret(getResources().getString(R.string.consumer_secret));
        cb.setOAuthAccessToken(getResources().getString(R.string.access_token));
        cb.setOAuthAccessTokenSecret(getResources().getString(R.string.access_secret));
        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();
    }

    private class DownloadSentimentData extends AsyncTask<Void, Void, HashMap<String, String>>{

        @Override
        protected HashMap<String, String> doInBackground(Void... params) {
            try {
                InputStream is = getActivity().getAssets().open("WordSentiment.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder out = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.substring(0, line.length() -1);
                    String[] splitString = line.split("\\s+");
                    String key = "";
                    String value = "";
                    for(int i =0; i< splitString.length; i++){
                        String x = splitString[i];
                        if(x.charAt(0) == '-' || Character.isDigit(x.charAt(0))){
                            value = x;
                        }
                        else {
                            key = key + splitString[i];
                        }
                    }
                    sentiMap.put(key, value);
                }
                reader.close();
            }catch(Exception e)
            {
                Log.v("Tweets Fragment:", "File not found");
            }
            return sentiMap;
        }
    }

    private class DownloadTwitterData extends AsyncTask<String, Void, List<Status>> {

        ProgressDialog progressDialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            posSentTweet.clear();
            negSentTweet.clear();
            mixedSentTweet.clear();
            unclassSentTweet.clear();
            progressDialog.setIndeterminate(true);
            progressDialog.setTitle("Getting " + tweet + " tweets, please wait!");
            progressDialog.show();
            verifyUser();
        }

        @Override
        protected List<twitter4j.Status> doInBackground(String... tweet) {
            try {
                Query query = new Query(tweet[0]);
                query.setCount(100);
                QueryResult result = twitter.search(query);
                tweetList = result.getTweets();

                for(int i = 0; i<tweetList.size(); i++){
                    String t = tweetList.get(i).getText().toString();
                    assignSentiment(t);
                }
            } catch (TwitterException e) {
                Log.e("Twitter Exception:", e.getMessage());
            }
            return tweetList;
        }

        @Override
        protected void onPostExecute(List<twitter4j.Status> statuses) {
            super.onPostExecute(statuses);
            HashMap<String, ArrayList<String>> sentiClassMap = new HashMap<String, ArrayList<String>>();
            ArrayList<String> psTweet = posSentTweet;
            ArrayList<String> nsTweet = negSentTweet;
            ArrayList<String> msTweet = mixedSentTweet;
            ArrayList<String> usTweet = unclassSentTweet;
            sentiClassMap.clear();
            sentiClassMap.put("Positive", psTweet);
            sentiClassMap.put("Negative", nsTweet);
            sentiClassMap.put("Mixed", msTweet);
            sentiClassMap.put("Unclassifiable",usTweet);
            progressDialog.dismiss();
            twitterAdapter.setTweetsList(statuses);
            twitterAdapter.notifyDataSetChanged();
            showSentimentFragment(sentiClassMap);
        }

        private void showSentimentFragment(HashMap<String, ArrayList<String>> sentiClassMap){

            for(Map.Entry<String, ArrayList<String>> entry : sentiClassMap.entrySet()){
                String key = entry.getKey();
                ArrayList<String> values = entry.getValue();
                Log.d("Key:", key);
                Log.d("Vaues Size:", String.valueOf(values.size()));
            }

            final HashMap<String, ArrayList<String>> sMap = sentiClassMap;
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
            dialogBuilder.setTitle("View Tweets with Sentiment")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getActivity(), SentimentVisualisationActivity.class);
                            intent.putExtra("SentimentMap", sMap);
                            intent.putExtra("StockName",tweet);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
        }
    }

    private synchronized void assignSentiment(String tweet){
        float posSent = 0;
        float negSent = 0;
        String currTweet = tweet;
        currTweet = currTweet.toLowerCase();
        String[] splitStr = currTweet.split("\\s+");
        for(int i = 0; i< splitStr.length;i++){
            String t = splitStr[i];
            if(sentiMap.containsKey(t)) {
                try {
                    NumberFormat format = NumberFormat.getNumberInstance();
                    Number number = format.parse(sentiMap.get(t));
                    int n = number.intValue();
                    if(n > 0){
                        posSent = posSent + n;
                    }
                    else{
                        negSent = negSent + n;
                    }
                } catch (ParseException e) {
                    Log.e("Tweets Fragment Number Parser on "+ t + ": ",e.getMessage());
                }
            }
        }

        if(posSent==0 && negSent ==0) {
            unclassSentTweet.add(currTweet);
        }
        else if(posSent == negSent) {
            mixedSentTweet.add(currTweet);
        }
        else if ((negSent * -1) > posSent) {
            negSentTweet.add(currTweet);
        }
        else {
            posSentTweet.add(currTweet);
        }

//        Log.d("Tweet: ", currTweet);
//        Log.d("Pos Sent:", String.valueOf(posSent));
//        Log.d("Neg Sent:", String.valueOf(negSent));
    }
}
