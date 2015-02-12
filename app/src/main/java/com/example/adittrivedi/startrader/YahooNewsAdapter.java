package com.example.adittrivedi.startrader;

import android.app.Activity;
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
    int layoutResourceId;
    ArrayList<YahooFeedsData> data = null;

    public YahooNewsAdapter (Context context, int layoutResourceId, ArrayList<YahooFeedsData> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        YahooFeedsDataHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new YahooFeedsDataHolder();
            holder.title = (TextView) row.findViewById(R.id.tvNewsTitle);
            holder.link = (TextView) row.findViewById(R.id.tvNewsLink);
            holder.date = (TextView) row.findViewById(R.id.tvNewsDate);
            row.setTag(holder);
        }
        else
        {
            holder = (YahooFeedsDataHolder)row.getTag();
        }

        YahooFeedsData yahooFeedsData = data.get(position);
        holder.title.setText(yahooFeedsData.getTitle());
        holder.link.setText(yahooFeedsData.getLink());
        holder.date.setText(yahooFeedsData.getDate());

        return row;
    }

    static class YahooFeedsDataHolder{
        TextView title, link, date;
    }
}
