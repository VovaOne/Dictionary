package com.im.dictionary.view.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


import com.im.dictionary.App;
import com.im.dictionary.R;
import com.im.dictionary.view.fragment.WordListFragment;
import com.im.dictionary.model.Card;

public class TabAdapter extends FragmentStatePagerAdapter {

    public static final int LEARNING_TAB = 0;
    public static final int STOCK_TAB = 1;

    public static final String CARD_TYPE_BUNDLE = "com.in.dictionary.card.type.bundle";


    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        WordListFragment wordListFragment = new WordListFragment();
        Bundle bdl = new Bundle(1);
        bdl.putInt(CARD_TYPE_BUNDLE, position);
        wordListFragment.setArguments(bdl);

        return wordListFragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case LEARNING_TAB:
                return App.getAppContext().getResources().getString(R.string.tab_learning_words);
            case STOCK_TAB:
                return App.getAppContext().getResources().getString(R.string.tab_all_words);
            default:
                throw new RuntimeException("Tab count error");
        }
    }




    public static int getTabNumberByCardType(Card.Type type) {
        switch (type) {
            case LEARNING:
                return LEARNING_TAB;
            case STOCK:
                return STOCK_TAB;
        }
        throw new RuntimeException("no card by tab number error");
    }

    public static Card.Type getCardTypeByTabNumber(final int number) {
        switch (number) {
            case LEARNING_TAB:
                return Card.Type.LEARNING;
            case STOCK_TAB:
                return Card.Type.STOCK;
        }
        throw new RuntimeException("no card by tab number error");
    }

}
