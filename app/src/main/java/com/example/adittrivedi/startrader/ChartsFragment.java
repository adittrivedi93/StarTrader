package com.example.adittrivedi.startrader;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.opencsv.CSVReader;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Created by adittrivedi on 26/01/2015.
 */
public class ChartsFragment extends Fragment {

    private Button btnCreateChart;
    private Spinner spnTimeHorizon;
    private EditText etStartDate, etEndDate, etStockSymbol;
    private String startDate, endDate, stockSymbol, timeHorizon;
    private LinearLayout chartView;
    private ArrayList<YahooDataHolder> yList = new ArrayList<YahooDataHolder>();
    private GraphicalView mChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_charts, container, false);
        btnCreateChart = (Button) rootView.findViewById(R.id.btnGenerateChart);
        etStartDate = (EditText) rootView.findViewById(R.id.etStartDate);
        etEndDate = (EditText) rootView.findViewById(R.id.etFinishDate);
        etStockSymbol = (EditText) rootView.findViewById(R.id.etStockSymbol);
        spnTimeHorizon = (Spinner) rootView.findViewById(R.id.spinnerTimeHorizon);
        chartView = (LinearLayout) rootView.findViewById(R.id.graphLayout);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void getData() {
        stockSymbol = etStockSymbol.getText().toString();
        startDate = etStartDate.getText().toString();
        endDate = etEndDate.getText().toString();
        timeHorizon = spnTimeHorizon.getSelectedItem().toString();
    }

    private String trimTimeHorizon(String timeHorizon) {
        String c = timeHorizon.substring(0, 1);
        return c;
    }

    private ArrayList<Integer> decodeDates(String date) {
        int day, month, year;
        day = Integer.parseInt(date.substring(0, 2));
        month = Integer.parseInt(date.substring(3, 5));
        year = Integer.parseInt(date.substring(6, 10));
        ArrayList<Integer> aList = new ArrayList<Integer>();
        aList.add(day);
        aList.add(month-1);
        aList.add(year);
        return aList;
    }

    private String getYahooData(ChartsFragmentHolder cHolder) {
        return "http://ichart.finance.yahoo.com/table.csv" + "?s=" + cHolder.getStockSymbol() +
                "&a=" + cHolder.getStartMonth() + "&b=" + cHolder.getStartDay() + "&c=" +
                cHolder.getStartYear() + "&d=" + cHolder.getEndMonth() + "&e=" +
                cHolder.getEndDay() + "&f=" + cHolder.getEndYear() + "&g=" +
                cHolder.getTimeHorizon().toLowerCase() + "&ignore=.csv";
    }

    @Override
    public void onResume() {
        super.onResume();

        btnCreateChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
                ChartsFragmentHolder cHolder = new ChartsFragmentHolder(stockSymbol,
                        trimTimeHorizon(timeHorizon),decodeDates(startDate).get(0),
                        decodeDates(startDate).get(1),decodeDates(startDate).get(2),
                        decodeDates(endDate).get(0),decodeDates(endDate).get(1),
                        decodeDates(endDate).get(2));
                //symbol, month, day, year, month, day, year, time horizon
                DownloadYahooData yData = new DownloadYahooData();
                yData.execute(getYahooData(cHolder));
            }
        });
    }

    public void drawChart(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                TimeSeries ySeries = new TimeSeries(stockSymbol + " Chart");
                for(int i = 0; i<yList.size();i++){
                    try {
                        Date date = dateFormat.parse(yList.get(i).getDate());
                        //Log.v("YList", date.toString());
                        ySeries.add(date, yList.get(i).getClose());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                XYMultipleSeriesDataset yDataset = new XYMultipleSeriesDataset();
                yDataset.addSeries(ySeries);

                XYSeriesRenderer yDataRenderer = new XYSeriesRenderer();
                yDataRenderer.setColor(Color.BLACK);
                yDataRenderer.setPointStyle(PointStyle.DIAMOND);
                yDataRenderer.setFillPoints(true);
                yDataRenderer.setLineWidth(5);

                XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
                multiRenderer.setChartTitle(stockSymbol.toUpperCase() + " Chart");
                multiRenderer.setXTitle("Dates");
                multiRenderer.setYTitle("Closing Price");
                multiRenderer.setXLabels(20);
                multiRenderer.setYLabelsColor(0,Color.RED);
                multiRenderer.setLabelsColor(Color.RED);
                multiRenderer.setZoomButtonsVisible(true);
                multiRenderer.addSeriesRenderer(yDataRenderer);

                for (int i = 0; i < yList.size(); i++) {
                    multiRenderer.addXTextLabel(i, yList.get(i).getDate());
                }

                mChart = (GraphicalView) ChartFactory.getTimeChartView(getActivity(),
                        yDataset, multiRenderer, "dd-MMM-yyyy");
                multiRenderer.setClickEnabled(true);
                multiRenderer.setSelectableBuffer(10);
                chartView.addView(mChart);
            }
        });

    }

    private class DownloadYahooData extends AsyncTask<String, Void, ArrayList<YahooDataHolder>>{
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setIndeterminate(true);
            progressDialog.setTitle("Creating chart, please wait!");
            progressDialog.show();
        }

        @Override
        protected ArrayList<YahooDataHolder> doInBackground(String... urls) {
            try{
                yList.clear();
                URL url = new URL (urls[0]);
                Log.v("URL", urls[0]);
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                String line = null;
                line = in.readLine();

                while((line = in.readLine())!=null){
                    CSVReader reader = new CSVReader(new StringReader(line));
                    String[] tokens;
                    while((tokens = reader.readNext()) != null){
                        YahooDataHolder yHolder = new YahooDataHolder(tokens[0],
                                Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2]),
                                Double.parseDouble(tokens[3]), Double.parseDouble(tokens[4]),
                                Double.parseDouble(tokens[5]), Double.parseDouble(tokens[6]));

                        yList.add(yHolder);
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            if(yList.size()>0) {
                drawChart();
            }
            return yList;
        }

        @Override
        protected void onPostExecute(ArrayList<YahooDataHolder> yahooDataHolders) {
            super.onPostExecute(yahooDataHolders);
            if(progressDialog!=null && progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }
    }
}
