package com.example.adittrivedi.startrader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by adittrivedi on 09/02/2015.
 */
public class NewsFragment extends Fragment{

    private TextView newsCheck;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View newsView = inflater.inflate(R.layout.news_fragment, container, false);
        return newsView;
    }
}
