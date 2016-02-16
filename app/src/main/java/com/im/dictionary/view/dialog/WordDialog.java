package com.im.dictionary.view.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.im.dictionary.R;
import com.im.dictionary.model.Card;

import static com.im.dictionary.storage.db.DB.CARD_DAO_INSTANCE;

public class WordDialog {

    AlertDialog.Builder builder;

    private EditText word;
    private EditText translate;
    private Context context;

    public ItemChanged itemChangedCallback = null;
    public ItemAdded itemAddedCallback = null;

    public static final String ADDED_CARD_INTENT = "com.im.dictionary.card.added.intent";
    public static final String ADDED_CARD_ID_EXTRA = "com.im.dictionary.card.id.extra";

    public WordDialog(Context context) {
        this.context = context;
        builder = new AlertDialog.Builder(context);
    }

    public void show() {

        final View view = LayoutInflater.from(context).inflate(R.layout.word_dialog, null);
        word = (EditText) view.findViewById(R.id.input_word);
        translate = (EditText) view.findViewById(R.id.input_translate);

        builder.setView(view)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Card newCard = new Card(word.getText().toString(), translate.getText().toString());
                        CARD_DAO_INSTANCE.persist(newCard);
                        if (itemAddedCallback != null) itemAddedCallback.apply(newCard);
                        dialog.cancel();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        builder.create().show();
    }

    public void show(final Card card) {

        final View view = LayoutInflater.from(context).inflate(R.layout.word_dialog, null);
        word = (EditText) view.findViewById(R.id.input_word);
        translate = (EditText) view.findViewById(R.id.input_translate);

        word.setText(card.word);
        translate.setText(card.translate);

        builder.setView(view)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (card.id == null) {
                            Card newCard = new Card(word.getText().toString(), translate.getText().toString());
                            CARD_DAO_INSTANCE.persist(newCard);
                            if (itemAddedCallback != null) itemAddedCallback.apply(newCard);
                        } else {
                            Card newCard = new Card(card.id, word.getText().toString(), translate.getText().toString(), card.type);
                            CARD_DAO_INSTANCE.update(newCard);
                            if (itemChangedCallback != null) itemChangedCallback.apply(newCard);
                        }
                        dialog.cancel();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        builder.create().show();
    }

    public interface ItemChanged {
        void apply(Card card);
    }

    public interface ItemAdded {
        void apply(Card card);
    }

    public WordDialog setItemChangedCallback(ItemChanged itemChangedCallback) {
        this.itemChangedCallback = itemChangedCallback;
        return this;
    }

    public WordDialog setItemAddedCallback(ItemAdded itemAddedCallback) {
        this.itemAddedCallback = itemAddedCallback;
        return this;
    }
}
