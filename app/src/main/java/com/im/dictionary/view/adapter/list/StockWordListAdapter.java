package com.im.dictionary.view.adapter.list;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.im.dictionary.App;
import com.im.dictionary.R;
import com.im.dictionary.view.adapter.card.CloseCardHolder;
import com.im.dictionary.view.adapter.card.ExpandedCardHolder;
import com.im.dictionary.view.adapter.card.Holder;
import com.im.dictionary.model.Card;

import java.util.List;

import static com.im.dictionary.view.activity.MainActivity.SEARCH_CARDS_EXTRA_PARAM;
import static com.im.dictionary.view.activity.MainActivity.SEARCH_CARD_INTENT;
import static com.im.dictionary.storage.db.DB.CARD_DAO_INSTANCE;
import static com.im.dictionary.model.Card.Type.LEARNING;
import static com.im.dictionary.model.Card.Type.STOCK;

public class StockWordListAdapter extends WordListAdapter {

    public StockWordListAdapter(List<Card> cards) {
        super(cards);
        initBroadcastReceivers();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        Holder vh = null;
        if (viewType == Card.OPEN) {
            v = getOpenedCard(parent);
            vh = new ExpandedCardHolder.Builder()
                    .setView(v)
                    .setCardChangedCallback(new ExpandedCardHolder.CardChanged() {
                        @Override
                        public void apply(Card card, int position) {
                            cards.set(position, card);
                            notifyItemChanged(position);
                        }
                    })
                    .setCardDeletedCallback(new ExpandedCardHolder.CardDeleted() {
                        @Override
                        public void apply(int position) {
                            cards.remove(position);
                            notifyItemRemoved(position);
                        }
                    })
                    .createStockExpandedCardHolder();
        } else if (viewType == Card.CLOSE) {
            v = getClosedCard(parent);
            vh = new CloseCardHolder(v);
        }

        vh.onClickCallback = this;

        initBroadcastReceivers();
        return vh;
    }

    private void initBroadcastReceivers() {

        LocalBroadcastManager.getInstance(App.getAppContext()).unregisterReceiver(searchCardBroadcastReceiver);
        LocalBroadcastManager.getInstance(App.getAppContext()).registerReceiver(searchCardBroadcastReceiver,
                new IntentFilter(SEARCH_CARD_INTENT));
    }

    protected BroadcastReceiver searchCardBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String searchParam = intent.getStringExtra(SEARCH_CARDS_EXTRA_PARAM);
            if (searchParam.isEmpty()) cards = CARD_DAO_INSTANCE.getCardsByType(LEARNING);
            else cards = CARD_DAO_INSTANCE.getCardsByMatchesAndType(searchParam, LEARNING);
            notifyDataSetChanged();
        }
    };

    public static View getOpenedCard(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.word_card_expanded_stock, parent, false);
    }

    @Override
    public void reloadCards(Card.Type type) {
        this.cards = CARD_DAO_INSTANCE.getCardsByType(STOCK);
        notifyDataSetChanged();
    }
}
