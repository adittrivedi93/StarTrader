package com.example.adittrivedi.startrader;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by adittrivedi on 05/03/2015.
 */
public class RatesFragment extends Fragment {

    private SearchView sView;
    private ListView lView;
    private ArrayList<String> listCellText = new ArrayList<String>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View ratesView = inflater.inflate(R.layout.news_fragment, container, false);
        initialiseViews(ratesView);
        getInstrumentData();
        filterSearchView();
        quickScroll();
        return ratesView;
    }

    private void quickScroll(){
        lView.setTextFilterEnabled(true);
        lView.setFastScrollEnabled(true);
    }

    private void filterSearchView(){
        sView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<String> ratesListCopy = new ArrayList<String>();
                for(int i = 0; i< listCellText.size(); i++){
                    if(listCellText.get(i).toLowerCase().contains(newText.toLowerCase()))
                        ratesListCopy.add(listCellText.get(i));
                }
                if(ratesListCopy.size() == 0) {
                    String message = "No rates data available for text entered!";
                    showCustomToast(message,1000);
                }
                initialiseListView(ratesListCopy);
                return false;
            }
        });
    }

    private void showCustomToast(String message, int milliSeconds){
        final Toast toast = Toast.makeText(getActivity(),
                message, Toast.LENGTH_SHORT);
        toast.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, milliSeconds);
    }

    private void initialiseViews(View ratesView) {
        sView = (SearchView) ratesView.findViewById(R.id.searchViewNews);
        lView = (ListView) ratesView.findViewById(android.R.id.list);
        sView.setQueryHint("Enter rate symbol to show related prices");
        sView.setIconifiedByDefault(false);
    }

    private void getInstrumentData(){
        GetInstrumentData getInstrumentData = new GetInstrumentData();
        getInstrumentData.execute((Void) null);
    }

    private JSONArray httpGetData(String urlString, String instrumentArray) {
        JSONArray result = null;
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(urlString);

        try{
            HttpResponse getResponse = client.execute(request);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader
                            (getResponse.getEntity().getContent(), "UTF-8"));
            StringBuilder builder = new StringBuilder();
            for (String line = null; (line = reader.readLine()) != null;) {
                builder.append(line).append("\n");
            }
            result = new JSONObject(builder.toString()).getJSONArray(instrumentArray);
        } catch (Exception e){
            Log.e("Rates Fragment Http Method:", e.getMessage());
        }
        return result;
    }

    private void initialiseListView(ArrayList<String> data) {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getActivity(),R.layout.row_rates,data);
        lView.setAdapter(adapter);
    }

    private class GetInstrumentData extends AsyncTask<Void, Void, JSONArray>{

        ProgressDialog progressDialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setIndeterminate(true);
            progressDialog.setTitle("Getting rates data, please wait!");
            progressDialog.show();
        }

        @Override
        protected JSONArray doInBackground(Void... params) {
            String urlString = "http://api-sandbox.oanda.com/v1/instruments";
            String instrumentArray = "instruments";
            return httpGetData(urlString, instrumentArray);
        }

        @Override
        protected void onPostExecute(JSONArray result) {
            super.onPostExecute(result);
            ArrayList<String> instruments = new ArrayList<String>();
            for(int i = 0; i<result.length(); i++) {
                try {
                    JSONObject obj = result.getJSONObject(i);
                    instruments.add(obj.getString("instrument"));
                } catch (Exception e) {
                    Log.e("Rates Fragment Instrument Data:", e.getMessage());
                }
            }
            GetInstrumentPrices getInstrumentPrices = new GetInstrumentPrices(instruments);
            getInstrumentPrices.execute((Void) null);
            progressDialog.cancel();
        }
    }

    private class GetInstrumentPrices extends AsyncTask<Void, Void, JSONArray>{

        ProgressDialog progressDialog = new ProgressDialog(getActivity());

        String urlString = "http://api-sandbox.oanda.com/v1/prices?instruments=";

        public GetInstrumentPrices(List <String> pairs){
            for(int i = 0; i< pairs.size(); i++){
                if(i == 0){
                    urlString += pairs.get(i) + "%2";
                }
                else if(i != pairs.size() -1 ){
                    urlString += "C" + pairs.get(i) + "%2";
                }
                    else{
                        urlString += "C" + pairs.get(i);
                    }
            }
            Log.v("URL:", urlString);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONArray doInBackground(Void... params) {
            return httpGetData(urlString, "prices");
        }

        @Override
        protected void onPostExecute(JSONArray result) {
            super.onPostExecute(result);
            try {
                for (int i = 0; i < result.length(); i++) {
                    JSONObject obj = result.getJSONObject(i);
                    listCellText.add(obj.getString("instrument")
                            + "\nBid Price:"
                            + obj.getString("bid") + "\nAsk Price:"
                            + obj.getString("ask"));
                }
            } catch (Exception e) {
                Log.e("Rates Fragment Instrument Prices:", e.getMessage());
            }
            initialiseListView(listCellText);
        }
    }
}
