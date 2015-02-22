package com.example.adittrivedi.startrader;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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

    private class DownloadTwitterData extends AsyncTask<String, Void, List<Status>> {

        ProgressDialog progressDialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
//                for (int i = 0; i < tweetList.size(); i++) {
//                    tweetList.get(i).getText().toString();
//                    tweetList.get(i).getCreatedAt().toString();
//                }
            } catch (TwitterException e) {
                Log.e("Twitter Exception:", e.getMessage());
            }
            return tweetList;
        }

        @Override
        protected void onPostExecute(List<twitter4j.Status> statuses) {
            super.onPostExecute(statuses);
            progressDialog.dismiss();
//            for(int i = 0; i< statuses.size(); i++){
//                Log.d("Tag:", statuses.get(i).getText().toString());
//            }
            twitterAdapter.setTweetsList(statuses);
            twitterAdapter.notifyDataSetChanged();
        }
    }
}
