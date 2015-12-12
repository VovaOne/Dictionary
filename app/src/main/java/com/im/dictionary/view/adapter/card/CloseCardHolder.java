package com.im.dictionary.view.adapter.card;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.im.dictionary.R;

public class CloseCardHolder extends Holder {

    public View mView;
    public final TextView textView;
    public final LinearLayout cardView;

    @Override
    protected void applyModel() {
        textView.setText(model.word);
    }

    public CloseCardHolder(final View view) {
        super(view);
        mView = view;
        textView = (TextView) view.findViewById(R.id.word_text);
        cardView = (LinearLayout) view.findViewById(R.id.card_view);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickCallback != null) onClickCallback.onCardClick(getAdapterPosition());
            }
        });
    }
}
