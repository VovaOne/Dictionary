package com.im.dictionary.view.adapter.card;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.im.dictionary.model.Card;

public abstract class Holder extends RecyclerView.ViewHolder {

    public int position;
    public Card model;

    public interface OnCardClickListener {
        void onCardClick(int position);
    }

    public OnCardClickListener onClickCallback;

    protected abstract void applyModel();

    public Card getModel() {
        return model;
    }

    public void setModel(Card model, int position) {
        this.model = model;
        this.position = position;
        applyModel();
    }

    public Holder(View itemView) {
        super(itemView);
    }



}
