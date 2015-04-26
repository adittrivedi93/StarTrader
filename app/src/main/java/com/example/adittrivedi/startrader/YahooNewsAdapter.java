package com.example.adittrivedi.startrader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adittrivedi on 11/02/2015.
 */
public class YahooNewsAdapter extends ArrayAdapter<YahooFeedsData> {

    Context context;
    private ArrayList<YahooFeedsData> yahooFeedsDatas;

    public YahooNewsAdapter(ArrayList<YahooFeedsData> yahooFeedsDatas, Context context){
        super(context, android.R.layout.simple_list_item_1, yahooFeedsDatas);
        this.yahooFeedsDatas = yahooFeedsDatas;
        this.context = context;
    }

    public int getCount(){
        if(yahooFeedsDatas != null){
            return yahooFeedsDatas.size();
        }
        return 0;
    }

    public YahooFeedsData getItem(int position){
        if(yahooFeedsDatas != null){
            return yahooFeedsDatas.get(position);
        }
        return null;
    }

    public long getItemId(int position) {
        if (yahooFeedsDatas != null)
            return yahooFeedsDatas.get(position).hashCode();
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_single_news, null);
        }

        YahooFeedsData yahooFeedsData = yahooFeedsDatas.get(position);

        TextView title = (TextView) v.findViewById(R.id.tvNewsTitle);
        title.setText(yahooFeedsData.getTitle());

        TextView date = (TextView) v.findViewById(R.id.tvNewsDate);
        date.setText(yahooFeedsData.getDate());

<<<<<<< HEAD
=======
        TextView link = (TextView) v.findViewById(R.id.tvNewsLink);
        link.setText(yahooFeedsData.getLink());

>>>>>>> 266e5aadf12b73949269bdc7e8dc6531746cd5af
        return v;
    }

    public List<YahooFeedsData> getYahooFeedsData() {
        return yahooFeedsDatas;
    }

    public void setYahooFeedsDatas(ArrayList<YahooFeedsData> yahooFeedsDatas) {
        this.yahooFeedsDatas = yahooFeedsDatas;
    }
}
