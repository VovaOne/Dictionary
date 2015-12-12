package com.im.dictionary.view.adapter.card;

import android.view.View;
import android.widget.ImageView;

import com.im.dictionary.R;
import com.im.dictionary.model.Card;

import static com.im.dictionary.storage.db.DB.CARD_DAO_INSTANCE;

public class LearningExpandedCardHolder extends ExpandedCardHolder {

    public final ImageView moveToStockImage;

    public LearningExpandedCardHolder(View view, CardChanged cardChangedCallback, CardDeleted cardDeletedCallback) {
        super(view, cardChangedCallback, cardDeletedCallback);
        moveToStockImage = (ImageView) view.findViewById(R.id.move_to_stock);

        initEvents();
    }

    private void initEvents() {
        moveToStockImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.type = Card.Type.STOCK;
                CARD_DAO_INSTANCE.update(model);
                if (cardDeletedCallback != null) cardDeletedCallback.apply(position);
            }
        });
    }
}
