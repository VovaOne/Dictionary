package com.im.dictionary.model;

import java.io.Serializable;

public class Card implements Serializable {

    public Long id;
    public String word;
    public String translate;

    public Type type;

    private boolean expand = false;
    public static final int OPEN = 1;
    public static final int CLOSE = 2;

    public Card() {
    }

    public Card(String word, String translate) {
        this.word = word;
        this.translate = translate;
        this.type = Type.LEARNING;
    }

    public Card(Long id, String word, String translate, Type type) {
        this.id = id;
        this.word = word;
        this.translate = translate;
        this.type = type;
    }

    public int isExpanded() {
        if (expand) return OPEN;
        else return CLOSE;
    }

    public void expand() {
        expand = true;
    }

    public void close() {
        expand = false;
    }

    public void toggle() {
        expand = !expand;
    }

    public static enum Type implements Serializable {
        LEARNING(1),
        STOCK(0);

        private int intType;

        Type(int intType) {
            this.intType = intType;
        }

        public int getIntType() {
            return intType;
        }

        public static Type getType(final int i) {
            for (Type type : Type.values()) {
                if (type.intType == i) return type;
            }

            throw new RuntimeException("no card type by int");
        }

    }
}
