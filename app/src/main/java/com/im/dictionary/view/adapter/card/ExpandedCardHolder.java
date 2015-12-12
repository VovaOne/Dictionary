package com.im.dictionary.view.adapter.card;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.im.dictionary.R;
import com.im.dictionary.view.dialog.WordDialog;
import com.im.dictionary.model.Card;

import static com.im.dictionary.storage.db.DB.*;

public abstract class ExpandedCardHolder extends Holder {

    public CardChanged cardChangedCallback;
    public CardDeleted cardDeletedCallback;

    public View mView;
    public final TextView word;
    public final TextView translate;
    public final LinearLayout cardView;
    public final ImageView editCardImage;
    public final ImageView deleteCardImage;

    @Override
    protected void applyModel() {
        word.setText(model.word);
        translate.setText(model.translate);
    }

    protected ExpandedCardHolder(View view, CardChanged cardChangedCallback, CardDeleted cardDeletedCallback) {
        super(view);
        mView = view;
        word = (TextView) view.findViewById(R.id.word_text);
        translate = (TextView) view.findViewById(R.id.translate_text);
        cardView = (LinearLayout) view.findViewById(R.id.card_view);
        editCardImage = (ImageView) view.findViewById(R.id.edit_card);
        deleteCardImage = (ImageView) view.findViewById(R.id.delete_card);

        this.cardChangedCallback = cardChangedCallback;
        this.cardDeletedCallback = cardDeletedCallback;

        this.initEvents();
    }

    private void initEvents() {
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickCallback != null) onClickCallback.onCardClick(getAdapterPosition());
            }
        });

        editCardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new WordDialog(mView.getContext())
                        .setItemChangedCallback(new WordDialog.ItemChanged() {
                            @Override
                            public void apply(Card card) {
                                if (cardChangedCallback != null)
                                    cardChangedCallback.apply(card, position);
                            }
                        })
                        .show(model);
            }
        });

        deleteCardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CARD_DAO_INSTANCE.delete(model.id);
                if (cardDeletedCallback != null) cardDeletedCallback.apply(position);
            }
        });

    }


    public interface CardChanged {
        void apply(Card card, int position);
    }

    public interface CardDeleted {
        void apply(int position);
    }

    public static class Builder {
        private View view;
        private ExpandedCardHolder.CardChanged cardChangedCallback;
        private ExpandedCardHolder.CardDeleted cardDeletedCallback;

        public Builder setView(View view) {
            this.view = view;
            return this;
        }

        public Builder setCardChangedCallback(ExpandedCardHolder.CardChanged cardChangedCallback) {
            this.cardChangedCallback = cardChangedCallback;
            return this;
        }

        public Builder setCardDeletedCallback(ExpandedCardHolder.CardDeleted cardDeletedCallback) {
            this.cardDeletedCallback = cardDeletedCallback;
            return this;
        }

        public ExpandedCardHolder createLearningExpandedCardHolder() {
            return new LearningExpandedCardHolder(view, cardChangedCallback, cardDeletedCallback);
        }

        public ExpandedCardHolder createStockExpandedCardHolder() {
            return new StockExpandedCardHolder(view, cardChangedCallback, cardDeletedCallback);
        }
    }

}
