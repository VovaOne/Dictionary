package com.im.dictionary.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static com.im.dictionary.view.adapter.TabAdapter.*;

import com.im.dictionary.App;
import com.im.dictionary.view.adapter.list.LearningWordListAdapter;
import com.im.dictionary.view.adapter.list.StockWordListAdapter;
import com.im.dictionary.view.adapter.list.WordListAdapter;

import static com.im.dictionary.view.adapter.list.WordListAdapter.RELOAD_CARD_LIST_INTENT;
import static com.im.dictionary.view.adapter.list.WordListAdapter.RELOAD_CARD_LIST_TYPE_PARAM;
import static com.im.dictionary.storage.db.DB.CARD_DAO_INSTANCE;

import com.im.dictionary.model.Card;

import java.util.List;

import static com.im.dictionary.model.Card.*;


public class WordListFragment extends Fragment {

    private Type type;

    public WordListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int tabNumber;
        if (savedInstanceState != null) tabNumber = savedInstanceState.getInt(CARD_TYPE_BUNDLE);
        else tabNumber = getArguments().getInt(CARD_TYPE_BUNDLE);

        this.type = getCardTypeByTabNumber(tabNumber);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CARD_TYPE_BUNDLE, getTabNumberByCardType(type));
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView rv = new RecyclerView(getContext());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(mLayoutManager);

        RecyclerView.Adapter mAdapter = getListAdapterByTab();
        rv.setAdapter(mAdapter);
        return rv;
    }

    WordListAdapter getListAdapterByTab() {
        switch (type) {
            case LEARNING:
                return new LearningWordListAdapter(loadCardByType(Type.LEARNING));
            case STOCK:
                return new StockWordListAdapter(loadCardByType(Type.STOCK));
            default:
                throw new RuntimeException("card type error");
        }
    }

    private List<Card> loadCardByType(Card.Type type) {
        return CARD_DAO_INSTANCE.getCardsByType(type);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible() && isVisibleToUser) return;
        //Tab changed
        Intent changeTabIntent = new Intent(RELOAD_CARD_LIST_INTENT);
        changeTabIntent.putExtra(RELOAD_CARD_LIST_TYPE_PARAM, type);
        LocalBroadcastManager.getInstance(App.getAppContext()).sendBroadcast(changeTabIntent);

    }


}
