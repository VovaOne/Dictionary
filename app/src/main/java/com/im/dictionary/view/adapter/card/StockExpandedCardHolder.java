package com.im.dictionary.view.adapter.card;

import android.view.View;
import android.widget.ImageView;

import static com.im.dictionary.storage.db.DB.*;

import com.im.dictionary.R;
import com.im.dictionary.model.Card;

public class StockExpandedCardHolder extends ExpandedCardHolder {

    public final ImageView moveToLearningImage;

    public StockExpandedCardHolder(View view, CardChanged cardChangedCallback, CardDeleted cardDeletedCallback) {
        super(view, cardChangedCallback, cardDeletedCallback);

        moveToLearningImage = (ImageView) view.findViewById(R.id.move_to_learn);
        initEvents();
    }


    private void initEvents() {
        moveToLearningImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.type = Card.Type.STOCK;
                CARD_DAO_INSTANCE.update(model);
                if (cardDeletedCallback != null) cardDeletedCallback.apply(position);
            }
        });
    }

}
