package com.example.adittrivedi.startrader;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Status;

/**
 * Created by adittrivedi on 20/02/2015.
 */
public class TwitterAdapter extends ArrayAdapter<Status> {

    private Context context;
    private List<Status> tweetsList = new ArrayList<Status>();

    public TwitterAdapter(ArrayList<Status> tweetsList, Context context) {
        super(context, android.R.layout.simple_list_item_1, tweetsList);
        this.tweetsList = tweetsList;
        this.context = context;
    }

    public int getCount(){
        if(tweetsList != null){
            return tweetsList.size();
        }
        return 0;
    }

    public Status getItem(int position){
        if(tweetsList != null){
            return tweetsList.get(position);
        }
        return null;
    }

    public long getItemId(int position) {
        if (tweetsList != null)
            return tweetsList.get(position).hashCode();
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_single_news, null);
        }

        Status tweets = tweetsList.get(position);

        TextView title = (TextView) v.findViewById(R.id.tvNewsTitle);
        title.setText(tweets.getText().toString());

        TextView date = (TextView) v.findViewById(R.id.tvNewsDate);
        date.setText(tweets.getCreatedAt().toString());

        return v;
    }

    public List<Status> getTweetsList() {
        return tweetsList;
    }

    public void setTweetsList(List<Status> tweetsList) {
        Log.d("Size of list:", Integer.toString(tweetsList.size()));
        this.tweetsList = tweetsList;
    }
}
