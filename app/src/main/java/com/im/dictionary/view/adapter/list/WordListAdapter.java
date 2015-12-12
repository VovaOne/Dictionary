package com.im.dictionary.view.adapter.list;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.im.dictionary.App;
import com.im.dictionary.R;
import com.im.dictionary.view.adapter.card.Holder;
import com.im.dictionary.model.Card;

import java.util.List;


public abstract class WordListAdapter extends RecyclerView.Adapter<Holder> implements Holder.OnCardClickListener {

    List<Card> cards;


    public static final String RELOAD_CARD_LIST_INTENT = "com.im.dictionary.reload.card.list.intent";
    public static final String RELOAD_CARD_LIST_TYPE_PARAM = "com.im.dictionary.reload.card.list.type.param";


    public WordListAdapter(List<Card> cards) {
        this.cards = cards;
        initBroadcastReceivers();
    }


    private void initBroadcastReceivers() {
        LocalBroadcastManager.getInstance(App.getAppContext()).unregisterReceiver(reloadCardsBroadcastReceiver);
        LocalBroadcastManager.getInstance(App.getAppContext()).registerReceiver(reloadCardsBroadcastReceiver,
                new IntentFilter(RELOAD_CARD_LIST_INTENT));
    }

    protected BroadcastReceiver reloadCardsBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Card.Type type = (Card.Type) intent.getSerializableExtra(RELOAD_CARD_LIST_TYPE_PARAM);
            reloadCards(type);
        }
    };

    public static View getClosedCard(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.word_card, parent, false);
    }


    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Card word = cards.get(position);
        holder.setModel(word, position);
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    @Override
    public int getItemViewType(int position) {
        Card card = cards.get(position);
        return card.isExpanded();
    }

    @Override
    public void onCardClick(int position) {
        Card card = cards.get(position);
        card.toggle();
        notifyItemChanged(position);
    }

    public abstract void reloadCards(Card.Type type);
}
