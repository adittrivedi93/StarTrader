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

/**
 * Created by adittrivedi on 03/03/2015.
 */
public class GlossaryAdapter extends ArrayAdapter<GlossaryDataHolder> {

    Context context;
    private ArrayList<GlossaryDataHolder> glossaryData;

    public GlossaryAdapter(ArrayList<GlossaryDataHolder> glossaryData, Context context) {
        super(context, android.R.layout.simple_list_item_1, glossaryData);
        this.glossaryData = glossaryData;
        this.context = context;
    }

    public int getCount(){
        if(glossaryData != null){
            return glossaryData.size();
        }
        return 0;
    }

    public GlossaryDataHolder getItem(int position){
        if(glossaryData != null){
            return glossaryData.get(position);
        }
        return null;
    }

    public long getItemId(int position) {
        if (glossaryData != null)
            return glossaryData.get(position).hashCode();
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_single_news, null);
        }

        GlossaryDataHolder glossaryDataHolder = glossaryData.get(position);

        TextView title = (TextView) v.findViewById(R.id.tvNewsTitle);
        title.setText(glossaryDataHolder.getName());

        TextView date = (TextView) v.findViewById(R.id.tvNewsDate);
        date.setText(glossaryDataHolder.getDescription());

        return v;
    }

    public List<GlossaryDataHolder> getGlossaryData() {
        return glossaryData;
    }

    public void setGlossaryData(ArrayList<GlossaryDataHolder> glossaryData) {
        this.glossaryData = glossaryData;
    }
}
