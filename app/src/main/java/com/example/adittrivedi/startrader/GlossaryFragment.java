package com.example.adittrivedi.startrader;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by adittrivedi on 01/03/2015.
 */
public class GlossaryFragment extends Fragment {

    private SearchView sView;
    private ListView lView;
    private ArrayList<String> gListUnstructured = new ArrayList<String>();
    private ArrayList<String> name = new ArrayList<String>();
    private ArrayList<String> desc = new ArrayList<String>();
    private ArrayList<GlossaryDataHolder> glossary = new ArrayList<GlossaryDataHolder>();

    private GlossaryAdapter glossaryAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View glossaryView = inflater.inflate(R.layout.news_fragment, container, false);
        sView = (SearchView) glossaryView.findViewById(R.id.searchViewNews);
        lView = (ListView) glossaryView.findViewById(android.R.id.list);
        designAttributes(lView);
        populateGlossary();
        filterSearchView(sView);
        glossaryAdapter = new GlossaryAdapter(new ArrayList<GlossaryDataHolder>(), getActivity());
        lView.setAdapter(glossaryAdapter);
        return glossaryView;
    }

    private void filterSearchView(final SearchView sView) {
        sView.setIconifiedByDefault(false);
        sView.setQueryHint("Enter a financial term to retrieve the definition");
        sView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //show listview containing letter(s) typed
                ArrayList<GlossaryDataHolder> glossaryCopy = new ArrayList<GlossaryDataHolder>();
                for(GlossaryDataHolder gList : glossary){
                    if(gList.getName().toLowerCase().contains(newText.toLowerCase())){
                        String n = gList.getName();
                        String d = gList.getDescription();
                        GlossaryDataHolder glossaryDataHolder = new GlossaryDataHolder(n,d);
                        glossaryCopy.add(glossaryDataHolder);
                    }
                }
                glossaryAdapter.setGlossaryData(glossaryCopy);
                glossaryAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    private void designAttributes(ListView lView) {
        lView.setTextFilterEnabled(true);
        lView.setFastScrollEnabled(true);
    }
    private void populateGlossary(){
        //download Glossary data from website
        String url = "http://textuploader.com/4366";
        DownloadGlossaryData downloadGlossaryData = new DownloadGlossaryData();
        downloadGlossaryData.execute(url);
    }

    private class DownloadGlossaryData extends AsyncTask<String, Void, ArrayList<GlossaryDataHolder>>{

        ProgressDialog progressDialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setIndeterminate(true);
            progressDialog.setTitle("Getting glossary details, please wait!");
            progressDialog.show();
        }

        @Override
        protected ArrayList<GlossaryDataHolder> doInBackground(String... url) {
            try {
                Document glossary = Jsoup.connect(url[0]).
                        userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 " +
                                "(KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2").get();
                if(glossary != null){
                    Elements e = glossary.select("code");
                    String g = e.text();
                    splitString(g);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return glossary;
        }

        @Override
        protected void onPostExecute(ArrayList<GlossaryDataHolder> glossaryDataHolders) {
            super.onPostExecute(glossaryDataHolders);
            progressDialog.dismiss();
            //Log.d("Glossary size:", Integer.toString(glossaryDataHolders.size()));
            glossaryAdapter.setGlossaryData(glossaryDataHolders);
            glossaryAdapter.notifyDataSetChanged();
        }
    }

    private void splitString(String element) {
        StringTokenizer st = new StringTokenizer(element,"\n");
        while (st.hasMoreTokens()) {
            splitStringByDelim(st.nextToken());
        }
        insertNameDesc(gListUnstructured);
        createGlossaryList(name,desc);
        //checkGlossary();
    }

    public void splitStringByDelim(String s){
        StringTokenizer st = new StringTokenizer(s,"|");
        while (st.hasMoreTokens()) {
            gListUnstructured.add(st.nextToken());
        }
    }

    private void insertNameDesc(ArrayList<String> glossaryList) {
        int count =0;
        for(int i = 0; i<glossaryList.size();i++){
            if(count % 2 ==0){
                name.add(glossaryList.get(i));
                //name.add(glossaryList.get(i));
            }
            else{
                desc.add(glossaryList.get(i));
            }
            count++;
        }
    }

    private void createGlossaryList(ArrayList<String> gName, ArrayList<String> gDesc) {
        String n,d;
        for(int i = 0; i<gName.size();i++){
            n = gName.get(i);
            d = gDesc.get(i);
            GlossaryDataHolder gHolder = new GlossaryDataHolder(n,d);
            glossary.add(gHolder);
        }
    }

    private void checkGlossary() {
        for(int i =0; i<glossary.size(); i++){
            Log.d("Name:",glossary.get(i).getName());
            Log.d("Desc:",glossary.get(i).getDescription());
        }
    }
}
