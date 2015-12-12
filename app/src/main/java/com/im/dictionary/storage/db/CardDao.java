package com.im.dictionary.storage.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.im.dictionary.model.Card;

import java.util.ArrayList;
import java.util.List;

import static com.im.dictionary.storage.db.DB.*;

public class CardDao {

    public static final String TABLE_NAME = "cards";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_WORD = "word";
    public static final String COLUMN_TRANSLATE = "translate";
    public static final String COLUMN_TYPE = "type";


    public long persist(Card card) {
        SQLiteDatabase db = getSQLiteDataBase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_WORD, card.word);
        values.put(COLUMN_TRANSLATE, card.translate);
        values.put(COLUMN_TYPE, card.type.getIntType());
        card.id = db.insert(TABLE_NAME, null, values);
        return card.id;
    }


    public boolean update(Card card) {
        SQLiteDatabase db = getSQLiteDataBase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, card.id);
        values.put(COLUMN_WORD, card.word);
        values.put(COLUMN_TRANSLATE, card.translate);
        values.put(COLUMN_TYPE, card.type.getIntType());

        db.update(TABLE_NAME, values, "id = ? ", new String[]{Long.toString(card.id)});
        return true;
    }

    public Card getById(Long id) {
        SQLiteDatabase db = getSQLiteDataBase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where " + COLUMN_ID + " = ?",
                new String[]{id.toString()});
        cursor.moveToFirst();
        Card card = cursorToCard(cursor);
        cursor.close();
        return card;
    }

    public int delete(Long id) {
        SQLiteDatabase db = getSQLiteDataBase();
        return db.delete(TABLE_NAME, "id = ? ", new String[]{Long.toString(id)});
    }

    public List<Card> getAllCards() {
        List<Card> list = new ArrayList<Card>();

        Cursor res = getSQLiteDataBase().rawQuery("select * from " + TABLE_NAME, null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            list.add(cursorToCard(res));
            res.moveToNext();
        }

        res.close();
        return list;
    }

    public List<Card> getCardsByType(Card.Type type) {
        List<Card> list = new ArrayList<Card>();

        Cursor res = getSQLiteDataBase().rawQuery(
                "select * from " + TABLE_NAME + "" +
                        " where " + COLUMN_TYPE + " = " + type.getIntType(), null
        );
        res.moveToFirst();

        while (!res.isAfterLast()) {
            list.add(cursorToCard(res));
            res.moveToNext();
        }

        res.close();
        return list;
    }

    public Integer getCount() {
        List<Card> list = new ArrayList<Card>();

        Cursor res = getSQLiteDataBase().rawQuery("select count(*) from " + TABLE_NAME, null);
        res.moveToFirst();

        res.moveToFirst();
        int count = res.getInt(0);
        res.close();
        return count;
    }

    private Card cursorToCard(Cursor cursor) {
        Card card = new Card();
        card.id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
        card.word = cursor.getString(cursor.getColumnIndex(COLUMN_WORD));
        card.translate = cursor.getString(cursor.getColumnIndex(COLUMN_TRANSLATE));
        card.type = Card.Type.getType(cursor.getInt(cursor.getColumnIndex(COLUMN_TYPE)));
        return card;
    }

    public List<Card> getCardsByMatchesAndType(String matcher, Card.Type type) {
        List<Card> list = new ArrayList<Card>();

        Cursor res = getSQLiteDataBase().rawQuery(
                "select * from " + TABLE_NAME + "" +
                        " where ( " + COLUMN_WORD + " like ? " +
                        " or " + COLUMN_TRANSLATE + " like ? ) " +
                        " and " + COLUMN_TYPE + " = " + type.getIntType(),
                new String[]{"%" + matcher + "%", "%" + matcher + "%"}
        );
        res.moveToFirst();

        while (!res.isAfterLast()) {
            list.add(cursorToCard(res));
            res.moveToNext();
        }

        res.close();
        return list;
    }


}
