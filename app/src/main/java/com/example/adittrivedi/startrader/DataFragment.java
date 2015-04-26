package com.example.adittrivedi.startrader;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.SearchView;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by adittrivedi on 12/03/2015.
 */
public class DataFragment extends Fragment {

    private SearchView stockSearch;
    private ExpandableListView expListView;
    private String baseYahooURL = "http://finance.yahoo.com/d/quotes.csv?s=";
    private String infoWantedURL = "&f=";
    private HashMap<Integer, ArrayList<String>> map = new HashMap<Integer, ArrayList<String>>();
    private ArrayList<String> priceList = new ArrayList<String>();
    private ArrayList<String> dividendList = new ArrayList<String>();
    private ArrayList<String> datesList = new ArrayList<String>();
    private ArrayList<String> averagesList = new ArrayList<String>();
    private ArrayList<String> fiftyTwoWeekList = new ArrayList<String>();
    private ArrayList<String> symbolList = new ArrayList<String>();
    private ArrayList<String> volumeList = new ArrayList<String>();
    private ArrayList<String> ratioList = new ArrayList<String>();
    private volatile int count;

    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    private ExpandableListAdapter listAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        count =0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.data_list_fragment, container, false);
        expListView = (ExpandableListView) rootView.findViewById(R.id.lvExp);
        stockSearch = (SearchView) rootView.findViewById(R.id.searchStockData);
        setStockSearch();
        return rootView;
    }

    private void setStockSearch(){
        stockSearch.setIconified(false);
        stockSearch.setQueryHint("Please enter a stock symbol for related stock data");
        stockSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                clearListContents();
                String stockSymbol = query;
                downloadStockData(stockSymbol);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void clearListContents(){
        priceList.clear();
        dividendList.clear();
        datesList.clear();
        averagesList.clear();
        fiftyTwoWeekList.clear();
        symbolList.clear();
        volumeList.clear();
        ratioList.clear();
    }

    private void prepareListData(){
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        listDataHeader.add("Pricing");
        listDataHeader.add("Dividends");
        listDataHeader.add("Dates");
        listDataHeader.add("Averages");
        listDataHeader.add("52 Week Pricing");
        listDataHeader.add("Symbol Info.");
        listDataHeader.add("Volume");
        listDataHeader.add("Ratios");

        List<String> prList = new ArrayList<String>();
        for(int i = 0; i<priceList.size(); i++){
            prList.add(priceList.get(i));
        }

        List<String> diList = new ArrayList<String>();
        for(int i = 0; i<dividendList.size(); i++){
            diList.add(dividendList.get(i));
        }

        List<String> daList = new ArrayList<String>();
        for(int i = 0; i<datesList.size(); i++){
            daList.add(datesList.get(i));
        }

        List<String> avList = new ArrayList<String>();
        for(int i = 0; i<averagesList.size(); i++){
            avList.add(averagesList.get(i));
        }

        List<String> fiList = new ArrayList<String>();
        for(int i = 0; i<fiftyTwoWeekList.size(); i++){
            fiList.add(fiftyTwoWeekList.get(i));
        }

        List<String> syList = new ArrayList<String>();
        for(int i = 0; i<symbolList.size(); i++){
            Log.d("Symbol info:", symbolList.get(i));
            syList.add(symbolList.get(i));
        }

        List<String> voList = new ArrayList<String>();
        for(int i = 0; i<volumeList.size(); i++){
            voList.add(volumeList.get(i));
        }

        List<String> raList = new ArrayList<String>();
        for(int i = 0; i<ratioList.size(); i++){
            raList.add(ratioList.get(i));
        }
//        Log.d("RaList:", String.valueOf(raList.size()));

        listDataChild.put(listDataHeader.get(0), prList);
        listDataChild.put(listDataHeader.get(1), diList);
        listDataChild.put(listDataHeader.get(2), daList);
        listDataChild.put(listDataHeader.get(3), avList);
        listDataChild.put(listDataHeader.get(4), fiList);
        listDataChild.put(listDataHeader.get(5), syList);
        listDataChild.put(listDataHeader.get(6), voList);
        listDataChild.put(listDataHeader.get(7), raList);

        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
    }

    private void downloadStockData (String stockSymbol){
        String stockURL = baseYahooURL + stockSymbol + infoWantedURL;
        ArrayList<String> urls = getAllUrls(stockURL);
        initialiseData();
        for(int i = 0; i< urls.size(); i++){
            DownloadYahooStockData yData = new DownloadYahooStockData();
            yData.execute(urls.get(i),String.valueOf(i));
        }
    }

    private void initialiseData(){
        InitialiseYahooData yahooData = new InitialiseYahooData();
        map.put(0, yahooData.initPricing());
        map.put(1, yahooData.initDividends());
        map.put(2, yahooData.initDates());
        map.put(3, yahooData.initAverages());
        map.put(4, yahooData.initFiftyTwoWeekPricing());
        map.put(5, yahooData.initSymbolInfo());
        map.put(6, yahooData.initVolume());
        map.put(7, yahooData.initRatios());
    }

    private ArrayList<String> getAllUrls(String stockURL){
        YahooInfoStrings infoString = new YahooInfoStrings();
        ArrayList<String> newURLs = new ArrayList<String>();
        newURLs.add(stockURL + infoString.getPricingSymbols());
        newURLs.add(stockURL + infoString.getDividendSymbols());
        newURLs.add(stockURL + infoString.getDateSymbols());
        newURLs.add(stockURL + infoString.getAverageSymbols());
        newURLs.add(stockURL + infoString.getFiftyTwoWeekSymbols());
        newURLs.add(stockURL + infoString.getSymbolInfoSymbols());
        newURLs.add(stockURL + infoString.getVolumeSymbols());
        newURLs.add(stockURL + infoString.getRatioSymbols());
        return newURLs;
    }

    private class DownloadYahooStockData extends AsyncTask<String, Void, ArrayList<String>>{

        ProgressDialog progressDialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setIndeterminate(true);
            progressDialog.setTitle("Getting stock related data, please wait!");
            progressDialog.show();
        }

        @Override
        protected synchronized ArrayList<String> doInBackground(String... urls) {
            InitialiseYahooData yData = new InitialiseYahooData();
//            Log.d("URL", urls[0]);
//            Log.d("Number", map.get(Integer.parseInt(urls[1])).toString());
//            Log.d("Break:", "break");
            ArrayList<String> yList = new ArrayList<String>();
            try{
                URL url = new URL (urls[0]);
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                String line = null;
                int count =0;
                while((line = in.readLine())!=null) {
                    //Log.d("Line",line);
                    String[] lineElements = line.split(",");
                    ArrayList<String> aList = map.get(Integer.parseInt(urls[1]));
                    for(int i = 0; i<lineElements.length; i++){
//                        Log.d("Elements", aList.get(i).toString());
//                        Log.d("Line",lineElements[i]);
                        String name = aList.get(i).toString();
                        String element = lineElements[i];
                        String total = name + ": " + element;
                        yList.add(total);
                    }
                }
            }catch (Exception e){
                Log.e("Unable to download data:", e.getMessage());
            }
            return yList;
        }

        @Override
        protected synchronized void onPostExecute(ArrayList<String> yList) {
            super.onPostExecute(yList);
            storeData(yList);
            count++;
            progressDialog.dismiss();
        }

        private synchronized void storeData(ArrayList<String> yList) {
            Log.d("Arrived:","yes");
            int x = count;
            ArrayList<String> dataList = yList;
            switch (x){
                case 0:
                    priceList = dataList;
                    break;
                case 1:
                    dividendList = dataList;
                    break;
                case 2:
                    datesList = dataList;
                    break;
                case 3:
                    averagesList = dataList;
                    break;
                case 4:
                    fiftyTwoWeekList = dataList;
                    break;
                case 5:
                    symbolList = dataList;
                    break;
                case 6:
                    volumeList = dataList;
                    break;
                case 7:
                    ratioList = dataList;
                    count=-1;
                    prepareListData();
                    break;
            }
        }
    }
}
