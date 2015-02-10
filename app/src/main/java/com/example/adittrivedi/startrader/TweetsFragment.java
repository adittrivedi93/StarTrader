package com.example.adittrivedi.startrader;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by adittrivedi on 09/02/2015.
 */
public class TweetsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View tweetsView = inflater.inflate(R.layout.tweets_fragment, container, false);
        return tweetsView;
    }
}
