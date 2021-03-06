package com.androidbelieve.drawerwithswipetabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.AccessToken;

import java.util.ArrayList;

public class ActiveAdsFragment extends Fragment {
    private ViewGroup rootView;
    private RecyclerView recyclerView;
    private LinearLayoutManager llm;
    private ArrayList<MyAds> list_ad;
    MyActiveAdsAdapter adsAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.pendingads_layout,null);
        recyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view_pending_ads);
        recyclerView.setHasFixedSize(true);
        list_ad = new ArrayList<>();
        adsAdapter=new MyActiveAdsAdapter(getContext(),list_ad);
        new FAd(Config.link+"activeads.php?pid="+ AccessToken.getCurrentAccessToken().getUserId()+"&status=ACTIVE",adsAdapter,list_ad).execute();
        llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setAdapter(adsAdapter);
        //setupListItems();

        recyclerView.setOnScrollListener(new onListenerScroll(adsAdapter,list_ad));
        recyclerView.setLayoutManager(llm);
        return rootView;
    }
}