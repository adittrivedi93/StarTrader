package com.example.adittrivedi.startrader;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by adittrivedi on 20/03/2015.
 */
public class SentimentVisualisationActivity extends ActionBarActivity {

    private HashMap<String, ArrayList<String>> sentimentMap = new HashMap<String, ArrayList<String>>();
    private LinearLayout chartView;
    private ExpandableListView expandableListView;
    private ExpandableListAdapter listAdapter;
    private GraphicalView mChart;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        setContentView(R.layout.sentiment_visu);
        chartView = (LinearLayout) findViewById(R.id.sentiChartLayout);
        expandableListView = (ExpandableListView) findViewById(R.id.sentiExpListView);
        Intent intent = getIntent();
        actionBarSet(intent);
        getMapDetails(intent);
        setPieChartView();
        setListView();
    }

    private void setListView(){
        prepareListData();
    }

    private void prepareListData(){
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        List<String> posList = getMapValue("Positive");
        List<String> negList = getMapValue("Negative");
        List<String> mixList = getMapValue("Mixed");
        List<String> unclassList = getMapValue("Unclassifiable");

        listDataHeader.add("Positive Tweets (" + posList.size() + ")");
        listDataHeader.add("Negative Tweets (" + negList.size() + ")");
        listDataHeader.add("Mixed Tweets (" + mixList.size() + ")");
        listDataHeader.add("Unclassified Tweets (" + unclassList.size() + ")");

        if(mixList.size() ==0){
            mixList.add("No mixed sentiment tweets were found!");
        }

        listDataChild.put(listDataHeader.get(0), posList);
        listDataChild.put(listDataHeader.get(1), negList);
        listDataChild.put(listDataHeader.get(2), mixList);
        listDataChild.put(listDataHeader.get(3), unclassList);

        listAdapter = new ExpandableListAdapter(getApplicationContext(), listDataHeader, listDataChild);
        expandableListView.setAdapter(listAdapter);
    }

    private ArrayList<String> getMapValue(String sentiType){
        ArrayList<String> listVals = new ArrayList<String>();
        for(Map.Entry<String, ArrayList<String>> entry : sentimentMap.entrySet()){
            String key = entry.getKey();
            ArrayList<String> values = entry.getValue();
            if(key.contains(sentiType)){
                listVals = values;
                return listVals;
            }
        }
        return listVals;
    }

    private void setPieChartView(){
        String[] code = new String[]{"Positive", "Negative"};
        double[] distribution = new double[2];
        distribution[0] = getSentimentSize("Positive");
        distribution[1] = getSentimentSize("Negative");
        int[] colours = {Color.GREEN, Color.RED};
        CategorySeries distributionSeries = new CategorySeries("Sentiment of Tweets");
        for(int i = 0; i< distribution.length; i++){
            distributionSeries.add(code[i], distribution[i]);
        }
        DefaultRenderer defaultRenderer = new DefaultRenderer();
        for(int i = 0; i< distribution.length;i++){
            SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
            seriesRenderer.setColor(colours[i]);
            seriesRenderer.setDisplayChartValues(true);
            defaultRenderer.addSeriesRenderer(seriesRenderer);
        }
        Intent intent = getIntent();
        defaultRenderer.setLegendTextSize(30);
        defaultRenderer.setLabelsTextSize(30);
        defaultRenderer.setDisplayValues(true);
        defaultRenderer.setChartTitle(intent.getStringExtra("StockName").toUpperCase());
        defaultRenderer.setChartTitleTextSize(50);
        defaultRenderer.setBackgroundColor(Color.BLACK);
        mChart = (GraphicalView) ChartFactory.getPieChartView(getApplicationContext(),
                distributionSeries,defaultRenderer);
        chartView.addView(mChart);
    }

    private int getSentimentSize(String name){
        int x =0;
        for(Map.Entry<String, ArrayList<String>> entry : sentimentMap.entrySet()){
            String key = entry.getKey();
            List<String> values = entry.getValue();
            if(key.contains(name)){
                return values.size();
            }
        }
        return x;
    }

    private void actionBarSet(Intent intent){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Twitter Sentiment");
    }

    private void getMapDetails(Intent intent){
        sentimentMap = (HashMap<String, ArrayList<String>>) intent.getSerializableExtra("SentimentMap");
    }
}
