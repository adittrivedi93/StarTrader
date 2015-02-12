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
    ArrayList<YahooFeedsData> yahooFeedsDatas;

    public YahooNewsAdapter(Context context, int resource,
                            ArrayList<YahooFeedsData> yahooFeedsDatas) {
        super(context, resource);
        this.context = context;
        this.yahooFeedsDatas = yahooFeedsDatas;
    }

    private class ViewHolder{
        TextView txtTitle, txtDate, txtLink;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        YahooFeedsData yahooFeedsData = getItem(position);
        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.list_single_news, null);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.tvNewsTitle);
            holder.txtDate = (TextView) convertView.findViewById(R.id.tvNewsDate);
            holder.txtLink = (TextView) convertView.findViewById(R.id.tvNewsLink);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtTitle.setText(yahooFeedsData.getTitle());
        holder.txtDate.setText(yahooFeedsData.getDate());
        holder.txtLink.setText(yahooFeedsData.getLink());
        return convertView;
    }
}
